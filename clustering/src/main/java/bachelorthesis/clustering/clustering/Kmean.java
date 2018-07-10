package bachelorthesis.clustering.clustering;

import bachelorthesis.clustering.data.DataPoint;
import bachelorthesis.clustering.statistics.VectorMath;

import java.util.ArrayList;
import java.util.List;

public class Kmean {

    private int k;
    private ClusterDBSCAN[] clusters;
    private List<DataPoint> dataPoints;
    private List<double[]> centroids;

    public Kmean(int k, List<DataPoint> dataPoints) {

        this.k = k;
        this.dataPoints = dataPoints;
        clusters = new ClusterDBSCAN[k];
        centroids = new ArrayList<>();
        findCentroids();
    }

    private void findCentroids() {

        int n = dataPoints.size() / k;
        int dim = dataPoints.get(0).getDim();
        for (int i = 0; i < n; ++i) {
            double[] centroid = new double[dim];
            VectorMath.fillVectorWithZeros(centroid);
            for (int j = i * n; j < (i+1) * n; ++j) {
                for (int k = 0; k < dim; ++k) {

                    centroid[k] += dataPoints.get(j).getVector()[k];
                }
            }
            for (int l = 0; l < dim; ++l) {
                centroid[l] /= n;
            }
            centroids.add(centroid);
        }
    }

    public void performKmeans() {


    }
}
