package ui.graph;

import java.awt.ItemSelectable;
import java.awt.event.InputEvent;

import documentmap.DocumentMap;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;

public final class Mouse extends AbstractModalGraphMouse implements ModalGraphMouse, ItemSelectable {
	private ScalingControl scalingControl;
	private PickingMousePlugin pickingPlugin;
	private DocumentMap documentMap;
	
	public Mouse(DocumentMap documentMap) {
		this(1.1f, 1 / 1.1f, documentMap);
	}

	public Mouse(float in, float out, DocumentMap documentMap) {
		super(in, out);
		this.documentMap = documentMap;
		scalingControl = new CrossoverScalingControl();
		loadPlugins();
	}

	@Override
	protected void loadPlugins() {
		translatingPlugin = new TranslatingGraphMousePlugin(InputEvent.BUTTON1_MASK);
		scalingPlugin = new ScalingGraphMousePlugin(scalingControl, 0, out, in);
		pickingPlugin = new PickingMousePlugin(documentMap);
		add(scalingPlugin);
		add(translatingPlugin);
		add(pickingPlugin);
	}
}