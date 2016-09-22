package org.mslab.tool.educ.client.core.ui;

import org.mslab.tool.educ.shared.text.MessageFormat;
import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Widget;

public class StyleUtil {

	public static void setUnselectable(Widget widget) {
		Style style = widget.getElement().getStyle();
		style.setProperty("UserSelect", "none");
		style.setProperty("KhtmlUserSelect", "none");
		style.setProperty("WebkitUserSelect", "none");
		style.setProperty("MozUserSelect", "none");
		//style.setProperty("MsUserSelect", "none");
	}
	
	public static void setBorderRadius(Widget widget, String value) {
		Style style = widget.getElement().getStyle();
		style.setProperty("borderRadius", value);
		style.setProperty("mozBorderRadius", value);
		style.setProperty("webkitBorderRadius", value);
		style.setProperty("oBorderRadius", value);
	}
	
	public static void setBoxShadow(Widget widget, String value) {
		Style style = widget.getElement().getStyle();
		style.setProperty("boxShadow", value);
		style.setProperty("mozBoxShadow", value);
		style.setProperty("webkitBoxShadow", value);
		style.setProperty("oBoxShadow", value);
	} 
	
	public static void transform(Widget widget, String value) {
		Style style = widget.getElement().getStyle();
		style.setProperty("Transform", value);
		style.setProperty("WebkitTransform", value);
		style.setProperty("MozTransform", value);
		style.setProperty("OTransform", value);
		//style.setProperty("MsTransform", value);
		
		//_imageHoder.getElement().getStyle().setProperty("transform", text);
		//_imageHoder.getElement().getStyle().setProperty("webkitTransform", text);
	}

	public static void rotate(Widget widget, int degrees) {
		Style style = widget.getElement().getStyle();
		String value = "rotate(" + degrees + "deg)"; 
		style.setProperty("Transform", value);
		style.setProperty("WebkitTransform", value);
		style.setProperty("MozTransform", value);
		style.setProperty("OTransform", value);
		//style.setProperty("MsTransform", value);
	}

	public static void transition(Widget widget, String value) {
		Style style = widget.getElement().getStyle();
		style.setProperty("Transition", "all 0.4s ease-in-out");
		style.setProperty("MozTransition", "all 0.4s ease-in-out");
		style.setProperty("WebkitTransition", "all 0.4s ease-in-out");
		style.setProperty("OTransition", "all 0.4s ease-in-out");
		//style.setProperty("MsTransition", "all 0.4s ease-in-out");
	}
	
	//value (e.g. "blur(1.7em)" )
	public static void filter(Widget widget, String value) {
		Style style = widget.getElement().getStyle();
		style.setProperty("Filter", value);
		style.setProperty("MozFilter", value);
		style.setProperty("WebkitFilter", value);
		style.setProperty("OFilter", value);
		//style.setProperty("MsFilter", "blur(1.7em)");
	}

	private static final String[] BROWSERS = new String[] {"moz", "ms", "o", "webkit"};
	
	public static void setLinearGradient(Widget widget, int degrees, Color leftColor, Color rightColor) {
		Color backgroundColor = leftColor; 
		Color foregroundColor = backgroundColor.getContrastColor();
		
		widget.getElement().getStyle().setBackgroundColor(backgroundColor.toString()); //fallback if gradient not supported
		widget.getElement().getStyle().setColor(foregroundColor.toString());
		
		//set gradient
		String gradient = MessageFormat.format("{0}deg, {1}, {2}", new Object[] {
			degrees, leftColor.toString(), rightColor.toString()});
		StyleUtil.setLinearGradient(widget, gradient);
		
	}
	
	public static void setLinearGradient(Widget widget, String value) {
		Style style = widget.getElement().getStyle();
		
		for (String browser : BROWSERS) {
			String image =  MessageFormat.format(LINEAR_GRADIENT, new Object[] {browser, value}); 
			style.setBackgroundImage(image);
		}
	}
	private static final String LINEAR_GRADIENT = "-{0}-linear-gradient({1})"; 
	
	public static void setRepeatingLinearGradient(Widget widget, String value) {
		Style style = widget.getElement().getStyle();
		
		for (String browser : BROWSERS) {
			String image =  MessageFormat.format(REPEATING_LINEAR_GRADIENT, new Object[] {browser, value}); 
			style.setBackgroundImage(image);
		}
	}
	private static final String REPEATING_LINEAR_GRADIENT = "-{0}-repeating-linear-gradient({1})";




	




	
}