package som.layers;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import som.neurons.OutputNeuron;

public class CompetitiveLayer implements Serializable {
	private static final long serialVersionUID = 3862734525479727680L;
	private OutputNeuron[][] neurons;
	private Dimension size;

	public CompetitiveLayer(int y, int x, int n) {
		neurons = new OutputNeuron[x][y];
		size = new Dimension(y, x);
		initateNeurons(n);
	}

	public Dimension getSize() {
		return size;
	}

	public double[][][] getVectors() {
		double[][][] tab = new double[neurons.length][neurons[0].length][];
		for (int i = 0; i < tab.length; i++) {
			for (int j = 0; j < tab[i].length; j++) {
				tab[i][j] = neurons[i][j].getWeights();
			}
		}
		return tab;
	}

	public OutputNeuron getWinnerNeuron(double[] inputVector) {
		double minDistance = Double.MAX_VALUE;
		OutputNeuron bestNeuron = null;
		for (int i = 0; i < neurons.length; i++) {
			for (int j = 0; j < neurons[i].length; j++) {
				OutputNeuron neuron = neurons[i][j];
				double distance = neuron.getDistance(inputVector);
				if (distance < minDistance) {
					minDistance = distance;
					bestNeuron = neuron;
				}
			}
		}
		return bestNeuron;
	}

	public int[] getIndexWinnerNeuron(double[] inputVector) {
		double minDistance = Double.MAX_VALUE;
		int[] bestNeuron = null;
		for (int i = 0; i < neurons.length; i++) {
			for (int j = 0; j < neurons[i].length; j++) {
				OutputNeuron neuron = neurons[i][j];
				double distance = neuron.getDistance(inputVector);
				if (distance < minDistance) {
					bestNeuron = new int[] { i, j };
					minDistance = distance;
				}
			}
		}
		if (bestNeuron == null) {
			System.out.println();
		}
		return bestNeuron;
	}

	public OutputNeuron[] getWinnerNeighbourhood(double[] inputVector, double radius) {
		int[] winnerId = getIndexWinnerNeuron(inputVector);
		OutputNeuron winner = neurons[winnerId[0]][winnerId[1]];
		List<OutputNeuron> neighbours = new ArrayList<>();
		neighbours.add(winner);
		for (int i = 0; i < neurons.length; i++) {
			for (int j = 0; j < neurons[i].length; j++) {
				OutputNeuron neuron = neurons[i][j];
				double distance = distance(winnerId, new int[] { i, j });// winner.getDistance(neuron);
				if (distance < radius)
					neighbours.add(neuron);
			}
		}
		OutputNeuron[] temp = new OutputNeuron[neighbours.size()];
		return neighbours.toArray(temp);
	}

	public double distance(int[] neuron1, int[] neuron2) {
		double i1 = (double) neuron1[0] / (double) neurons.length;
		double i2 = (double) neuron2[0] / (double) neurons.length;
		double j1 = (double) neuron1[1] / (double) neurons[0].length;
		double j2 = (double) neuron2[1] / (double) neurons[0].length;
		return Math.sqrt((i1 - i2) * (i1 - i2) + (j1 - j2) * (j1 - j2));
	}

	private void initateNeurons(int n) {
		if (neurons != null)
			for (int i = 0; i < neurons.length; i++)
				for (int j = 0; j < neurons[i].length; j++)
					neurons[i][j] = new OutputNeuron(n);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("CompetitiveLayer[ \r\n");
		for (int i = 0; i < neurons.length; i++) {
			for (int j = 0; j < neurons[i].length; j++) {
				sb.append(neurons[i][j]);
				if (j + 1 != neurons[i].length)
					sb.append(", ");
			}
			sb.append("\r\n");
		}
		sb.append("]");
		return sb.toString();
	}
}
