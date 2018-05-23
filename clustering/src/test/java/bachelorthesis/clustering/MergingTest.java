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

        DataGenerator generator = new DataGenerator(2);
        List<DataPoint> dataPoints = new ArrayList<DataPoint>();
        double[] mean = new double[2];
        mean[0] = 50;
        mean[1] = 50;
        for (int i = 0; i < 1000; ++i) {

            dataPoints.add(generator.generateDataPoint(mean, 10.0));
        }
        //Cell testCell = new Cell( dataPoints, 2, mean);
        //System.out.println("Computing Cost of single cell: " + testCell.calculateComputingCost());
        Grid testGrid = new Grid(2, dataPoints, 100, 100);
        testTestGridSimple(testGrid);

        System.out.println("Now perform the clustering:");
        testGrid = new Grid(2, dataPoints, 100, 100);
        testGrid.setupCells();
        testGrid.setupClusters();
        //System.out.println("testGrid.getNeighbors(): " + testGrid.getClusters().get(0).getNeighbors().size());
        testGrid.performClustering();
        System.out.println("Results of Clustering:");
        System.out.println("     Number clusters: " + testGrid.getClusters().size());
        System.out.println("     Computing Cost: " + testGrid.calculateComputingCost());
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
        //System.out.println("     Deviation: " + cell.getDeviation());
        Cell testCell = new Cell( dataPoints, 2, mean);
        System.out.println("Computing Cost of single cell: " + testCell.calculateComputingCost());
        cellMean = getMeanDataPoint(testCell);
        System.out.println("     Mean: " + cellMean[0] + " , " + cellMean[1]);
        //System.out.println("     Mean: " + testCell.getMean());
        //System.out.println("     Deviation: " + testCell.getDeviation());

        System.out.println("Now for the fun part: Do it with the test data");

        List<DataPoint> dataPointsTest = extractDataPointsFromFile("results/testData.csv");
        DataPartitioner dataPartitioner = new DataPartitioner(dataPointsTest, 100, 100);
        int k = dataPartitioner.findOptimalPartition("results/results.txt", "results/areas.txt");

        testGrid = new Grid(k, dataPointsTest, 100, 100);
        testGrid.setupCells();
        testGrid.setupClusters();
        //System.out.println("testGrid.getNeighbors(): " + testGrid.getClusters().get(0).getNeighbors().size());
        testGrid.performClustering();
        resultsOfClustering(testGrid, mean);

        dataPointsTest = extractDataPointsFromFile("results/testData1.csv");
        DataPartitioner dataPartitioner1 = new DataPartitioner(dataPointsTest, 100, 100);
        k = dataPartitioner1.findOptimalPartition("results/results1.txt", "results/areas1.txt");
        System.out.println("k " + k);

        testGrid = new Grid(k, dataPointsTest, 100, 100);
        testGrid.setupCells();
        testGrid.setupClusters();
        //System.out.println("testGrid.getNeighbors(): " + testGrid.getClusters().get(0).getNeighbors().size());
        testGrid.performClustering();
        resultsOfClustering(testGrid, mean);

        dataPointsTest = extractDataPointsFromFile("results/testData2.csv");
        DataPartitioner dataPartitioner2 = new DataPartitioner(dataPointsTest, 100, 100);
        k = dataPartitioner2.findOptimalPartition("results/results2.txt", "results/areas2.txt");
        System.out.println("k " + k);

        testGrid = new Grid(k, dataPointsTest, 100, 100);
        testGrid.setupCells();
        testGrid.setupClusters();
        //System.out.println("testGrid.getNeighbors(): " + testGrid.getClusters().get(0).getNeighbors().size());
        testGrid.performClustering();
        resultsOfClustering(testGrid, mean);

        dataPointsTest = extractDataPointsFromFile("results/testData3.csv");
        DataPartitioner dataPartitioner3 = new DataPartitioner(dataPointsTest, 100, 100);
        k = dataPartitioner3.findOptimalPartition("results/results3.txt", "results/areas3.txt");
        System.out.println("k " + k);

        testGrid = new Grid(k, dataPointsTest, 100, 100);
        testGrid.setupCells();
        testGrid.setupClusters();
        //System.out.println("testGrid.getNeighbors(): " + testGrid.getClusters().get(0).getNeighbors().size());
        testGrid.performClustering();
        resultsOfClustering(testGrid, mean);

        dataPointsTest = extractDataPointsFromFile("results/testData4.csv");
        DataPartitioner dataPartitioner4 = new DataPartitioner(dataPointsTest, 100, 100);
        k = dataPartitioner4.findOptimalPartition("results/results4.txt", "results/areas4.txt");
        System.out.println("k " + k);

        testGrid = new Grid(k, dataPointsTest, 100, 100);
        testGrid.setupCells();
        testGrid.setupClusters();
        //System.out.println("testGrid.getNeighbors(): " + testGrid.getClusters().get(0).getNeighbors().size());
        testGrid.performClustering();
        resultsOfClustering(testGrid, mean);
    }

    private static void resultsOfClustering(Grid testGrid, double[] mean) {

        System.out.println("Results of Clustering:");
        System.out.println("     Number clusters: " + testGrid.getClusters().size());
        System.out.println("     Computing Cost: " + testGrid.calculateComputingCost());
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

    private static double[] getMeanDataPoint(Cell cell) {

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

    private static void testTestGridSimple(Grid testGrid) {

        testGrid.setupCells();
        testGrid.setupClusters();
        double sum = 0.0;
        System.out.println("Computing Cost of grid, 4 cells: ");
        System.out.println("    " + testGrid.getCells()[0][0].calculateComputingCost());
        System.out.println("    " + testGrid.getCells()[0][1].calculateComputingCost());
        System.out.println("    " + testGrid.getCells()[1][0].calculateComputingCost());
        System.out.println("    " + testGrid.getCells()[1][1].calculateComputingCost());

        sum += testGrid.getCells()[0][0].calculateComputingCost();
        sum += testGrid.getCells()[0][1].calculateComputingCost();
        sum += testGrid.getCells()[1][0].calculateComputingCost();
        sum += testGrid.getCells()[1][1].calculateComputingCost();

        System.out.println("Sum of all cells: " + sum);

        // other method
        System.out.println("Now the sum using merging of cells:");
        printSum(testGrid);

        testGrid.mergeClusters(testGrid.getClusters().get(0), testGrid.getClusters().get(1));
        printSum(testGrid);
        testGrid.mergeClusters(testGrid.getClusters().get(0), testGrid.getClusters().get(1));
        printSum(testGrid);
        testGrid.mergeClusters(testGrid.getClusters().get(0), testGrid.getClusters().get(1));
        printSum(testGrid);
    }

    private static void printSum(Grid testGrid) {

        double sum = 0.0;
        for (Cluster cluster : testGrid.getClusters()) {

            sum += cluster.calculateComputingCost();
        }
        System.out.println("Sum: " + sum);
    }
}