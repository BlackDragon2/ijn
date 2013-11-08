package bayes;

import java.io.File;

import bayes.classifier.BayesClassifier;
import bayes.enums.RunningMode;
import bayes.learner.BayesLearner;
import bayes.params.BayesParams;

/**
 * Main class for managing Naive Bayes Classifier.
 * @author Bartek
 * @version 1.0
 */
public class Bayes 
{
	/**
	 * Method starting demanded (by params) operations. Public only for debug purpose.
	 * @param params
	 */
	public static void run(BayesParams params) 
	{
		BayesLearner learner=BayesLearner.getLearner();
		BayesClassifier classifier=BayesClassifier.getClassifier();
		if(params.get_runningMode()==RunningMode.LEARN_AND_CLASSIFY)
		{
			learner.learn(params);
			printResult(classifier.classify(learner, params));
		}
		if(params.get_runningMode()==RunningMode.LEARN_ONLY)
		{
			learner.learn(params);
		}
		if(params.get_runningMode()==RunningMode.CLASSIFY_ONLY)
		{
			learner.loadProbabilities(params);
			printResult(classifier.classify(learner, params));
		}		
	}

	/**
	 * Coders only! Method printing classification results for debug purpose.
	 * @param Probabilities to be printed onto console.
	 */
	private static void printResult(double[][] tab) 
	{
		String result="";
		for(int i=0;i<tab.length;i++)
		{
			for(int j=0;j<tab[i].length;j++)
			{
				result+=tab[i][j]+"  ";
			}
			System.out.println(result);
			result="";
		}		
	}

	/**
	 * Method transforms params passed in args into valid BayesParams and then calling true run method.
	 * @param args
	 */
	public static void run(String[] args) 
	{
		BayesParams params;
		switch(args.length)
		{
			case 0: params=new BayesParams(RunningMode.LEARN_AND_CLASSIFY);
			break;
			case 1:
			{
				try
				{
					params=new BayesParams(RunningMode.valueOf(args[0]));
				}
				catch(IllegalArgumentException e)
				{
					params=new BayesParams(new File(args[0]));
				}
			}
			break;
			default: params=new BayesParams(args);
		}
		run(params);
	}
	

}
