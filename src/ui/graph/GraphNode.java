package ui.graph;

import java.awt.geom.Point2D;

import documentmap.document.Document;

public class GraphNode {
	private int xInSom;
	private int yInSom;
	private Point2D position;
	private Document document;
	private boolean isClosest;
	private double[] weights;
	
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

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public void setClosest(boolean isClosest) {
		this.isClosest = isClosest;
	}

	public boolean isClosest() {
		return isClosest;
	}
	
	public void setWeights(double[] weights) {
		this.weights = weights;
	}
	
	public double[] getWeights() {
		return weights;
	}
}