package org.mslab.tool.educ.client.tool.educ.school.viewer.content;

import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.AreaChart;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;

public class ChartLoader {
	private Runnable _onLoadAction; 
	private Runnable _onLoadAreachartAction = new OnLoadAreaChartAction();
	private Runnable _onLoadBarchartAction = new OnLoadBarChartAction();
	private Runnable _onLoadPiechartAction = new OnLoadPieChartAction();
	
	
	public void load(Runnable onLoadAction) {
		_onLoadAction = onLoadAction;
		VisualizationUtils.loadVisualizationApi(_onLoadAreachartAction, AreaChart.PACKAGE);
	}
	
	private class OnLoadAreaChartAction implements Runnable {
		@Override
		public void run() {
			VisualizationUtils.loadVisualizationApi(_onLoadBarchartAction, BarChart.PACKAGE);
		}
	}
	
	private class OnLoadBarChartAction implements Runnable {
		@Override
		public void run() {
			VisualizationUtils.loadVisualizationApi(_onLoadPiechartAction, PieChart.PACKAGE);
		}
	}
	
	private class OnLoadPieChartAction implements Runnable {
		@Override
		public void run() {
			_onLoadAction.run();
		}
	}
	

	


}
