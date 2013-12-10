package ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLayeredPane;

import ui.graph.GraphNode;
import ui.graph.GraphNodePositionTransformer;
import ui.graph.Mouse;
import documentmap.DocumentMap;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;

public class GraphPanel extends JLayeredPane {
	private static final long serialVersionUID = -3489720440432237700L;
	private static final int NODE_GAP = 50;
	private Graph<GraphNode, Void> graph;
	private VisualizationViewer<GraphNode, Void> vv;
	private StaticLayout<GraphNode, Void> layout;
	private DocumentMap documentMap;

	public GraphPanel(DocumentMap documentMap) {
		this.documentMap = documentMap;
		graph = new SparseGraph<>();
		buildGraph();
		layout = new StaticLayout<>(graph, new GraphNodePositionTransformer());
		vv = new VisualizationViewer<>(layout);
		vv.setBackground(Color.WHITE);
		vv.setGraphMouse(new Mouse());
		GraphZoomScrollPane scrollPane = new GraphZoomScrollPane(vv);
		add(scrollPane, JLayeredPane.DEFAULT_LAYER);
		vv.revalidate();
		vv.repaint();
	}
	
	@Override
	public void doLayout() {
		for(Component c : getComponents()) {
			if(c instanceof GraphZoomScrollPane) {
				c.setBounds(getBounds());
			}
		}
	}

	private void buildGraph() {
		for (int i = 0; i < documentMap.getMapHeight(); i++) {
			for (int j = 0; j < documentMap.getMapWidth(); j++) {
				GraphNode node = new GraphNode();
				node.setPosition(i * NODE_GAP, j * NODE_GAP);
				graph.addVertex(node);
			}
		}
	}
}