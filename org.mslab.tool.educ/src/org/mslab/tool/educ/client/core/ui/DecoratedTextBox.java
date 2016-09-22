package org.mslab.tool.educ.client.core.ui;

import org.mslab.tool.educ.client.core.ui.interfaces.Buildable;
import org.mslab.tool.educ.client.core.ui.interfaces.Renderer;
import org.mslab.tool.educ.client.core.ui.panels.GridFocusPanel;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public abstract class DecoratedTextBox extends GridFocusPanel 
	implements MouseOverHandler, MouseOutHandler, BlurHandler, Buildable, KeyUpHandler {
	private static final String BORDER_COLOR_NORMAL = "#cccccc";
	private static final String BORDER_COLOR_HOVER = "#777777";
	private Widget _prefix = new Label(), _suffix = new Label();  
	protected TextBox _textBox; 
	protected Renderer _renderer = null;
	
	protected DecoratedTextBox() {
		//build UI
		_grid.setCellPadding(0);
		_grid.setCellSpacing(0);
		_grid.setSize("100%", "100%");
		
		Style style = getElement().getStyle();
		style.setBorderStyle(BorderStyle.SOLID);
		style.setBorderWidth(2, Unit.PX);
		style.setBorderColor(BORDER_COLOR_NORMAL);
		
		addMouseOverHandler(this); 
		addMouseOutHandler(this); 
	}
	
	public void setEnabled(boolean enabled) {
		TextBox textBox = getTextBox(); 
		textBox.setEnabled(enabled);
	}
	
	protected void setPrefix(Widget prefix) {
		_prefix = prefix;
	}
	
	protected void setSuffix(Widget suffix) {
		_suffix = suffix;
	}
	
	public void build() {
		int col = 0;
		StyleUtil.setBorderRadius(this, "5px 5px 5px 5px");
		
		if (_prefix != null) {
			_grid.setWidget(0, col, _prefix); 
			_prefix.setWidth("18px");
			_prefix.setHeight("20px");
			_prefix.getElement().getStyle().setBackgroundColor(BORDER_COLOR_NORMAL);
			_prefix.getElement().getStyle().setTextAlign(TextAlign.CENTER);
			_grid.getFlexCellFormatter().setVerticalAlignment(0, col, HasVerticalAlignment.ALIGN_BOTTOM);
			//_prefix.getElement().getStyle().setBorderStyle(BorderStyle.NONE);
			col++;
		}
			
		_textBox = new TextBox();
		_textBox.setWidth("7em");
		_textBox.getElement().getStyle().setPadding(0, Unit.PX);
		_textBox.getElement().getStyle().setBorderStyle(BorderStyle.NONE);
		_grid.setWidget(0, col, _textBox); 
		_grid.getFlexCellFormatter().setWidth(0, col, "95%");
		col++;
		
		if (_suffix != null) { 
			_grid.setWidget(0, col, _suffix); 
			_suffix.setWidth("18px");
			_suffix.setHeight("20px");
			_suffix.getElement().getStyle().setBackgroundColor(BORDER_COLOR_NORMAL);
			_suffix.getElement().getStyle().setTextAlign(TextAlign.CENTER);
			_grid.getFlexCellFormatter().setVerticalAlignment(0, col, HasVerticalAlignment.ALIGN_BOTTOM);
			col++;
		}
		
		_textBox.addBlurHandler(this);
		_textBox.addKeyUpHandler(this);
	}
	
	@Override
	public void onMouseOver(MouseOverEvent event) {
		TextBox textBox = getTextBox(); 
		if (textBox.isEnabled()) {
			Style style = getElement().getStyle();
			style.setBorderColor(BORDER_COLOR_HOVER);
		}
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		Style style = getElement().getStyle();
		style.setBorderColor(BORDER_COLOR_NORMAL);
	}
	
	@Override
	public void onKeyUp(KeyUpEvent event) {
		if (_renderer != null) {
			String value = _renderer.fromDisplayText(_textBox.getValue());
			
			int charCode= event.getNativeKeyCode();
		    if (charCode != KeyCodes.KEY_BACKSPACE) {
		    	String display = _renderer.toDisplayText(value); 
		    	_textBox.setText(display);
		    }
		}
	}
	
	@Override
	public void onBlur(BlurEvent event) {
		if (_renderer != null) {
			String value = _renderer.fromDisplayText(_textBox.getValue());
			String display = _renderer.toDisplayText(value); 
			_textBox.setText(display);
		}
	}

	public TextBox getTextBox() {
		return _textBox;
	}

	public void setRenderer(Renderer renderer) {
		_renderer = renderer;
	}

}
