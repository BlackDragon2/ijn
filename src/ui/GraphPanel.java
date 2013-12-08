package ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import documentmap.DocumentMap;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;

public class GraphPanel extends JPanel {
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
		setLayout(new GridBagLayout());
		vv.setBackground(Color.WHITE);
		vv.setGraphMouse(new Mouse());
		add(new GraphZoomScrollPane(vv), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		vv.revalidate();
		vv.repaint();
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