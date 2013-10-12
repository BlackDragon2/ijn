package som.layers;

import java.io.Serializable;

import som.neurons.InputNeuron;

public class InputLayer implements Serializable {
	private static final long serialVersionUID = -5454652062640158971L;
	private InputNeuron[] inputNeurons;
	private int length;

	public InputLayer(int n) {
		length = n;
		inputNeurons = new InputNeuron[n];
		initateNeurons();
	}

	public int getLength() {
		return length;
	}

	public void setInput(double[] input) {
		if (input.length != inputNeurons.length)
			throw new IllegalArgumentException();
		double sum = 0;
		for (int i = 0; i < input.length; i++) {
			sum += input[i] * input[i];
		}
		sum = Math.sqrt(sum);
		for (int i = 0; i < input.length; i++)
			inputNeurons[i].setNet(sum == 0 ? 0 : input[i] / sum);
	}

	private void initateNeurons() {
		if (inputNeurons != null)
			for (int i = 0; i < inputNeurons.length; i++)
				inputNeurons[i] = new InputNeuron();
	}

	public double[] getInputVector() {
		double[] tab = new double[inputNeurons.length];
		for (int i = 0; i < inputNeurons.length; i++) {
			tab[i] = inputNeurons[i].getNet();
		}
		return tab;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("InputLayer[ ");
		for (int i = 0; i < inputNeurons.length; i++) {
			sb.append(inputNeurons[i]);
			if (i + 1 != inputNeurons.length) {
				sb.append(", ");
			}
		}
		sb.append("]= ");
		double sum = 0;
		for (int i = 0; i < inputNeurons.length; i++) {
			sum += inputNeurons[i].getNet();
		}
		sb.append(sum);
		return sb.toString();
	}
}
