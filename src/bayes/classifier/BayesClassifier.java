package bayes.classifier;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import CSVIO.CSVReader;
import bayes.enums.BayesMode;
import bayes.pair.Pair;
import bayes.params.BayesParams;
import bayesLearner.BayesLearner;

public final class BayesClassifier 
{
	private final static BayesClassifier _instance = new BayesClassifier();
	private final static double _MIN_PROBIBILITY=0.00000001;
	 
	public double[][] classify(BayesLearner learner, BayesMode bayesMode, File file)
	{
		double[][] result=null;
		CSVReader reader=new CSVReader();
		try 
		{
			reader.open(file);		
			result=new double[CSVReader.rowsCount()][learner.getNumberOfCategories()];
			for(int i=0;i<result.length;i++)
			{
				List<String> attributes=reader.readRow();
				result[i]=classify(attributes, bayesMode, learner);
			}
		} 
		catch (IOException e) 
		{
			System.out.println("Error reading observations");
			e.printStackTrace();
		}
		return result;
	}
	
	private double[] classify(List<String> attributes, BayesMode bayesMode, BayesLearner learner) 
	{
		double[] result=new double[learner.getNumberOfCategories()];
		ListIterator<String> iterator;
		String attribute;
		double value;
		double sum=0;
		double probability=0;
		int i=0;
		Collection<String> categories=learner.getCategories();
		for(String category: categories)
		{
			iterator=attributes.listIterator();
			while(iterator.hasNext())
			{
				attribute=iterator.next();
				if(bayesMode==BayesMode.MULTINOMINAL)
				{
					iterator.next();
					value=1;
					sum+=1;
				}
				else
				{
					value=Double.parseDouble(iterator.next());
					sum+=value;
				}
				probability=learner.getProbability(new Pair<String, String>(category, attribute));
				probability=probability==0?_MIN_PROBIBILITY:probability;
				result[i]+=Math.log(value*probability);
			}
			result[i]/=sum;
			result[i]+=Math.log(learner.getCategoryProbability(category));
			sum=0;
			i++;
		}
		return result;
	}

	public static BayesClassifier getClassifier()
	{
		return _instance;
	}

	public double[][] classify(BayesLearner learner, BayesParams params) 
	{
		return classify(learner, params.get_bayesMode(), params.get_observations());
	}	
}
