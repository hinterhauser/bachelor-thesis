package bachelorthesis.clustering;

import bachelorthesis.clustering.clustering.DBSCANer;
import bachelorthesis.clustering.fileIO.FileIO;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NMIresultsCollector {

    private static List<Double> mdlResults = new ArrayList<>();
    private static List<Double> kmeansResults = new ArrayList<>();
    private static List<Double> DBSCANresults = new ArrayList<>();
    private static List<Double> hierarchicalResults = new ArrayList<>();

    private static final String directory = "/results/performanceTests/results";

    public static void main(String[] args) {

        collectDataSerieOne();
        writeResults(10);
    }

    private static void writeResults(int length) {

        writeResultsToFile("firstSerieNear.txt", 10);
        writeResultsToFile("firstSerieOver.txt", 10);
        writeResultsToFile("firstSerieArbitrary.txt", 10);
        writeResultsToFile("firstSerieMoons.txt", 10);
    }

    private static void writeResultsToFile(String filename, int length) {

        String file = directory + "/" + filename;
        StringBuilder builder = new StringBuilder();
        try {
            FileWriter writer = new FileWriter(file);
            for (int i = 0; i < length; ++i) {
                builder.append(mdlResults.get(i));
                builder.append("   ");
                builder.append(kmeansResults.get(i));
                builder.append("   ");
                builder.append(DBSCANresults.get(i));
                builder.append("   ");
                builder.append(hierarchicalResults.get(i));
                builder.append("\n");
            }
            writer.append(builder);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void collectDataSerieOne() {

        for (int i = 1; i <= 10; ++i) {

            collectResults("/MDL", "mdl", 5, i, mdlResults);
            collectResults("/Kmean", "kmean", 5, i, kmeansResults);
            collectResults("/DBSCAN", "dbscan", 5, i, DBSCANresults);
            collectResults("/hier", "hier", 5, i, hierarchicalResults);
        }
    }

    private static void collectResults(String dir, String algorithm, int dim, int factor, List<Double> resultList) {

        getNMIresult(dir, algorithm, "near", dim, factor, resultList);
        getNMIresult(dir, algorithm, "over", dim, factor, resultList);
        getNMIresult(dir, algorithm, "arbitrary", dim, factor, resultList);
        getNMIresult(dir, algorithm, "moons", dim, factor, resultList);
    }

    private static void getNMIresult(String dir, String algorithm, String type, int dim, int factor, List<Double> resultList) {

        String fileName = directory + dir + "/compare_";
        fileName += algorithm + "_" + type + "_dim" + dim;
        fileName += "size" + factor;
        fileName += "x_results.txt";

        String[] results;
        try {
            results = FileIO.readNMIcsvData(fileName);
            resultList.add(Double.parseDouble(results[0]));
        } catch (IOException e) {
            resultList.add(Double.NaN);
        }
    }
}
