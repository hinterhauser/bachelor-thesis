package bachelorthesis.clustering.grid;

import bachelorthesis.clustering.data.DataPoint;

import java.util.*;

public class Grid implements StatsObj {

    private int k;
    private Cell[][] cells;
    private List<DataPoint> dataPoints;
    private List<Cluster> clusters;
    private double x;
    private double y;

    private double mean;
    private double deviation;

    public Grid(int k, List<DataPoint> dataPoints, double x, double y) {

        setK(k);
        setDataPoints(dataPoints);
        cells = new Cell[k][k];
        clusters = new ArrayList<>();
        setX(x);
        setY(y);
    }

    public double getMean() {
        return mean;
    }

    public double getDeviation() {
        return deviation;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public void setClusters(List<Cluster> clusters) {
        this.clusters = clusters;
    }

    // -------------- end Setter and Getter ------------------------------------

    private void calculateMean() {

        double sum = 0.0;
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {

                sum += cells[i][j].getMean();
            }
        }
        mean = sum / (k * k);
    }

    @Override
    public void calculateDeviationAndMean() {

        calculateMean();
        double sum = 0.0;
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {

                double partDiff = cells[i][j].getMean() - mean;
                sum += Math.pow(partDiff, 2.0);
            }
        }
        deviation = Math.sqrt(sum / (k * k));
    }

    @Override
    public double calculateCV() {

        if (mean == 0.0) {
            return 0.0;
        }
        return deviation / mean * 100;
    }

    public void setupCells() {

        double xCellLength = x / k;
        double yCellLength = y / k;

        int xCellIndex = 0;
        int yCellIndex = 0;
        for (double i = 0.0; (i < y) & (yCellIndex < k); i += yCellLength) {
            for (double j = 0.0; (j < x) & (xCellIndex < k); j += xCellLength) {

                double cellCenter[] = new double[2];
                cellCenter[0] = (i + i + yCellLength) / 2;
                cellCenter[1] = (j + j + xCellLength) / 2;
                cells[yCellIndex][xCellIndex++] = new Cell(new ArrayList<DataPoint>(), 2, cellCenter);
            }
            xCellIndex = 0;
            yCellIndex++;
        }
        assignDataPointsToCells(xCellLength, yCellLength);
        calculateCellsDeviationAndMean();
    }

    private void calculateCellsDeviationAndMean() {

        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {

                cells[i][j].calculateDeviationAndMean();
                cells[i][j].calculateMuAndSigma();
            }
        }
    }

    private void assignDataPointsToCells(double xCellLength, double yCellLength) {

        for (DataPoint dataPoint : dataPoints) {

            int indexX = (int) (dataPoint.getVector()[1] / xCellLength);
            int indexY = (int) (dataPoint.getVector()[0] / yCellLength);
            cells[indexY][indexX].getDataPoints().add(dataPoint);
        }
    }

    public int nonEmptyCells() {

        int sum = 0;
        for (int i = 0; i < getK(); ++i) {
            for (int j = 0; j < getK(); ++j) {

                if (getCells()[i][j].getDataPoints().size() > 0) {
                    ++sum;
                }
            }
        }
        return sum;
    }

    public void setupClusters() {

        Cluster[][] clusterArray = new Cluster[k][k];
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {

                if (getCells()[i][j] != null & getCells()[i][j].getDataPoints().size() > 0) {

                    clusterArray[i][j] = new Cluster(getCells()[i][j]);
                }
            }
        }
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {

                if (getCells()[i][j].getDataPoints().size() > 0) {

                    clusterArray[i][j].setNeighbors(assignNeighbors(i, j, clusterArray));
                    clusters.add(clusterArray[i][j]);
                }
            }
        }
    }

    private Set<Cluster> assignNeighbors(int i, int j, Cluster[][] clusterArray) {

        Set<Cluster> neighbors = new LinkedHashSet<>();
        for (int y = i-1; y <= i+1; ++y) {
            for (int x = j-1; x <= j+1; ++x) {

                if ((y >= 0 && x >= 0) && (y < k && x < k) && clusterArray[y][x] != null && !(y == i && x == j)) {
                    //System.out.println("y: " + y + " x: " + x);
                    neighbors.add(clusterArray[y][x]);
                }
            }
        }
        //System.out.println("assign: " + neighbors.size());
        return neighbors;
    }

    public void mergeClusters(Cluster cluster, Cluster merger) {

        cluster.mergeClusters(merger);
        for (Cluster clus : clusters) {
            clus.removeNeighbor(merger);
        }
        clusters.remove(merger);
    }

    public void performClustering() {

        double actualCost = 0.0;
        double costBeforeMerging = 0.0;
        List<Cluster> notConvergedClusters = new ArrayList<>();
        List<Cluster> convergedClusters = new ArrayList<>();
        /*for (Cluster cluster : clusters) {      // TODO this does not work, fix it

            for (Cluster c : cluster.getNeighbors()) {
                System.out.println("ComputationCost before: " + calculateComputingCost());

                System.out.println("   " + (actualCost = cluster.calculateComputingCost() + c.calculateComputingCost()));
                System.out.println("   " + (costBeforeMerging = cluster.calculateComputingCostBeforeMerging(c)));

                if (costBeforeMerging < actualCost) {

                    mergeClusters(cluster, c);
                }

                System.out.println("ComputationCost after: " + calculateComputingCost());
            }
        }*/
        do {

            getNotConvergedClusters(notConvergedClusters);
            if (notConvergedClusters.size() <= 0) {
                break;
            }
            int index = (int) Math.random() * notConvergedClusters.size();
            mergeWithNeighbors(notConvergedClusters.get(index), convergedClusters);
        } while (notConvergedClusters.size() > 0);
        clusters = convergedClusters;
    }

    private void mergeWithNeighbors(Cluster cluster, List<Cluster> convergedClusters) {

        double actualCost = 0.0;
        double costBeforeMerging = 0.0;
        List<Cluster> neighbors = new ArrayList<>();
        int neighborIndex = 0;
        while(true) {

            neighbors.addAll(cluster.getNeighbors());
            if (neighborIndex >= neighbors.size()) {
                break;
            }
            //System.out.println("neighbors size: " + neighbors.size());
            Cluster merger = neighbors.get(neighborIndex);
            actualCost = cluster.calculateCodingCost() + merger.calculateCodingCost();
            costBeforeMerging = cluster.calculateCodingCostBeforeMerging(merger);
            //System.out.println("   " + (actualCost = cluster.calculateComputingCost() + merger.calculateComputingCost()));
            //System.out.println("   " + (costBeforeMerging = cluster.calculateComputingCostBeforeMerging(merger)));
            /*if (Double.isNaN(actualCost)) { // just an experiment
                mergeClusters(cluster, neighbors.get(neighborIndex));
            }*/
            if (costBeforeMerging < actualCost || Double.isNaN(actualCost)) {

                mergeClusters(cluster, neighbors.get(neighborIndex));
            } else {

                ++neighborIndex;
            }
            //System.out.println("ni: " + neighborIndex + "  ns: " + neighbors.size());
            neighbors.clear();
        }
        //System.out.println("End");
        cluster.setConverged(true);
        convergedClusters.add(cluster);
    }

    private void getNotConvergedClusters(List<Cluster> notConvergedClusters) {

        notConvergedClusters.clear();
        for (Cluster cluster : clusters) {
            if (!cluster.isConverged()) {

                notConvergedClusters.add(cluster);
            }
        }
    }

    public double calculateCodingCost() {

        double cost = 0.0;
        for (Cluster cluster : clusters) {

            cost += cluster.calculateCodingCost();
        }
        return cost;
    }
}
