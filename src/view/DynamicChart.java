package view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

import control.WorldController;

public class DynamicChart extends ApplicationFrame {
	private TimeSeries series;
	private double lastValue = 100.0;
	public JPanel content;
	private WorldController wc;

	public DynamicChart(final String title, WorldController wc) {

		super(title);
		this.series = new TimeSeries("", Millisecond.class);
		this.wc = wc;

		final TimeSeriesCollection dataset = new TimeSeriesCollection(
				this.series);
		final JFreeChart chart = createChart(dataset);

		// timer.setInitialDelay(20);

		// Sets background color of chart
		chart.setBackgroundPaint(Color.LIGHT_GRAY);

		// Created JPanel to show graph on screen
		content = new JPanel(new BorderLayout());

		// Created Chartpanel for chart area
		final ChartPanel chartPanel = new ChartPanel(chart);

		// Added chartpanel to main panel
		content.add(chartPanel);

		// Sets the size of whole window (JPanel)
		chartPanel.setPreferredSize(new java.awt.Dimension(200, 140));

		// Puts the whole content on a Frame
		// setContentPane(content);

		// timer.start();

	}

	public void updateData() {

		this.lastValue = this.wc.getAverageSpeed();

		final Millisecond now = new Millisecond();
		this.series.addOrUpdate(new Millisecond(), this.lastValue);
		
	}

	private JFreeChart createChart(final XYDataset dataset) {
		final JFreeChart result = ChartFactory.createTimeSeriesChart(
				"Average Car Speed", "Time", "Speed", dataset, false, true, false);
		
		final XYPlot plot = result.getXYPlot();

		plot.setBackgroundPaint(new Color(255,255,255));
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.lightGray);
	

		ValueAxis xaxis = plot.getDomainAxis();
		xaxis.setAutoRange(true);

		// Domain axis would show data of 60 seconds for a time
		xaxis.setFixedAutoRange(3000.0); // 60 seconds
		// xaxis.setVerticalTickLabels(true);

		ValueAxis yaxis = plot.getRangeAxis();
		yaxis.setRange(0.0, 200.0);

		return result;
	}
}
