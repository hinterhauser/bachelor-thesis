package bachelorthesis.clustering;

import bachelorthesis.clustering.data.DataPoint;
import bachelorthesis.clustering.grid.Cell;
import bachelorthesis.clustering.grid.Cluster;
import bachelorthesis.clustering.grid.Grid;
import bachelorthesis.clustering.statistics.RegressionAnalyser;
import org.junit.Test;

import java.util.*;

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

    @Test
    public void testClusters() {

        // look at getInitialClusters() for the structure of the clusters
        List<Cluster> clusters = getInitialClusters();

        // initial
        assertEquals(clusters.size(), 5);
        assertEquals(clusters.get(0).getNeighbors().size(), 1);
        assertEquals(clusters.get(0).getClusterCells().size(), 5);
        assertEquals(clusters.get(1).getNeighbors().size(), 2);
        assertEquals(clusters.get(1).getClusterCells().size(), 1);
        assertEquals(clusters.get(2).getNeighbors().size(), 3);
        assertEquals(clusters.get(2).getClusterCells().size(), 3);
        assertEquals(clusters.get(3).getNeighbors().size(), 1);
        assertEquals(clusters.get(3).getClusterCells().size(), 4);
        assertEquals(clusters.get(4).getNeighbors().size(), 1);
        assertEquals(clusters.get(4).getClusterCells().size(), 2);

        // merging B and E (index 1 and 4)
        clusters.get(1).mergeClusters(clusters.get(4));
        removeMerger(clusters, clusters.get(4));

        //debugMerging(clusters);

        assertEquals(clusters.size(), 4);
        assertEquals(clusters.get(0).getNeighbors().size(), 1);
        assertEquals(clusters.get(0).getClusterCells().size(), 5);
        assertEquals(clusters.get(1).getNeighbors().size(), 1);
        assertEquals(clusters.get(1).getClusterCells().size(), 3);
        assertEquals(clusters.get(2).getNeighbors().size(), 3);
        assertEquals(clusters.get(2).getClusterCells().size(), 3);
        assertEquals(clusters.get(3).getNeighbors().size(), 1);
        assertEquals(clusters.get(3).getClusterCells().size(), 4);

        // merging C and D (index 2 and 3)
        clusters.get(2).mergeClusters(clusters.get(3));
        removeMerger(clusters, clusters.get(3));
        //debugMerging(clusters);

        assertEquals(clusters.size(), 3);
        assertEquals(clusters.get(0).getNeighbors().size(), 1);
        assertEquals(clusters.get(0).getClusterCells().size(), 5);
        assertEquals(clusters.get(1).getNeighbors().size(), 1);
        assertEquals(clusters.get(1).getClusterCells().size(), 3);
        assertEquals(clusters.get(2).getNeighbors().size(), 2);
        assertEquals(clusters.get(2).getClusterCells().size(), 7);

        // merging A and CD (index 0 and 2)
        clusters.get(0).mergeClusters(clusters.get(2));
        removeMerger(clusters, clusters.get(2));
        //debugMerging(clusters);

        assertEquals(clusters.size(), 2);
        assertEquals(clusters.get(0).getNeighbors().size(), 1);
        assertEquals(clusters.get(0).getClusterCells().size(), 12);
        assertEquals(clusters.get(1).getNeighbors().size(), 1);
        assertEquals(clusters.get(1).getClusterCells().size(), 3);

        // merging ACD and BE (index 0 and 1)
        //System.out.println("\n\n Last Iteration \n\n");
        clusters.get(0).mergeClusters(clusters.get(1));
        clusters.remove(1);

        assertEquals(clusters.size(), 1);
        assertEquals(clusters.get(0).getNeighbors().size(), 0);
        assertEquals(clusters.get(0).getClusterCells().size(), 15);

        int sum = 0;
        for (Cell cell : clusters.get(0).getClusterCells()) {
            sum++;
        }
        assertEquals(sum, 15);
    }

    @Test
    public void testClusters1() {

        List<Cluster> clusters = getInitialClusters();

        // merging C and D (index 2 and 3)
        clusters.get(2).mergeClusters(clusters.get(3));

        assertEquals(clusters.size(), 5);
        assertEquals(clusters.get(0).getNeighbors().size(), 2);
        assertEquals(clusters.get(0).getClusterCells().size(), 5);
        assertEquals(clusters.get(1).getNeighbors().size(), 3);
        assertEquals(clusters.get(1).getClusterCells().size(), 1);
        assertEquals(clusters.get(2).getNeighbors().size(), 2);
        assertEquals(clusters.get(2).getClusterCells().size(), 7);
        assertEquals(clusters.get(3).getNeighbors().size(), 2);
        assertEquals(clusters.get(3).getClusterCells().size(), 7);
        assertEquals(clusters.get(4).getNeighbors().size(), 1);
        assertEquals(clusters.get(4).getClusterCells().size(), 2);

        // merging C and A (index 2 and 3)
        clusters.get(2).mergeClusters(clusters.get(0));

        assertEquals(clusters.size(), 5);
        assertEquals(clusters.get(0).getNeighbors().size(), 2);
        assertEquals(clusters.get(0).getClusterCells().size(), 12);
        assertEquals(clusters.get(1).getNeighbors().size(), 4);
        assertEquals(clusters.get(1).getClusterCells().size(), 1);
        assertEquals(clusters.get(2).getNeighbors().size(), 2);
        assertEquals(clusters.get(2).getClusterCells().size(), 12);
        assertEquals(clusters.get(3).getNeighbors().size(), 3);
        assertEquals(clusters.get(3).getClusterCells().size(), 7);
        assertEquals(clusters.get(4).getNeighbors().size(), 1);
        assertEquals(clusters.get(4).getClusterCells().size(), 2);

        // merging E and B (index 4 and 1)
        clusters.get(4).mergeClusters(clusters.get(1));

        assertEquals(clusters.size(), 5);
        assertEquals(clusters.get(0).getNeighbors().size(), 3);
        assertEquals(clusters.get(0).getClusterCells().size(), 12);
        assertEquals(clusters.get(1).getNeighbors().size(), 3);
        assertEquals(clusters.get(1).getClusterCells().size(), 3);
        assertEquals(clusters.get(2).getNeighbors().size(), 3);
        assertEquals(clusters.get(2).getClusterCells().size(), 12);
        assertEquals(clusters.get(3).getNeighbors().size(), 4);
        assertEquals(clusters.get(3).getClusterCells().size(), 7);
        assertEquals(clusters.get(4).getNeighbors().size(), 3);
        assertEquals(clusters.get(4).getClusterCells().size(), 3);

        // merging B and C (index 1 and 2)
        clusters.get(1).mergeClusters(clusters.get(2));

        assertEquals(clusters.size(), 5);
        assertEquals(clusters.get(0).getNeighbors().size(), 4);
        assertEquals(clusters.get(0).getClusterCells().size(), 12);
        assertEquals(clusters.get(1).getNeighbors().size(), 3);
        assertEquals(clusters.get(1).getClusterCells().size(), 15);
        assertEquals(clusters.get(2).getNeighbors().size(), 3);
        assertEquals(clusters.get(2).getClusterCells().size(), 15);
        assertEquals(clusters.get(3).getNeighbors().size(), 4);
        assertEquals(clusters.get(3).getClusterCells().size(), 7);
        assertEquals(clusters.get(4).getNeighbors().size(), 4);
        assertEquals(clusters.get(4).getClusterCells().size(), 3);

        // What is going on here? Well, the merging process converges

        // merging D and E (index 3 and 4)
        clusters.get(3).mergeClusters(clusters.get(4));
        clusters.get(0).mergeClusters(clusters.get(1));
        clusters.get(3).mergeClusters(clusters.get(4));
        clusters.get(0).mergeClusters(clusters.get(4));
        clusters.get(0).mergeClusters(clusters.get(3));
        clusters.get(3).mergeClusters(clusters.get(4));
        clusters.get(0).mergeClusters(clusters.get(1));
        clusters.get(3).mergeClusters(clusters.get(4));
        clusters.get(0).mergeClusters(clusters.get(4));
        clusters.get(0).mergeClusters(clusters.get(3));
        clusters.get(3).mergeClusters(clusters.get(4));
        clusters.get(0).mergeClusters(clusters.get(1));
        clusters.get(3).mergeClusters(clusters.get(4));
        clusters.get(0).mergeClusters(clusters.get(4));
        clusters.get(0).mergeClusters(clusters.get(3));

        // the number of cluster cells converges to the total sum (as it should be)
        // if the merger is not removed after the merging process, the number of
        // neighbors grows, else it decreases

        assertEquals(clusters.size(), 5);
        assertEquals(clusters.get(0).getNeighbors().size(), 3);
        assertEquals(clusters.get(0).getClusterCells().size(), 15);
        assertEquals(clusters.get(1).getNeighbors().size(), 4);
        assertEquals(clusters.get(1).getClusterCells().size(), 15);
        assertEquals(clusters.get(2).getNeighbors().size(), 4);
        assertEquals(clusters.get(2).getClusterCells().size(), 15);
        assertEquals(clusters.get(3).getNeighbors().size(), 3);
        assertEquals(clusters.get(3).getClusterCells().size(), 15);
        assertEquals(clusters.get(4).getNeighbors().size(), 4);
        assertEquals(clusters.get(4).getClusterCells().size(), 15);
    }

    private ArrayList<Cluster> getInitialClusters() {

        /*
            The test is run on 5 clusters with the following structure:

              A
            B C D
            E

            These 5 clusters are merged into one
         */
        List<Cluster> clusters = new ArrayList<>();

        Cluster a = new Cluster();
        Cluster b = new Cluster();
        Cluster c = new Cluster();
        Cluster d = new Cluster();
        Cluster e = new Cluster();

        a.setName("Cluster A");
        b.setName("Cluster B");
        c.setName("Cluster C");
        d.setName("Cluster D");
        e.setName("Cluster E");

        a.addNeighbor(c);
        b.addNeighbor(c);
        b.addNeighbor(e);
        c.addNeighbor(a);
        c.addNeighbor(b);
        c.addNeighbor(d);
        d.addNeighbor(c);
        e.addNeighbor(b);

        Set<Cell> dataPointsA = new LinkedHashSet<>();
        dataPointsA.add(new Cell());
        dataPointsA.add(new Cell());
        dataPointsA.add(new Cell());
        dataPointsA.add(new Cell());
        dataPointsA.add(new Cell());

        a.setClusterCells(dataPointsA);

        Set<Cell> dataPointsB = new LinkedHashSet<>();
        dataPointsB.add(new Cell());

        b.setClusterCells(dataPointsB);

        Set<Cell> dataPointsC = new LinkedHashSet<>();
        dataPointsC.add(new Cell());
        dataPointsC.add(new Cell());
        dataPointsC.add(new Cell());

        c.setClusterCells(dataPointsC);

        Set<Cell> dataPointsD = new LinkedHashSet<>();
        dataPointsD.add(new Cell());
        dataPointsD.add(new Cell());
        dataPointsD.add(new Cell());
        dataPointsD.add(new Cell());

        d.setClusterCells(dataPointsD);

        Set<Cell> dataPointsE = new LinkedHashSet<>();
        dataPointsE.add(new Cell());
        dataPointsE.add(new Cell());

        e.setClusterCells(dataPointsE);

        clusters.add(a);
        clusters.add(b);
        clusters.add(c);
        clusters.add(d);
        clusters.add(e);

        return (ArrayList<Cluster>) clusters;
    }

    private void removeMerger(List<Cluster> clusters, Cluster merger) {

        for (Cluster cluster : clusters) {
            cluster.removeNeighbor(merger);
        }
        clusters.remove(merger);
    }

    @Test
    public void testGridClustering() {

        Grid testGrid = new Grid(5, null, 100, 100);

        /*
            The testGrid should look like this:

                0   0   0   0   0
                0   1   3   2   0
                0   5   7   1   0
                0   2   1   4   0
                0   0   0   0   0
         */

        fillTestGridCells(testGrid);
        testGrid.setupClusters();

        // test for correct initialisation
        List<Cluster> clusters = testGrid.getClusters();

        // test for the length of the list of all clusters and cluster cells
        assertEquals(clusters.size(), 9);
        for (int i = 0; i < 9; ++i)
            assertEquals(clusters.get(i).getClusterCells().size(), 1);
        // test for assigned datapoints
        Iterator<Cell> iterator;
        iterator = clusters.get(0).getClusterCells().iterator();
        assertEquals(iterator.next().getDataPoints().size(), 2);
        iterator = clusters.get(1).getClusterCells().iterator();
        assertEquals(iterator.next().getDataPoints().size(), 1);
        iterator = clusters.get(2).getClusterCells().iterator();
        assertEquals(iterator.next().getDataPoints().size(), 4);
        iterator = clusters.get(3).getClusterCells().iterator();
        assertEquals(iterator.next().getDataPoints().size(), 5);
        iterator = clusters.get(4).getClusterCells().iterator();
        assertEquals(iterator.next().getDataPoints().size(), 7);
        iterator = clusters.get(5).getClusterCells().iterator();
        assertEquals(iterator.next().getDataPoints().size(), 1);
        iterator = clusters.get(6).getClusterCells().iterator();
        assertEquals(iterator.next().getDataPoints().size(), 1);
        iterator = clusters.get(7).getClusterCells().iterator();
        assertEquals(iterator.next().getDataPoints().size(), 3);
        iterator = clusters.get(8).getClusterCells().iterator();
        assertEquals(iterator.next().getDataPoints().size(), 2);
        // test for neighbors
        assertEquals(clusters.get(0).getNeighbors().size(), 3); // TODO something is wrong here, fix it
        assertEquals(clusters.get(1).getNeighbors().size(), 3);
        assertEquals(clusters.get(2).getNeighbors().size(), 3);
        assertEquals(clusters.get(3).getNeighbors().size(), 3);
        assertEquals(clusters.get(4).getNeighbors().size(), 3);
        assertEquals(clusters.get(5).getNeighbors().size(), 3);
        assertEquals(clusters.get(6).getNeighbors().size(), 3);
        assertEquals(clusters.get(7).getNeighbors().size(), 3);
        assertEquals(clusters.get(8).getNeighbors().size(), 3);
    }

    private void fillTestGridCells(Grid testGrid) {

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {

                testGrid.getCells()[i][j] = new Cell();
            }
        }
        testGrid.getCells()[1][1] = fillTestCell(2);
        testGrid.getCells()[1][2] = fillTestCell(1);
        testGrid.getCells()[1][3] = fillTestCell(4);
        testGrid.getCells()[2][1] = fillTestCell(5);
        testGrid.getCells()[2][2] = fillTestCell(7);
        testGrid.getCells()[2][3] = fillTestCell(1);
        testGrid.getCells()[3][1] = fillTestCell(1);
        testGrid.getCells()[3][2] = fillTestCell(3);
        testGrid.getCells()[3][3] = fillTestCell(2);
    }

    private Cell fillTestCell(int length) {

        List<DataPoint> dataPoints = new ArrayList<>();

        for (int i = 0; i < length; ++i) {
            dataPoints.add(new DataPoint());
        }

        Cell cell = new Cell();
        cell.setDataPoints(dataPoints);
        return cell;
    }

    /*
        ***************************
        * functions for debugging *
        ***************************
     */

    private void debugMerging(List<Cluster> clusters) {

        System.out.println("debug:");
        for (Cluster cluster : clusters) {
            System.out.println(cluster.getName());
            for (Cluster neighbor : cluster.getNeighbors()) {
                neighbor.printNeighbors();
            }
        }
    }
}
