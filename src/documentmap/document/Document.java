package documentmap.document;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class Document {
	private String title;
	private String author;
	private File xmlFile;

	public Document(String t, String au, String file) {
		this(t, au, new File(file));
	}

	public Document(String title, String author, File file) {
		this.title = title;
		this.author = author;
		xmlFile = file;
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

	public File getXmlFile() {
		return xmlFile;
	}

	public void setXmlFile(File xmlFile) {
		this.xmlFile = xmlFile;
	}

	public List<String> getXml() throws IOException {
		return Files.readAllLines(xmlFile.toPath(), Charset.defaultCharset());
	}
}