package bayes.learner;

import io.csv.CSVReader;
import io.csv.CSVWriter;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import bayes.enums.BayesMode;
import bayes.enums.CategoryProbabilityMode;
import bayes.enums.ConProbabilityMode;
import bayes.pair.Pair;
import bayes.params.BayesParams;

/**
 * Class teaching Naive Bayes, singleton.
 * @author Bartek
 * @version 1.0
 */
public final class BayesLearner 
{
	//instance of BayesLearner
    private final static BayesLearner _instance = new BayesLearner();
    
    private double _totalCount=0;//total number of attributes
    private double _totalExamples=0;//total number of examples
    private static HashMap<String, Double> _examplesPerCategory;
    private static HashMap<String, Double> _numberOfAttributesPerCategory;//later P(Category)
    private static HashMap<String, Double> _allAttributeOccurances;//later P(x) 
    private static HashMap<Pair<String, String>, Double> _attributesPerCategory;//later P(xi|Category)
    
    /**
     * Method loading training set into appropriate HashMap.
     * @param file CSV file with training set.
     * @param bayesMode
     */
    private void loadExamples(File file, BayesMode bayesMode)
    {
    	init();
    	CSVReader reader=new CSVReader();
    	reader.open(file);
    	List<String> row;    	
		try 
		{
			row = reader.readRow();
			for(String category : row)
			{
				_numberOfAttributesPerCategory.put(category, 0.0);
			}
	    	String category;
	    	String attribute;
	    	Pair<String, String> pair;
	    	double value;
	    	ListIterator<String> iterator;
			while(reader.hasNext())
			{
				row=reader.readRow();
				iterator=row.listIterator();
				category=iterator.next();
				_totalExamples++;
				//increase number of examples for category
				_examplesPerCategory.put(category, _examplesPerCategory.get(category)==null?1:_examplesPerCategory.get(category)+1);
				while(iterator.hasNext())
				{
					attribute=iterator.next();
					value=Double.parseDouble(iterator.next());
					//binary case
					if(bayesMode==BayesMode.MULTINOMINAL)
						value=1;
					
					//number of attributes in training set, increases by 1 if multinominal, else by number of occurances
					_totalCount+=value;
					//number of attributes per category, 1 for every occurance attribute in training set for multinominal, number of occurances in single example for every example for multivarianble
					_numberOfAttributesPerCategory.put(category, _numberOfAttributesPerCategory.get(category)==null?value:_numberOfAttributesPerCategory.get(category)+value);
					//number of attribute occurances in whole training set, 1 every occurance in multinominal, number of occurances for every occurance in multivariable
					_allAttributeOccurances.put(attribute, _allAttributeOccurances.get(attribute)==null?value:_allAttributeOccurances.get(attribute)+value);
					
					pair=new Pair<String, String>(category, attribute);
					//number of attribute occurances for each category, 1 for every occurance in multinominal, number of occurances in multivariable
					_attributesPerCategory.put(pair, _attributesPerCategory.get(pair)==null?value:_attributesPerCategory.get(pair)+value);				
				}
			}
		} 
		catch (IOException e) 
		{
			System.out.println("Error reading learning set");
			e.printStackTrace();
		}
		reader.close();
    }
    
    /**
     * Init HasMaps.
     */
    private void init() 
    {
        _examplesPerCategory=new HashMap<String, Double>();
        _numberOfAttributesPerCategory=new HashMap<String, Double>();
        _allAttributeOccurances=new HashMap<String, Double>(); 
        _attributesPerCategory=new HashMap<Pair<String, String>, Double>();
	}

    /**
     * Loads learnt probabilities from a file.
     * @param params Bayes params.
     */
	public void loadProbabilities(BayesParams params)
    {
		init();
		CSVReader reader=new CSVReader();
		reader.open(params.get_learningFile());
    	List<String> row;    	
		try 
		{
			row = reader.readRow();
	    	ListIterator<String> iterator = row.listIterator();
			while(iterator.hasNext())
			{
				_numberOfAttributesPerCategory.put(iterator.next(), Double.parseDouble(iterator.next()));
			}
			row = reader.readRow();
	    	iterator = row.listIterator();
			while(iterator.hasNext())
			{
				_allAttributeOccurances.put(iterator.next(), Double.parseDouble(iterator.next()));
			}
			Collection<String> categories=_numberOfAttributesPerCategory.keySet();
			Collection<String> attributes=_allAttributeOccurances.keySet();
			Pair<String, String> pair;
			for(String category : categories)
			{
				row = reader.readRow();
		    	iterator = row.listIterator();
				for(String attribute : attributes)
				{
					pair=new Pair<String, String>(category, attribute);
					_attributesPerCategory.put(pair, Double.parseDouble(iterator.next()));
				}
			}			
		}
		catch (IOException e) 
		{
			System.out.println("Error reading learning set");
			e.printStackTrace();
		}
		reader.close();
    }
	
	/**
	 * 
	 * @param file
	 * @param bayesMode
	 * @param probMode
	 * @param catProbMode
	 * @param param
	 * @param withSave
	 */
	private void learn(File file, BayesMode bayesMode, ConProbabilityMode probMode, CategoryProbabilityMode catProbMode, double param, boolean withSave)
	{
		loadExamples(file, bayesMode);
		countPobabilities(bayesMode, probMode, catProbMode, param);
		if(withSave)
			save(file);		
	}
    
    private void save(File dir) 
    {
    	CSVWriter writer=new CSVWriter();
		try 
		{
	    	File file=new File(dir.getParent(), dir.getName().replace(".", "Learned."));
	    	writer.open(file);
	    	
			List<String> line=new LinkedList<String>();
			Collection<String> categories=_numberOfAttributesPerCategory.keySet();
			Collection<String> attributes=_allAttributeOccurances.keySet();
			for(String category : categories)
			{
				line.add(category);
				line.add(_numberOfAttributesPerCategory.get(category).toString());
			}
			writer.write(line);
			line.clear();
			for(String attribute : attributes)
			{
				line.add(attribute);
				line.add(_allAttributeOccurances.get(attribute).toString());
			}
			writer.write(line);
			line.clear();
			for(String category : categories)
			{
				for(String attribute : attributes)
				{
					line.add(_attributesPerCategory.get(new Pair<String, String>(category, attribute)).toString());
				}
				writer.write(line);
				line.clear();
			}
		} 
		catch (IOException e) 
		{
			System.out.println("Error while saving learned set");
			e.printStackTrace();
		}
		writer.close();
	}

	private void countPobabilities(BayesMode bayesMode, ConProbabilityMode probMode, CategoryProbabilityMode catProbMode, double param) 
    {
		Collection<String> categories=_numberOfAttributesPerCategory.keySet();
		Collection<String> attributes=_allAttributeOccurances.keySet();
		Pair<String, String> pair;
		double probability;
		for(String category : categories)
		{
			for(String attribute : attributes)
			{
				pair=new Pair<String, String>(category, attribute);
				if(probMode==ConProbabilityMode.SIMPLE_SMOOTHING)
					param=attributes.size();
				//P(x|Cj)=number of occurances of attribute in training set for Cj/number of all attributes in Cj
				probability=countProbability(probMode, param, _attributesPerCategory.get(pair)==null?0:_attributesPerCategory.get(pair), _numberOfAttributesPerCategory.get(category)==null?0:_numberOfAttributesPerCategory.get(category), _allAttributeOccurances.get(attribute)/_totalCount);
				_attributesPerCategory.put(pair, probability);				
			}
		}
		for(String category : categories)
		{
			if(catProbMode==CategoryProbabilityMode.BY_WORD)
			{
				//counting P(category) as totalAttributesIncategory/Attributes_in_total
				_numberOfAttributesPerCategory.put(category, _numberOfAttributesPerCategory.get(category)/_totalCount);
			}
			else
			{
				//counting P(category) as numberOfExamplesForcategoryInTrainingset/sizeOfTrainingset
				_numberOfAttributesPerCategory.put(category, _examplesPerCategory.get(category)/_totalExamples);
			}				
		}
		for(String attribute : attributes)
		{
			//counting P(x) as numberOfAttributeOccurances/numberOfAllAttributesOccurances
			_allAttributeOccurances.put(attribute, _allAttributeOccurances.get(attribute)/_totalCount);
		}		
	}

	private double countProbability(ConProbabilityMode probMode, double param, double occurances, double wordsInCategory, double fraction) 
	{
		double result = 0;
		if(probMode==ConProbabilityMode.NONE)
		{
			//error when no example of category in trainingset
			result=occurances/wordsInCategory;
		}
		if(probMode==ConProbabilityMode.SIMPLE_SMOOTHING)
		{
			result=(occurances+1)/(wordsInCategory+param);
		}
		if(probMode==ConProbabilityMode.PROBABILITY_SMOOTHING)
		{
			result=(occurances+param*fraction)/(wordsInCategory+param);
		}
		return result;
	}

	public void destroy()
    {
        _examplesPerCategory=null;
        _numberOfAttributesPerCategory=null;
        _allAttributeOccurances=null;
        _attributesPerCategory=null;
    }
	
	public static BayesLearner getLearner()
	{
		return _instance;
	}

	public int getNumberOfCategories() 
	{
		return _numberOfAttributesPerCategory.size();
	}

	public Collection<String> getCategories() 
	{
		return _numberOfAttributesPerCategory.keySet();
	}

	public double getProbability(Pair<String, String> pair) 
	{
		return _attributesPerCategory.get(pair)==null?0.0:_attributesPerCategory.get(pair);
	}

	public double getCategoryProbability(String category) 
	{
		return _numberOfAttributesPerCategory.get(category);
	}

	public void learn(BayesParams params) 
	{
		learn(params.get_learningFile(), params.get_bayesMode(), params.get_probMode(), params.get_catProbMode(), params.get_probParam(), params.is_withSave());
	}
}
