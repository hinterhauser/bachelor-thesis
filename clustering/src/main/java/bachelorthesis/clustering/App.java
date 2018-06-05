package bachelorthesis.clustering;

import bachelorthesis.clustering.charts.DataChartAlternateDesign;
import bachelorthesis.clustering.data.*;
import bachelorthesis.clustering.grid.Grid;
import org.jfree.ui.RefineryUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {

        //createShape("halfmoon");
        String shapeName = "halfmoon";
        ArbitraryShape shape = new ArbitraryShape(2);
        List<Segment> segments = new ArrayList<>();
        selectShape(shapeName, segments);
        shape.setSegments(segments);
        ShapeGenerator generator = new ShapeGenerator(shape);
        List<DataPoint> dataPoints = generator.generateShape(1000);
        DataPartitioner partitioner = new DataPartitioner(dataPoints, 100.0, 100.0);
        int k = partitioner.findOptimalPartition("results/shapes/results.txt", "results/shapes/areas.txt");
        Grid grid = new Grid(k, dataPoints, 100.0, 100.0);
        grid.setupCells();
        grid.setupClusters();
        grid.performClustering(false);

        DataChartAlternateDesign chart = new DataChartAlternateDesign(shapeName, grid);
        chart.showChart();
    }

    private static void createShape(String shapeName) {

        ArbitraryShape shape = new ArbitraryShape(2);
        List<Segment> segments = new ArrayList<>();
        selectShape(shapeName, segments);
        shape.setSegments(segments);
        ShapeGenerator generator = new ShapeGenerator(shape);
        List<DataPoint> dataPoints = generator.generateShape(1000);
        Grid grid = new Grid(10, dataPoints, 100, 100);
        grid.setupCells();
        grid.setupClusters();

        DataChartAlternateDesign chart = new DataChartAlternateDesign(shapeName, grid);
        chart.showChart();
    }

    private static void selectShape(String shapeName, List<Segment> segments) {

        if (shapeName.equals("halfmoon-simple")) {
            createHalfmoonSimple(segments);
        } else if (shapeName.equals("circle")) {
            createCircle(segments);
        } else if (shapeName.equals("halfmoon")) {
            createHalfMoon(segments);
        }
    }

    private static void createHalfMoon(List<Segment> segments) {

        double[] center = new double[2];
        center[0] = 50.0;
        center[1] = 50.0;

        double radius = 10.0;
        double x = 0.0;
        double y = radius;
        while (y > -radius) {

            x = Math.sqrt(Math.pow(radius, 2.0) - Math.pow(y, 2.0));

            segments.add(new Segment(getXandY(center, -x, y), 0.1));
            segments.add(new Segment(getXandY(center, -x, -y), 0.1));

            y -= 0.1;
        }
    }

    private static void createCircle(List<Segment> segments) {

        double[] center = new double[2];
        center[0] = 50.0;
        center[1] = 50.0;

        double radius = 10.0;
        double x = 0.0;
        double y = 0.0;
        while (x < radius) {

            y = Math.sqrt(Math.pow(radius, 2.0) - Math.pow(x, 2.0));

            segments.add(new Segment(getXandY(center, x, y), 0.1));
            segments.add(new Segment(getXandY(center, -x, y), 0.1));
            segments.add(new Segment(getXandY(center, x, -y), 0.1));
            segments.add(new Segment(getXandY(center, -x, -y), 0.1));

            x += 0.1;
        }
    }

    private static double[] getXandY(double[] center, double x, double y) {

        double[] vector = new double[2];

        vector[0] = center[0] + x;
        vector[1] = center[1] + y;

        return vector;
    }

    private static void createHalfmoonSimple(List<Segment> segments) {

        double[] leftestPoint = new double[2];
        double x = 40.0;
        double y = 50.0;
        leftestPoint[0] = x;
        leftestPoint[1] = y;
        List<double[]> points = new ArrayList<>();
        points.add(leftestPoint);

        double bow = 1.000001;
        for (int i = 0; i < 20; ++i) {

            double[] point = new double[2];
            point[0] = x += (0.03 * bow);
            point[1] = y += 0.5;
            points.add(point);
            bow *= bow;
        }
        x = 40.0;
        y = 50.0;
        bow = 1.000001;
        for (int i = 0; i < 20; ++i) {

            double[] point = new double[2];
            point[0] = x += (0.03 * bow);
            point[1] = y -= 0.4;
            points.add(point);
            bow *= bow;
        }
        for (double[] point : points) {

            segments.add(new Segment(point, 0.1));
        }
    }
}
