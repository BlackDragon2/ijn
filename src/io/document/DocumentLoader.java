package io.document;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import documentmap.document.Document;

public class DocumentLoader {
	private File kpwrFolder;
	private File csvFolder;
	private static final int ALL = Integer.MAX_VALUE;
	private static final String INI_EXTENSION = "ini";
	private static final String CSV_EXTENSION = ".csv";
	private static final String INI_SEPARATOR = "=";
	private Random rand = new Random();
	
	public DocumentLoader(File kpwrFolder, File csvFolder){
		if(!kpwrFolder.isDirectory()||!csvFolder.isDirectory())
			throw new IllegalArgumentException("Both File arguments have to be directories!");
		this.csvFolder = csvFolder;
		this.kpwrFolder = kpwrFolder;
	}
	
	public List<String> getCategoryList(){
		List<String> list = new ArrayList<>();
		File[] files = kpwrFolder.listFiles();
		for(File f: files)
			if(f.isDirectory())
				list.add(f.getName());
		return list;
	}
	
	/**
	 * @param category
	 * @param amount
	 * @param randomize
	 * @return Specified amount of documents from specified category, documents can be randomized
	 * @throws IOException
	 */
	public List<Document> loadDocuments(String category, int amount, boolean randomize) throws IOException{
		List<Document> docList = new ArrayList<>();
		File[] iniFiles = listIniFiles(new File(kpwrFolder.getAbsolutePath() + File.separatorChar+category));
		if(randomize)
			shuffle(iniFiles);
		for(int i = 0; i < iniFiles.length && i < amount; i++)
			docList.add(readIniFile(iniFiles[i]));
		return docList;
	}
	
	/**
	 * @param category
	 * @param amount
	 * @return Specified amount of documents from specified category,
	 * documents are not randomized (method always returns the same documents)
	 * @throws IOException
	 */
	public List<Document> loadDocuments(String category, int amount) throws IOException{
		return loadDocuments(category, amount, false);
	}
	
	/**
	 * @param category
	 * @return All documents for specified category
	 * @throws IOException
	 */
	public List<Document> loadDocuments(String category) throws IOException{
		return loadDocuments(category, ALL, false);
	}
	
	/**
	 * @param amount
	 * @param randomize
	 * @param amountForEach
	 * @return Specified amount of documents,
	 * the amount of documents in each category will be randomized if amountForEach==false,
	 * otherwise this method works as loadDocuments(amount, randomize)
	 * @throws IOException
	 */
	public List<Document> loadDocuments(int amount, boolean randomize, boolean amountForEach) throws IOException{
		List<Document> list = new ArrayList<>();
		List<String> categories = getCategoryList();
		if(!amountForEach)
			Collections.shuffle(categories);
		for(int i = 0; i < categories.size() && amount > 0; i++){
			String cat =categories.get(i);
			if(amountForEach)
				list.addAll(loadDocuments(cat, amount, randomize));
			else{
				int catAmount = rand.nextInt(amount+1);
				List<Document> temp = loadDocuments(cat, catAmount, randomize);
				amount -= temp.size();
				list.addAll(temp);
			}
		}
		return list;
	}
	
	
	/**
	 * @param amount
	 * @param randomize
	 * @return Specified amount of documents from each of the categories
	 * @throws IOException
	 */
	public List<Document> loadDocuments(int amount, boolean randomize) throws IOException{
		return loadDocuments(amount, randomize, true);
	}
	
	/**
	 * @param amount
	 * @return Specified amount of documents from each of the categories,
	 *  documents are not randomized (method always returns the same documents)  
	 * @throws IOException
	 */
	public List<Document> loadDocuments(int amount) throws IOException{
		return loadDocuments(amount, false);
	}
	
	/**
	 * @return All documents id kpwr
	 * @throws IOException
	 */
	public List<Document> loadDocuments() throws IOException{
		return loadDocuments(ALL);
	}
	
	
	
	private Document readIniFile(File file) throws IOException{
		List<String> strings = Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));
		Document d = new Document();
		for(String s: strings){
			if(s.contains(INI_SEPARATOR)){
				String[] split = s.split(INI_SEPARATOR);
				String a = split[1].trim();
				switch(split[0].trim()){
				case "id" : 
					d.setId(a);
					d.setCsvFile(new File(csvFolder.getAbsolutePath()+File.separator+file.getParentFile().getName()+File.separator+file.getName().replace(".ini", "")+CSV_EXTENSION));
					break;
				case "date" :
					d.setDate(a);
					break;
				case "source" : 
					d.setSource(a);
					break;
				case "title" : 
					d.setTitle(a);
					break;
				case "author" : 
					d.setAuthor(a);
					break;
				case "tokenization" :
					d.setTokenization(a);
					break;
				case "subcorpus" : 
					d.setSubcorpus(a);
					break;
				case "subject" : 
					d.setSubject(a);
					break;
				case "keywords" : 
					String[] temp = a.split(",");
					String[] keywords = new String[temp.length];
					for(int i = 0; i < temp.length; i++)
						keywords[i] = temp[i].trim();
					d.setKeywords(keywords);
					break;
				}
			}
		}
		return d;
	}
	
	private File[] listIniFiles(File directory){
		return directory.listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File file, String name) {
				return name.substring(name.lastIndexOf('.')+1).equals(INI_EXTENSION);
			}
		});
	}
	
	private <T> void shuffle(T[] array){
		for(int i = array.length-1; i >= 0 ; i--){
			int index = rand.nextInt(i+1);
			T temp = array[i];
			array[i] = array[index];
			array[index] = temp;
		}
	}
	
}
