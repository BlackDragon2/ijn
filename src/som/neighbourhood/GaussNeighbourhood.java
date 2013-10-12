package som.neighbourhood;

public class GaussNeighbourhood implements NeighbourhoodFunction {
	private static final long serialVersionUID = -5869823617560406684L;

	public double neighbourhoodFunction(double distance, double radius) {
		return Math.exp(-(distance * distance) / (2 * radius * radius));
	}
}