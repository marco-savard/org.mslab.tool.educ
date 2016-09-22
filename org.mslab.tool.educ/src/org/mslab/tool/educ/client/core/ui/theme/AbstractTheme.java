package org.mslab.tool.educ.client.core.ui.theme;

import java.util.ArrayList;
import java.util.List;

import org.mslab.tool.educ.shared.types.Color;

public abstract class AbstractTheme {
	protected static AbstractTheme _currentTheme; 
	private List<ThemeChangeHandler> _handlers = new ArrayList<ThemeChangeHandler>();
	
	public static void setTheme(AbstractTheme theme) {
		_currentTheme = theme;
		_currentTheme.fireThemeChangeEvent(); 
	}
	
	private void fireThemeChangeEvent() {
		//fire event
		ThemeChangeEvent event = new ThemeChangeEvent();
		for (ThemeChangeHandler handler : _handlers) {
			handler.onThemeChange(event);
		}
	}

	public static AbstractTheme getTheme() {
		if (_currentTheme == null) {
			String msg = "Error: call AbstractTheme.setTheme() before calling AbstractTheme.getTheme()"; 
			RuntimeException ex = new RuntimeException(msg); 
			throw ex;
		}
		
		return _currentTheme;
	}

	public Color getPrimaryFgColor() { return _primaryFgColor; }
	public void setPrimaryFgColor(Color color) { 
		_primaryFgColor = color; 
		fireThemeChangeEvent();
	}
	protected Color _primaryFgColor = ThematicColors.COLOR_INFO_FG;
	
	public Color getPrimaryBgColor() { return _primaryBgColor; }
	public void setPrimaryBgColor(Color color) { 
		_primaryBgColor = color; 
		fireThemeChangeEvent();
	}
	protected Color _primaryBgColor = ThematicColors.COLOR_INFO_BG;

	public void addThemeChangeHandler(ThemeChangeHandler handler) {
		_handlers.add(handler); 
	}

	public String getFontFamily() {
		return "Arial";
	}

	

	
}
