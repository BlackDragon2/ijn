package som;

import java.awt.Dimension;
import java.io.Serializable;

import som.layers.CompetitiveLayer;
import som.layers.InputLayer;
import som.learningSequence.LearningSequenceElement;
import som.neighbourhood.NeighbourhoodFunction;
import som.neurons.OutputNeuron;

public class SelfOrganizingMap implements Serializable {
	private static final long serialVersionUID = -1724020710085170904L;
	private InputLayer inputLayer;
	private CompetitiveLayer competitiveLayer;

	public SelfOrganizingMap(int inputCount, int outRows, int outColumns) {
		inputLayer = new InputLayer(inputCount);
		competitiveLayer = new CompetitiveLayer(outRows, outColumns, inputCount);
	}

	public void setInput(double[] d) {
		inputLayer.setInput(d);
	}

	public double[][][] getAllOutputVectors() {
		return competitiveLayer.getVectors();
	}

	public Dimension getCompetitiveLayerSize() {
		return competitiveLayer.getSize();
	}

	public int getInputLayerLength() {
		return inputLayer.getLength();
	}

	public <T> LearningSequenceElement<T> getClosestElement(double[] features, LearningSequenceElement<T>[] elements) {
		if (elements == null || features == null || elements.length == 0)
			throw new IllegalArgumentException();
		setInput(features);
		OutputNeuron closest = competitiveLayer.getWinnerNeuron(inputLayer.getInputVector());
		double minDistance = Double.MAX_VALUE;
		LearningSequenceElement<T> bestElement = null;
		for (int i = 0; i < elements.length; i++) {
			double[] elemFeatures = elements[i].getFeatures();
			setInput(elemFeatures);
			double distance = closest.getDistance(inputLayer.getInputVector());
			if (distance < minDistance) {
				minDistance = distance;
				bestElement = elements[i];
			}
		}
		return bestElement;
	}

	public <T> LearningSequenceElement<T> getPhysicallyClosestElement(double[] features, LearningSequenceElement<T>[] elements) {
		if (elements == null || features == null || elements.length == 0)
			throw new IllegalArgumentException();
		setInput(features);
		int[] closest = competitiveLayer.getIndexWinnerNeuron(inputLayer.getInputVector());
		int[][] elementNeurons = new int[elements.length][];
		for (int i = 0; i < elementNeurons.length; i++) {
			setInput(elements[i].getFeatures());
			elementNeurons[i] = competitiveLayer.getIndexWinnerNeuron(inputLayer.getInputVector());
		}
		double minDistance = Double.MAX_VALUE;
		LearningSequenceElement<T> bestElement = null;
		for (int i = 0; i < elementNeurons.length; i++) {
			int[] elemPosition = elementNeurons[i];
			double distance = competitiveLayer.distance(closest, elemPosition);
			if (distance < minDistance) {
				minDistance = distance;
				bestElement = elements[i];
			}
		}
		return bestElement;
	}

	public double[] getOutput() {
		return competitiveLayer.getWinnerNeuron(inputLayer.getInputVector()).getWeights();
	}
	
	public int[] getOutputIndex(){
		return competitiveLayer.getIndexWinnerNeuron(inputLayer.getInputVector());
	}

	public void learn(double maxR, double minR, double tMax, double[][] inputVectors, NeighbourhoodFunction function) {
		if (maxR < 0 || maxR > Math.sqrt(2) || minR > maxR || minR < 0 || minR > 1 || tMax < 0)
			throw new IllegalArgumentException();
		double radiusRatio = minR / maxR;
		for (int i = 0; i < tMax; i++) {
			double radius = maxR * Math.pow(radiusRatio, i / (tMax - 1));
			inputLayer.setInput(inputVectors[i % inputVectors.length]);
			double[] inputVector = inputLayer.getInputVector();
			OutputNeuron[] winnerNeurons = competitiveLayer.getWinnerNeighbourhood(inputVector, radius);
			for (OutputNeuron neuron : winnerNeurons)
				neuron.modifyWieghts(inputVector, radius, function);
		}
	}

	public String toString() {
		return competitiveLayer + "\r\n" + inputLayer;
	}
	
	public int getWidth(){
		return competitiveLayer.getSize().width;
	}
	
	public int getHeight(){
		return competitiveLayer.getSize().height;
	}
}