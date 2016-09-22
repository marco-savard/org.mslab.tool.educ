package org.mslab.tool.educ.client.tool.educ.school.viewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mslab.tool.educ.client.core.ui.panels.GridPanel;
import org.mslab.tool.educ.client.tool.educ.EducTheme;
import org.mslab.tool.educ.client.tool.educ.OrganizationCategories;
import org.mslab.tool.educ.client.tool.educ.school.explorer.AbstractFilterCategory.AbstractCategorizer;
import org.mslab.tool.educ.client.tool.educ.school.explorer.EntityViewable;
import org.mslab.tool.educ.shared.text.StringExt;
import org.mslab.tool.educ.shared.types.educ.Organization;
import org.mslab.tool.educ.shared.types.educ.SchoolBoard;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Properties;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.AreaChart;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;

public class OrganizationGraphicViewer extends AbstractViewer<Organization> {
	//Google Chart
	private enum ChartType {AREA_CHART, BAR_CHART, PIE_CHART, SCATTER_CHART};

	private OrganizationGraphicViewerContent _content; 


	private List<OrganizationCategories.AbstractCategorizer> _categorizers = 
			new ArrayList<OrganizationCategories.AbstractCategorizer>();

	public OrganizationGraphicViewer() {
		//add UI
		_content = new OrganizationGraphicViewerContent();
		add(_content);

		addCategorizer(new OrganizationCategories.RegionCategorizer());
		addCategorizer(new OrganizationCategories.CityCategorizer());
		addCategorizer(new OrganizationCategories.OrganizationTypeCategorizer());
		addCategorizer(new OrganizationCategories.OrdreAppartenanceCategorizer());
		addCategorizer(new OrganizationCategories.LanguageCategorizer());
		addCategorizer(new OrganizationCategories.EnvironmentCategorizer());
		addCategorizer(new OrganizationCategories.SchoolBoardCategorizer());
	
	}

	private void addCategorizer(OrganizationCategories.AbstractCategorizer categorizer) {
		_categorizers.add(categorizer);
		_content.addChart(categorizer); 
	}
	
	@Override
	public void update(String listName, List<Organization> organizations) {
		_content.update(listName, organizations);
	}

	//
	// inner classes
	//
	private static class OrganizationGraphicViewerContent extends GridPanel implements EntityViewable<Organization>, ResizeHandler {
		private List<OrganizationChart> _chartPanels = new ArrayList<OrganizationChart>(); 
		Timer _resizeTimer = new ResizeTimer();

		OrganizationGraphicViewerContent() {
			_grid.setWidth("100%");
			Window.addResizeHandler(this);
		}
		
		@Override
		public void update(String listName, List<Organization> organizations) {
			List<SchoolBoard> schoolBoards = SchoolBoard.getList(organizations); 
			
			for (OrganizationChart chart : _chartPanels) {
				chart.showOrganizations(organizations, schoolBoards);
			}	
		}

		public void addChart(AbstractCategorizer categorizer) {
			OrganizationChart chartPanel = new OrganizationChart(categorizer);
			_chartPanels.add(chartPanel);
			refresh(); 
			
			//int row = 1 + _chartPanels.size();
			//_grid.setWidget(row, 0, chartPanel);			
		}

		@Override
		public void onResize(ResizeEvent event) {
			_resizeTimer.cancel();
			_resizeTimer.schedule(500);
		}
		
		private void refresh() {
			int ww = Window.getClientWidth();
			int contentWidth = (int)(ww * 2.0 / 3.0) - 48;
			int nbCols = contentWidth / OrganizationChart.CHART_WIDTH; 
			nbCols = (nbCols < 1) ? 1 : nbCols;
			
			_grid.clear();
			int idx = 0;
			for (OrganizationChart chart : _chartPanels) {
				int row = (idx / nbCols); 
				int col = (idx % nbCols); 
				_grid.setWidget(row, col, chart);	
				_grid.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				idx++;
			}
		}
		
		private class ResizeTimer extends Timer {
			@Override
			public void run() {
				refresh(); 
			}
		}
	}
		
	
	private static class OrganizationChart extends SimplePanel {
		private static final int CHART_WIDTH = 440;
		private AbstractCategorizer _categorizer; 
		private List<String> _categories = new ArrayList<String>();
		private Map<String, Integer> _counts = new HashMap<String, Integer>(); 
		
		private SimplePanel _chartHolder;
		private ChartType _chartType = ChartType.PIE_CHART;
		private boolean  _areaChartLoaded = false, _barChartLoaded = false, _pieChartLoaded = false;
		private Runnable _areaChartDisplayCommand, _barChartDisplayCommand, _pieChartDisplayCommand;
		private CoreChart _areaChart,_barChart, _pieChart;

		OrganizationChart(AbstractCategorizer categorizer) {
			_categorizer = categorizer;

			//init
			_areaChartDisplayCommand = new AreaChartDisplayCommand();
			_barChartDisplayCommand = new BarChartDisplayCommand();
			_pieChartDisplayCommand = new PieChartDisplayCommand(); 
			VisualizationUtils.loadVisualizationApi(_areaChartDisplayCommand, AreaChart.PACKAGE);
			VisualizationUtils.loadVisualizationApi(_barChartDisplayCommand, BarChart.PACKAGE);
			VisualizationUtils.loadVisualizationApi(_pieChartDisplayCommand, PieChart.PACKAGE);

			_chartHolder = new SimplePanel();
			setWidget(_chartHolder);
		}

		public void showOrganizations(List<Organization> organizations, List<SchoolBoard> schoolBoards) {
			Map<String, Integer> counts = new HashMap<String, Integer>(); 
			List<String> categories = new ArrayList<String>();

			for (Organization organization : organizations) {
				String category = _categorizer.categorize(organization, schoolBoards); 
				int count; 

				if (! categories.contains(category)) {
					categories.add(category);
					count = 1; 
				} else {
					count = counts.get(category) + 1;
				}

				counts.put(category, count);
			} //end for
			
			CategoryComparator comparator = new CategoryComparator(counts);
			Collections.sort(categories, comparator);

			showCategory(categories, counts); 
		}

		private void showCategory(List<String> categories, Map<String, Integer> counts) {
			_categories = categories;
			_counts = counts;
			update();
		}

		private boolean isAllLoaded() {
			boolean allLoaded = _areaChartLoaded && _barChartLoaded && _pieChartLoaded;
			return allLoaded;
		}

		public void refresh() {    		
			if ((_chartType == ChartType.AREA_CHART) && _areaChartLoaded) {
				_chartHolder.setWidget(_areaChart);
			}

			if ((_chartType == ChartType.BAR_CHART) && _barChartLoaded) {
				_chartHolder.setWidget(_barChart);
			}

			if ((_chartType == ChartType.PIE_CHART) && _pieChartLoaded) {
				_chartHolder.setWidget(_pieChart);
			}
		}

		private void update() {
			if ((_chartType == ChartType.AREA_CHART) && _areaChartLoaded) {
				_areaChartDisplayCommand.run(); 
			}

			if ((_chartType == ChartType.BAR_CHART) && _barChartLoaded) {
				_barChartDisplayCommand.run(); 
			}

			if ((_chartType == ChartType.PIE_CHART) && _pieChartLoaded) {
				_pieChartDisplayCommand.run(); 
			}
		}
		
		private CoreChart createChart(ChartType type) {
			// create a datatable
			AbstractDataTable data = createTable();
			Options options = createOptions();
			CoreChart chart = null; 

			if (type == ChartType.AREA_CHART) {
				chart = new AreaChart(data, options);
			} else if (type == ChartType.BAR_CHART) {
				chart = new BarChart(data, options);
			} else if (type == ChartType.PIE_CHART) {
				chart = new PieChart(data, options);
			}

			return chart;
		}

		private AbstractDataTable createTable() {
			DataTable data = DataTable.create();
			data.addColumn(ColumnType.STRING, _categorizer.getName());
			data.addColumn(ColumnType.NUMBER, "Nb Ecoles");
			
			List<Row> rows = new ArrayList<Row>(); 
			int nbCategories = _categories.size();
			for (int i=0; i<nbCategories; i++) {
				String category = _categories.get(i); 
				if (! StringExt.isNullOrWhitespace(category)) {
					int count = _counts.get(category);
					Row row = new Row(category, count); 
					rows.add(row); 
				}
			}
			
			data.addRows(rows.size());
			for (Row row : rows) {
				int i = rows.indexOf(row); 
				data.setValue(i, 0, row._category);
				data.setValue(i, 1, row._count);
			}
			
			return data;
		}

		private Options createOptions() {
			Options options = Options.create();
			options.setWidth(CHART_WIDTH);
			options.setHeight(150);
			
			EducTheme theme = (EducTheme)EducTheme.getTheme();
			options.setFontName(theme.getFontFamily());
			options.setBackgroundColor("yellow");
			//options.setTitle(_categorizer.getName());
			
			 com.google.gwt.ajaxloader.client.Properties animation = Properties.create();
		     animation.set("duration", 1000.0);
		     animation.set("easing", "out");
		     options.set("animation", animation);
		        
			return options;
		}

		private class AreaChartDisplayCommand implements Runnable {
			@Override
			public void run() {
				_areaChartLoaded = true;
				_areaChart = createChart(ChartType.AREA_CHART);  

				if (isAllLoaded()) {
					refresh();
				}
			}
		}

		private class BarChartDisplayCommand implements Runnable {
			@Override
			public void run() {
				_barChartLoaded = true;
				_barChart = createChart(ChartType.BAR_CHART);  

				if (isAllLoaded()) {
					refresh();
				}
			}
		}

		private class PieChartDisplayCommand implements Runnable {
			@Override
			public void run() {
				_pieChartLoaded = true;
				_pieChart = createChart(ChartType.PIE_CHART);  

				if (isAllLoaded()) {
					refresh();
				}
			}
		}
	}
	
	private static class Row {
		private String _category;
		private int _count; 

		public Row(String category, int count) {
			_category = category; 
			_count = count;
		}
		
	}



	private static class CategoryComparator implements Comparator<String> {
		private Map<String, Integer> _counts;

		public CategoryComparator(Map<String, Integer> counts) {
			_counts = counts;
		}

		@Override
		public int compare(String c1, String c2) {
			int n1 = _counts.get(c1);
			int n2 = _counts.get(c2);
			return n2 - n1;
		}

	}




}
