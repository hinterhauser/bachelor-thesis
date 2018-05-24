package bachelorthesis.clustering.data;

import bachelorthesis.clustering.grid.Grid;
import bachelorthesis.clustering.statistics.RegressionAnalyser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DataPartitioner {

    private List<DataPoint> dataPoints;
    private double x;
    private double y;

    public DataPartitioner(List<DataPoint> dataPoints, double x, double y) {

        setDataPoints(dataPoints);
        setX(x);
        setY(y);
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

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public int findOptimalPartition(String fileName, String areaFile) {

        String outputResults = "";
        String outputAreas = "";
        Grid grid;
        RegressionAnalyser CostAnalyser = new RegressionAnalyser();
        RegressionAnalyser AreaAnalyser = new RegressionAnalyser();
        int sizeTestSeries = 150;

        for (int k = 1; k <= sizeTestSeries; ++k) {
            grid = new Grid(k, dataPoints, x, y);
            grid.setupCells();
            grid.calculateDeviationAndMean();
            double partitioningCost = calculatePartitioningCost(grid);
            outputResults += k + " " + partitioningCost + "\n";
            double areaCost = (x / k) * (y / k) * grid.nonEmptyCells();
            outputAreas += k + " " + areaCost + "\n";
            CostAnalyser.addDataPoint(partitioningCost);
            AreaAnalyser.addDataPoint(areaCost);
        }
        System.out.println("First the optimal grid size is computed via partitioning cost, then via areas");
        int costK = computeOptimalGridSize(CostAnalyser, sizeTestSeries);
        int costA = computeOptimalGridSize(AreaAnalyser, sizeTestSeries);

        try {
            writeToFile(outputResults, fileName);
            System.out.println(fileName + " Successfully written");
            writeToFile(outputAreas, areaFile);
            System.out.println(areaFile + " Successfully written");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (costA > costK) {
            return costA;
        }
        return costK;
    }

    private int computeOptimalGridSize(RegressionAnalyser analyser, int sizeTestSeries) {

        analyser.setK(sizeTestSeries); // TODO that might not be necessary, change it eventually
        analyser.linearRegression();
        double sumDiffs = 0.0;
        for (int i = 1; i <= sizeTestSeries; ++i) {
            double predicted = i * analyser.getAlpha1() + analyser.getAlpha0();
            double diff = Math.abs(predicted - analyser.getDataPoint(i-1));
            sumDiffs += diff;
        }
        sumDiffs /= sizeTestSeries;
        int i = 1;
        for (; i <= sizeTestSeries; ++i) {
            double predicted = i * analyser.getAlpha1() + analyser.getAlpha0();
            double diff = predicted - analyser.getDataPoint(i-1);
            if (Math.abs(diff) < sumDiffs) {
                break;
            }
        }
        ++i; // because it is rounded upwards
        System.out.println("The size of the grid should be at least " + i + " times " + i);
        return i;
    }

    private void writeToFile(String output, String fileName) throws IOException {

        FileWriter writer = new FileWriter(fileName);

        writer.append(output);

        writer.flush();
        writer.close();
    }

    public double calculatePartitioningCost(Grid grid) {

        double cost = 0.0;
        //cost = grid.calculateCV() / maxCVlocal(grid);     // initially the maximum of local CVs was used, because the results fluctuated a lot, the average of non - zero cells was used
        cost = grid.calculateCV() / avgCVlocal(grid);
        return cost;
    }

    private double maxCVlocal(Grid grid) {

        double cvLocal = 0.0;
        for (int i = 0; i < grid.getK(); ++i) {
            for (int j = 0; j < grid.getK(); ++j) {

                double cv = grid.getCells()[i][j].calculateCV();
                if (cv > cvLocal) {
                    cvLocal = cv;
                }
            }
        }
        return cvLocal;
    }

    private double avgCVlocal(Grid grid) {

        double cvLocal = 0.0;
        int numCells = 0;
        for (int i = 0; i < grid.getK(); ++i) {
            for (int j = 0; j < grid.getK(); ++j) {

                double cv = grid.getCells()[i][j].calculateCV();
                cvLocal += cv;
                if (cv != 0.0) {
                    numCells++;
                }
            }
        }
        return cvLocal / numCells;
    }
}
