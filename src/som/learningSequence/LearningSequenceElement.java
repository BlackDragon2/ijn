package som.learningSequence;

import java.io.File;
import java.io.Serializable;
import java.util.regex.Pattern;

public class LearningSequenceElement implements Serializable {
	private static final long serialVersionUID = 1L;
	private String _path;
	private double[] _featureVector;

	public LearningSequenceElement(String path, double[] feats) {
		_path = path;
		_featureVector = feats;
	}

	public String getPath() {
		return _path;
	}

	public String getName() {
		String[] subDirs = _path.split(Pattern.quote(File.separator));
		return subDirs[subDirs.length - 1];
	}

	public double[] getFeatures() {
		return _featureVector;
	}

	public String toString() {
		return getName();
	}
}