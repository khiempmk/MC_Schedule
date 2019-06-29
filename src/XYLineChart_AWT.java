import java.util.* ;
import java.awt.Color;
import java.awt.BasicStroke;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class XYLineChart_AWT extends ApplicationFrame {

    public XYLineChart_AWT( String applicationTitle, String chartTitle,int[] b, int numRequest, double[] posX, double[] posY ) {
        super(applicationTitle);
        JFreeChart xylineChart = ChartFactory.createXYLineChart(chartTitle , "" , "" , createDataset(b,numRequest,posX,posY) ,
                PlotOrientation.VERTICAL , true , true , false);
   // ChartFactory.createXYStepChart()
        ChartPanel chartPanel = new ChartPanel( xylineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        final XYPlot plot = xylineChart.getXYPlot( );

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        for (int i = 0 ; i <= numRequest; i++) {
            renderer.setSeriesPaint(i, Color.RED);
            renderer.setSeriesStroke(i, new BasicStroke(4.0f));
        }
      //  renderer.setSeriesStroke( 1 , new BasicStroke( 3.0f ) );
     //   renderer.setSeriesStroke( 2 , new BasicStroke( 2.0f ) );
        plot.setRenderer( renderer );
        plot.setBackgroundPaint(Color.white);
        setContentPane( chartPanel );
    }

    private XYDataset createDataset(int[] b, int numRequest, double[] posX , double[] posY ) {

        final XYSeriesCollection dataset = new XYSeriesCollection( );
        //XYSeries []path = new XYSeries[numRequest];
        XYSeries[] path;
        path = new XYSeries[numRequest+1];
        for (int i = 1 ; i <= numRequest ; i++) {
            String key = Integer.toString(i);
            path[i] = new XYSeries(key);
        }

        for( int i = 1 ; i <= numRequest ;i++) {
            path[i].add(posX[b[i-1]], posY[b[i-1]]);
            path[i].add(posX[b[i]], posY[b[i]]);
            dataset.addSeries(path[i]);
        }
      //  dataset.addSeries(path[1]);

        return dataset;
    }

  }