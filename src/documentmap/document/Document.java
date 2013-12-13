package documentmap.document;

import java.io.File;
import java.util.Arrays;

public class Document {
	private String title;
	private String author;
	private String id;
	private String date;
	private String source;
	private String tokenization;
	private String subcorpus;
	private String subject;
	private String[] keywords; 
	private File csvFile;

	public Document(){
		this("","",null,"","","","","","",new String[]{});
	}
	
	public Document(String t, String au, String file) {
		this(t,au,new File(file),"","","","","","",new String[]{});
	}

	public Document(String title, String author, File file) {
		this(title,author,file,"","","","","","",new String[]{});
	}
	
	public Document(String title, String author, File file, String id, String date, String source, String tokenization, String subcorpus, String subject, String[] keywords){
		this.title = title;
		this.author = author;
		this.csvFile = file;
		this.id = id;
		this.date = date;
		this.source = source;
		this.tokenization = tokenization;
		this.subcorpus = subcorpus;
		this.subject = subject;
		this.keywords = keywords;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public File getCsvFile() {
		return csvFile;
	}

	public void setCsvFile(File csvFile) {
		this.csvFile = csvFile;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTokenization() {
		return tokenization;
	}

	public void setTokenization(String tokenization) {
		this.tokenization = tokenization;
	}

	public String getSubcorpus() {
		return subcorpus;
	}

	public void setSubcorpus(String subcorpus) {
		this.subcorpus = subcorpus;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String[] getKeywords() {
		return keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}
	
	public String toString(){
		return 	super.toString() + "\r\n" + 
				"Id: " + id + "\r\n" +
				"Author: " + author + "\r\n" + 
				"Title: " + title + "\r\n" + 
				"Subject: " + subject + "\r\n" +
				"Subcorpus: " + subcorpus + "\r\n"+
				"Keywors: " + Arrays.toString(keywords)+"\r\n";
	}

}