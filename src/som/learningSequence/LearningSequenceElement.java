package som.learningSequence;

import java.io.Serializable;

public class LearningSequenceElement<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private T object;
	private double[] _featureVector;

	public LearningSequenceElement(T object, double[] feats) {
		this.object = object;
		_featureVector = feats;
	}

	public T getObject() {
		return object;
	}

	public String getName() {
		return object.toString();
	}

	public double[] getFeatures() {
		return _featureVector;
	}

	public String toString() {
		return getName();
	}
}