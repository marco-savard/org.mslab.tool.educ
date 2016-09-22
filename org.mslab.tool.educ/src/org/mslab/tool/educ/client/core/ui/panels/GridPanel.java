package org.mslab.tool.educ.client.core.ui.panels;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.SimplePanel;

//
// A GWT equivalent of Swing's GridBagLayout or SWT's GridLayout
//
public abstract class GridPanel extends FocusPanel {
	public FlexTable _grid; 
	
	protected GridPanel() {
		_grid = new FlexTable();
		setWidget(_grid);
	}
	
	public void clear() {
		_grid.clear();
	}
}
