package ui;

import java.awt.geom.Point2D;

public class GraphNode {
	private Point2D position;
	
	public void setPosition(int x, int y) {
		position = new Point2D.Double(x, y);
	}
	
	public Point2D getPosition() {
		return position;
	}
}