package ui.graph;

import java.awt.ItemSelectable;
import java.awt.event.InputEvent;

import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;

public final class Mouse extends AbstractModalGraphMouse implements ModalGraphMouse, ItemSelectable {
	private ScalingControl scalingControl;
	private PickingMousePlugin pickingPlugin;
	
	public Mouse() {
		this(1.1f, 1 / 1.1f);
	}

	public Mouse(float in, float out) {
		super(in, out);
		scalingControl = new CrossoverScalingControl();
		loadPlugins();
	}

	@Override
	protected void loadPlugins() {
		translatingPlugin = new TranslatingGraphMousePlugin(InputEvent.BUTTON1_MASK);
		scalingPlugin = new ScalingGraphMousePlugin(scalingControl, 0, out, in);
		pickingPlugin = new PickingMousePlugin();
		add(scalingPlugin);
		add(translatingPlugin);
		add(pickingPlugin);
	}
}