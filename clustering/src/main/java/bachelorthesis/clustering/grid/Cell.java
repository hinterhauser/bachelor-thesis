package bachelorthesis.clustering.grid;

import bachelorthesis.clustering.data.DataPoint;

import java.util.ArrayList;
import java.util.List;

public class Cell implements StatsObj {

    private int dim;
    private List<DataPoint> dataPoints;
    private double center[];

    private double mean;
    private double deviation;

    public Cell() {
        // currently only used for testing
        dataPoints = new ArrayList<>();
    }

    public Cell(ArrayList<DataPoint> dataPoints, int dim, double[] center) {

        setDataPoints(dataPoints);
        setDim(dim);
        setCenter(center);
        calculateDeviationAndMean();
    }

    public double getMean() {
        return mean;
    }

    public double getDeviation() {
        return deviation;
    }

    public double[] getCenter() {
        return center;
    }

    public void setCenter(double[] center) {
        this.center = center;
    }

    public int getDim() {
        return dim;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    private void calculateMean() {

        double sum = 0.0;
        if (dataPoints.size() != 0) {
            for (DataPoint dataPoint : dataPoints) {
                sum += calculateCenterDistance(dataPoint);
            }
            sum /= dataPoints.size();
        }
        mean = sum;
    }

    @Override
    public void calculateDeviationAndMean() {

        calculateMean();
        double sum = 0.0;
        if (dataPoints.size() != 0) {
            for (DataPoint dataPoint : dataPoints) {
                double dist = calculateCenterDistance(dataPoint);
                double partDiff = dist - mean;
                sum += Math.pow(partDiff, 2.0);
            }
            sum /= dataPoints.size();
        }
        deviation = Math.sqrt(sum);
    }

    @Override
    public double calculateCV() {
        if (mean == 0.0) {
            return 0.0;
        }
        return deviation / mean * 100;
    }

    private double calculateCenterDistance(DataPoint dataPoint) {

        double sum = 0.0;
        for (int i = 0; i < dim; ++i) {

            double partSum = dataPoint.getVector()[i] - center[i];
            sum += Math.pow(partSum, 2.0);
        }
        return Math.sqrt(sum);
    }

    public void emptyCell() {
        dataPoints.clear();
    }
}
