package ui;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JLayeredPane;

import ui.graph.GraphNode;
import ui.graph.GraphNodeColorTransformer;
import ui.graph.GraphNodePositionTransformer;
import ui.graph.Mouse;
import documentmap.DocumentMap;
import documentmap.document.Document;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;

public class GraphPanel extends JLayeredPane {
	private static final long serialVersionUID = -3489720440432237700L;
	private static final int NODE_GAP = 25;
	private Graph<GraphNode, Void> graph;
	private VisualizationViewer<GraphNode, Void> vv;
	private StaticLayout<GraphNode, Void> layout;

	public GraphPanel(DocumentMap documentMap, List<int[]> somCoords, List<Document> documents) {
		graph = new SparseGraph<>();
		buildGraph(documentMap, somCoords, documents);
		layout = new StaticLayout<>(graph, new GraphNodePositionTransformer());
		vv = new VisualizationViewer<>(layout);
		vv.getRenderContext().setVertexFillPaintTransformer(new GraphNodeColorTransformer());
		vv.setBackground(Color.WHITE);
		vv.setGraphMouse(new Mouse());
		GraphZoomScrollPane scrollPane = new GraphZoomScrollPane(vv);
		add(scrollPane, JLayeredPane.DEFAULT_LAYER);
		vv.revalidate();
		vv.repaint();
	}

	@Override
	public void doLayout() {
		for (Component c : getComponents()) {
			if (c instanceof GraphZoomScrollPane) {
				c.setBounds(getBounds());
			}
		}
	}

	private void buildGraph(DocumentMap documentMap, List<int[]> somCoords, List<Document> documents) {
		GraphNode[][] map = new GraphNode[documentMap.getMapHeight()][documentMap.getMapWidth()];
		for (int i = 0; i < documentMap.getMapHeight(); i++) {
			for (int j = 0; j < documentMap.getMapWidth(); j++) {
				GraphNode node = new GraphNode();
				node.setPosition(i * NODE_GAP, j * NODE_GAP);
				node.setPositionInSom(i, j);
				map[i][j] = node;
				graph.addVertex(node);
			}
		}
		for(int i = 0; i<somCoords.size(); i++) {
			int[] coords = somCoords.get(i);
			Document document = documents.get(i);
			map[coords[0]][coords[1]].addDocument(document);
		}
	}
}