package documentmap;

import io.csv.CSVReader;
import io.csv.CSVWriter;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import som.SelfOrganizingMap;
import som.learningSequence.LearningSequenceElement;
import som.neighbourhood.GaussNeighbourhood;
import bayes.Bayes;
import documentmap.document.Document;

public class DocumentMap implements Serializable{
	private static final long serialVersionUID = -4662168227594214699L;
	private SelfOrganizingMap som;
	private final double maxR = 0.9; //TODO
	private final double minR = 0.02; //TODO
	private final double tMax = 100000; //TODO
	private List<LearningSequenceElement<Document>> elements;
	
	public DocumentMap(int inputLength, int mapWidth, int mapHeight){
		som = new SelfOrganizingMap(inputLength, mapHeight, mapWidth);
	}
	
	public void createMap(String[] args, List<Document> documents, List<Document> learningDocs, List<String> classes)
	{
		if(learningDocs!=null)
			createBayesFile(args[1], learningDocs, true, classes);
		createBayesFile(args.length==8?args[7]:args[6], documents, false, classes);
		double[][] probabilities = classifyDocuments(args);
		for(int i=0;i<probabilities.length;i++)
			for(int j=0;j<probabilities[i].length;j++)
				probabilities[i][j]=Math.exp(probabilities[i][j]);
		elements = createLearningSequenceElementsList(probabilities, documents);
		som.learn(maxR, minR, tMax, probabilities, new GaussNeighbourhood());
	}

	public List<int[]> mapDocuments(String[] args, List<Document> documents, List<Document> learningDocs)
	{
		if(learningDocs!=null)
			createBayesFile(args[1], learningDocs, true, null);
		createBayesFile(args.length==8?args[7]:args[6], documents, false, null);
		double[][] probabilities = classifyDocuments(args);
		List<int[]> result = new ArrayList<>();
		for(int i =0; i < probabilities.length; i++){
			som.setInput(probabilities[i]);
			result.add(som.getOutputIndex());
		}
		return result;
	}
	
	private List<LearningSequenceElement<Document>> createLearningSequenceElementsList(double[][] probabilities, List<Document> documents) {
		List<LearningSequenceElement<Document>> elements = new ArrayList<>();
		for(int i = 0; i<documents.size(); i++) {
			LearningSequenceElement<Document> lse = new LearningSequenceElement<Document>(documents.get(i), probabilities[i]);
			elements.add(lse);
		}
		return elements;
	}

	private double[][] classifyDocuments(String[] args) 
	{
		return Bayes.run(args);
	}
	
	public int getMapWidth(){
		return som.getWidth();
	}
	
	public int getMapHeight(){
		return som.getHeight();
	}
	
	public double[][][] getAllOutputVectors() {
		return som.getAllOutputVectors();
	}
	
	public List<LearningSequenceElement<Document>> getElements() {
		return elements;
	}
	
	public Document getClosestDocument(double[] features) {
		@SuppressWarnings("unchecked")
		LearningSequenceElement<Document>[] docs = new LearningSequenceElement[elements.size()];
		elements.toArray(docs);
		LearningSequenceElement<Document> closestDoc = som.getClosestElement(features, docs);
		return closestDoc.getObject();
	}
	
	private void createBayesFile(String file, List<Document> documents, boolean withClasses, List<String> classes)
	{
		try
		{
			CSVWriter writer=new CSVWriter();
			CSVReader reader=new CSVReader();
			writer.open(file);
			List<String> list=new LinkedList<String>();
			if(withClasses)
				writer.write(classes);
			for(Document d: documents)
			{
				reader.open(d.getCsvFile());
				if(!withClasses)
					reader.readRow();
				while(reader.hasNext())
					list.addAll(reader.readRow());
				writer.write(list);
				list.clear();
				reader.close();
			}
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
