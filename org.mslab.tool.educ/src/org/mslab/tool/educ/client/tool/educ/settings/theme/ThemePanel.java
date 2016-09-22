package org.mslab.tool.educ.client.tool.educ.settings.theme;

import org.mslab.tool.educ.client.core.ui.Refreshable;
import org.mslab.tool.educ.client.core.ui.panels.GridPanel;
import org.mslab.tool.educ.client.core.ui.slider.Slider2;
import org.mslab.tool.educ.client.core.ui.slider.SliderEvent2;
import org.mslab.tool.educ.client.core.ui.slider.SliderListener2;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

public class ThemePanel extends GridPanel implements SliderListener2, Refreshable {
	private Slider2 _slider; 
	private HueLabel _label;
	private int _value = 240;
	
	public ThemePanel() {
		int row = 0;
		_grid.setWidth("100%");
		
		_label = new HueLabel();
		_grid.setWidget(row, 0, _label);
		row++; 
		
		_slider = new Slider2("", 0, 360, _value); 
		_slider.setWidth("100%");
		_slider.getElement().getStyle().setMarginTop(36, Unit.PX);
		_slider.addListener(this);
		_grid.setWidget(row, 0, _slider);
		row++; 

		refresh();
	}

	@Override
	public void onStart(SliderEvent2 e) {
	}

	@Override
	public boolean onSlide(SliderEvent2 e) {
		boolean changed = false;
		
		Slider2 source = e.getSource();
		if (source == _slider) {
			changed = (_value != e.getValues()[0]);
			if (changed) {
				_value = e.getValues()[0];
				refresh();  
			}
		}
		
		return changed;
	}

	@Override
	public void onChange(SliderEvent2 e) {
	}

	@Override
	public void onStop(SliderEvent2 e) {
	}

	@Override
	public void refresh() {
		_label.refresh();
		_slider.refresh();
	}

	@Override
	public void setRefreshable(boolean refreshable) {
	}

	@Override
	public boolean isRefreshable() {
		return false;
	}
	
	private class HueLabel extends HTML {
		HueLabel() {
			this.getElement().getStyle().setFontSize(160, Unit.PCT);
		}
		
		public void refresh() {
			//Color color = Color.createFromHsl(_value, 100, 100); 
			//AbstractTheme theme = AbstractTheme.getTheme(); 
			//theme.setPrimaryFgColor(color);
			
			//Color.Hue hue = color.getHueCode();
			//String text = MessageFormat.format("Teinte : {0}&deg; {1}", 
			//	_value, HUE_NAMES[hue.ordinal()]); 
			//_label.setHTML(text);
		}
	}
	private static final String[] HUE_NAMES = {"Rouge", "Orange", "Jaune", "Vert", "Cyan", "Bleu", "Mauve"}; 

}
