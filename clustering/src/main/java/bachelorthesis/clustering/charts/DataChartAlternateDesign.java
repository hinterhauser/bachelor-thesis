package bachelorthesis.clustering.charts;

import bachelorthesis.clustering.data.DataPoint;
import bachelorthesis.clustering.grid.Cell;
import bachelorthesis.clustering.grid.Cluster;
import bachelorthesis.clustering.grid.Grid;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import java.awt.*;

public class DataChartAlternateDesign extends ApplicationFrame {

    public DataChartAlternateDesign(String title, Grid grid) {

        super(title);
        final XYSeriesCollection collection = new XYSeriesCollection();
        int clusterIndex = 0;
        System.out.println("Clusters: " + grid.getClusters().size());
        for (Cluster cluster : grid.getClusters()) {

            final XYSeries serie = new XYSeries("Cluster " + ++clusterIndex);
            for (Cell cell : cluster.getClusterCells()) {
                for (DataPoint dataPoint : cell.getDataPoints()) {

                    serie.add(dataPoint.getVector()[0], dataPoint.getVector()[1]);
                }
            }
            collection.addSeries(serie);
        }
        final JFreeChart chart = ChartFactory.createScatterPlot(title, "x", "y", collection, PlotOrientation.VERTICAL, true, false, false);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 600));
        setContentPane(chartPanel);
    }
}
