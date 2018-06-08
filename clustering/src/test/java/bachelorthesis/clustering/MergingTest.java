package bachelorthesis.clustering;

import bachelorthesis.clustering.charts.DataChartAlternateDesign;
import bachelorthesis.clustering.data.DataGenerator;
import bachelorthesis.clustering.data.DataPartitioner;
import bachelorthesis.clustering.data.DataPoint;
import bachelorthesis.clustering.grid.Cell;
import bachelorthesis.clustering.grid.Cluster;
import bachelorthesis.clustering.grid.Grid;
import org.jfree.chart.ChartUtilities;
import org.jfree.ui.RefineryUtilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static bachelorthesis.clustering.PartitionTest.extractDataPointsFromFile;

public class MergingTest {

    public static void main(String[] args) {

        double[] mean = new double[2];
        mean[0] = 50;
        mean[1] = 50;
        int k;

        System.out.println("Test merging with the testdata");

        List<DataPoint> dataPointsTest0 = null;
        Grid testGrid0 = null;
        List<DataPoint> dataPointsTest1 = null;
        Grid testGrid1 = null;
        List<DataPoint> dataPointsTest2 = null;
        Grid testGrid2 = null;
        List<DataPoint> dataPointsTest3 = null;
        Grid testGrid3 = null;
        List<DataPoint> dataPointsTest4 = null;
        Grid testGrid4 = null;

        testClustering(testGrid0, dataPointsTest0, 0, false, mean);
        testClustering(testGrid1, dataPointsTest1, 1, false, mean);
        testClustering(testGrid2, dataPointsTest2, 2, false, mean);
        testClustering(testGrid3, dataPointsTest3, 3, false, mean);
        testClustering(testGrid4, dataPointsTest4, 4, false, mean);

        /*DataChartAlternateDesign chart = new DataChartAlternateDesign("Grid " + 3, testGrid3);
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);*/

        List<Grid> testGrids = new ArrayList<>();
        testGrids.add(testGrid0);
        testGrids.add(testGrid1);
        testGrids.add(testGrid2);
        testGrids.add(testGrid3);
        testGrids.add(testGrid4);

        //visualiseGrids(testGrids);
        //visualiseAllGrids(testGrids);
        /*System.out.println("A quick test:");
        double[] mean1 = new double[2];
        List<DataPoint> dataPoints1 = new ArrayList<>();
        DataGenerator generator = new DataGenerator(2);
        mean1[0] = 30;
        mean1[1] = 70;
        for (int i = 0; i < 1000; ++i) {
            dataPoints1.add(generator.generateDataPoint(mean1, 2.0)); // before: 5.0
        }
        Cell cell1 = new Cell(dataPoints1, 2, mean1);
        System.out.println(cell1.calculateCodingCost());

        double[] mean2 = new double[2];
        List<DataPoint> dataPoints2 = new ArrayList<>();
        mean2[0] = 50;
        mean2[1] = 50;
        for (int i = 0; i < 500; ++i) {
            dataPoints2.add(generator.generateDataPoint(mean,2.0));
        }
        Cell cell2 = new Cell(dataPoints2, 2, mean2);
        System.out.println(cell2.calculateCodingCost());

        double[] mean3 = new double[2];
        List<DataPoint> dataPoints3 = new ArrayList<>();
        mean3[0] = 36;
        mean3[1] = 63;
        dataPoints3.addAll(dataPoints1);
        dataPoints3.addAll(dataPoints2);
        Cell cell3 = new Cell(dataPoints3, 2, mean3);
        System.out.println(cell3.calculateCodingCost());

        DataChartAlternateDesign chart = new DataChartAlternateDesign("Grid " + 3, testGrids.get(3));
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);*/
    }

    private static void testClustering(Grid testGrid, List<DataPoint> dataPointsTest, int index, boolean debug, double[] mean) {

        dataPointsTest = extractDataPointsFromFile("results/testData" + index + ".csv");
        DataPartitioner dataPartitioner = new DataPartitioner(dataPointsTest, 100, 100);
        int k = dataPartitioner.findOptimalPartition("results/results" + index + ".txt", "results/areas" + index + ".txt");
        if (index == 3) {
            k += 20;
        }
        testGrid = new Grid(k, dataPointsTest, 100, 100);
        testGrid.setupCells();
        testGrid.setupClusters();
        if (debug) {
            System.out.println("clusters: " + testGrid.getClusters().size());
        }
        testGrid.performClustering(debug);
        resultsOfClustering(testGrid, mean);
        saveToFile(testGrid, index, new File("results/mergingTests/testGrid" + index + ".jpg"));
    }

    private static void saveToFile(Grid grid, int index, File filename) {

        DataChartAlternateDesign chart = new DataChartAlternateDesign("Grid " + index, grid);
        //chart.pack();
        //RefineryUtilities.centerFrameOnScreen(chart);
        try {
            ChartUtilities.saveChartAsJPEG(filename, chart.getChart(), 500, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //chart.setVisible(false);
    }

    private static void visualiseAllGrids(List<Grid> grids) {

        int input = 0;
        for (Grid grid : grids) {

            DataChartAlternateDesign chart = new DataChartAlternateDesign("Grid " + input, grids.get(input++));
            chart.pack();
            RefineryUtilities.centerFrameOnScreen(chart);
            chart.setVisible(true);
        }
    }

    private static void visualiseGrids(List<Grid> grids) {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        DataChartAlternateDesign chart;

        while (true) {
            System.out.println("Type 0 to " + (grids.size()-1) + " to visualize the grid");
            int input = -1;
            try {
                input = Integer.parseInt(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }

            chart = new DataChartAlternateDesign("Grid " + input, grids.get(input));
            chart.pack();
            RefineryUtilities.centerFrameOnScreen(chart);
            chart.setVisible(true);
            /*System.out.println("Type exit to exit");
            String inputString = "";
            try {
                inputString = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (inputString.equals("exit")) {
                break;
            }*/
            if (input == -1) {
                break;
            }
        }
    }

    private static void resultsOfClustering(Grid testGrid, double[] mean) {

        System.out.println("Results of Clustering:");
        System.out.println("     Number clusters: " + testGrid.getClusters().size());
        System.out.println("     MDL:             " + testGrid.calculateCodingCost());
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