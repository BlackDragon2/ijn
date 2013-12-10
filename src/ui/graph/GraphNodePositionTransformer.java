package ui.graph;

import java.awt.geom.Point2D;

import org.apache.commons.collections15.Transformer;

public class GraphNodePositionTransformer implements Transformer<GraphNode, Point2D> {

	@Override
	public Point2D transform(GraphNode node) {
		return node.getPosition();
	}
}