package org.mslab.tool.educ.client.core.ui.theme;

import org.mslab.tool.educ.client.core.ui.StyleUtil;
import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;

public class ThematicBadge extends HTML implements ThemeChangeHandler {
	public ThematicBadge(String html) {
		super(html); 
		StyleUtil.setBorderRadius(this, "3px");
		Style style = getElement().getStyle();
		style.setColor(Color.WHITE.toString());
		style.setPaddingLeft(3, Unit.PX);
		style.setPaddingRight(3, Unit.PX);
		style.setTextAlign(TextAlign.CENTER);
		
		AbstractTheme theme = AbstractTheme.getTheme(); 
		theme.addThemeChangeHandler(this); 
		refresh();
	}

	@Override
	public void onThemeChange(ThemeChangeEvent event) {
		refresh();
	}

	private void refresh() {
		Style style = getElement().getStyle();
		AbstractTheme theme = AbstractTheme.getTheme(); 
		Color primaryColor = theme.getPrimaryFgColor();
		int brightness = primaryColor.getBrightness();
		brightness = (brightness > 80) ? 80 : brightness;
		Color color = Color.createFromHsl(primaryColor.getHue(), primaryColor.getSaturation(), brightness);
		style.setBackgroundColor(color.toString());
	}
}
