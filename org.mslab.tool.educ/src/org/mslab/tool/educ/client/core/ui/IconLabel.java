package org.mslab.tool.educ.client.core.ui;

import org.mslab.tool.educ.client.core.ui.panels.GridPanel;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class IconLabel extends GridPanel {
	private Label _label;
	
	public IconLabel(Widget icon, String text) {
		_grid.setWidget(0, 0, icon);
		_grid.getFlexCellFormatter().setWidth(0, 0, "5%"); 
		
		_label = new Label(text); 
		_grid.setWidget(0, 1, _label);
	}

	public void setText(String text) {
		_label.setText(text);
	}

	public void setIcon(HTML icon) {
		_grid.setWidget(0, 0, icon);
	}
}
