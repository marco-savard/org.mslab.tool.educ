package org.mslab.tool.educ.client.core.ui;

import org.mslab.tool.educ.client.core.ui.panels.GridPanel;
import org.mslab.tool.educ.shared.text.MessageFormat;
import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

public class ProgressBar extends AbsolutePanel {
	private double _value;
	private ProgressBarPill _pill;
	private Label _label; 
	
	public ProgressBar() {
		Style style = getElement().getStyle();
		style.setTextAlign(TextAlign.CENTER);
		setSize("100%", "100%");
		
		_pill = new ProgressBarPill(); 
		_pill.setSize("100%", "100%");
		add(_pill, 0, 0);
		
		//add Label
		_label = new Label();
		_label.setSize("100%", "100%");
		_label.getElement().getStyle().setFontSize(80, Unit.PCT);
		add(_label, 0, 4);
	}
	
	public void setValue(double value) {
		_value = value;
		_pill.refresh();
		
		String text = NumberFormat.getFormat("0.##").format(value) + " %";
		_label.setText(text);
		Color color = (value < 50) ? Color.WHITE : Color.BLACK;
		_label.getElement().getStyle().setColor(color.toString());
	}
	
	public void setColors(Color fgColor, Color bgColor) {
		_pill.setColors(fgColor, bgColor); 
	}
	
	private class ProgressBarPill extends GridPanel {
		private CandyStrip _candyStrip;
		
		ProgressBarPill() {
			//outer panel
			Style style = getElement().getStyle();
			style.setBackgroundColor("#555");
			StyleUtil.setBorderRadius(this, "20px");
			_grid.setSize("100%", "100%");
			
			//candy strip
			_candyStrip = new CandyStrip();
			_grid.setWidget(0, 0, _candyStrip);
			_grid.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
					
			refresh();
		}
		
		private void refresh() {
			_candyStrip.setValue(_value); 
		}
		
	    void setColors(Color fgColor, Color bgColor) {
			_candyStrip.setColors(fgColor, bgColor); 
		}
	} //end ProgressBarPill
	
	private class CandyStrip extends AbsolutePanel {
		
		CandyStrip() {
			Style style = getElement().getStyle(); 
			style.setHeight(12, Unit.PX);
			style.setVerticalAlign(VerticalAlign.MIDDLE);
			
			StyleUtil.setBorderRadius(this, "20px 10px 10px 20px");
			StyleUtil.transition(this, "all 2.0s ease-in-out");	
			//setColors(new Color(43, 194, 83));
		}
		
		void setValue(double value) {
			getElement().getStyle().setWidth(_value, Unit.PCT);
		}
		
	    void setColors(Color fgColor, Color bgColor) {
	    	//fallback
	    	getElement().getStyle().setBackgroundColor(fgColor.toString());
	    	//Color topColor = primaryColor.brighter(1.5); 
	    	
	    	//apply gradient
	    	/*
	    	repeating-linear-gradient(135deg, 
	    			  #000, #000 .25em / * black stripe * /, 
	    			  #0092b7 0, #0092b7 .75em / * blue stripe * /
	    			)
	    			*/
	    			
	    	Color blend = fgColor.blendWith(bgColor, 80);
	    	String gradient = MessageFormat.format("{0}deg, {1}, {1} .25em, {2} 0, {2} .75em",
	    		45, blend.toString(), bgColor.toString()); 
			StyleUtil.setRepeatingLinearGradient(this, gradient);
		}
	}
}
