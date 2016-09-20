package org.mslab.commons.client.tool.educ.school.viewer;

import java.util.ArrayList;
import java.util.List;

import org.mslab.commons.client.core.ui.panels.GridPanel;
import org.mslab.commons.client.tool.educ.EducShell;
import org.mslab.commons.client.tool.educ.school.explorer.EntityViewable;
import org.mslab.commons.client.tool.services.ServiceStore;
import org.mslab.commons.shared.types.Address;
import org.mslab.commons.shared.types.GeoLocation;
import org.mslab.commons.shared.types.PostalCode;
import org.mslab.commons.shared.types.educ.Organization;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.MapVisualization;
import com.google.gwt.visualization.client.visualizations.MapVisualization.Options;

public class OrganizationMapViewer extends AbstractViewer<Organization> implements ResizeHandler {
	private OrganizationMapViewerContent _content;
	
	public OrganizationMapViewer() {
		_content = new OrganizationMapViewerContent(); 
		add(_content);
		
		//Window.addResizeHandler(this);
		//refresh();
	}
	
	@Override
	public void onResize(ResizeEvent event) {
		//refresh();
	}
	
	private void refresh() {
		//int height = Window.getClientHeight() - EducShell.HEADER_HEIGHT_PX; 
		//int width = ((2 * Window.getClientWidth()) / 3) - 36; 
		
		//setHeight(height + "px");
		//setWidth(width + "px");
		_content.refresh();
	}
	
	@Override
	public void update(String listName, List<Organization> organizations) {
		_content.showOrganizations(organizations);
	}
	
	//
	// inner classes
	//
	private static class OrganizationMapViewerContent extends GridPanel {
		private MapPanel _mapPanel;
		
		OrganizationMapViewerContent() {
			int row = 0;
			
			HTML html = new HTML("Carte"); 
			html.getElement().getStyle().setFontSize(150, Unit.PCT);
			_grid.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_CENTER);
			_grid.setWidget(row, 0, html);
			row++;
			
			_mapPanel = new MapPanel();
			_grid.setWidget(row, 0, _mapPanel);
		}
		
		public void refresh() {
			_mapPanel.refresh();
		}

		private void showOrganizations(List<Organization> organizations) {	
			_mapPanel.display(organizations);
		}
	}
	
	private static class MapPanel extends SimplePanel {
		private static final int MAP_HEIGHT = 150; //px
		private MapVisualization _mapVisualization = null;
		
		MapPanel() {
			Runnable displayCommand = new MapDisplayCommand(this); 
			VisualizationUtils.loadVisualizationApi(displayCommand, MapVisualization.PACKAGE);
		}

		public void init() {
			DataTable data = buildData(); 
			Options options = buildOptions(); 
			
			_mapVisualization = new MapVisualization(data, options, "600px", "400px");
			setWidget(_mapVisualization); 
			refresh();
		}
		
		//called on resize()
		public void refresh() {
			if (_mapVisualization != null) {
				int height = Window.getClientHeight() - EducShell.HEADER_HEIGHT_PX- 48; 
				int width = ((2 * Window.getClientWidth()) / 3) - 60; 

				_mapVisualization.setHeight(height + "px");
				_mapVisualization.setWidth(width + "px");
			}
		}
		
		public void display(String code, double lat, double lon) {
			DataTable data = DataTable.create();
			
			data.addColumn(ColumnType.NUMBER, "Lat");
		    data.addColumn(ColumnType.NUMBER, "Lon");
		    data.addColumn(ColumnType.STRING, "Name");
		    data.addRows(1);
		    
		    data.setValue(0, 0, lat);
		    data.setValue(0, 1, lon);
		    data.setValue(0, 2, code);
			
		    Options options = Options.create();
		    options.setZoomLevel(14);
		    options.setEnableScrollWheel(true);
		    options.setMapType(MapVisualization.Type.NORMAL);
		    options.setShowTip(true);
		    
			_mapVisualization.draw(data, options);
			refresh();
		}
		
		public void display(final List<Organization> organizations) {
			List<String> postalCodes = getPostalCodes(organizations); 		
			//final Organization org = organizations.get(0); 
			//String postalCode = org.getAddress().getPostalCode().toString(); 
			
			AsyncCallback<List<GeoLocation>> callback = new AsyncCallback<List<GeoLocation>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onSuccess(List<GeoLocation> locations) {
					for (Organization org : organizations) {
						int idx = organizations.indexOf(org); 
						GeoLocation result = (idx >= locations.size()) ? null : locations.get(idx); 
						if (result != null) {
							Address address = org.getAddress(); 
							GeoLocation location = (address == null) ? null : address.getLocation();
							
							if (location != null) {
								location.setLatitude(result.getLatitude()); 
								location.setLongitude(result.getLongitude()); 
							}
						}

					}
					
					refreshDisplay(organizations);
					/*
					List<Organization> oneOrg = new ArrayList<Organization>(); 
					oneOrg.add(organizations.get(0)); 
					refreshDisplay(oneOrg);
					*/ 
				}
			}; 
			
			ServiceStore.getService().findPostalCodes(postalCodes, callback);   
		}
		
		private List<String> getPostalCodes(List<Organization> organizations) {
			List<String> postalCodes = new ArrayList<String>(); 
			for (Organization org : organizations) {
				Address address = org.getAddress(); 
				PostalCode postalCode = (address == null) ? null : address.getPostalCode(); 
				String code = (postalCode == null) ? "" : postalCode.toString(); 
				postalCodes.add(code);
			}
			return postalCodes;
		}

		private void refreshDisplay(List<Organization> organizations) {
			DataTable data = DataTable.create();
			
			data.addColumn(ColumnType.NUMBER, "Lat");
		    data.addColumn(ColumnType.NUMBER, "Lon");
		    data.addColumn(ColumnType.STRING, "Name");
		    
		    List<Row> rows = new ArrayList<Row>(); 
		    for (Organization org : organizations) {
		    	Address address = org.getAddress();
		    	if (address != null) {
		    		GeoLocation geo = address.getLocation(); 
			    	double lat = geo.getLatitude(); 
			    	double lon = geo.getLongitude();
			    	String code = org.getName();
			    	
			    	if (lat >= 40) {
			    		Row row = new Row(lat, lon, code);
			    		rows.add(row); 
			    	}
			    }
		    }
		    
		    data.addRows(rows.size());
		    int idx = 0;
		    for (Row row : rows) {
		    	 data.setValue(idx, 0, row._lat);
				 data.setValue(idx, 1, row._lon);
				 data.setValue(idx, 2, row._code);
				 idx++;
		    }
		    
		 
		    Options options = Options.create();
		    int zoomLevel = computeZoomLevel(organizations); 
		    options.setZoomLevel(zoomLevel);
		    options.setEnableScrollWheel(true);
		    options.setMapType(MapVisualization.Type.NORMAL);
		    options.setShowTip(true);
		    
			_mapVisualization.draw(data, options);
			refresh();
		}
		
		private int computeZoomLevel(List<Organization> organizations) {
			double lowestLat = 90, higestLat = -90;
			double lowestLon = 180, higestLon = -180;
			 
			for (Organization organization : organizations) {
				Address address = organization.getAddress(); 
				GeoLocation location = (address == null) ? null : address.getLocation();
				if (location != null) {
					double lat = location.getLatitude();
					double lon = location.getLongitude();
					boolean valid = lat > 45; 
					
					if (valid) {
						lowestLat = Math.min(lat, lowestLat);
						higestLat = Math.max(lat, higestLat);
						lowestLon = Math.min(lon, lowestLon);
						higestLon = Math.max(lon, higestLon);
					}
				}
			}
			
			double dLat = higestLat - lowestLat; 
			double dLon = lowestLon - higestLon; 
			double delta = Math.max(dLat, dLon * 1.2); 
			
			int zoomLevel = 10;
			
			if (delta > 3.0) {
				zoomLevel = 7;
			} else if (delta > 2.0) {
				zoomLevel = 8;
			} else if (delta > 1.0) {
				zoomLevel = 9;
			}
			
			if (delta < 0.5) {
				zoomLevel = 11;
			}
			
			return zoomLevel;
		}

		private DataTable buildData() {
			DataTable data = DataTable.create();
			
			data.addColumn(ColumnType.NUMBER, "Lat");
		    data.addColumn(ColumnType.NUMBER, "Lon");
		    data.addColumn(ColumnType.STRING, "Name");
		    data.addRows(1);
		    
		    data.setValue(0, 0, 46);
		    data.setValue(0, 1, -73);
		    data.setValue(0, 2, "Your City's Coordinates");
			
			return data;
		}
		
		private Options buildOptions() {
			Options options = Options.create();
			options.setZoomLevel(10);
		    options.setEnableScrollWheel(true);
		    options.setMapType(MapVisualization.Type.NORMAL);
		    options.setShowTip(true);
			return options;
		}
	}
	
	public static class Row {
		private double _lat, _lon;
		private String _code; 
		
		public Row(double lat, double lon, String code) {
			_lat = lat; 
			_lon = lon;
			_code = code;
		}
		
	}
	
	private static class MapDisplayCommand implements Runnable {
		private MapPanel _mapPanel;
		
		MapDisplayCommand(MapPanel mapPanel) {
			_mapPanel = mapPanel;
		}
		
		@Override
		public void run() {
			_mapPanel.init();
		}
	} //end MapDisplayCommand



}
