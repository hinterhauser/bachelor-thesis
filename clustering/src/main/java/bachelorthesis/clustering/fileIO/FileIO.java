package bachelorthesis.clustering.fileIO;

import bachelorthesis.clustering.data.DataPoint;
import bachelorthesis.clustering.grid.Cell;
import bachelorthesis.clustering.grid.Cluster;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileIO {

    public static void writeNMIFiles(List<DataPoint> dataPoints, String directory, String fileName, int index, String ending) throws IOException {

        FileWriter writerGroundTruth = new FileWriter(directory + fileName + index + "_truth" + ending);
        FileWriter writerCluster = new FileWriter(directory + fileName + index + "_results" + ending);

        StringBuilder stringBuilderGroundTruth = new StringBuilder();
        StringBuilder stringBuilderCluster = new StringBuilder();

        for (DataPoint dataPoint : dataPoints) {

            stringBuilderGroundTruth.append(dataPoint.getGroundTruth() + " ");
            stringBuilderCluster.append(dataPoint.getCluster() + " ");
        }

        writerGroundTruth.append(stringBuilderGroundTruth);
        writerCluster.append(stringBuilderCluster);

        writerGroundTruth.flush();
        writerCluster.flush();
        writerGroundTruth.close();
        writerCluster.close();
    }

}
