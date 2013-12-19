package documentmap;

import io.csv.CSVReader;
import io.csv.CSVWriter;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import som.SelfOrganizingMap;
import som.neighbourhood.GaussNeighbourhood;
import bayes.Bayes;
import documentmap.document.Document;

public class DocumentMap implements Serializable{
	private static final long serialVersionUID = -4662168227594214699L;
	private SelfOrganizingMap som;
	private final double maxR = 0.9; //TODO
	private final double minR = 0.02; //TODO
	private final double tMax = 100000; //TODO
	
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
