package bayes.classifier;

import io.csv.CSVReader;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import bayes.enums.BayesMode;
import bayes.learner.BayesLearner;
import bayes.pair.Pair;
import bayes.params.BayesParams;

/**
 * Class classifying given examples using given BayesLearner. As learner is singleton it is also singleton.
 * @author Bartek
 * @version 1.0
 */
public final class BayesClassifier 
{
	//instance of classifier
	private final static BayesClassifier _instance = new BayesClassifier();
	//const minimum probability (used in case an attribute did not occurred in category in learning sequence
	private final static double _MIN_PROBIBILITY=0.00000001;
	
	/**
	 * Method classifying set of observations.
	 * @param learner Bayes learner which was taught for current problem.
	 * @param bayesMode Enum indicating if bayes should calculate values as binary occurrences or take number of occurrences into consideration.
	 * @param file CSV file with list of attributes in one row of every observation to be classified.
	 * @return Two-dimensional array of observations in rows and probability for each class in every column.
	 */
	public double[][] classify(BayesLearner learner, BayesMode bayesMode, File file)
	{
		double[][] result=null;
		CSVReader reader=new CSVReader();
		try 
		{
			reader.open(file);		
			result=new double[CSVReader.rowsCount()][learner.getNumberOfCategories()];
			//for every observation
			for(int i=0;i<result.length;i++)
			{
				//get attributes
				List<String> attributes=reader.readRow();
				//find probability
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
	
	/**
	 * Method finding probabilities of being an instance of each category for an observation.
	 * @param attributes List of attributes of an observation.
	 * @param bayesMode  Enum indicating if bayes should calculate values as binary occurrences or take number of occurrences into consideration.
	 * @param learner Bayes learner which was taught for current problem.
	 * @return Array of probabilities of observation being an instance of each category.
	 */
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
				//if nominal treat number of occurrences as binary 1 else as true number
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
				//if an attribute did not appear in training set with a category or did not appear at all 
				//it would nullify all probabilities. To prevent minimal probability is returned instead.
				probability=probability==0?_MIN_PROBIBILITY:probability;
				//counting as sum of logs [1]DIS
				result[i]+=Math.log(value*probability);
			}
			result[i]/=sum;
			//add P(C)
			result[i]+=Math.log(learner.getCategoryProbability(category));
			sum=0;
			i++;
		}
		return result;
	}

	/**
	 * Gets instance of classifier.
	 * @return Instance of classifier.
	 */
	public static BayesClassifier getClassifier()
	{
		return _instance;
	}

	/**
	 * Method passing needed params from BayesParams to classifying method and returning its results.
	 * @param learner Bayes learner which was taught for current problem.
	 * @param params Bayes params.
	 * @return Two-dimensional array of observations in rows and probability for each class in every column.
	 */
	public double[][] classify(BayesLearner learner, BayesParams params) 
	{
		return classify(learner, params.get_bayesMode(), params.get_observations());
	}	
}
