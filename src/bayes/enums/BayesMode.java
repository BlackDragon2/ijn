package bayes.enums;

/**
 * Enums representing two aproaches for calculating Bayes theorem. Standard multinominal treats data as binary (if feature exist or not),
 * multivariable takes number for occurances into consideration. 
 * @author Bartek
 *
 */
public enum BayesMode 
{
	MULTINOMINAL,
	MULTIVARIABLE;

}
