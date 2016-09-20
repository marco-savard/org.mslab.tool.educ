package org.mslab.commons.client.tool.educ.school.viewer.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mslab.commons.client.core.ui.SelectionButton;
import org.mslab.commons.client.core.ui.SelectionButtonGroup;
import org.mslab.commons.client.core.ui.SelectionButtonGroup.SelectionChangeHandler;
import org.mslab.commons.client.core.ui.panels.GridPanel;
import org.mslab.commons.client.tool.educ.EducTheme;
import org.mslab.commons.client.tool.educ.OrganizationCategories;
import org.mslab.commons.client.tool.educ.school.viewer.content.OrganizationComparator.Criteria;
import org.mslab.commons.shared.text.StringExt;
import org.mslab.commons.shared.types.Color;
import org.mslab.commons.shared.types.educ.Organization;
import org.mslab.commons.shared.types.educ.SchoolBoard;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Properties;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.corechart.AreaChart;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;

public class OrganizationGraphicViewer4 extends AbstractContentViewer implements ResizeHandler {
	private static final int CHART_WIDTH = 300;
	
	private List<Organization> _organizations;
	private List<SchoolBoard> _schoolBoards; 
	private Timer _resizeTimer = new ResizeTimer();
	
	private List<OrganizationCategories.AbstractCategorizer> _categorizers = 
			new ArrayList<OrganizationCategories.AbstractCategorizer>();
	private List<OrganizationChartPanel> _chartPanels = new ArrayList<OrganizationChartPanel>(); 
	private boolean _chartLoaded = false;
	private HTML _feedback = new HTML();
	
	public OrganizationGraphicViewer4() {
		_grid.setWidth("100%");
		_grid.setWidget(0, 0, _feedback);
		
		addCategorizer(new OrganizationCategories.RegionCategorizer());
		addCategorizer(new OrganizationCategories.CityCategorizer());
		addCategorizer(new OrganizationCategories.OrganizationTypeCategorizer());
		addCategorizer(new OrganizationCategories.OrdreAppartenanceCategorizer());
		addCategorizer(new OrganizationCategories.LanguageCategorizer());
		addCategorizer(new OrganizationCategories.EnvironmentCategorizer());
		addCategorizer(new OrganizationCategories.SchoolBoardCategorizer());
		
		buildChartPanels();
		Window.addResizeHandler(this);
		refresh();
		
		ChartLoader loader = new ChartLoader(); 
		Runnable onLoadAction = new OnLoadAction();
		loader.load(onLoadAction); 
	}
	
	private void buildChartPanels() {
		for (OrganizationCategories.AbstractCategorizer categorizer : _categorizers) {
			OrganizationChartPanel panel = new OrganizationChartPanel(categorizer);
			_chartPanels.add(panel);
		}
	}
	
	private void buildCharts() {
		for (OrganizationChartPanel panel : _chartPanels) {
			panel.buildChart();
		}
	}

	@Override
	public void onResize() {
		refresh();
	}
	
	@Override
	public void onResize(ResizeEvent event) {
		refresh();
	}
	
	private void refresh() {
		_resizeTimer.cancel();
		_resizeTimer.schedule(500);
	}
	
	private void refreshDelayed() {
		int w = getContentWidth();
		int nbCols = (int)Math.floor(w / CHART_WIDTH); 
		nbCols = (nbCols < 1) ? 1 : nbCols;
		
		_grid.clear();
		int idx = 0;
		for (OrganizationChartPanel chartPanel : _chartPanels) {
			int row = (idx / nbCols); 
			int col = (idx % nbCols); 
			_grid.setWidget(row, col, chartPanel);	
			_grid.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			idx++;
		}
	}

	private int getContentWidth() {
		int w = getOffsetWidth(); 
		
		if (w == 0) {
			w = Window.getClientWidth() * 3 / 4; //approx
		}
		
		return w;
 	}

	private void addCategorizer(OrganizationCategories.AbstractCategorizer categorizer) {
		_categorizers.add(categorizer);
	}

	@Override
	public void showOrganizations(String listName, List<Organization> organizations) {
		_organizations = organizations;
		
		if (! _chartLoaded) {
			System.out.println("Chart Loaded 1");

		} else {
			refreshCharts();
		}
	}
	
	private void refreshCharts() {
		if (_organizations != null) {
			_schoolBoards = SchoolBoard.getList(_organizations); 
			
			for (OrganizationChartPanel chartPanel : _chartPanels) {
				chartPanel.update();
			}
		}
	}
	
	@Override
	public void sort(Criteria criteria, boolean ascendent) {
		// TODO Auto-generated method stub	
	}
	
	//
	// inner classes
	//

	private class OnLoadAction implements Runnable {

		@Override
		public void run() {
			_chartLoaded = true;
			System.out.println("Chart Loaded 2");
			buildCharts();
		}
	}
	
	private class ResizeTimer extends Timer {

		@Override
		public void run() {
			refreshDelayed();
		}
	}
	
	private class OrganizationChartPanel extends GridPanel implements SelectionChangeHandler {
		private SelectionButtonGroup _selectionButtonGroup;
		private CategoryChart _categoryChart;
		
		public OrganizationChartPanel(OrganizationCategories.AbstractCategorizer categorizer) {
			String name = categorizer.getName();
			int row = 0;
			
			EducTheme theme = (EducTheme)EducTheme.getTheme();
			Color color = theme.getPrimaryFgColor();
			_selectionButtonGroup = new ChartTypeSelectionButtonGroup(color); 
			_selectionButtonGroup.addSelectionChangeHandler(this);
			_grid.setWidget(row, 0, _selectionButtonGroup);
			
			HTML html = new EducLabel(name);
			_grid.setWidget(row, 1, html);
			_grid.getFlexCellFormatter().setWidth(row, 1, "95%");
			row++;
			
		    _categoryChart = new CategoryChart(categorizer);
			_grid.setWidget(row, 0, _categoryChart);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
		}

		public void buildChart() {
			_categoryChart.build();
		}

		public void update() {
			_categoryChart.update(_organizations);
		}

		@Override
		public void handleSelectionChange() {
			int idx = _selectionButtonGroup.getSelectedIndex();
			_categoryChart.display(idx);
		}
	}
	
	private class ChartTypeSelectionButtonGroup extends SelectionButtonGroup {
		private SelectionButton _areaChartBtn, _barChartBtn, _pieChartBtn; 
		
		ChartTypeSelectionButtonGroup(Color color) {
			super(color);
			_areaChartBtn = new SelectionButton(this, "<i class=\"fa fa-area-chart fa-lg\"></i>");
			_barChartBtn = new SelectionButton(this, "<i class=\"fa fa-bar-chart fa-lg\"></i>");
			_pieChartBtn = new SelectionButton(this, "<i class=\"fa fa-pie-chart fa-lg\"></i>");

			addItem(_areaChartBtn);
			addItem(_barChartBtn);
			addItem(_pieChartBtn);
		}

		@Override
		public void onSelectionChanged() {
			// TODO Auto-generated method stub
		}
	}
	
	private class CategoryChart extends DeckPanel {
		private OrganizationCategories.AbstractCategorizer _categorizer;
		private ChartContainer _areaChartPanel, _barChartPanel, _pieChartPanel;
		private DataTable _dataTable;
		private Options _optionsArea, _optionsBar, _optionsPie;
		
		public CategoryChart(OrganizationCategories.AbstractCategorizer categorizer) {
			_categorizer = categorizer;
		}
		
		public void display(int idx) {
			showWidget(idx);
			
			//int value = 5 + _dataTable.getValueInt(0, 1);
			//_dataTable.setValue(0, 1, value);
		}

		public void build() {
			createData();
			
			_optionsArea = createOptions();
			_areaChartPanel = new ChartContainer(new AreaChart(_dataTable, _optionsArea));
			
			_optionsBar = createOptions();
			_barChartPanel = new ChartContainer(new BarChart(_dataTable, _optionsBar));
			
			_optionsPie = createOptions();
			_pieChartPanel = new ChartContainer(new PieChart(_dataTable, _optionsPie));
			
			add(_areaChartPanel);
			add(_barChartPanel);
			add(_pieChartPanel);
			showWidget(0);
		}
		
		private Options createOptions() {
			Options options = Options.create();
			options.setWidth(CHART_WIDTH);
			options.setHeight(260);
			
			EducTheme theme = (EducTheme)EducTheme.getTheme();
			options.setFontName(theme.getFontFamily());
			
			 com.google.gwt.ajaxloader.client.Properties animation = Properties.create();
		     animation.set("duration", 800.0);
		     animation.set("easing", "out");
		     options.set("animation", animation);
			
			return options;
		}


		private DataTable createData() {
			_dataTable = DataTable.create();
			_dataTable.addColumn(ColumnType.STRING, _categorizer.getName());
			_dataTable.addColumn(ColumnType.NUMBER, "Nb");
			_dataTable.addRows(10);
			
			for (int i=0; i<10; i++) {
			}
			
			
			return _dataTable;
		}
		
		public void update(List<Organization> organizations) {
			List<String> categories = new ArrayList<String>();
			Map<String, Integer> counts = new HashMap<String, Integer>(); 
			
			for (Organization organization : organizations) {
				String category = _categorizer.categorize(organization, _schoolBoards); 
				int count; 

				if (! categories.contains(category)) {
					categories.add(category);
					count = 1; 
				} else {
					count = counts.get(category) + 1;
				}

				counts.put(category, count);
			}
			
			CategoryComparator comparator = new CategoryComparator(counts);
			Collections.sort(categories, comparator);
			
			int nb = categories.size();
			for (int i=0; i<10; i++) {
				String name = (i < nb) ? categories.get(i) : "";
				int count =  (i < nb) ? counts.get(name) : 0;
				
				_dataTable.setValue(i, 0, name);
				_dataTable.setValue(i, 1, count);
			}
			
			_areaChartPanel.draw(_dataTable, _optionsArea);
			_barChartPanel.draw(_dataTable, _optionsBar);
			_pieChartPanel.draw(_dataTable, _optionsPie);
		}
	}
	
	private static class ChartContainer extends SimplePanel {
		private CoreChart _chart;
		
		ChartContainer(CoreChart chart) {
			_chart = chart;
			setWidget(_chart);
		}

		public void draw(DataTable dataTable, Options options) {
			_chart.draw(dataTable, options);
		}
	}
	
	private static class EducLabel extends HTML {
		public EducLabel(String text) {
			super(text);
			refresh();
		}
		
		private void refresh() {
			EducTheme theme = (EducTheme)EducTheme.getTheme(); 
			Color color = theme.getPrimaryFgColor();
			String family = theme.getFontFamily();
			
			getElement().getStyle().setColor(color.toString());
			getElement().getStyle().setProperty("fontFamily", family);
			getElement().getStyle().setFontSize(140, Unit.PCT);
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
