package bachelorthesis.clustering;

import bachelorthesis.clustering.data.DataGenerator;
import bachelorthesis.clustering.data.DataPartitioner;
import bachelorthesis.clustering.data.DataPoint;
import bachelorthesis.clustering.grid.Cell;
import bachelorthesis.clustering.grid.Cluster;
import bachelorthesis.clustering.grid.Grid;

import java.util.ArrayList;
import java.util.List;

import static bachelorthesis.clustering.PartitionTest.extractDataPointsFromFile;

public class MergingTest {

    public static void main(String[] args) {

        double[] mean = new double[2];
        mean[0] = 50;
        mean[1] = 50;

        System.out.println("Test merging with the testdata");

        List<DataPoint> dataPointsTest = extractDataPointsFromFile("results/testData.csv");
        DataPartitioner dataPartitioner = new DataPartitioner(dataPointsTest, 100, 100);
        int k = dataPartitioner.findOptimalPartition("results/results.txt", "results/areas.txt");

        Grid testGrid = new Grid(k, dataPointsTest, 100, 100);
        testGrid.setupCells();
        testGrid.setupClusters();
        testGrid.performClustering();
        resultsOfClustering(testGrid, mean);

        dataPointsTest = extractDataPointsFromFile("results/testData1.csv");
        DataPartitioner dataPartitioner1 = new DataPartitioner(dataPointsTest, 100, 100);
        k = dataPartitioner1.findOptimalPartition("results/results1.txt", "results/areas1.txt");
        System.out.println("k " + k);

        testGrid = new Grid(k, dataPointsTest, 100, 100);
        testGrid.setupCells();
        testGrid.setupClusters();
        testGrid.performClustering();
        resultsOfClustering(testGrid, mean);

        dataPointsTest = extractDataPointsFromFile("results/testData2.csv");
        DataPartitioner dataPartitioner2 = new DataPartitioner(dataPointsTest, 100, 100);
        k = dataPartitioner2.findOptimalPartition("results/results2.txt", "results/areas2.txt");
        System.out.println("k " + k);

        testGrid = new Grid(k, dataPointsTest, 100, 100);
        testGrid.setupCells();
        testGrid.setupClusters();
        testGrid.performClustering();
        resultsOfClustering(testGrid, mean);

        dataPointsTest = extractDataPointsFromFile("results/testData3.csv");
        DataPartitioner dataPartitioner3 = new DataPartitioner(dataPointsTest, 100, 100);
        k = dataPartitioner3.findOptimalPartition("results/results3.txt", "results/areas3.txt");
        System.out.println("k " + k);

        testGrid = new Grid(k, dataPointsTest, 100, 100);
        testGrid.setupCells();
        testGrid.setupClusters();
        testGrid.performClustering();
        resultsOfClustering(testGrid, mean);

        dataPointsTest = extractDataPointsFromFile("results/testData4.csv");
        DataPartitioner dataPartitioner4 = new DataPartitioner(dataPointsTest, 100, 100);
        k = dataPartitioner4.findOptimalPartition("results/results4.txt", "results/areas4.txt");
        System.out.println("k " + k);

        testGrid = new Grid(k, dataPointsTest, 100, 100);
        testGrid.setupCells();
        testGrid.setupClusters();
        testGrid.performClustering();
        resultsOfClustering(testGrid, mean);
    }

    private static void resultsOfClustering(Grid testGrid, double[] mean) {

        System.out.println("Results of Clustering:");
        System.out.println("     Number clusters: " + testGrid.getClusters().size());
        System.out.println("     Computing Cost: " + testGrid.calculateCodingCost());
        List<DataPoint> dataPoints1 = new ArrayList<>();
        int i = 0;
        double[] cellMean;
        for (Cluster cluster : testGrid.getClusters()) {
            System.out.print("Cluster" + ++i + ": ");
            for (Cell cell : cluster.getClusterCells()) {

                dataPoints1.addAll(cell.getDataPoints());
            }
            Cell cell = new Cell(dataPoints1, 2, mean);
            cellMean = getMeanDataPoint(cell);
            System.out.println("     Mean: " + cellMean[0] + " , " + cellMean[1]);
            dataPoints1.clear();
        }
    }

    public static double[] getMeanDataPoint(Cell cell) {

        double[] mean = new double[2];
        mean[0] = 0.0;
        mean[1] = 0.0;
        for (DataPoint dp : cell.getDataPoints()) {
            mean[0] += dp.getVector()[0];
            mean[1] += dp.getVector()[1];
        }
        mean[0] /= cell.getDataPoints().size();
        mean[1] /= cell.getDataPoints().size();
        return mean;
    }

    public static void printSum(Grid testGrid) {

        double sum = 0.0;
        for (Cluster cluster : testGrid.getClusters()) {

            sum += cluster.calculateCodingCost();
        }
        System.out.println("Sum: " + sum);
    }
}