package ui.graph;

import java.awt.BasicStroke;
import java.awt.Stroke;

import org.apache.commons.collections15.Transformer;

public class GraphNodeStrokeTransformer implements Transformer<GraphNode, Stroke> {
	
	@Override
	public Stroke transform(GraphNode node) {
		Stroke stroke;
		if (node.isClosest()) {
			stroke = new BasicStroke(3.0f);
		} else {
			stroke = new BasicStroke(1.0f);
		}
		return stroke;
	}
}