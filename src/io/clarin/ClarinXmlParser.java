package io.clarin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class ClarinXmlParser {
	private String corpusPath;
	private String outputPath;
	private boolean singleOutputPath;
	private boolean useSynset;
	private Map<String, String> dictionary;

	public ClarinXmlParser(String corpusPath, String outputPath) {
		this.corpusPath = corpusPath;
		this.outputPath = outputPath;
		dictionary = new HashMap<>();
	}
	
	public void setSingleOutputPath(boolean singleOutputPath) {
		this.singleOutputPath = singleOutputPath;
	}

	@SuppressWarnings("unchecked")
	public void parse(final boolean useSynset) throws Exception {
		this.useSynset = useSynset;
		File corpusDir = new File(corpusPath);
		File[] cctList = corpusDir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.getName().equals("ccl.dtd");
			}
		});
		File cct = cctList[0];
		File[] files = corpusDir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				File copy = new File(file.getAbsolutePath() + "\\" + "ccl.dtd");
				if (!copy.exists()) {
					Files.copy(cct.toPath(), copy.toPath());
				}
				copy.deleteOnExit();
				File[] filesInDir = file.listFiles(new FileFilter() {

					@Override
					public boolean accept(File file) {
						String fileName = file.getName();
						return useSynset ? fileName.endsWith(".xml_wsd") : fileName.endsWith(".xml_dis");
					}
				});
				for (File fileInDir : filesInDir) {
					Map<String, Integer> nounHistogram = new HashMap<>();
					SAXReader reader = new SAXReader();
					Document document = reader.read(fileInDir.getPath());
					List<Node> tokens = document.selectNodes("//tok");
					for (Node token : tokens) {
						Node lex = token.selectSingleNode("lex");
						Node ctag = lex.selectSingleNode("ctag");
						String noun = "";
						if (ctag.getText().contains("subst")) {
							if (useSynset) {
								Node propKey = token.selectSingleNode("prop[@key='sense:ukb:syns_id']");
								Node propSynset = token.selectSingleNode("prop[@key='sense:ukb:unitsstr']");
								if (propKey != null && propSynset != null) {
									noun = propKey.getText();
									dictionary.put(noun, propSynset.getText());
								} else {
									noun = getNoun(lex);
									dictionary.put(noun, "");
								}
							} else {
								noun = getNoun(lex);
								dictionary.put(noun, "");
							}
							int counter = 1;
							if (nounHistogram.containsKey(noun)) {
								counter = nounHistogram.get(noun) + 1;
							}
							nounHistogram.put(noun, counter);
						}
					}
					String newFileName = "";
					if(singleOutputPath) {
						newFileName = outputPath;
					} else {
						String[] split = fileInDir.getAbsolutePath().split("\\\\");
						newFileName = outputPath + "\\" + split[split.length - 2];
					}
					File f = new File(newFileName);
					f.mkdirs();
					String nameWithoutExtension = fileInDir.getName().split("\\.")[0];
					f = new File(f.getAbsolutePath() + "\\" + nameWithoutExtension + ".csv");
					f.createNewFile();
					StringBuilder sb = new StringBuilder();
					sb.append(file.getName()).append("\r\n");
					for (String noun : nounHistogram.keySet()) {
						sb.append(noun).append(",").append(nounHistogram.get(noun)).append("\r\n");
					}
					sb.setLength(sb.length() - 2);
					BufferedWriter bw = new BufferedWriter(new FileWriter(f));
					bw.write(sb.toString());
					bw.flush();
					bw.close();
				}
			}
		}
	}
	
	public void saveDictionary(String dictionaryPath) throws IOException {
		File dictionary = new File(dictionaryPath);
		StringBuilder sb = new StringBuilder();
		for(String key : this.dictionary.keySet()) {
			if(useSynset) {
				sb.append(key).append(",").append(this.dictionary.get(key)).append("\r\n");
			} else {
				sb.append(key).append(",").append("\r\n");
			}
		}
		if(useSynset) {
			sb.setLength(sb.length() - 2);
		} else {
			sb.setLength(sb.length() - 1);
		}
		Files.write(dictionary.toPath(), sb.toString().getBytes());
	}

	private String getNoun(Node lex) {
		Node orth = lex.selectSingleNode("base");
		return orth.getText();
	}

}
