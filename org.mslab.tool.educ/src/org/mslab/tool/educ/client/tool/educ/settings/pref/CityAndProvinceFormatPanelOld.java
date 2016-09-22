package org.mslab.tool.educ.client.tool.educ.settings.pref;

import org.mslab.tool.educ.client.core.ui.panels.GridPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;

public class CityAndProvinceFormatPanelOld extends GridPanel implements ClickHandler {
	private RadioButton _rb0, _rb1;
	
	public CityAndProvinceFormatPanelOld() {
		int row = 0;
		
		Label title = new Label("Format des villes:"); 
		_grid.setWidget(row, 0, title);
		row++; 
		
		_rb0 = new RadioButton(CityAndProvinceFormatPanelOld.class.getName(), "Montréal, QC");
		_rb0.addClickHandler(this);
		_grid.setWidget(row, 0, _rb0);
		row++; 
		
		_rb1 = new RadioButton(CityAndProvinceFormatPanelOld.class.getName(), "Montréal (Québec)");
		_rb1.addClickHandler(this);
		_grid.setWidget(row, 0, _rb1);
		row++;
		
		_rb0.setValue(true);
	}

	@Override
	public void onClick(ClickEvent event) {
		Object src = event.getSource(); 
		String cityFormat; 
		
		if (_rb0.getValue()) {
			cityFormat = "{0}, {1}";
		} else {
			cityFormat = "{0} ({2})";
		}
		
		Preferences.getInstance().setCityFormat(cityFormat);
	}
	
}
