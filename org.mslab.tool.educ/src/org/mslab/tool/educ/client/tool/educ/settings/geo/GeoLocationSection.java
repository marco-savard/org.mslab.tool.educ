package org.mslab.commons.client.tool.educ.settings.geo;

import java.util.ArrayList;
import java.util.List;

import org.mslab.commons.client.core.system.IpInfo;
import org.mslab.commons.client.core.ui.PostalCodePicker;
import org.mslab.commons.client.core.ui.panels.GridPanel;
import org.mslab.commons.client.tool.educ.EducContext;
import org.mslab.commons.client.tool.educ.EducShell;
import org.mslab.commons.client.tool.educ.school.viewer.OrganizationMapViewer.Row;
import org.mslab.commons.client.tool.services.ServiceStore;
import org.mslab.commons.shared.text.GeoFormat;
import org.mslab.commons.shared.text.MessageFormat;
import org.mslab.commons.shared.types.GeoLocation;
import org.mslab.commons.shared.types.PostalCode;
import org.mslab.commons.shared.types.educ.Organization;

import com.google.gwt.core.client.Callback;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.MapVisualization;
import com.google.gwt.visualization.client.visualizations.MapVisualization.Options;

public class GeoLocationSection extends GridPanel {
	private GeoLocationMethodPanel _methodPanel;
	private GeoCurrentLocationPanel _currentLocationPanel;
	private GeoLocation _locationByGeolocation, _locationByPostalCode, _locationByIp;
	private Geolocation _geoLocation; 
	
	public GeoLocationSection () {
		int col = 0;
		
		_methodPanel = new GeoLocationMethodPanel(); 
		_grid.setWidget(0, col, _methodPanel);
		_grid.getFlexCellFormatter().setVerticalAlignment(0, col, HasVerticalAlignment.ALIGN_TOP);
		col++;
		
		_currentLocationPanel = new GeoCurrentLocationPanel(); 
		_currentLocationPanel.getElement().getStyle().setMarginLeft(80, Unit.PX);
		_grid.setWidget(0, col, _currentLocationPanel);
		_grid.getFlexCellFormatter().setVerticalAlignment(0, col, HasVerticalAlignment.ALIGN_TOP);
		col++;
		
		GeoLocation location = EducContext.getInstance().getClientLocation(); 
		if (location == null) {
			AsyncCallback<GeoLocation> callback = new AsyncCallback<GeoLocation>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onSuccess(GeoLocation location) {
					display(location);
				}
			};

			ServiceStore.getService().getClientLocation(callback);
		} else {
			display(location);
		}
	}
	
	private void display(GeoLocation location) {
		_currentLocationPanel.display(location);
		EducContext.getInstance().setCurrentLocation(location);
	}
	
	private class GeoLocationMethodPanel extends GridPanel implements ClickHandler, ChangeHandler { 
		private RadioButton _rb0, _rb1, _rb2;
		private Button _geoLocalizeBtn; 
		private PostalCodePicker _postalCodePicker; 
		
		GeoLocationMethodPanel() {
			int row = 0;
			PostalCode postalCode = new PostalCode("G1A 1A3");
			
			_rb0 = new RadioButton("GeoLocationMethodPanel", "Connaitre ma position en autorisant la géolocalisation");
			_rb0.addClickHandler(this);
			_grid.setWidget(row, 0, _rb0);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
			row++; 
			
			_geoLocalizeBtn = new Button("Géolocalisation"); 
			_geoLocalizeBtn.setHTML("<i class=\"fa fa-map-marker\"></i> Géolocalisation");
			_geoLocalizeBtn.getElement().getStyle().setMarginLeft(24, Unit.PX);
			_geoLocalizeBtn.getElement().getStyle().setMarginBottom(24, Unit.PX);
			_geoLocalizeBtn.addClickHandler(this); 
			_grid.setWidget(row, 0, _geoLocalizeBtn);
			row++; 
			
			_rb1 = new RadioButton("GeoLocationMethodPanel", "Connaitre ma position par mon code postal");
			_rb1.addClickHandler(this);
			_grid.setWidget(row, 0, _rb1);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
			row++; 
			
			_postalCodePicker = new PostalCodePicker("Code Postal"); 
			_postalCodePicker.getElement().getStyle().setMarginLeft(24, Unit.PX);
			_postalCodePicker.getElement().getStyle().setMarginBottom(24, Unit.PX);
			_postalCodePicker.addChangeHandler(this);
			_postalCodePicker.setPostalCode(postalCode);
			_grid.setWidget(row, 0, _postalCodePicker);
			
			SimplePanel filler = new SimplePanel();
			_grid.setWidget(row, 1, filler);
			_grid.getFlexCellFormatter().setWidth(row, 1, "80%");
			row++; 
			
			_rb2 = new RadioButton("GeoLocationMethodPanel", "Connaitre ma position par les informations IP");
			_rb2.addClickHandler(this);
			_grid.setWidget(row, 0, _rb2);
			_grid.getFlexCellFormatter().setColSpan(row, 0, 2);
			row++; 		
			
			filler = new SimplePanel(); 
			_grid.setWidget(row, 0, filler);
			_grid.getFlexCellFormatter().setHeight(row, 0, "80%");
			row++;
			
			init();
		}

		private void init() {
			_geoLocation = Geolocation.getIfSupported();
			if (_geoLocation == null) {
				String ptn = "{0} Connaitre ma position en autorisant la géolocalisation (non-disponible)"; 
				String html = MessageFormat.format(ptn, new Object[] {"<i class=\"fa fa-lock\"></i>"}); 
				_rb0.setHTML(html);
				_rb0.setEnabled(false);
				_geoLocalizeBtn.setEnabled(false);
			}
			
			refresh();
		}

		@Override
		public void onClick(ClickEvent event) {
			Object src = event.getSource(); 
			refresh(); 
			
			if (_rb0.equals(src)) {
				findLocationByGeolocation();
			} else if (_rb1.equals(src)) {
				findLocationByPostalCode();
			} else if (_rb2.equals(src)) {
				findLocationByIP();
			} else if (_geoLocalizeBtn.equals(src)) {
				geoLocalize();
			}
		}

		private void geoLocalize() {
			Callback<Position, PositionError> callback = new Callback<Position, PositionError>() {

				@Override
				public void onFailure(PositionError error) {
					String msg = error.toString();
					String html = "<i class=\"fa fa-exclamation-triangle\"></i> Erreur";
					_geoLocalizeBtn.setHTML(html);
				}

				@Override
				public void onSuccess(Position position) {
					try {
						double lat = position.getCoordinates().getLatitude();
						double lon = position.getCoordinates().getLongitude(); 
						_locationByGeolocation = new GeoLocation(lat, lon);
						display(_locationByGeolocation);
						
						String html = "<i class=\"fa fa-check\"></i> Géolocalisé";
						_geoLocalizeBtn.setHTML(html);
						_geoLocalizeBtn.setEnabled(false); 
					} catch (Exception ex) {
						String html = "<i class=\"fa fa-exclamation-triangle\"></i> Erreur";
						_geoLocalizeBtn.setHTML(html);
					}
				}
			};
			
			_geoLocation.getCurrentPosition(callback);
		}

		private void refresh() {
			_geoLocalizeBtn.setEnabled(_rb0.getValue());
			_postalCodePicker.setEnabled(_rb1.getValue());
		}

		
		private void findLocationByGeolocation() {
			if (_locationByGeolocation == null) {
				_locationByGeolocation = EducContext.getInstance().getClientLocation();
			}
			display(_locationByGeolocation);
		}
		
		private void findLocationByPostalCode() {
			if (_locationByPostalCode == null) {
				PostalCode postalCode = _postalCodePicker.getPostalCode();
				findLocationByPostalCode(postalCode);
			}
			
			display(_locationByPostalCode);
		}

		@Override
		public void onChange(ChangeEvent event) {
			Object src = event.getSource(); 
			
			if (_postalCodePicker.equals(src)) {
				PostalCode postalCode = _postalCodePicker.getPostalCode();
				findLocationByPostalCode(postalCode);
			}
		}
		
		private void findLocationByIP() {
			AsyncCallback<GeoLocation> callback = new AsyncCallback<GeoLocation>() {

				@Override
				public void onFailure(Throwable caught) {
				}

				@Override
				public void onSuccess(GeoLocation location) {
					display(location);
				}	
			};
			
			ServiceStore.getService().getClientLocation(callback);
		}
		
		private void findLocationByIPOld() {
			//get Ip info
			IpInfo.Callback callback = new IpInfo.Callback() {
				@Override
				public void onSuccess(IpInfo info) {
					double lat = info.getLatitude();
					double lon = info.getLongitude();
					String postalCode = info.getPostalCode();
					String city = info.getCity();
					_locationByIp = new GeoLocation(lat, lon, new PostalCode(postalCode), city);
					display(_locationByIp);
				}
			};
			IpInfo.request(callback); 
		}
		
		private void findLocationByPostalCode(PostalCode postalCode) {
			AsyncCallback<GeoLocation> callback = new AsyncCallback<GeoLocation>() {

				@Override
				public void onFailure(Throwable caught) {
				}

				@Override
				public void onSuccess(GeoLocation location) {
					_locationByPostalCode = location;
					display(_locationByPostalCode);
				}

			};
			ServiceStore.getService().findPostalCode(postalCode.toString(), callback);
			
		}


	}
	
	private class GeoCurrentLocationPanel extends GridPanel {
		private HTML _cityLbl, _latitudeLbl, _longitudeLbl; 
		private MapPanel _mapHolder;
		
		GeoCurrentLocationPanel() {
			int row = 0;
			HTML title = new HTML("Votre position actuelle est:"); 
			_grid.setWidget(row, 0, title);
			row++; 
			
			_cityLbl = new HTML(); 
			_grid.setWidget(row, 0, _cityLbl);
			row++; 
			
			_latitudeLbl = new HTML(); 
			_grid.setWidget(row, 0, _latitudeLbl);
			row++; 
			
			_longitudeLbl = new HTML(); 
			_grid.setWidget(row, 0, _longitudeLbl);
			row++; 
			
			_mapHolder = new MapPanel();
			_grid.setWidget(row, 0, _mapHolder);
			row++; 
			
			SimplePanel filler = new SimplePanel(); 
			_grid.setWidget(row, 0, filler);
			_grid.getFlexCellFormatter().setHeight(row, 0, "90%");
			row++; 
		}

		public void display(GeoLocation location) {
			if (location != null) {
				EducContext.getInstance().setCurrentLocation(location);
				String city = location.getCity().getName();
				String html = "Ville : " + city;
				_cityLbl.setHTML(html);
				
				GeoFormat format = GeoFormat.getInstance(LocaleInfo.getCurrentLocale()); 
				double lat = location.getLatitude();
				double lon = location.getLongitude();
				String code = location.getCity().toString();
				
				String la = format.formatLatitude(lat, true); 
				String lo = format.formatLongitude(lon, true);
				String coor = MessageFormat.format("{0} {1}", new Object[] {la, lo});
				
				html = "Latitude : " + la;
				_latitudeLbl.setHTML(html);
				
				html = "Longitude : " + lo;
				_longitudeLbl.setHTML(html);
	
				_mapHolder.display(location);
				
		
			}
		}
	}
	
	private static class MapPanel extends SimplePanel {
		private boolean _loaded = false;
		
		MapPanel() {
			Runnable displayCommand = new MapDisplayCommand(this); 
			VisualizationUtils.loadVisualizationApi(displayCommand, MapVisualization.PACKAGE);
		}

		public void display(GeoLocation location) {
			if (_loaded && (location != null)) {
				DataTable data = buildData(location);
				Options options = buildOptions(); 
				MapVisualization mapVisualization = new MapVisualization(data, options, "300px", "200px");
				setWidget(mapVisualization); 
			}	else {
				setSize("300px", "200px"); 
			}
		}

		public void initOnceLoaded() {
			_loaded = true; 
			GeoLocation location = EducContext.getInstance().getClientLocation();
			display(location); 
		}
		
		private DataTable buildData(GeoLocation location) {
			DataTable data = DataTable.create();
			
			data.addColumn(ColumnType.NUMBER, "Lat");
		    data.addColumn(ColumnType.NUMBER, "Lon");
		    data.addColumn(ColumnType.STRING, "Name");
		    data.addRows(1);
		    
		    data.setValue(0, 0, location.getLatitude());
		    data.setValue(0, 1, location.getLongitude());
		    data.setValue(0, 2, location.getCity().getName());
			
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
		
		/*
		//called on resize()
		public void refresh() {
			if (_mapVisualization != null) {
				int height = Window.getClientHeight() - EducShell.HEADER_HEIGHT_PX- 48; 
				int width = ((2 * Window.getClientWidth()) / 3) - 60; 

				_mapVisualization.setHeight(height + "px");
				_mapVisualization.setWidth(width + "px");
			}
		}
		*/
	}
	
	private static class MapDisplayCommand implements Runnable {
		private MapPanel _panel; 
		
		public MapDisplayCommand(MapPanel panel) {
			_panel = panel; 
		}

		@Override
		public void run() {
			_panel.initOnceLoaded();
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
}
