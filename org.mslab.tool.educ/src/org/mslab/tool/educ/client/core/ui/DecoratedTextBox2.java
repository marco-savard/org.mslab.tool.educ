package org.mslab.tool.educ.client.core.ui;

import org.mslab.tool.educ.client.core.ui.panels.GridPanel;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;

public abstract class DecoratedTextBox2 extends GridPanel implements KeyUpHandler {
	private int _widthPx, _heightPx, _delay;
	protected TextBox _textBox;
	private Timer _updateTimer;
	private String _text;
	
	protected DecoratedTextBox2(String decoratingIcon, String placeholder, String bgColor, int widthPx, int delay) {
		_widthPx = widthPx;
		_heightPx = 36;
		_delay = delay;
		_grid.setSize("100%", "100%");
		_grid.setCellPadding(0);
		_grid.setCellSpacing(0);
		
		Style style = getElement().getStyle();
		style.setBorderStyle(BorderStyle.SOLID);
		style.setBorderWidth(1, Unit.PX);
		StyleUtil.setBorderRadius(this, "5px");
		setSize(_widthPx + "px", _heightPx + "px");

		//the decorating icon
		HorizontalPanel prefix = new HorizontalPanel();
		prefix.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		style = prefix.getElement().getStyle();
		style.setBackgroundColor(bgColor);
		style.setHeight(100, Unit.PCT);
		style.setWidth(_heightPx, Unit.PX); //square
		StyleUtil.setBorderRadius(prefix, "5px 0px 0px 5px");
		_grid.setWidget(0, 0, prefix);
		prefix.setSize(_heightPx + "px", "100%");			
		
		HTML icon = new HTML(decoratingIcon);
		icon.getElement().getStyle().setTextAlign(TextAlign.CENTER);
		prefix.add(icon);
		
		//the text box
		SimplePanel center = new SimplePanel();
		_grid.setWidget(0, 1, center);
		_grid.getFlexCellFormatter().setWidth(0, 1, "95%");
					
		_textBox = new TextBox();
		_textBox.setWidth("11em");
		_textBox.getElement().setPropertyString("placeholder", placeholder);
		_textBox.getElement().getStyle().setBorderStyle(BorderStyle.NONE);
		_textBox.addKeyUpHandler(this);
		
		center.add(_textBox);
		
		//the right part
		SimplePanel suffix = new SimplePanel();
		_grid.setWidget(0, 2, suffix);
		suffix.setSize(_heightPx + "px", "100%");	
		
		_updateTimer = new Timer() {
			@Override
			public void run() {
				String text = _textBox.getText();
				if (! text.equals(_text)) {
					_text = text;
					onTextChanged(text);
				}
			}	
		};
	}
	
	public TextBox getTextBox() {
		return _textBox;
	}
	
	public String getText() {
		return _textBox.getText();
	}
	
	/*
	public void addKeyUpHandler(KeyUpHandler handler) {
		_textBox.addKeyUpHandler(handler);
	}
	*/
	
	protected abstract void onTextChanged(String textChanged);
	
	@Override
	public void onKeyUp(KeyUpEvent event) {
		_updateTimer.cancel();
		_updateTimer.schedule(_delay);
	}
}
