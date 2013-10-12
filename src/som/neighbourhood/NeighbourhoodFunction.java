package som.neighbourhood;

import java.io.Serializable;

public interface NeighbourhoodFunction extends Serializable {
	public double neighbourhoodFunction(double distance, double radius);
}