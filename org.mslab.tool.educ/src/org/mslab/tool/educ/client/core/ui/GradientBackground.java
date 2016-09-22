package org.mslab.tool.educ.client.core.ui;

import org.mslab.tool.educ.shared.text.MessageFormat;
import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.user.client.ui.Widget;

public class GradientBackground {
	
	public static void setGradient(Widget widget, Color bottom, Color top) {
		//fallback
		widget.getElement().getStyle().setBackgroundColor(bottom.toString());
		
		//set gradient
		String gradient = MessageFormat.format("{0}deg, {1}, {2}", new Object[] {
			90, bottom.toString(), top.toString()});
		StyleUtil.setLinearGradient(widget, gradient);
	}
	

	public static void setGradient(Widget widget, Color primaryColor) {
		int hue = primaryColor.getHue();
		int lightness = primaryColor.getBrightness();
		
		Color rightColor = Color.createFromHsl(hue+10, 100, 100); //(int)(5.0 * lightness)); 
		Color leftColor = Color.createFromHsl(hue, 100, 50); //(int)(1.2 * lightness)); 
		Color backgroundColor = leftColor; 
		Color foregroundColor = backgroundColor.getContrastColor();
		
		widget.getElement().getStyle().setBackgroundColor(backgroundColor.toString()); //fallback if gradient not supported
		widget.getElement().getStyle().setColor(foregroundColor.toString());
		
		//set gradient
		String gradient = MessageFormat.format("{0}deg, {1}, {2}", new Object[] {
				25, leftColor.toString(), rightColor.toString()});
		StyleUtil.setLinearGradient(widget, gradient);
		
	}

	

}
