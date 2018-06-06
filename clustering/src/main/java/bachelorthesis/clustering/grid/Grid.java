package bachelorthesis.clustering.grid;

import bachelorthesis.clustering.charts.DataChartAlternateDesign;
import bachelorthesis.clustering.data.DataPoint;
import org.jfree.ui.RefineryUtilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
                    clusterArray[i][j].setN(dataPoints.size());
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

    public void performClustering(boolean debug) {

        double actualCost = 0.0;
        double costBeforeMerging = 0.0;
        List<Cluster> notConvergedClusters = new ArrayList<>();
        List<Cluster> convergedClusters = new ArrayList<>();
        do {
            //System.out.println("   cost: " + calculateCodingCost());
            getNotConvergedClusters(notConvergedClusters);
            if (notConvergedClusters.size() <= 0) {
                break;
            }
            int index = (int) (Math.random() * notConvergedClusters.size());
            if (debug) {
                System.out.println("Random index: " + index);
            }
            mergeWithNeighbors(notConvergedClusters.get(index), convergedClusters, debug);
        } while (notConvergedClusters.size() > 0);
        clusters = convergedClusters;
    }

    private void mergeWithNeighbors(Cluster cluster, List<Cluster> convergedClusters, boolean debug) {

        double actualCost = 0.0;
        double costBeforeMerging = 0.0;
        List<Cluster> neighbors = new ArrayList<>();
        int neighborIndex = 0;
        int iteration = 0;
        while(true) {
            if (debug) {
                System.out.println("Iteration " + iteration);
                System.out.println("   cost, before: " + calculateCodingCost());
                DataChartAlternateDesign chart = new DataChartAlternateDesign("Iteration " + iteration++, this);
                chart.pack();
                RefineryUtilities.centerFrameOnScreen(chart);
                chart.setVisible(true);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            neighbors.addAll(cluster.getNeighbors());
            if (neighborIndex >= neighbors.size()) {
                break;
            }
            //System.out.println("neighbors size: " + neighbors.size());
            Cluster merger = neighbors.get(neighborIndex);
            //actualCost = cluster.calculateCodingCost() + merger.calculateCodingCost();
            //actualCost = calculateCodingCost();
            actualCost = calculateMDL();
            //costBeforeMerging = cluster.calculateCodingCostBeforeMerging(merger);
            //costBeforeMerging = calculateCodingCostBeforeMerging(cluster, merger);
            costBeforeMerging = calculateMDLBeforeMerging(cluster, merger);
            if (debug) {
                System.out.println("   actual cost " + actualCost);
                System.out.println("   mergin cost " + costBeforeMerging);
            }
            /*if (Double.isNaN(actualCost)) { // just an experiment
                mergeClusters(cluster, neighbors.get(neighborIndex));
            }*/
            if (costBeforeMerging < actualCost || Double.isNaN(actualCost) || merger.getClusterCells().iterator().next().getDataPoints().size() == 1) {

                mergeClusters(cluster, neighbors.get(neighborIndex));
            } else {

                ++neighborIndex;
            }
            //System.out.println("ni: " + neighborIndex + "  ns: " + neighbors.size());
            neighbors.clear();
            if (debug) {
                System.out.println("   cost, after: " + calculateMDL());
            }
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

    public double calculateMDL() {

        double para = 0.0;
        double id = 0.0;
        double mdl = 0.0;
        mdl += calculateCodingCost();
        for (Cluster cluster : clusters) {
            mdl += cluster.calculateParameterCost();
            mdl += cluster.calculateIDCost();
            para += cluster.calculateParameterCost();
            id += cluster.calculateIDCost();
        }
        //System.out.println("mdl: " + mdl);
        System.out.println(" actual: " + para + " . " + id);
        return mdl;
    }

    public double calculateCodingCost() {

        //System.out.println("calculateCodingCost");
        double cost = 0.0;
        for (Cluster cluster : clusters) {

            //System.out.println("cells: " + cluster.getClusterCells().size());
            /*for (Cell cell : cluster.getClusterCells()) {
                //System.out.println("   " + cell.getDataPoints().size());
            }*/
            cost += cluster.calculateCodingCost();
        }
        return cost;
    }

    public double calculateMDLBeforeMerging(Cluster c, Cluster merger) {

        double para = 0.0;
        double id = 0.0;
        double mdl = 0.0;
        mdl += calculateCodingCostBeforeMerging(c, merger);
        for (Cluster cluster : clusters) {

            if (!cluster.equals(c) && !cluster.equals(merger)) {
                mdl += cluster.calculateParameterCost();
                mdl += cluster.calculateIDCost();
                para += cluster.calculateParameterCost();
                id += cluster.calculateIDCost();
            }
        }
        //System.out.println("mdl: " + mdl);
        Cluster mergeCandidate = new Cluster();
        mergeCandidate.setN(dataPoints.size());
        mergeCandidate.getClusterCells().addAll(c.getClusterCells());
        mergeCandidate.getClusterCells().addAll(merger.getClusterCells());
        mdl += mergeCandidate.calculateParameterCost();
        mdl += mergeCandidate.calculateIDCost();
        para += mergeCandidate.calculateParameterCost();
        id += mergeCandidate.calculateIDCost();
        //System.out.println("mdl: " + mdl);
        System.out.println("before merge: " + para + " . " + id);
        return mdl;
    }

    public double calculateCodingCostBeforeMerging(Cluster c, Cluster merger) {

        //System.out.println("calculateCodingCost");
        double cost = 0.0;
        for (Cluster cluster : clusters) {

            //System.out.println("cells: " + cluster.getClusterCells().size());
            for (Cell cell : cluster.getClusterCells()) {
                //System.out.println("   " + cell.getDataPoints().size());
            }
            if (!cluster.equals(c) && !cluster.equals(merger)) {
                cost += cluster.calculateCodingCost();
            } else if (cluster.equals(c)) {
                cost += cluster.calculateCodingCostBeforeMerging(merger);
            }
        }
        return cost;
    }
}