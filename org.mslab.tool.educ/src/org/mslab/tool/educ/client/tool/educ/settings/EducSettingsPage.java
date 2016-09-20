package org.mslab.commons.client.tool.educ.settings;

import org.mslab.commons.client.core.ui.Refreshable;
import org.mslab.commons.client.core.ui.panels.CommonDisclosurePanel;
import org.mslab.commons.client.core.ui.panels.GridPanel;
import org.mslab.commons.client.tool.educ.settings.geo.GeoLocationSection;
import org.mslab.commons.client.tool.educ.settings.pref.AbstractFormatPanel;
import org.mslab.commons.client.tool.educ.settings.pref.CityAndProvinceFormatPanel;
import org.mslab.commons.client.tool.educ.settings.pref.NameFormatPanel;
import org.mslab.commons.client.tool.educ.settings.pref.PhoneFormatPanel;
import org.mslab.commons.client.tool.educ.settings.theme.ThemePanel;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class EducSettingsPage extends GridPanel implements ResizeHandler {	
	private Timer _resizeTimer;
	private SimplePanel _leftMargin, _rightMargin;
	private boolean _landscape; 
	
	public EducSettingsPage() {
		int row = 0; 
		_grid.setWidth("100%");
		
		_leftMargin = new SimplePanel();
		_grid.setWidget(row, 0, _leftMargin);
		
		GeoLocationSection geoPanel = new GeoLocationSection(); 
		SectionDisclosurePanel geoSection = new SectionDisclosurePanel(null, geoPanel, "Cacher la géolocalisation", "Afficher la géolocalisation");  
		geoSection.setExpanded(true);
		_grid.setWidget(row, 1, geoSection);
		
		_rightMargin = new SimplePanel();
		_grid.setWidget(row, 2, _rightMargin);
		row++; 
		
		AbstractFormatPanel namePrefPanel = new NameFormatPanel(); 
		SectionDisclosurePanel namePrefSection = new SectionDisclosurePanel(null, namePrefPanel, "Cacher le formattage des noms", "Afficher le formattage des noms");  
		_grid.setWidget(row, 1, namePrefSection);
		row++; 
		
		PhoneFormatPanel phonePrefPanel = new PhoneFormatPanel(); 
		SectionDisclosurePanel phonePrefSection = new SectionDisclosurePanel(null, phonePrefPanel, "Cacher le formattage des numéros de téléphone", "Afficher le formattage des numéros de téléphone");  
		_grid.setWidget(row, 1, phonePrefSection);
		row++; 
		
		CityAndProvinceFormatPanel cityPrefPanel = new CityAndProvinceFormatPanel(); 
		SectionDisclosurePanel cityPrefSection = new SectionDisclosurePanel(null, cityPrefPanel, "Cacher le formattage de l'adresse postale", "Afficher le formattage de l'adresse postale");
		_grid.setWidget(row, 1, cityPrefSection);
		row++; 
		
		ThemePanel themePanel = new ThemePanel(); 
		SectionDisclosurePanel themeSection = new SectionDisclosurePanel(null, themePanel, "Cacher le theme de l'application", "Afficher le theme de l'application");
		_grid.setWidget(row, 1, themeSection);
		row++; 
		
		//refresh() on window resize
		_resizeTimer = new ResizeTimer(); 
		refresh();
		Window.addResizeHandler(this); 
	}

	@Override
	public void onResize(ResizeEvent event) {
		_resizeTimer.schedule(200);
	}
	
	private class ResizeTimer extends Timer {
		@Override
		public void run() {
			 refresh();
		}
	}
	
	private void refresh() {
		int width = Window.getClientWidth(); 
		int height = Window.getClientHeight(); 
		_landscape = width >= height;
		
		String widthText = _landscape ? "18%" : "0%"; 
		_grid.getFlexCellFormatter().setWidth(0, 0, widthText);
		_grid.getFlexCellFormatter().setWidth(0, 2, widthText);
	}
	
	private static class SectionDisclosurePanel extends CommonDisclosurePanel {
		private String _hideText, _showText; 
		
		public SectionDisclosurePanel(String name, Widget childWidget, String hideText, String showText) {
			super(name, childWidget);
			_hideText = hideText;
			_showText = showText;
			setExpanded(false); 
			refresh();
		}
		
		@Override
		public void onOpen(OpenEvent<DisclosurePanel> event) {
			super.onOpen(event);
			refresh();
		}
		
		@Override
		public void onClose(CloseEvent<DisclosurePanel> event) {
			super.onClose(event);
			refresh();
		}
		
		private void refresh() {
			if (_childWidget instanceof Refreshable) {
				Refreshable refreshable = (Refreshable)_childWidget;
				refreshable.refresh();
			}
			
			String html = _expanded ? _hideText : _showText;
			HTML label = (HTML)_showHeader.getWidget(0); 
			label.setHTML(html); 
		}
	}
	
	
	
}
