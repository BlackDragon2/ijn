package ui.graph;

import java.awt.Color;
import java.awt.Paint;

import org.apache.commons.collections15.Transformer;

public class GraphNodeColorTransformer implements Transformer<GraphNode, Paint>{

	@Override
	public Paint transform(GraphNode node) {
		Color color;
		if(node.getDocument() == null) {
			color = Color.WHITE;
		} else {
			color = Color.RED;
		}
		return color;
	}
}