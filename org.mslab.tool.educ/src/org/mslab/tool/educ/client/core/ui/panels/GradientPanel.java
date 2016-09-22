package org.mslab.tool.educ.client.core.ui.panels;

import org.mslab.tool.educ.client.core.ui.Refreshable;
import org.mslab.tool.educ.shared.text.MessageFormat;

import com.google.gwt.user.client.Element;

/**
 * A simple panel with a gradient background
 * 
 * Based on: http://ie.microsoft.com/testdrive/graphics/cssgradientbackgroundmaker/default.html
 * 
 * @author Marco Savard
 *
 */
public class GradientPanel extends GridPanel implements Refreshable {
	private static final String PATT = "-{0}-linear-gradient({1}deg, {2} 0%, {3} 100%)"; 
	private static final String[] BROWSERS = new String[] {"moz", "ms", "o", "webkit"};
	private String _startColor, _endColor;
	private int _orientation = 0;
	
	public GradientPanel(String startGradientColor, String endGradientColor) {
		this(startGradientColor, endGradientColor, 0);
	}
	
	public GradientPanel(String startGradientColor, String endGradientColor, int orientation) {
		super.setSize("100%", "100%");
		_grid.setCellPadding(0);
		_grid.setCellSpacing(0);
		
		_startColor = startGradientColor;
		_endColor = endGradientColor;
		_orientation = orientation;
		refresh();
	}
	
	@Override
	public void setRefreshable(boolean refreshable) {
		_refreshable = refreshable;
	}
	
	@Override
	public boolean isRefreshable() {
		return _refreshable;
	}
	private boolean _refreshable = true;
	
	@Override
	public void refresh() {		
		getElement().getStyle().setBackgroundColor(_startColor);
		
		for (String browser : BROWSERS) {
			String image =  MessageFormat.format(PATT, new Object[] {browser, _orientation, _startColor, _endColor}); 
			getElement().getStyle().setBackgroundImage(image);
		}
	}
	
	public void setGradientStartColor(String color) {
		_startColor = color;
		
		if (isRefreshable()) {
			refresh();
		}
	}

	public void setGradientEndColor(String color) {
		_endColor = color;
		
		if (isRefreshable()) {
			refresh();
		}
	}
	
	public void setOrientation(int orientation) {
		_orientation = orientation;
		
		if (isRefreshable()) {
			refresh();
		}
	}

	public void setTitleFontSize(String fontSize) {
		getElement().getStyle().setProperty("fontSize", fontSize);
	}

	public void setTitleColor(String color) {
		getElement().getStyle().setColor(color);
	}

	
	

	
}
