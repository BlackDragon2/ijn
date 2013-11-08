package bayes.enums;

/**
 * Enum representing way of counting probability. NONE (docSom.pdf) with no smoothing, SIMPLE_SMOOTHING (DIS) with laplace smoothing 
 * or PROBABILITY_SMOOTHING (needs extra param)
 * @author Bartek
 * @version 1.0
 */
public enum ConProbabilityMode 
{
	NONE,
	SIMPLE_SMOOTHING,
	PROBABILITY_SMOOTHING;	
}
