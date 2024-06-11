package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;

public abstract class Charts  {
    static void plotRoute(String functionName, String label, String algorithmName, double[] result, double[] values){
        JFreeChart xyLineChart = ChartFactory.createXYLineChart(
                "Funkcja: " + functionName + ". Algorytm: " + algorithmName + ". Parametr: " + label,
                label,
                "Wartość f(x)",
                createDataset(result, values, label),
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ChartPanel chartPanel = new ChartPanel( xyLineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );

        XYPlot xyplot = xyLineChart.getXYPlot();

        try {
            ChartUtilities.saveChartAsPNG(new File("wykresy/" + functionName + "/" + algorithmName + "/" + label + ".png"), xyLineChart, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*try {
            ChartUtilities.saveChartAsPNG(new File("wykresy/Heuristic3+Population60+Random5/" + label + "png"), chart, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    static XYDataset createDataset(double[] result, double[] values, String label) {
        final XYSeries line = new XYSeries(label);
        for (int i = 0; i < result.length; i++) {
            line.add(values[i], result[i]);
        }


        final XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries(line);
        return dataset;
    }


}
