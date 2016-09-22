package org.mslab.tool.educ.client.core.ui.theme;

import org.mslab.tool.educ.client.core.ui.GradientBackground;
import org.mslab.tool.educ.client.core.ui.StyleUtil;
import org.mslab.tool.educ.shared.text.MessageFormat;
import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.TextOverflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.WhiteSpace;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Button;

public class ThematicButton extends Button implements MouseOverHandler, MouseOutHandler, ThemeChangeHandler {
	//private Color _fgColor, _bgColor;
	
	public ThematicButton() {
		this("");
	}
	
	public ThematicButton(String html) {
		super(html);
		removeStyleName("gwt-Button");
		
		Style style = getElement().getStyle();
		style.setBorderStyle(BorderStyle.SOLID);
		style.setBorderWidth(1, Unit.PX);
		style.setPadding(6, Unit.PX);
		style.setWhiteSpace(WhiteSpace.NOWRAP);
		style.setTextOverflow(TextOverflow.ELLIPSIS);
		style.setFontWeight(FontWeight.BOLD);
		StyleUtil.setBorderRadius(this, "5px");
		
		addMouseOverHandler(this);
		addMouseOutHandler(this);
		AbstractTheme theme = AbstractTheme.getTheme(); 
		theme.addThemeChangeHandler(this); 
		
		updateColor(); 
		refresh();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		refresh();
		/*
		
		AbstractTheme theme = AbstractTheme.getTheme(); 
		Color fg = theme.getPrimaryFgColor();
		Color bg = theme.getPrimaryBgColor();
		
		fg = enabled ? fg : fg.blendWith(bg).getGrayscale(); //.brighter(2.0); 
		bg = enabled ? bg : bg.getGrayscale(); //.brighter(2.0); 
		
		getElement().getStyle().setColor(fg.toString());
		getElement().getStyle().setBackgroundColor(bg.toString());
		getElement().getStyle().setBorderColor(fg.toString());
		*/
	}
	
	@Override
	public void onMouseOver(MouseOverEvent event) {
		getElement().getStyle().setCursor(Cursor.POINTER);
		AbstractTheme theme = AbstractTheme.getTheme(); 
		Color fg = theme.getPrimaryFgColor();
		Color bg = theme.getPrimaryBgColor();
		
		//Color primaryColor = AbstractTheme.getTheme().getPrimaryFgColor(); 
		//Color bgColor = primaryColor;
		//setGradientBackgroud(isEnabled() ? bgColor : bgColor.getGrayscale()); 
		
	    bg = isEnabled() ? bg : bg.getGrayscale(); //.brighter(2.0); 
		fg = isEnabled() ? fg : fg.blendWith(bg).getGrayscale(); //.brighter(2.0); 
		
		getElement().getStyle().setColor(fg.toString());
		getElement().getStyle().setBorderColor(fg.toString());
		GradientBackground.setGradient(this, bg.blendWith(Color.WHITE), bg);
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		//getElement().getStyle().setCursor(Cursor.DEFAULT);
		//Color bgColor = AbstractTheme.getTheme().getPrimaryFgColor();
		//setGradientBackgroud(isEnabled() ? bgColor : bgColor.getGrayscale()); 
		refresh();
	}
	
	@Override
	public void onThemeChange(ThemeChangeEvent event) {
		updateColor();
		refresh();
	}
	
	private void updateColor() {
		AbstractTheme theme = AbstractTheme.getTheme(); 
		//_fgColor = theme.getPrimaryFgColor();
		//_bgColor = theme.getPrimaryBgColor();
	}
	
	/*
	public void setColor(Color color) {
		_color = color;
		refresh();
	}
	*/
	
	private void refresh() {
		AbstractTheme theme = AbstractTheme.getTheme(); 
		Color fg = theme.getPrimaryFgColor();
		Color bg = theme.getPrimaryBgColor();
		
		fg = isEnabled() ? fg : fg.blendWith(bg).getGrayscale(); //.brighter(2.0); 
		bg = isEnabled() ? bg : bg.getGrayscale(); //.brighter(2.0); 
		
		getElement().getStyle().setColor(fg.toString());
		getElement().getStyle().setBorderColor(fg.toString());
		GradientBackground.setGradient(this, bg, bg.blendWith(Color.WHITE));
	}

	private void setGradientBackgroud(Color primaryColor) {
		Color topColor = Color.createFromHsl(primaryColor.getHue(), primaryColor.getSaturation(), 100); //primaryColor.blendWith(Color.WHITE); 
		Color bottomColor = Color.createFromHsl(primaryColor.getHue(), primaryColor.getSaturation(), 60); //.darker(1.5);
		String gradient = MessageFormat.format("{0}deg, {1}, {2}", new Object[] {
				90, bottomColor.toString(), topColor.toString()});
		StyleUtil.setLinearGradient(this, gradient);
	}


	
}
