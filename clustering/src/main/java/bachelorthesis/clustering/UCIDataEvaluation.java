package bachelorthesis.clustering;

import bachelorthesis.clustering.charts.DataChartAlternateDesign;
import bachelorthesis.clustering.clustering.DBSCANer;
import bachelorthesis.clustering.clustering.Kmean;
import bachelorthesis.clustering.data.DataPartitioner;
import bachelorthesis.clustering.data.DataPoint;
import bachelorthesis.clustering.fileIO.CsvDataReader;
import bachelorthesis.clustering.fileIO.FileIO;
import bachelorthesis.clustering.grid.Cluster;
import bachelorthesis.clustering.grid.HigherDimGrid;
import org.jfree.ui.ApplicationFrame;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class UCIDataEvaluation {

    private static int groundTruthNumber = 3;
    private static final String dirResults = "results/uciResults/";

    public static void main(String[] args) {

        CsvDataReader csvDataReader = new CsvDataReader("results/uciData/iris.csv", 4);
        List<DataPoint> dataPoints = csvDataReader.getDataPoints();

        System.out.println("Start MDL");
        DataPartitioner partitioner = new DataPartitioner(dataPoints);
        int opt = partitioner.findOptimalPartition();
        System.out.println("optimal partition: " + opt);
        //int k = 4;
        for (int k = 2; k <= 20; ++k) {
            HigherDimGrid grid = new HigherDimGrid(k, dataPoints);
            //DataChartAlternateDesign chart = new DataChartAlternateDesign("Test, Ground Truth", grid, groundTruthNumber);
            //chart.setDefaultCloseOperation(ApplicationFrame.DISPOSE_ON_CLOSE);
            //chart.saveToJpegFile(new File(dirResults + "iris_mdl.jpg"));
            System.out.println("Clustering start");
            grid.performClustering(false);
            System.out.println("Clustering end");
            int i = 1;
            for (Cluster cluster : grid.getClusters()) {
                for (DataPoint dp : cluster.getDataPoints()) {
                    dp.setCluster("" + i);
                }
                ++i;
            }
            //DataChartAlternateDesign chartClustered = new DataChartAlternateDesign("Test, MDL", grid);
            //chartClustered.saveToJpegFile(new File(dirResults + "chartTest_mdl.jpg"));
            try {
                FileIO.writeNMIFiles(grid.getDataPoints(), dirResults + "mdl_nmi" + k, ".txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Finish MDL");

        System.out.println("Start Kmeans");
        Kmean kmean = new Kmean(groundTruthNumber, dataPoints);
        kmean.performKmeans();
        kmean.assignClusterIds();
        //chartClustered = new DataChartAlternateDesign("Test, k - means", kmean.getClusters());
        //chartClustered.saveToJpegFile(new File(dirResults + "chartTest_kmean.jpg"));
        try {
            FileIO.writeNMIFiles(kmean.getDataPoints(), dirResults + "kmean_nmi", ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Finish Kmeans");

        System.out.println("Start DBSCAN");
        DBSCANer dbscan = new DBSCANer(dataPoints);
        int k = 8;
        dbscan.findKnearestNeighborDistance(k, dirResults + "8nearestNeighbors.jpg");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Type in Epsilon: ");
        double epsilon = 0;
        try {
            epsilon = Double.parseDouble(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dbscan.performDBSCAN(epsilon, k);
        dbscan.assignClusterIds();
        //chartClustered = new DataChartAlternateDesign("Test, DBSCAN", dbscan.getClusters());
        //chartClustered.saveToJpegFile(new File(dirResults + "chartTest_dbscan_eps=" + epsilon + ".jpg"));
        try {
            FileIO.writeNMIFiles(dbscan.getDataPoints(), dirResults + "dbscan_nmi", ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Finish DBSCAN");
    }
}
