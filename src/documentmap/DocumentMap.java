package documentmap;

import java.util.ArrayList;
import java.util.List;

import som.SelfOrganizingMap;
import som.neighbourhood.GaussNeighbourhood;
import bayes.classifier.BayesClassifier;
import documentmap.document.Document;

public class DocumentMap {
	private SelfOrganizingMap som;
	private BayesClassifier classifier = new BayesClassifier();
	private final double maxR = 100000; //TODO
	private final double minR = 100; //TODO
	private final double tMax = 10000000; //TODO
	
	public DocumentMap(int inputLength, int mapWidth, int mapHeight){
		som = new SelfOrganizingMap(inputLength, mapHeight, mapWidth);
	}
	
	public void createMap(List<Document> documents){
		learnBayesClassifier();
		double[][] probabilities = classifyDocuments(documents);
		som.learn(maxR, minR, tMax, probabilities, new GaussNeighbourhood());
	}
	
	public List<int[]> mapDocuments(List<Document> documents){
		double[][] probabilities = classifyDocuments(documents);
		List<int[]> result = new ArrayList<>();
		for(int i =0; i < probabilities.length; i++){
			som.setInput(probabilities[i]);
			result.add(som.getOutputIndex());
		}
		return result;
	}

	private double[][] classifyDocuments(List<Document> documents) {
		// TODO Bartek, do your magic
		return null;
	}

	private void learnBayesClassifier() {
		// TODO Bartek, do your magic
	}
	
}
