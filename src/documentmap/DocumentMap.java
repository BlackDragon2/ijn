package documentmap;

import io.csv.CSVReader;
import io.csv.CSVWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import som.SelfOrganizingMap;
import som.neighbourhood.GaussNeighbourhood;
import bayes.Bayes;
import documentmap.document.Document;

public class DocumentMap {
	private SelfOrganizingMap som;
	private final double maxR = 100000; //TODO
	private final double minR = 100; //TODO
	private final double tMax = 10000000; //TODO
	
	public DocumentMap(int inputLength, int mapWidth, int mapHeight){
		som = new SelfOrganizingMap(inputLength, mapHeight, mapWidth);
	}
	
	public void createMap(String[] args, List<Document> documents, List<Document> learningDocs)
	{
		if(learningDocs!=null)
			createBayesFile(args[1], learningDocs, true);
		createBayesFile(args.length==8?args[7]:args[6], documents, false);
		double[][] probabilities = classifyDocuments(args);
		som.learn(maxR, minR, tMax, probabilities, new GaussNeighbourhood());
	}

	public List<int[]> mapDocuments(String[] args, List<Document> documents, List<Document> learningDocs)
	{
		if(learningDocs!=null)
			createBayesFile(args[1], learningDocs, true);
		createBayesFile(args.length==8?args[7]:args[6], documents, false);
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
	
	private void createBayesFile(String file, List<Document> documents, boolean withClasses)
	{
		try
		{
			CSVWriter writer=new CSVWriter();
			CSVReader reader=new CSVReader();
			writer.open(file);
			List<String> list=new LinkedList<String>();
			if(withClasses)
			{
				setClasses(list);
				writer.write(list);
				list.clear();
			}
			for(Document d: documents)
			{
				reader.open(d.getCsvFile());
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

	private void setClasses(List<String> list) 
	{
		list.add("Blogs");
		//...
		//TODO
		
	}	
}
