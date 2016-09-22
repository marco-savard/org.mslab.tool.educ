package org.mslab.tool.educ.client.core.ui.theme;

import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Anchor;

public class ThematicAnchor extends Anchor implements MouseOverHandler, MouseOutHandler, ThemeChangeHandler {
	public ThematicAnchor() {
		this("");
	}
	
	public ThematicAnchor(String text) {
		super(text);
		addMouseOverHandler(this);
		addMouseOutHandler(this);
		AbstractTheme.getTheme().addThemeChangeHandler(this); 
		refresh();
	}
	
	@Override
	public void onMouseOver(MouseOverEvent event) {
		getElement().getStyle().setCursor(Cursor.POINTER);
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		getElement().getStyle().setCursor(Cursor.DEFAULT);
	}
	
	@Override
	public void onThemeChange(ThemeChangeEvent event) {
		refresh();
	}
	
	private void refresh() {
		AbstractTheme theme = AbstractTheme.getTheme(); 
		Color primaryColor = theme.getPrimaryFgColor();
		int brightness = primaryColor.getBrightness();
		brightness = (brightness > 80) ? 80 : brightness;
		Color color = Color.createFromHsl(primaryColor.getHue(), primaryColor.getSaturation(), brightness);
		getElement().getStyle().setColor(color.toString());
	}
	
}
