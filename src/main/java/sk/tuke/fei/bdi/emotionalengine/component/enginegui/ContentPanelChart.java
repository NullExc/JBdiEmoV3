package sk.tuke.fei.bdi.emotionalengine.component.enginegui;

/*

   Created with IntelliJ IDEA.

   Bc. Tomáš Herich
   --------------------------- 
   01. 03. 2013
   6:36 PM
   
*/

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.ui.RectangleInsets;
import sk.tuke.fei.bdi.emotionalengine.component.Element;
import sk.tuke.fei.bdi.emotionalengine.component.emotion.Emotion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Set;

public class ContentPanelChart extends JPanel {

    private static final int COUNT = 60;

    private Element element;
    private DynamicTimeSeriesCollection dataSet;
    private JFreeChart chart;


    public ContentPanelChart(Element element) {

        this.element = element;

        // Borders and layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(MyBorders.getEmptyBorder());

        // Small preferred size used for GridBagLayout weight to work as needed
        // weights distribute extra free space in ratio specified
        // when component has small preferred size, there is more extra free space
        // so weights have bigger impact on layout
        setPreferredSize(new Dimension(100, 100));

        initialize();
        createTimer();
    }

    private void initialize() {

        JFreeChart chart = createChart();
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        add(panel);

    }

    private JFreeChart createChart() {

        // Get emotions
        Set<Emotion> emotions = element.getEmotions();

        // Create data set (series for every emotion, resolution second)
        dataSet = new DynamicTimeSeriesCollection(emotions.size(), COUNT, new Second());

        // Set starting time
        dataSet.setTimeBase(new Second(new Date()));


        // Set up chart
        chart = ChartFactory.createTimeSeriesChart(
                null,                       // title
                null,                     // x-axis label
                "Intensity",                // y-axis label
                dataSet,                    // data
                true,                       // create legend?
                true,                       // generate tooltips?
                false                       // generate URLs?
        );

        chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(2.0, 2.0, 2.0, 2.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        chart.removeLegend();

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;

            // Disable points on data lines (too much clutter)
            renderer.setBaseShapesVisible(false);
            renderer.setBaseShapesFilled(false);

            renderer.setDrawSeriesLineAsPath(true);
        }

        ValueAxis domain = plot.getDomainAxis();
        domain.setAutoRange(true);
        ValueAxis range = plot.getRangeAxis();
        range.setRange(0, 1);

        addDataSeries(dataSet);

        return chart;

    }


    private void addDataSeries(DynamicTimeSeriesCollection dataSet) {

        int seriesNumber = 0;

        // Get emotions
        Set<Emotion> emotions = element.getEmotions();

        for (Emotion emotion : emotions) {

            XYItemRenderer r = ((XYPlot) chart.getPlot()).getRenderer();
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;

            // Set line width
            renderer.setSeriesStroke(seriesNumber, new BasicStroke(1.5f, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL));

            // Set line color based on emotion
            if (emotion.getColor() != null) {
                renderer.setSeriesPaint(seriesNumber, emotion.getColor());
            }


//            LegendTitle legend = chart.getLegend();
//            if (legend != null) {
//                legend.setItemFont(new Font("Dialog", Font.PLAIN, 18));
//            }


            dataSet.addSeries(new float[]{0}, seriesNumber, emotion.getEmotionName());
            seriesNumber++;
        }

    }

    private void createTimer() {

        // Every second add emotion intensity values
        Timer timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                int seriesNumber = 0;

                // Get emotions
                Set<Emotion> emotions = element.getEmotions();

                float[] values = new float[emotions.size()];

                // Iterate emotions to get their intensity value
                for (Emotion emotion : emotions) {

                    double intensity = emotion.getIntensity();

                    values[seriesNumber] = (float) intensity;
                    seriesNumber++;

                }

                // Add data to chart and advance time
                dataSet.appendData(values);
                dataSet.advanceTime();

            }
        });


        timer.start();

    }

}
