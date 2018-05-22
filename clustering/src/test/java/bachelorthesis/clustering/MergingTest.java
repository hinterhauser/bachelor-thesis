package bachelorthesis.clustering;

import bachelorthesis.clustering.data.DataGenerator;
import bachelorthesis.clustering.data.DataPoint;
import bachelorthesis.clustering.grid.Cell;
import bachelorthesis.clustering.grid.Cluster;
import bachelorthesis.clustering.grid.Grid;

import java.util.ArrayList;
import java.util.List;

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
        Cell testCell = new Cell( dataPoints, 2, mean);
        System.out.println("Computing Cost of single cell: " + testCell.calculateComputingCost());
        Grid testGrid = new Grid(2, dataPoints, 100, 100);


        System.out.println("Now perform the clustering:");
        testGrid = new Grid(2, dataPoints, 100, 100);
        testGrid.setupCells();
        testGrid.setupClusters();
        testGrid.performClustering();
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