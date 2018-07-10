package bachelorthesis.clustering.clustering;

import bachelorthesis.clustering.data.DataPoint;
import bachelorthesis.clustering.grid.Cluster;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DBSCANer {

    private List<ClusterDBSCAN> clusters;
    private List<DataPoint> dataPoints;

    public DBSCANer(List<DataPoint> dataPoints) {

        setDataPoints(dataPoints);
        assignUnvisited();
    }

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public List<ClusterDBSCAN> getClusters() {
        return clusters;
    }

    public void setClusters(List<ClusterDBSCAN> clusters) {
        this.clusters = clusters;
    }

    private void assignUnvisited() {

        for (DataPoint dataPoint : dataPoints) {
            dataPoint.setCluster("-1");
        }
    }

    public void performDBSCAN(double epsilon, int minPoints) {

        clusters = new ArrayList<>();
        int clusterID = 1;
        Set<DataPoint> queriedNeighbors;
        for (DataPoint dataPoint : dataPoints) {
            if (dataPoint.getCluster().equals("-1")) {
                queriedNeighbors = regionQuery(dataPoint, epsilon);
                // noise is marked with "0"
                if (queriedNeighbors.size() < minPoints) {
                    dataPoint.setCluster("1");
                } else {
                    clusters.add(new ClusterDBSCAN());
                    dataPoint.setCluster("" + clusterID+1);
                    clusters.get(clusterID-1).addDataPoint(dataPoint);
                    expandCluster(queriedNeighbors, epsilon, minPoints, clusters.get(clusterID-1), clusterID+1);
                    ++clusterID;
                }
            }
        }
    }

    private void expandCluster(Set<DataPoint> queriedNeighbors, double epsilon, int minPoints, ClusterDBSCAN cluster, int clusterID) {

        Set<DataPoint> collectedNeighbors = new HashSet<>();
        Set<DataPoint> neighbors;
        for (DataPoint dataPoint : queriedNeighbors) {
            if (dataPoint.getCluster().equals("-1")) {
                dataPoint.setCluster("" + clusterID);
                neighbors = regionQuery(dataPoint, epsilon);
                if (neighbors.size() >= minPoints) {
                    collectedNeighbors.addAll(neighbors);
                }
                cluster.addDataPoint(dataPoint);
            }
        }
        if (collectedNeighbors.size() > 0) {
            expandCluster(collectedNeighbors, epsilon, minPoints, cluster, clusterID);
        }
    }

    private Set<DataPoint> regionQuery(DataPoint center, double epsilon) {

        Set<DataPoint> queriedDP = new HashSet<>();
        double distance;
        for (DataPoint dataPoint : dataPoints) {

            distance = 0.0;
            for (int i = 0; i < dataPoint.getDim(); ++i) {

                double diff = dataPoint.getVector()[i] - center.getVector()[i];
                distance += Math.pow( diff, 2.0 );
            }
            if (Math.sqrt(distance) < epsilon) {
                queriedDP.add(dataPoint);
            }
        }
        return queriedDP;
    }
}
