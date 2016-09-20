package org.mslab.commons.client.tool.educ.school.viewer;

import java.util.ArrayList;
import java.util.List;

import org.mslab.commons.client.core.ui.IconLabel;
import org.mslab.commons.client.core.ui.NumberTextBox;
import org.mslab.commons.client.core.ui.SelectionButton;
import org.mslab.commons.client.core.ui.SelectionButtonGroup;
import org.mslab.commons.client.core.ui.icons.InfoIcon;
import org.mslab.commons.client.core.ui.panels.GridPanel;
import org.mslab.commons.client.core.ui.panels.ShowSourcePanel;
import org.mslab.commons.client.core.ui.theme.AbstractTheme;
import org.mslab.commons.client.tool.bundles.SourceBundle;
import org.mslab.commons.shared.types.Color;
import org.mslab.commons.shared.types.educ.Organization;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.AreaChart;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;

public class OrganizationGraphicViewer2 extends GridPanel implements KeyUpHandler {
	public static final String ID = "googleChart";
	private static final int NB_CITIES = 5;
	public enum ChartType {AREA_CHART, BAR_CHART, PIE_CHART, SCATTER_CHART};
	private ChartType _chartType = ChartType.AREA_CHART;
	
	private List<TextBox> _cities = new ArrayList<TextBox>(); 
	private List<NumberTextBox> _populations = new ArrayList<NumberTextBox>();
	
	//charts
	private IconLabel _generatingChartLbl;
	private Runnable _areaChartDisplayCommand, _barChartDisplayCommand, _pieChartDisplayCommand;
	private boolean  _areaChartLoaded, _barChartLoaded = false, _pieChartLoaded = false;
	private CoreChart _areaChart,_barChart, _pieChart;
	private SimplePanel _chartHolder;

	public OrganizationGraphicViewer2() {
		setTitle("Google Charts");
		build();
	}
	
	private void build() {
		int row = 0;
		
		//header
		SimplePanel leftMargin = new SimplePanel() ;
		_grid.setWidget(row, 0, leftMargin);
		_grid.getFlexCellFormatter().setWidth(row, 0, "40%"); 
		
		Label cityLbl = new Label("City");
		cityLbl.setWordWrap(false); 
		cityLbl.getElement().getStyle().setFontSize(150, Unit.PCT);
		_grid.setWidget(row, 1, cityLbl);
		_grid.getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT); 
		
		Label popLbl = new Label("Population (in K)");
		popLbl.setWordWrap(false); 
		popLbl.getElement().getStyle().setFontSize(150, Unit.PCT);
		_grid.setWidget(row, 2, popLbl);
		_grid.getFlexCellFormatter().setHorizontalAlignment(row, 2, HasHorizontalAlignment.ALIGN_LEFT); 
		
		SimplePanel rightMargin = new SimplePanel() ;
		_grid.setWidget(row, 3, rightMargin);
		_grid.getFlexCellFormatter().setWidth(row, 3, "40%"); 
		row++;
		
		//city
		for (int i=0; i<NB_CITIES; i++) {
			TextBox city = new TextBox(); 
			_cities.add(city); 
			_grid.setWidget(row, 1, city);
			city.addKeyUpHandler(this);
			
			NumberTextBox population = new NumberTextBox(int.class); 
			_populations.add(population); 
			_grid.setWidget(row, 2, population);
			population.addKeyUpHandler(this);
			row++;
		}
		
		SimplePanel spacer = new SimplePanel();
		spacer.setHeight("24px"); 
		_grid.setWidget(row, 0, spacer);
		row++;
		
		Widget icon = new InfoIcon();
		_generatingChartLbl = new IconLabel(icon, "Generating chart.."); 
		_grid.getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_CENTER); 
		_grid.getFlexCellFormatter().setColSpan(row, 1, 2);
		row++;
		
		//chart display
		_chartHolder = new SimplePanel(); 
		_grid.setWidget(row, 1, _chartHolder);
		_grid.getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_CENTER); 
		_grid.getFlexCellFormatter().setColSpan(row, 1, 2);
		row++;
		
		//chart type 
		Color color = AbstractTheme.getTheme().getPrimaryFgColor();
		GridPanel _chartTypeChooser = new ChartTypeSelector(color); 
		_grid.setWidget(row, 1, _chartTypeChooser);
		_grid.getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_CENTER); 
		_grid.getFlexCellFormatter().setColSpan(row, 1, 2);
		row++;
		
		//show source
		ShowSourcePanel srcPanel = new ShowSourcePanel();
		srcPanel.setWidth("100%");
		srcPanel.addTab(OrganizationGraphicViewer2.class, SourceBundle.INSTANCE.GoogleChartPanel().getText()); 
		srcPanel.getElement().getStyle().setMarginTop(24, Unit.PX);
		_grid.setWidget(row, 0, srcPanel); 
		_grid.getFlexCellFormatter().setColSpan(row, 0, 4);
		_grid.getFlexCellFormatter().setWidth(row, 0, "100%");
		row++;
		
		//init
		_areaChartDisplayCommand = new AreaChartDisplayCommand();
		_barChartDisplayCommand = new BarChartDisplayCommand();
		_pieChartDisplayCommand = new PieChartDisplayCommand(); 
		
		VisualizationUtils.loadVisualizationApi(_areaChartDisplayCommand, AreaChart.PACKAGE);
		VisualizationUtils.loadVisualizationApi(_barChartDisplayCommand, BarChart.PACKAGE);
		VisualizationUtils.loadVisualizationApi(_pieChartDisplayCommand, PieChart.PACKAGE);

		initData();
	}
	
	private void initData() {
		String chartData = new GoogleChartData().getData(); 
		JSONValue value = JSONParser.parseStrict(chartData); 
		JSONObject cities = value.isObject(); 
		JSONArray cityArray = (JSONArray)cities.get(GoogleChartData.CITIES);
		int nb = cityArray.size(); 
		
		for (int i=0; i<nb; i++) {
			value = cityArray.get(i); 
			JSONObject city = value.isObject(); 
			
			value = city.get(GoogleChartData.CITY); 
			JSONString name = (JSONString)value; 
			
			value = city.get(GoogleChartData.POPULATION); 
			JSONNumber pop = (JSONNumber)value; 
			
			_cities.get(i).setText(name.stringValue()); 
			_populations.get(i).setValue((int)pop.doubleValue()); 
		}
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
	
	private class ChartTypeSelector extends SelectionButtonGroup {
		private ChartType[] types = new ChartType[] {ChartType.AREA_CHART, ChartType.BAR_CHART, ChartType.PIE_CHART}; 
		
		ChartTypeSelector(Color selectionColor) {
			super(selectionColor);
			addItem(new SelectionButton(this, "<span class=\"fa fa-area-chart fa-2x\"></span>"));
			addItem(new SelectionButton(this, "<span class=\"fa fa-bar-chart fa-2x\"></span>"));
			addItem(new SelectionButton(this, "<span class=\"fa fa-pie-chart fa-2x\"></span>"));
		}
		
		@Override
		public void onSelectionChanged() {
			int idx = getSelectedIndex();
			_chartType = types[idx];
			update();
		}
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
	
	private boolean isAllLoaded() {
		boolean allLoaded = _areaChartLoaded && _barChartLoaded && _pieChartLoaded;
		return allLoaded;
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
		    data.addColumn(ColumnType.STRING, "City");
		    data.addColumn(ColumnType.NUMBER, "Population");
		    
		    data.addRows(NB_CITIES);
		    for (int i=0; i<NB_CITIES; i++) {
		    	TextBox cityBox = _cities.get(i); 
		    	String city = cityBox.getText();
		    	data.setValue(i, 0, city);
		    	
		    	NumberTextBox popBox = _populations.get(i); 
		    	Number pop = popBox.getNumberValue();
		    	int value = pop.intValue();
			    data.setValue(i, 1, value);
		    }
		    
		    return data;
	  }
	 
	  private Options createOptions() {
		    Options options = Options.create();
		    options.setWidth(400);
		    options.setHeight(240);
		    options.setTitle("City Population");
		    return options;
	 }

	@Override
	public void onKeyUp(KeyUpEvent event) {
		update();
		refresh();
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
	
	public static class GoogleChartData {
		//Json fields
		public static final String CITIES = "cities";
		public static final String CITY = "city";
		public static final String POPULATION = "population";
		
		//testing data
		private static final String[] CITY_NAMES = new String[] {"Montreal", "Quebec City", "Laval", "Gatineau", "Sherbrooke"}; 
		private static final int[] CITY_POPULATIONS = new int[] {1600, 500, 400, 250, 150}; 

		//simulate server call
		public String getData() {
			//simulate server processing
			JSONObject json = buildCities();
			String data = json.toString(); 
			return data;
		}
		
		private JSONObject buildCities() {
			JSONArray cityArray = new JSONArray();
			
			for (int i=0; i<CITY_NAMES.length; i++) {
				JSONObject city = new JSONObject(); 
				
				JSONValue name = new JSONString(CITY_NAMES[i]);
				city.put(CITY, name); 
				
				JSONValue pop = new JSONNumber(CITY_POPULATIONS[i]);
				city.put(POPULATION, pop); 
				
				cityArray.set(i, city); 
			}
			
			JSONObject cities = new JSONObject(); 
			cities.put(CITIES, cityArray); 
			
			return cities;
		}
	}

	public void showOrganizations(List<Organization> organizations) {
		// TODO Auto-generated method stub
		
	}
}
