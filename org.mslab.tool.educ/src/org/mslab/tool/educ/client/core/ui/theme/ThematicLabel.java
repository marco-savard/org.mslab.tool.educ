package org.mslab.tool.educ.client.core.ui.theme;

import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.user.client.ui.Label;

public class ThematicLabel extends Label implements ThemeChangeHandler {
	private Color _color;
	
	public ThematicLabel() {
		this("");
	}
	
	public ThematicLabel(String text) {
		super(text);
		AbstractTheme.getTheme().addThemeChangeHandler(this); 
		
		updateColor(); 
		refresh();
	}

	@Override
	public void onThemeChange(ThemeChangeEvent event) {
		updateColor();
		refresh();
	}

	private void updateColor() {
		AbstractTheme theme = AbstractTheme.getTheme(); 
		Color primaryColor = theme.getPrimaryFgColor();
		int brightness = primaryColor.getBrightness();
		_color = primaryColor.setBrightness((brightness > 80) ? 80 : brightness); 
	}
	
	private void refresh() {
		this.getElement().getStyle().setColor(_color.toString());
	}

}
