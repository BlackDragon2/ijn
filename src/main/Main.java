package main;

import io.clarin.ClarinWsdWorker;
import io.clarin.ClarinXmlParser;
import io.document.DocumentLoader;
import io.documentmap.ObjectIOManager;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ui.GraphPanel;
import bayes.params.BayesParams;
import documentmap.DocumentMap;
import documentmap.document.Document;

public class Main {

	public static void main(String[] args) throws Exception {
		if (args.length > 0) {
			String task = args[0];
			if (task.equals("DISAMB")) {
				String corpusPath = args[1];
				String text = args[2];
				disambiguate(corpusPath, text.equals("XML"));
			} else if (task.equals("EXTRACT")) {
				String corpusPath = args[1];
				String outputPath = args[2];
				String synsets = args[3];
				String dictionaryPath = args[4];
				extract(corpusPath, outputPath, synsets.equals("SYNSETS"), dictionaryPath);
			} else if (task.equals("LEARN")) {
				String type = args[1];
				if (type.equals("BAYES")) {
					String directory = args[2];
					String corpusPath = args[3];
					String csvPath = args[4];
					String savePath = args[5];
					learnBayes(directory, corpusPath, csvPath, savePath);
				} else if (type.equals("SOM")) {
					String corpusPath = args[2];
					String csvPath = args[3];
					String dictionaryPath = args[4];
					String savePath = args[5];
					String mapX = args[6];
					String mapY = args[7];
					int[] mapDim = new int[] { Integer.valueOf(mapX), Integer.valueOf(mapY) };
					learnSom(corpusPath, csvPath, dictionaryPath, savePath, mapDim);
				}
			} else if (task.equals("VISUALIZE")) {
				String mapPath = args[2];
				String corpusPath = args[3];
				String csvPath = args[4];
				String dictionaryPath = args[5];
				visualize(mapPath, corpusPath, csvPath, dictionaryPath);
			}
		}
	}

	private static void disambiguate(String corpusPath, boolean text) throws Exception {
		ClarinWsdWorker worker = new ClarinWsdWorker(corpusPath);
		worker.disambiguateCorpus(text);
	}

	private static void extract(String corpusPath, String outputPath, boolean synsets, String dictionaryPath) throws Exception {
		ClarinXmlParser parser = new ClarinXmlParser(corpusPath, outputPath);
		parser.parse(synsets);
		parser.saveDictionary(dictionaryPath);
	}

	public static void learnSom(String corpusPath, String csvPath, String dictionaryPath, String savePath, int[] mapDim) throws IOException {
		DocumentLoader loader = new DocumentLoader(new File(corpusPath), new File(csvPath));
		List<Document> documents = loader.loadDocuments();
		double[][] vectors = Document.vectorizeDocuments(documents, new File(dictionaryPath));
		DocumentMap map = new DocumentMap(vectors[0].length, mapDim[0], mapDim[1]);
		map.createMap(vectors);
		ObjectIOManager.save(map, new File(savePath));
	}

	private static void learnBayes(String directory, String corpusPath, String csvPath, String mapOutputPath) throws Exception {
		DocumentMap map = new DocumentMap(12, 50, 50);
		List<String> args = new LinkedList<String>();
		args.add("LEARN_AND_CLASSIFY");
		args.add(directory + "\\learn.csv");
		args.add("s");
		args.add("MULTINOMINAL");
		args.add("BY_WORD");
		args.add("SIMPLE_SMOOTHING");
		args.add(directory + "\\test.csv");
		BayesParams params = new BayesParams(args.toArray(new String[args.size()]));
		DocumentLoader loader = new DocumentLoader(new File(corpusPath), new File(csvPath));
		List<String> categories = loader.getCategoryList();
		List<Document> learning = new LinkedList<Document>();
		List<Document> test = new LinkedList<Document>();
		List<Document> test2 = new LinkedList<Document>();
		for (String cat : categories) {
			learning.addAll(loader.loadDocuments(cat, 5, true));
			test.addAll(loader.loadDocuments(cat, 5, true));
			test2.addAll(loader.loadDocuments(cat, 10, true));
		}
		map.createMap(params, test, learning, categories);
		ObjectIOManager.save(map, new File(mapOutputPath));
	}

	public static void visualize(String mapPath, String corpusPath, String csvPath, String dictionaryPath) throws Exception {
		DocumentMap map = ObjectIOManager.load(new File(mapPath));
		DocumentLoader loader = new DocumentLoader(new File(corpusPath), new File(csvPath));
		List<Document> documents = loader.loadDocuments();
		List<int[]> list = map.mapDocuments(documents, new File(dictionaryPath));
		final JFrame frame = new JFrame();
		frame.setSize(new Dimension(800, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GraphPanel panel = new GraphPanel(map, list, documents);
		frame.setContentPane(panel);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}
}