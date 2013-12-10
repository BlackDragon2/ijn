package ui.graph;

import java.awt.geom.Point2D;

import documentmap.document.Document;

public class GraphNode {
	private Point2D position;
	private Document document;
	
	public void setPosition(int x, int y) {
		position = new Point2D.Double(x, y);
	}
	
	public Point2D getPosition() {
		return position;
	}
	
	public Document getDocument() {
		return document;
	}
	
	public void setDocument(Document document) {
		this.document = document;
	}
}