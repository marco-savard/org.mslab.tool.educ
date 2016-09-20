package org.mslab.commons.client.tool.educ.settings.pref;

import org.mslab.commons.client.core.ui.panels.GridPanel;
import org.mslab.commons.shared.types.PostalCode;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;

public class PhoneFormatPanelOld extends GridPanel implements ClickHandler {
	private RadioButton _rb0, _rb1, _rb2, _rb3;
	
	private PhoneFormatPanelOld() {
		int row = 0;
		
		Label title = new Label("Format des numéros de téléphone:"); 
		_grid.setWidget(row, 0, title);
		row++; 
		
		_rb0 = new RadioButton(PhoneFormatPanelOld.class.getName(), "514 123-4567");
		_rb0.addClickHandler(this);
		_grid.setWidget(row, 0, _rb0);
		row++; 
		
		_rb1 = new RadioButton(PhoneFormatPanelOld.class.getName(), "514-123-4567");
		_rb1.addClickHandler(this);
		_grid.setWidget(row, 0, _rb1);
		row++;
		
		_rb2 = new RadioButton(PhoneFormatPanelOld.class.getName(), "(514) 123-4567");
		_rb2.addClickHandler(this);
		_grid.setWidget(row, 0, _rb2);
		row++;
		
		_rb3 = new RadioButton(PhoneFormatPanelOld.class.getName(), "514.123.4567");
		_rb3.addClickHandler(this);
		_grid.setWidget(row, 0, _rb3);
		row++;
		
		_rb0.setValue(true);
	}
	
	private static final String[] FORMATS = new String[] {
		"{0} {1}-{2}",
		"{0}-{1}-{2}",
		"({0}) {1}-{2}",
		"{0}.{1}.{2}"
	};

	@Override
	public void onClick(ClickEvent event) {
		Object src = event.getSource(); 
		
		String phoneFormat; 
		
		if (_rb0.getValue()) {
			phoneFormat = FORMATS[0];
		} else if (_rb1.getValue()) {
			phoneFormat = FORMATS[1];
		} else if (_rb2.getValue()) { 
			phoneFormat = FORMATS[2];
		} else {
			phoneFormat = FORMATS[3];
		}
		
		Preferences.getInstance().setPhoneFormat(phoneFormat);
	}
	
}
