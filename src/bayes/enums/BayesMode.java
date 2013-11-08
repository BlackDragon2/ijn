package bayes.enums;

/**
 * Enum representing two aproaches for calculating Bayes theorem. Standard multinominal treats data as binary (if feature exist or not),
 * multivariable takes number for occurances into consideration. 
 * @author Bartek
 * @version 1.0
 */
public enum BayesMode 
{
	MULTINOMINAL,
	MULTIVARIABLE;
}
