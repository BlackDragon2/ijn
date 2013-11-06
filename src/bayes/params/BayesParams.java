package bayes.params;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import bayes.enums.BayesMode;
import bayes.enums.CategoryProbabilityMode;
import bayes.enums.ConProbabilityMode;
import bayes.enums.RunningMode;

public class BayesParams 
{
	private RunningMode _runningMode;
	private File _learningFile;//file with training set or with probabilities 
	private boolean _withSave;
	private BayesMode _bayesMode;
	private CategoryProbabilityMode _catProbMode;
	private ConProbabilityMode _probMode;
	private double _probParam=0; //k or m in smoothing
	private File _observations;
	
	private final String _LEARNING_FILE="C:\\Users\\Bartek\\Desktop\\values.csv";
	private final String _OBSERVATIONS="C:\\Users\\Bartek\\Desktop\\test.csv";
	
	public BayesParams(String[] params) 
	{
		_runningMode=RunningMode.valueOf(params[0]);
		if(_runningMode==RunningMode.LEARN_ONLY)
		{
			_learningFile=new File(params[1]);
			_withSave=params[2].equals("s");
			_bayesMode=BayesMode.valueOf(params[3]);
			_catProbMode=CategoryProbabilityMode.valueOf(params[4]);
			_probMode=ConProbabilityMode.valueOf(params[5]);
			if(_probMode==ConProbabilityMode.PROBABILITY_SMOOTHING)
				_probParam=Double.parseDouble(params[6]);			
		}
		if(_runningMode==RunningMode.LEARN_AND_CLASSIFY)
		{
			_learningFile=new File(params[1]);
			_withSave=params[2].equals("s");
			_bayesMode=BayesMode.valueOf(params[3]);
			_catProbMode=CategoryProbabilityMode.valueOf(params[4]);
			_probMode=ConProbabilityMode.valueOf(params[5]);
			if(_probMode==ConProbabilityMode.PROBABILITY_SMOOTHING)
			{
				_probParam=Double.parseDouble(params[6]);
				_observations=new File(params[7]);
			}
			else
				_observations=new File(params[6]);
		}
		if(_runningMode==RunningMode.CLASSIFY_ONLY)
		{
			_learningFile=new File(params[1]);
			_bayesMode=BayesMode.valueOf(params[2]);
			_observations=new File(params[3]);
		}
	}
	
	public RunningMode get_runningMode() {
		return _runningMode;
	}

	public void set_runningMode(RunningMode _runningMode) {
		this._runningMode = _runningMode;
	}

	public File get_learningFile() {
		return _learningFile;
	}

	public void set_learningFile(File _learningFile) {
		this._learningFile = _learningFile;
	}

	public BayesMode get_bayesMode() {
		return _bayesMode;
	}

	public void set_bayesMode(BayesMode _bayesMode) {
		this._bayesMode = _bayesMode;
	}

	public ConProbabilityMode get_probMode() {
		return _probMode;
	}

	public void set_probMode(ConProbabilityMode _probMode) {
		this._probMode = _probMode;
	}

	public double get_probParam() {
		return _probParam;
	}

	public void set_probParam(double _probParam) {
		this._probParam = _probParam;
	}

	public File get_observations() {
		return _observations;
	}

	public void set_observations(File _observations) {
		this._observations = _observations;
	}

	public BayesParams(RunningMode mode)
	{
		setDefault(mode);
	}
	
	private void setDefault(RunningMode mode) 
	{
		_runningMode=mode;
		if(_runningMode==RunningMode.LEARN_ONLY)
		{
			_learningFile=new File(_LEARNING_FILE);
			_withSave=true;
			_bayesMode=BayesMode.MULTINOMINAL;
			_catProbMode=CategoryProbabilityMode.BY_WORD;
			_probMode=ConProbabilityMode.SIMPLE_SMOOTHING;
			_probParam=1;			
		}
		if(_runningMode==RunningMode.LEARN_AND_CLASSIFY)
		{
			_learningFile=new File(_LEARNING_FILE);
			_withSave=true;
			_bayesMode=BayesMode.MULTINOMINAL;
			_catProbMode=CategoryProbabilityMode.BY_WORD;
			_probMode=ConProbabilityMode.SIMPLE_SMOOTHING;
			_probParam=1;	
			_observations=new File(_OBSERVATIONS);
			
		}
		if(_runningMode==RunningMode.CLASSIFY_ONLY)
		{
			_learningFile=new File(_LEARNING_FILE);
			_bayesMode=BayesMode.MULTINOMINAL;
			_observations=new File(_OBSERVATIONS);
		}		
	}

	public BayesParams(File file)
	{
		try 
		{
			Scanner scanner=new Scanner(file);
			_runningMode=RunningMode.valueOf(scanner.next());
			if(_runningMode==RunningMode.LEARN_ONLY)
			{
				_learningFile=new File(scanner.next());
				_withSave=scanner.next().equals("s");
				_bayesMode=BayesMode.valueOf(scanner.next());
				_catProbMode=CategoryProbabilityMode.valueOf(scanner.next());
				_probMode=ConProbabilityMode.valueOf(scanner.next());
				if(_probMode==ConProbabilityMode.PROBABILITY_SMOOTHING)
					_probParam=Double.parseDouble(scanner.next());			
			}
			if(_runningMode==RunningMode.LEARN_AND_CLASSIFY)
			{
				_learningFile=new File(scanner.next());
				_withSave=scanner.next().equals("s");
				_bayesMode=BayesMode.valueOf(scanner.next());
				_catProbMode=CategoryProbabilityMode.valueOf(scanner.next());
				_probMode=ConProbabilityMode.valueOf(scanner.next());
				if(_probMode==ConProbabilityMode.PROBABILITY_SMOOTHING)
					_probParam=Double.parseDouble(scanner.next());		
				_observations=new File(scanner.next());
				
			}
			if(_runningMode==RunningMode.CLASSIFY_ONLY)
			{
				_learningFile=new File(scanner.next());
				_bayesMode=BayesMode.valueOf(scanner.next());		
				_observations=new File(scanner.next());
			}	
			scanner.close();
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Loadning parameters from file failed");
			setDefault(RunningMode.LEARN_AND_CLASSIFY);
			e.printStackTrace();
		}
	}
	
	public CategoryProbabilityMode get_catProbMode() {
		return _catProbMode;
	}

	public void set_catProbMode(CategoryProbabilityMode _catProbMode) {
		this._catProbMode = _catProbMode;
	}

	public boolean is_withSave() {
		return _withSave;
	}

	public void set_withSave(boolean _withSave) {
		this._withSave = _withSave;
	}

	public BayesParams()
	{
	}
}
