package bachelorthesis.clustering.charts;

import bachelorthesis.clustering.data.DataPoint;
import bachelorthesis.clustering.grid.Cell;
import bachelorthesis.clustering.grid.Cluster;
import bachelorthesis.clustering.grid.Grid;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;

public class DataChartAlternateDesign extends ApplicationFrame {

    final JFreeChart chart;

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
        chart = ChartFactory.createScatterPlot(title, "x", "y", collection, PlotOrientation.VERTICAL, true, false, false);
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.black);
        plot.setRangeGridlinePaint(Color.black);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);

        NumberTickUnit tickUnit = new NumberTickUnit(grid.getX() / grid.getK());
        //TickUnitSource ticks = NumberAxis.createIntegerTickUnits();
        NumberAxis domain = (NumberAxis) plot.getDomainAxis();
        //domain.setStandardTickUnits(ticks);
        domain.setTickUnit(tickUnit);
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        //range.setStandardTickUnits(ticks);
        range.setTickUnit(tickUnit);

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 600));
        setContentPane(chartPanel);
    }

    public JFreeChart getChart() {
        return chart;
    }

    public void showChart() {

        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }
}
