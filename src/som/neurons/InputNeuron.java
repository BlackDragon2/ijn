package som.neurons;

import java.io.Serializable;

public class InputNeuron implements Serializable {
	private static final long serialVersionUID = -8296392604420434835L;
	private double net;

	public InputNeuron() {
		setNet(0);
	}

	public InputNeuron(double w) {
		setNet(w);
	}

	public double getNet() {
		return net;
	}

	public void setNet(double w) {
		net = w;
	}

	public String toString() {
		return "O(" + net + ")";
	}
}