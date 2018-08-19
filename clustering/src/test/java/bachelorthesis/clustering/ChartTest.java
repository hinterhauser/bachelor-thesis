package bachelorthesis.clustering;

import bachelorthesis.clustering.charts.DataChartAlternateDesign;
import bachelorthesis.clustering.clustering.DBSCANer;
import bachelorthesis.clustering.clustering.Kmean;
import bachelorthesis.clustering.data.*;
import bachelorthesis.clustering.fileIO.FileIO;
import bachelorthesis.clustering.grid.Cluster;
import bachelorthesis.clustering.grid.HigherDimGrid;
import org.jfree.ui.ApplicationFrame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChartTest {

    private static DataGenerator generator;
    private static ShapeGenerator shapeGenerator;
    private static int dim = 5;
    private static int factor = 1;
    private static int groundTruthNumber;

    public static void main(String[] args) {

        double[] mean = new double[dim];
        initMean(mean);
        List<DataPoint> dataPoints = new ArrayList<>();
        generator = new DataGenerator(dim);

        //halfmoonWithGaussian(dataPoints, mean);
        //yinYang(dataPoints, mean);
        //doubleMoon(dataPoints, mean);
        circlesInCircles(dataPoints, mean);

        DataPartitioner partitioner = new DataPartitioner(dataPoints);
        int k = partitioner.findOptimalPartition();
        //int k = 8;
        HigherDimGrid grid = new HigherDimGrid(k, dataPoints);
        DataChartAlternateDesign chart = new DataChartAlternateDesign("Test, Ground Truth", grid, groundTruthNumber);
        chart.setDefaultCloseOperation(ApplicationFrame.DISPOSE_ON_CLOSE);
        chart.saveToJpegFile(new File("testResults/chartTest_truth.jpg"));
        grid.performClustering(false);
        int i = 1;
        for (Cluster cluster : grid.getClusters()) {
            for (DataPoint dp : cluster.getDataPoints()) {
                dp.setCluster("" + i);
            }
                ++i;
        }
        DataChartAlternateDesign chartClustered = new DataChartAlternateDesign("Test, MDL", grid);
        chartClustered.saveToJpegFile(new File("testResults/chartTest_mdl.jpg"));

        Kmean kmean = new Kmean(groundTruthNumber, dataPoints);
        kmean.performKmeans();
        kmean.assignClusterIds();
        chartClustered = new DataChartAlternateDesign("Test, k - means", kmean.getClusters());
        chartClustered.saveToJpegFile(new File("testResults/chartTest_kmean.jpg"));

        DBSCANer dbscan = new DBSCANer(dataPoints);
        dbscan.performDBSCAN(1.0, 5);
        dbscan.assignClusterIds();
        chartClustered = new DataChartAlternateDesign("Test, DBSCAN", dbscan.getClusters());
        chartClustered.saveToJpegFile(new File("testResults/chartTest_dbscan.jpg"));

        try {
            FileIO.writeNMIFiles(grid.getDataPoints(), "testResults/mdl_nmi", ".txt");
            FileIO.writeNMIFiles(kmean.getDataPoints(), "testResults/kmean_nmi", ".txt");
            FileIO.writeNMIFiles(dbscan.getDataPoints(), "testResults/dbscan_nmi", ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void circlesInCircles(List<DataPoint> dataPoints, double[] mean) {

        mean[0] = 50;
        mean[1] = 50;

        for (int i = 0; i < 100; ++i) {
            dataPoints.add(generator.generateDataPoint(mean, 0.3, "1"));
        }
        List<DataPoint> shapePoints;
        shapeGenerator = new ShapeGenerator(new ArbitraryShape(dim));
        shapeGenerator.createDoubleMoonLeft(mean, 30);
        shapePoints = shapeGenerator.generateShape(2000 * factor);
        for (DataPoint dp : shapePoints) {
            dp.setGroundTruth("2");
        }
        dataPoints.addAll(shapePoints);
        mean[0] = 45;
        shapeGenerator = new ShapeGenerator(new ArbitraryShape(dim));
        shapeGenerator.createDoubleMoonRight(mean, 30);
        shapePoints = shapeGenerator.generateShape(2000 * factor);
        for (DataPoint dp : shapePoints) {
            dp.setGroundTruth("2");
        }
        dataPoints.addAll(shapePoints);
        mean[0] = 50;
        shapeGenerator = new ShapeGenerator(new ArbitraryShape(dim));
        shapeGenerator.createDoubleMoonLeft(mean, 50);
        shapePoints = shapeGenerator.generateShape(2000 * factor);
        for (DataPoint dp : shapePoints) {
            dp.setGroundTruth("3");
        }
        dataPoints.addAll(shapePoints);
        mean[0] = 45;
        shapeGenerator = new ShapeGenerator(new ArbitraryShape(dim));
        shapeGenerator.createDoubleMoonRight(mean, 50);
        shapePoints = shapeGenerator.generateShape(2000 * factor);
        for (DataPoint dp : shapePoints) {
            dp.setGroundTruth("3");
        }
        dataPoints.addAll(shapePoints);
        mean[0] = 50;
        shapeGenerator = new ShapeGenerator(new ArbitraryShape(dim));
        shapeGenerator.createDoubleMoonLeft(mean, 75);
        shapePoints = shapeGenerator.generateShape(3000 * factor);
        for (DataPoint dp : shapePoints) {
            dp.setGroundTruth("4");
        }
        dataPoints.addAll(shapePoints);
        mean[0] = 45;
        shapeGenerator = new ShapeGenerator(new ArbitraryShape(dim));
        shapeGenerator.createDoubleMoonRight(mean, 75);
        shapePoints = shapeGenerator.generateShape(3000 * factor);
        for (DataPoint dp : shapePoints) {
            dp.setGroundTruth("4");
        }
        dataPoints.addAll(shapePoints);
        groundTruthNumber = 4;
    }

    private static void halfmoonWithGaussian(List<DataPoint> dataPoints, double[] mean) {

        mean[0] = 50;
        mean[1] = 50;
        shapeGenerator = new ShapeGenerator(new ArbitraryShape(dim));
        shapeGenerator.createHalfMoon(50, mean);
        List<DataPoint> shapePoints = shapeGenerator.generateShape(4000 * factor);
        for (DataPoint dp : shapePoints) {
            dp.setGroundTruth("1");
        }
        dataPoints.addAll(shapePoints);

        for (int i = 0; i < 2000 * factor; ++i) {
            dataPoints.add(generator.generateDataPoint(mean,5.0, "2"));
        }

        groundTruthNumber = 2;
    }

    private static void yinYang(List<DataPoint> dataPoints, double[] mean) {

        mean[0] = 52;
        mean[1] = 75;
        shapeGenerator = new ShapeGenerator(new ArbitraryShape(dim));
        shapeGenerator.createHalfMoon(25, mean);
        List<DataPoint> shapePoints = shapeGenerator.generateShape(4000 * factor);
        for (DataPoint dp : shapePoints) {
            dp.setGroundTruth("1");
        }
        dataPoints.addAll(shapePoints);

        for (int i = 0; i < 2000 * factor; ++i) {
            dataPoints.add(generator.generateDataPoint(mean,2.0, "2"));
        }

        mean[0] = 48;
        mean[1] = 25;
        shapeGenerator = new ShapeGenerator(new ArbitraryShape(dim));
        shapeGenerator.createDoubleMoonRight(mean, 25);
        shapePoints = shapeGenerator.generateShape(4000 * factor);
        for (DataPoint dp : shapePoints) {
            dp.setGroundTruth("1");
        }
        dataPoints.addAll(shapePoints);

        for (int i = 0; i < 2000 * factor; ++i) {
            dataPoints.add(generator.generateDataPoint(mean,2.0, "3"));
        }

        groundTruthNumber = 3;
    }

    private static void doubleMoon(List<DataPoint> dataPoints, double mean[]) {

        List<DataPoint> shapePoints;
        mean[0] = 25;
        mean[1] = 65;
        shapeGenerator = new ShapeGenerator(new ArbitraryShape(dim));
        shapeGenerator.createDoubleMoonLeft(mean, 25);
        shapePoints = shapeGenerator.generateShape(3000 * factor);
        for (DataPoint dp : shapePoints) {
            dp.setGroundTruth("1");
        }
        dataPoints.addAll(shapePoints);

        mean[0] = 10.5;
        mean[1] = 40;
        shapeGenerator = new ShapeGenerator(new ArbitraryShape(dim));
        shapeGenerator.createDoubleMoonRight(mean, 25);
        shapePoints = shapeGenerator.generateShape(3000 * factor);
        for (DataPoint dp : shapePoints) {
            dp.setGroundTruth("2");
        }
        dataPoints.addAll(shapePoints);

        groundTruthNumber = 2;
    }

    private static void initMean(double[] mean) {

        for (int i = 0; i < dim; ++i) {

            mean[i] = 1;
        }
    }
}
