package bayes;

import java.io.File;

import bayes.enums.BayesMode;
import bayes.enums.CategoryProbabilityMode;
import bayes.enums.ConProbabilityMode;
import bayes.enums.RunningMode;
import bayes.params.BayesParams;

/**
 * Running class for Bayes. Parameter should be passed inf args, unless CODE_PARAMS mode is on.
 * @author Bartek
 * @version 1.0
 */
public class BayesRunner 
{
	//testing flag, causing program to read params prepered in getParams(); 
	private static final boolean CODE_PARAMS=true;
	
	public static void main(String[] args)
	{	
		if(CODE_PARAMS)
			Bayes.run(getDefaultParams());
		else
			Bayes.run(args);
	}

	/**
	 * Coders only! Set default params in this method.
	 * @return Default params for Bayes.
	 */
	private static BayesParams getDefaultParams()
	{
		BayesParams params=new BayesParams();
		params.set_runningMode(RunningMode.LEARN_AND_CLASSIFY);
		params.set_bayesMode(BayesMode.MULTINOMINAL);
		params.set_learningFile(new File("C:\\Users\\Bartek\\Desktop\\values.csv")); 
		params.set_withSave(true);
		params.set_probMode(ConProbabilityMode.NONE);
		params.set_catProbMode(CategoryProbabilityMode.BY_EXAMPLES);
		params.set_probParam(1);
		params.set_observations(new File("C:\\Users\\Bartek\\Desktop\\test.csv"));
		return params;
	}
	
}
