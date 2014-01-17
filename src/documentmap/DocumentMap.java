package documentmap;

import io.csv.CSVReader;
import io.csv.CSVWriter;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import som.SelfOrganizingMap;
import som.neighbourhood.GaussNeighbourhood;
import bayes.Bayes;
import bayes.params.BayesParams;
import documentmap.document.Document;

public class DocumentMap implements Serializable {
	private static final long serialVersionUID = -4662168227594214699L;
	private SelfOrganizingMap som;
	private final double maxR = 0.9; // TODO
	private final double minR = 0.02; // TODO
	private final double tMax = 100000; // TODO

	public DocumentMap(int inputLength, int mapWidth, int mapHeight) {
		som = new SelfOrganizingMap(inputLength, mapHeight, mapWidth);
	}

	public void createMap(BayesParams params, List<Document> documents, List<Document> learningDocs, List<String> classes) {
		if (learningDocs != null)
			createBayesFile(params.get_learningFile(), learningDocs, true, classes);
		createBayesFile(params.get_observations(), documents, false, classes);
		double[][] probabilities = classifyDocuments(params);
		for (int i = 0; i < probabilities.length; i++)
			for (int j = 0; j < probabilities[i].length; j++)
				probabilities[i][j] = Math.exp(probabilities[i][j]);
		som.learn(maxR, minR, tMax, probabilities, new GaussNeighbourhood());
	}

	public void createMap(double[][] vectors) {
		som.learn(maxR, minR, tMax, vectors, new GaussNeighbourhood());
	}

	public List<int[]> mapDocuments(BayesParams params, List<Document> documents, List<Document> learningDocs) {
		if (learningDocs != null)
			createBayesFile(params.get_learningFile(), learningDocs, true, null);
		createBayesFile(params.get_observations(), documents, false, null);
		double[][] probabilities = classifyDocuments(params);
		List<int[]> result = new ArrayList<>();
		for (int i = 0; i < probabilities.length; i++) {
			som.setInput(probabilities[i]);
			result.add(som.getOutputIndex());
		}
		return result;
	}

	public List<int[]> mapDocuments(List<Document> documents, File dict) throws IOException {
		double[][] features = Document.vectorizeDocuments(documents, dict);
		List<int[]> result = new ArrayList<>();
		for (int i = 0; i < features.length; i++) {
			System.out.println("feats: " + i);
			som.setInput(features[i]);
			result.add(som.getOutputIndex());
		}
		return result;
	}

	private double[][] classifyDocuments(BayesParams params) {
		return Bayes.run(params);
	}

	public int getMapWidth() {
		return som.getWidth();
	}

	public int getMapHeight() {
		return som.getHeight();
	}

	private void createBayesFile(File file, List<Document> documents, boolean withClasses, List<String> classes) {
		try {
			CSVWriter writer = new CSVWriter();
			CSVReader reader = new CSVReader();
			writer.open(file);
			List<String> list = new LinkedList<String>();
			if (withClasses)
				writer.write(classes);
			for (Document d : documents) {
				reader.open(d.getCsvFile());
				if (!withClasses)
					reader.readRow();
				while (reader.hasNext())
					list.addAll(reader.readRow());
				writer.write(list);
				list.clear();
				reader.close();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createVectors(File destination, File dictionary, List<Document> documents) {
		try {
			HashMap<String, Double> map = new HashMap<String, Double>();
			CSVWriter writer = new CSVWriter();
			CSVReader reader = new CSVReader();
			reader.open(dictionary);
			List<String> words = reader.readRow();
			reader.close();
			List<String> temp = new LinkedList<String>();
			List<Double> vector = new LinkedList<Double>();
			writer.open(destination);
			for (Document d : documents) {
				reader.open(d.getCsvFile());
				reader.readRow();
				while (reader.hasNext()) {
					temp = reader.readRow();
					map.put(temp.get(0), Double.parseDouble(temp.get(1)));
				}
				double sum = 0;
				for (String w : words) {
					if (map.get(w) != null) {
						vector.add(map.get(w));
						sum += map.get(w);
					} else
						vector.add(0.0);

				}
				for (int i = 0; i < vector.size(); i++) {
					vector.set(i, vector.get(i) / sum);
				}
				writer.write(vector);
				vector.clear();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}