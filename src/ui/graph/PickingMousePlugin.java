package ui.graph;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.JLayeredPane;

import ui.DocumentInfoPanel;
import ui.GraphPanel;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;

public class PickingMousePlugin extends PickingGraphMousePlugin<GraphNode, Void> {
	private DocumentInfoPanel panel;
	private GraphNode previousNode;

	@Override
	public void mouseClicked(MouseEvent e) {
		e.consume();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		e.consume();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		e.consume();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		e.consume();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point p = e.getPoint();
		if (panel == null || !panel.getBounds().contains(p)) {
			@SuppressWarnings("unchecked")
			VisualizationViewer<GraphNode, Void> vv = (VisualizationViewer<GraphNode, Void>) e.getSource();
			Layout<GraphNode, Void> layout = vv.getGraphLayout();
			GraphElementAccessor<GraphNode, Void> pickSupport = vv.getPickSupport();
			GraphNode node = pickSupport.getVertex(layout, p.getX(), p.getY());
			GraphPanel gp = (GraphPanel) vv.getParent().getParent();
			if (node != null && node != previousNode && panel == null) {
				if (!node.getDocuments().isEmpty()) {
					createAndShowPanel(node, vv, gp);
				}
			} else if (node == null && panel != null) {
				panel.setVisible(false);
				gp.remove(panel);
				gp.repaint(panel.getBounds());
				panel = null;
				previousNode = null;
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		e.consume();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		e.consume();
	}

	private void createAndShowPanel(GraphNode node, VisualizationViewer<GraphNode, Void> vv, GraphPanel gp) {
		panel = new DocumentInfoPanel(node.getDocuments());
		Point2D convertedPoint = vv.getRenderContext().getMultiLayerTransformer().transform(node.getPosition());
		Point point = new Point();
		point.setLocation(convertedPoint);
		panel.setBounds((int) point.getX(), (int) point.getY(), panel.getWidth(), panel.getHeight());
		panel.setVisible(true);
		previousNode = node;
		gp.add(panel, JLayeredPane.PALETTE_LAYER);
		gp.revalidate();
		gp.repaint(panel.getBounds());
		gp.repaint();
	}

}
