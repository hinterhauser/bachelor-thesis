package bachelorthesis.clustering;

import bachelorthesis.clustering.data.DataPoint;
import bachelorthesis.clustering.grid.Cell;
import bachelorthesis.clustering.grid.Grid;
import bachelorthesis.clustering.statistics.RegressionAnalyser;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UnitTest {

    @Test
    public void testEmptyCell() {

        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        double[] center = new double[2];
        center[0] = 1;
        center[1] = 1;
        Cell cell = new Cell(dataPoints, 2, center);

        assertEquals(cell.getMean(), 0, 0.0001);
        assertEquals(cell.getDeviation(), 0, 0.0001);
    }

    @Test
    public void testCell() {

        Cell cell = populateTestCell1();

        assertEquals(cell.getDeviation(), 1.29442366, 0.0001);
        assertEquals(cell.getMean(), 10.35975228460709931035730151336, 0.0001);
    }

    @Test
    public void testGrid() {

        Cell cell1 = populateTestCell1();
        Cell cell2 = populateTestCell2();
        Cell cell3 = populateTestCell3();

        assertEquals(cell1.getDeviation(), 1.29442366, 0.0001);
        assertEquals(cell1.getMean(), 10.359752284607, 0.0001);
        assertEquals(cell2.getDeviation(), 0.51034206, 0.0001);
        assertEquals(cell2.getMean(), 1.86803399, 0.0001);
        assertEquals(cell3.getDeviation(), 0.51368836, 0.0001);
        assertEquals(cell3.getMean(), 1.55009385, 0.0001);

        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        double[] center = new double[2];
        center[0] = 1;
        center[1] = 1;
        Cell cell = new Cell(dataPoints, 2, center);

        assertEquals(cell.getMean(), 0, 0.0001);
        assertEquals(cell.getDeviation(), 0, 0.0001);

        Cell[][] cells = new Cell[2][2];
        cells[0][0] = cell;
        cells[0][1] = cell1;
        cells[1][0] = cell2;
        cells[1][1] = cell3;

        dataPoints.addAll(cell1.getDataPoints());
        dataPoints.addAll(cell2.getDataPoints());
        dataPoints.addAll(cell3.getDataPoints());

        Grid grid = new Grid(2, dataPoints, 1000, 1000);
        grid.setCells(cells);
        grid.calculateDeviationAndMean();

        assertEquals(grid.getDataPoints().size(), 12);
        assertEquals(grid.getMean(), 3.44447003, 0.0001);
        assertEquals(grid.getDeviation(), 4.05460584, 0.0001);
    }

    @Test
    public void testSetupCells() {

        ArrayList<DataPoint> dataPoints = getTestDataPoints();
        Grid grid = new Grid(2 , dataPoints, 20, 20);
        grid.setupCells();

        assertEquals(grid.getDataPoints().size(), 12);
        double[] center = { 5.0, 5.0};
        assertEquals(grid.getCells()[0][0].getCenter()[0], center[0], 0.0001);
        assertEquals(grid.getCells()[0][0].getCenter()[1], center[1], 0.0001);
        center[1] = 15.0;
        assertEquals(grid.getCells()[0][1].getCenter()[0], center[0], 0.0001);
        assertEquals(grid.getCells()[0][1].getCenter()[1], center[1], 0.0001);
        center[0] = 15.0;
        center[1] = 5.0;
        assertEquals(grid.getCells()[1][0].getCenter()[0], center[0], 0.0001);
        assertEquals(grid.getCells()[1][0].getCenter()[1], center[1], 0.0001);
        center[1] = 15.0;
        assertEquals(grid.getCells()[1][1].getCenter()[0], center[0], 0.0001);
        assertEquals(grid.getCells()[1][1].getCenter()[1], center[1], 0.0001);

        // lower left cell
        assertEquals(grid.getCells()[0][0].getDataPoints().size(), 6);
        for (DataPoint dataPoint : grid.getCells()[0][0].getDataPoints()) {
            System.out.println("00 : y=" + dataPoint.getVector()[0] + " x=" + dataPoint.getVector()[1]);
        }
        // lower right cell
        assertEquals(grid.getCells()[0][1].getDataPoints().size(), 2);
        for (DataPoint dataPoint : grid.getCells()[0][1].getDataPoints()) {
            System.out.println("01 : y=" + dataPoint.getVector()[0] + " x=" + dataPoint.getVector()[1]);
        }
        // upper left cell
        assertEquals(grid.getCells()[1][0].getDataPoints().size(), 4);
        for (DataPoint dataPoint : grid.getCells()[1][0].getDataPoints()) {
            System.out.println("10 : y=" + dataPoint.getVector()[0] + " x=" + dataPoint.getVector()[1]);
        }
        // upper right cell
        assertEquals(grid.getCells()[1][1].getDataPoints().size(), 0);
    }

    private ArrayList<DataPoint> getTestDataPoints() {

        ArrayList<DataPoint> dataPoints = new ArrayList<>();

        double[] p1 = new double[2];
        p1[0] = 2;
        p1[1] = 2;
        double[] p2 = new double[2];
        p2[0] = 3;
        p2[1] = 4;
        double[] p3 = new double[2];
        p3[0] = 5;
        p3[1] = 2;

        dataPoints.add(new DataPoint(2, p1));
        dataPoints.add(new DataPoint(2, p2));
        dataPoints.add(new DataPoint(2, p3));

        double[] p4 = new double[2];
        p4[0] = 4;
        p4[1] = 10;
        double[] p5 = new double[2];
        p5[0] = 4;
        p5[1] = 8;
        double[] p6 = new double[2];
        p6[0] = 6;
        p6[1] = 8;
        double[] p7 = new double[2];
        p7[0] = 7;
        p7[1] = 10;

        dataPoints.add(new DataPoint(2, p4));
        dataPoints.add(new DataPoint(2, p5));
        dataPoints.add(new DataPoint(2, p6));
        dataPoints.add(new DataPoint(2, p7));

        double[] p8 = new double[2];
        p8[0] = 9;
        p8[1] = 3;
        double[] p9 = new double[2];
        p9[0] = 10;
        p9[1] = 5;
        double[] p10 = new double[2];
        p10[0] = 11;
        p10[1] = 4;
        double[] p11 = new double[2];
        p11[0] = 12;
        p11[1] = 3;
        double[] p12 = new double[2];
        p12[0] = 12;
        p12[1] = 6;

        dataPoints.add(new DataPoint(2, p8));
        dataPoints.add(new DataPoint(2, p9));
        dataPoints.add(new DataPoint(2, p10));
        dataPoints.add(new DataPoint(2, p11));
        dataPoints.add(new DataPoint(2, p12));

        return dataPoints;
    }

    private Cell populateTestCell3() {

        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        double[] center = new double[2];
        center[0] = 3;
        center[1] = 3;

        double[] p1 = new double[2];
        p1[0] = 2;
        p1[1] = 2;
        double[] p2 = new double[2];
        p2[0] = 3;
        p2[1] = 4;
        double[] p3 = new double[2];
        p3[0] = 5;
        p3[1] = 2;

        dataPoints.add(new DataPoint(2, p1));
        dataPoints.add(new DataPoint(2, p2));
        dataPoints.add(new DataPoint(2, p3));

        return new Cell(dataPoints, 2, center);
    }

    private Cell populateTestCell2() {

        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        double[] center = new double[2];
        center[0] = 5;
        center[1] = 10;

        double[] p1 = new double[2];
        p1[0] = 4;
        p1[1] = 10;
        double[] p2 = new double[2];
        p2[0] = 4;
        p2[1] = 8;
        double[] p3 = new double[2];
        p3[0] = 6;
        p3[1] = 8;
        double[] p4 = new double[2];
        p4[0] = 7;
        p4[1] = 10;

        dataPoints.add(new DataPoint(2, p1));
        dataPoints.add(new DataPoint(2, p2));
        dataPoints.add(new DataPoint(2, p3));
        dataPoints.add(new DataPoint(2, p4));

        return new Cell(dataPoints, 2, center);
    }

    private Cell populateTestCell1() {

        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        double[] center = new double[2];
        center[0] = 1;
        center[1] = 1;

        double[] p1 = new double[2];
        p1[0] = 9;
        p1[1] = 3;
        double[] p2 = new double[2];
        p2[0] = 10;
        p2[1] = 5;
        double[] p3 = new double[2];
        p3[0] = 11;
        p3[1] = 4;
        double[] p4 = new double[2];
        p4[0] = 12;
        p4[1] = 3;
        double[] p5 = new double[2];
        p5[0] = 12;
        p5[1] = 6;

        dataPoints.add(new DataPoint(2, p1));
        dataPoints.add(new DataPoint(2, p2));
        dataPoints.add(new DataPoint(2, p3));
        dataPoints.add(new DataPoint(2, p4));
        dataPoints.add(new DataPoint(2, p5));

        return new Cell(dataPoints, 2, center);
    }

    @Test
    public void testRegressionAnalyserLinear() {

        RegressionAnalyser analyser = new RegressionAnalyser();

        analyser.addDataPoint(0);
        analyser.addDataPoint(2.529);
        analyser.addDataPoint(5.732);
        analyser.addDataPoint(5.674);
        analyser.addDataPoint(6.832);
        analyser.setK(5);

        analyser.linearRegression();

        assertEquals(analyser.getAlpha1(), 1.6809, 0.0001);
        assertEquals(analyser.getAlpha0(),-0.8893, 0.0001);
        assertEquals(analyser.getDeviation(), 2.8226, 0.0001);
    }
}
