package som.neurons;

import java.io.Serializable;
import java.util.Random;

import som.neighbourhood.NeighbourhoodFunction;

public class OutputNeuron implements Serializable {
	private static final long serialVersionUID = 8324696852042840669L;
	private double[] weights;

	public OutputNeuron(int n) {
		weights = new double[n];
		initateWeights();
	}

	public void setWeight(int i, double d) {
		weights[i] = d;
	}

	public double[] getWeights() {
		return weights;
	}

	public double getDistance(double[] inputVector) {
		return euclideanDistance(inputVector);
		// return scalarDistance(inputVector);
		// return manhattanDistance(inputVector);
		// return normLDistance(inputVector);
	}

	public void modifyWieghts(double[] inputVector, double radius, NeighbourhoodFunction function) {
		if (inputVector.length != weights.length)
			throw new IllegalArgumentException();
		double distance = getDistance(inputVector);
		for (int i = 0; i < inputVector.length; i++)
			weights[i] += function.neighbourhoodFunction(distance, radius) * (inputVector[i] - weights[i]);
	}

	private void initateWeights() {
		Random rand = new Random();
		for (int i = 0; i < weights.length; i++) {
			weights[i] = rand.nextDouble();
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("O(");
		for (int i = 0; i < weights.length; i++) {
			sb.append(weights[i]);
			if (i + 1 != weights.length) {
				sb.append(", ");
			}
		}
		sb.append(")");
		return sb.toString();
	}

	private double euclideanDistance(double[] inputVector) {
		if (inputVector.length != weights.length)
			throw new IllegalArgumentException();
		double sum = 0;
		for (int i = 0; i < inputVector.length; i++)
			sum += (inputVector[i] - weights[i]) * (inputVector[i] - weights[i]);
		if (Math.sqrt(sum) == Double.NaN)
			System.out.println();
		return Math.sqrt(sum);
	}

	protected double scalarDistance(double[] inputVector) {
		if (inputVector.length != weights.length)
			throw new IllegalArgumentException();
		double sum = 0;
		for (int i = 0; i < inputVector.length; i++) {
			sum += weights[i] * inputVector[i];
		}
		return sum;
	}

	protected double manhattanDistance(double[] inputVector) {
		if (inputVector.length != weights.length)
			throw new IllegalArgumentException();
		double sum = 0;
		for (int i = 0; i < inputVector.length; i++) {
			sum += Math.abs(weights[i] - inputVector[i]);
		}
		return sum;
	}

	protected double normLDistance(double[] inputVector) {
		if (inputVector.length != weights.length)
			throw new IllegalArgumentException();
		double max = 0;
		for (int i = 0; i < inputVector.length; i++) {
			double abs = Math.abs(weights[i] - inputVector[i]);
			if (abs > max)
				max = abs;
		}
		return max;
	}
}
