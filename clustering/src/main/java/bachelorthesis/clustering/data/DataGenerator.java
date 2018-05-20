package bachelorthesis.clustering.data;

import java.util.Random;

public class DataGenerator {

	private int dim;	// dimensions are very redundant in the code, could be useful for defensive programming
	private Random seed;
	
	public DataGenerator(int dim) {
		
		this.dim = dim;
		seed = new Random();
	}
	
	public DataPoint generateDataPoint(double[] mean, double deviation) {
		
		double[] point = new double[dim];
		for (int i = 0; i < dim; ++i) {
			point[i] = mean[i] + seed.nextGaussian() * deviation;
		}
		return new DataPoint(dim, point, makeIdentifier(mean, deviation));
	}

	private String makeIdentifier(double[] mean, double deviation) {

		String idString = "mean:";
		for (int i = 0; i < dim; ++i) {
			idString += mean[i] + "/";	// TODO a reimplementation using StringBuilders might be preferable
		}
		return idString + "deviation:" + deviation;
	}
}
