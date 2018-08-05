package bachelorthesis.clustering.grid;

import bachelorthesis.clustering.data.DataPoint;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class HigherDimGrid implements StatsObj {

    private int dim;
    private int k;
    private Cell[] cells;
    private List<DataPoint> dataPoints;
    private List<Cluster> clusters;
    private double[][] domain;

    private double mean;
    private double deviation;

    public HigherDimGrid(int k, List<DataPoint> dataPoints) {

        setDim(dataPoints.get(0).getDim());
        setK(k);
        setDataPoints(dataPoints);
        cells = new Cell[calcNumberCells()];
        clusters = new ArrayList<>();
        defineDomain();
        setupCells();
        setupClusters();
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public void setK(int k) {
        this.k = k;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    private int calcNumberCells() {
        return (int) Math.pow(k, dim);
    }

    public void setupClusters() {

        Cluster[] clusterArray = new Cluster[cells.length];
        for (int i = 0; i < cells.length; ++i) {
            if (cells[i] != null & cells[i].getDataPoints().size() > 0) {

                clusterArray[i] = new Cluster(cells[i]);
            }
        }
        for (int i = 0; i < cells.length; ++i) {

            if (cells[i].getDataPoints().size() > 0) {
                clusterArray[i].setNeighbors(assignNeighbors(i, clusterArray));
                clusterArray[i].setN(dataPoints.size());
                clusters.add(clusterArray[i]);
            }
        }
    }

    private Set<Cluster> assignNeighbors(int index, Cluster[] clusterArray) {

        Set<Cluster> neighbors = new LinkedHashSet<>();

        for (int i = 0; i < dim; ++i) {

            //System.out.println("Index: " + i + "    " + Math.pow(k, i));
            //System.out.println("Length: " + clusterArray.length);
            int lowerNeighborIndex = index - (int) Math.pow(k, i);
            int upperNeighborIndex = index + (int) Math.pow(k, i);
            //System.out.println("Neighbor indexes: ");
            //System.out.println(lowerNeighborIndex);
            //System.out.println(upperNeighborIndex);
            if (lowerNeighborIndex >= 0 && clusterArray[lowerNeighborIndex] != null) {
                neighbors.add(clusterArray[lowerNeighborIndex]);
            }
            if (upperNeighborIndex < k && clusterArray[upperNeighborIndex] != null) {
                neighbors.add(clusterArray[upperNeighborIndex]);
            }
        }
        return neighbors;
    }

    private void defineDomain() {

        defineInitialDomain();
        for (DataPoint dataPoint : dataPoints) {
            for (int i = 0; i < dim; ++i) {

                if (dataPoint.getVector()[i] < domain[i][0]) {
                    domain[i][0] = dataPoint.getVector()[i];
                } else if (dataPoint.getVector()[i] > domain[i][1]) {
                    domain[i][1] = dataPoint.getVector()[i];
                }
            }
        }
        for (int i = 0; i < dim; ++i) {

            domain[i][0] -= 10;
            domain[i][1] += 10;
        }
    }

    private void defineInitialDomain() {

        domain = new double[dim][2];
        for (int i = 0; i < dim; ++i) {
            domain[i][0] = dataPoints.get(0).getVector()[i];
            domain[i][1] = dataPoints.get(0).getVector()[i];
        }
    }

    private void calcCellCenter(double[] cellCenter, double[] cellLength, int index) {

        for (int i = 0; i < dim; ++i) {

            int factor = index % k;
            cellCenter[i] = domain[i][0] + factor * cellLength[i] + cellLength[i] / 2;
            index = index / k;
        }
    }

    public void setupCells() {

        double[] cellLengths = new double[dim];
        for (int i = 0; i < dim; ++i) {
            cellLengths[i] = (domain[i][1] - domain[i][0]) / k;
        }
        for (int i = 0; i < cells.length; ++i) {

            double cellCenter[] = new double[dim];
            calcCellCenter(cellCenter, cellLengths, i);
            cells[i] = new Cell(new ArrayList<>(), dim, cellCenter);
        }

        assignDataPointsToCells(cellLengths);
        calculateCellsDeviationAndMean();
    }

    private void calculateCellsDeviationAndMean() {

        for (int i = 0; i < cells.length; ++i) {

            cells[i].calculateDeviationAndMean();
            cells[i].calculateMuAndSigma();
        }
    }

    private int resolveIndex(DataPoint dataPoint, double[] cellLengths) {

        int index = 0;
        for (int i = 0; i < dim; ++i) {

            int temp = (int) ((dataPoint.getVector()[i] - domain[i][0]) / cellLengths[i]);
            index += (temp * Math.pow(k, i));
        }
        return index;
    }

    private void assignDataPointsToCells(double[] cellLengths) {

        for (DataPoint dataPoint : dataPoints) {

            int index = resolveIndex(dataPoint, cellLengths);
            cells[index].getDataPoints().add(dataPoint);
        }
    }

    private void calculateMean() {

        double sum = 0.0;
        for (int i = 0; i < cells.length; ++i) {

            sum += cells[i].getMean();
        }
        mean = sum / cells.length;
    }

    @Override
    public void calculateDeviationAndMean() {

        calculateMean();
        double sum = 0.0;
        for (int i = 0; i < cells.length; ++i) {

            double partDiff = cells[i].getMean() - mean;
            sum += Math.pow(partDiff, 2.0);
        }
        deviation = Math.sqrt(sum / cells.length);
    }

    @Override
    public double calculateCV() {
        return 0;
    }

    public void performClustering() {

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
