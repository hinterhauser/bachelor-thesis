package bachelorthesis.clustering.grid;

import bachelorthesis.clustering.data.DataPoint;

import java.util.ArrayList;
import java.util.List;

public class Grid implements StatsObj {

    private int k;
    private Cell[][] cells;
    private List<DataPoint> dataPoints;
    private double x;
    private double y;

    private double mean;
    private double deviation;

    public Grid(int k, List<DataPoint> dataPoints, double x, double y) {

        setK(k);
        setDataPoints(dataPoints);
        cells = new Cell[k][k];
        setX(x);
        setY(y);
    }

    public double getMean() {
        return mean;
    }

    public double getDeviation() {
        return deviation;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
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

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    private void calculateMean() {

        double sum = 0.0;
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {

                sum += cells[i][j].getMean();
            }
        }
        mean = sum / (k * k);
    }

    @Override
    public void calculateDeviationAndMean() {

        calculateMean();
        double sum = 0.0;
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {

                double partDiff = cells[i][j].getMean() - mean;
                sum += Math.pow(partDiff, 2.0);
            }
        }
        deviation = Math.sqrt(sum / (k * k));
    }

    @Override
    public double calculateCV() {

        if (mean == 0.0) {
            return 0.0;
        }
        return deviation / mean * 100;
    }

    public void setupCells() {

        double xCellLength = x / k;
        double yCellLength = y / k;

        int xCellIndex = 0;
        int yCellIndex = 0;
        for (double i = 0.0; (i < y) & (yCellIndex < k); i += yCellLength) {
            for (double j = 0.0; (j < x) & (xCellIndex < k); j += xCellLength) {

                double cellCenter[] = new double[2];
                cellCenter[0] = (i + i + yCellLength) / 2;
                cellCenter[1] = (j + j + xCellLength) / 2;
                cells[yCellIndex][xCellIndex++] = new Cell(new ArrayList<DataPoint>(), 2, cellCenter);
            }
            xCellIndex = 0;
            yCellIndex++;
        }
        assignDataPointsToCells(xCellLength, yCellLength);
        calculateCellsDeviationAndMean();
    }

    private void calculateCellsDeviationAndMean() {

        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {

                cells[i][j].calculateDeviationAndMean();
            }
        }
    }

    private void assignDataPointsToCells(double xCellLength, double yCellLength) {

        for (DataPoint dataPoint : dataPoints) {

            int indexX = (int) (dataPoint.getVector()[1] / xCellLength);
            int indexY = (int) (dataPoint.getVector()[0] / yCellLength);
            cells[indexY][indexX].getDataPoints().add(dataPoint);
        }
    }

    public int nonEmptyCells() {

        int sum = 0;
        for (int i = 0; i < getK(); ++i) {
            for (int j = 0; j < getK(); ++j) {

                if (getCells()[i][j].getDataPoints().size() > 0) {
                    ++sum;
                }
            }
        }
        return sum;
    }
}
