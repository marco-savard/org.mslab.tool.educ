package org.mslab.tool.educ.client.core.ui.panels;

import com.google.gwt.dom.client.Style.OutlineStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.SimplePanel;

//A general-purpose panel with a grid a unique direct component, similar to a 
//panel with GridBagLayout in Swing, or GridLayout in SWT

public abstract class GridFocusPanel extends FocusPanel {
	protected FlexTable _grid;
	
	protected GridFocusPanel() {
		_grid = new FlexTable();
		_grid.setSize("100%", "100%");
		add(_grid); 
		
		_grid.setCellSpacing(6);
		
		//remove the orange outline in Chrome
		getElement().getStyle().setOutlineStyle(OutlineStyle.NONE); 
		getElement().getStyle().setOutlineWidth(0, Unit.PX);
	}
	
	public void clear() {
		_grid.clear();
	}
}
