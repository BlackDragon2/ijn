package documentmap.document;

import java.io.File;

public class Document {
	private String title;
	private String author;
	private File csvFile;

	public Document(String t, String au, String file) {
		this(t, au, new File(file));
	}

	public Document(String title, String author, File file) {
		this.title = title;
		this.author = author;
		csvFile = file;
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

}