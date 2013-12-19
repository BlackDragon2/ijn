package ui.graph;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import documentmap.document.Document;

public class GraphNode {
	private int xInSom;
	private int yInSom;
	private Point2D position;
	private List<Document> documents;
	
	public GraphNode() {
		documents = new ArrayList<>();
	}
	
	public void setPositionInSom(int x, int y) {
		xInSom = x;
		yInSom = y;
	}
	
	public int getxInSom() {
		return xInSom;
	}
	
	public int getyInSom() {
		return yInSom;
	}
	
	public void setPosition(int x, int y) {
		position = new Point2D.Double(x, y);
	}

	public Point2D getPosition() {
		return position;
	}
	
	public List<Document> getDocuments() {
		return documents;
	}

	public void addDocument(Document document) {
		if(!documents.contains(document)) {
			documents.add(document);
		}
	}
}