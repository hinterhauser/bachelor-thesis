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

    private double[] mu;
    private double[] sigma;

    public Cell() {
        // currently only used for testing
        dataPoints = new ArrayList<>();
    }

    public Cell(List<DataPoint> dataPoints, int dim, double[] center) {

        setDataPoints(dataPoints);
        setDim(dim);
        setCenter(center);
        calculateMuAndSigma();
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

    public double[] getMu() {
        return mu;
    }

    public double[] getSigma() {
        return sigma;
    }

    // ------------------------ end Getter and Setter --------------------------------------

    private void fillVectorWithZeros(double[] vector) {

        for (int i = 0; i < vector.length; ++i) {

            vector[i] = 0.0;
        }
    }

    private void fillMuAndSigmaWithZeros() {

        mu = new double[dim];
        sigma = new double[dim];
        fillVectorWithZeros(mu);
        fillVectorWithZeros(sigma);
    }

    private void calculateMu() {

        for (DataPoint dataPoint : dataPoints) {
            for (int i = 0; i < dim; ++i) {

                mu[i] += dataPoint.getVector()[i];
            }
        }
        scalarVectorDivision(mu, dataPoints.size());
    }

    private void scalarVectorDivision(double[] vector, double scalar) {

        for (int i = 0; i < vector.length; ++i) {

            vector[i] /= scalar;
        }
    }

    private void scalarVectorPow(double[] vector, double scalar) {

        for (int i = 0; i < vector.length; ++i) {

            vector[i] = Math.pow(vector[i], scalar);
        }
    }

    private void scalarVectorSqrt(double[] vector) {

        for (int i = 0; i < vector.length; ++i) {

            vector[i] = Math.sqrt(vector[i]);
        }
    }

    public void calculateMuAndSigma() {

        fillMuAndSigmaWithZeros();
        calculateMu();
        for (DataPoint dataPoint : dataPoints) {

            for (int i = 0; i < sigma.length; ++i) {

                sigma[i] += Math.pow(dataPoint.getVector()[i] - mu[i], 2.0);
            }
        }
        scalarVectorDivision(sigma, dataPoints.size());
        scalarVectorSqrt(sigma);
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

    public double calculateCodingCost() {

        double[] codingCosts = new double[dim];
        fillVectorWithZeros(codingCosts);
        for (DataPoint dataPoint : dataPoints) {
            for (int i = 0; i < dim; ++i) {

                double p = probability(dataPoint, i);
                codingCosts[i] += -p * Math.log(p) / Math.log(2.0);
            }
        }
        return vectorSum(codingCosts);
    }

    private double vectorSum(double[] vector) {

        double sum = 0.0;
        for (int i = 0; i < vector.length; ++i) {

            sum += vector[i];
        }
        return sum;
    }

    private double probability(DataPoint dataPoint, int index) {

        double factor1 = 1.0 / (sigma[index] * Math.sqrt(2.0 * Math.PI));
        double x = Math.pow(dataPoint.getVector()[index] - mu[index], 2.0);
        double factor2 = Math.exp(-x / (2 * Math.pow(sigma[index], 2.0)));
        return factor1 * factor2;
    }

    public void emptyCell() {
        dataPoints.clear();
    }
}
