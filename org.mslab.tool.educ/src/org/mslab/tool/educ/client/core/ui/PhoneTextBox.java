package org.mslab.tool.educ.client.core.ui;

import java.util.ArrayList;
import java.util.List;

import org.mslab.tool.educ.client.core.ui.interfaces.Renderer;
import org.mslab.tool.educ.client.core.ui.panels.GridFocusPanel;
import org.mslab.tool.educ.client.core.ui.panels.GridPanel;
import org.mslab.tool.educ.shared.text.MessageFormat;
import org.mslab.tool.educ.shared.types.Color;
import org.mslab.tool.educ.shared.types.PhoneNumber;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class PhoneTextBox extends DecoratedTextBox  {
	private static final String PLACE_HOLDER = "(999) 999-9999";

	public PhoneTextBox() {
		DropDownControl control = new DropDownControl(this, new NumericPadPopupPanel());
		setPrefix(new HTML("<span class=\"fa fa-phone\"></span>"));
		setSuffix(control);
		build();
		
		TextBox textBox = getTextBox(); 
		textBox.getElement().getStyle().setProperty("placeHolder", "phone");
		textBox.getElement().setPropertyString("placeholder", PLACE_HOLDER);
		textBox.addKeyUpHandler(this); 
		
		Renderer renderer = new PhoneRenderer(); 
		super.setRenderer(renderer); 
	}
	
	public PhoneNumber getPhoneNumber() {
		TextBox textBox = getTextBox(); 
		String text = textBox.getText(); 
		PhoneNumber phone = PhoneNumber.parse(text);
		return phone;
	}
	
	public void addChangeHandler(ChangeHandler handler) {
		_changeHandlers.add(handler); 
	}
	private List<ChangeHandler> _changeHandlers = new ArrayList<ChangeHandler>();
	
	@Override
	public void onKeyUp(KeyUpEvent event) {
		ChangeEvent ce = new PhoneChangeEvent(this); 
		for (ChangeHandler handler : _changeHandlers) {
			handler.onChange(ce);
		}
		
		super.onKeyUp(event);
	}
	
	private static class PhoneChangeEvent extends ChangeEvent {
		private PhoneTextBox _source; 
		
		public PhoneChangeEvent(PhoneTextBox phoneTextBox) {
			_source = phoneTextBox;
		}
		
		@Override 
		public Object getSource() {
			return _source;
		}
	}
	
	private static class PhoneRenderer implements Renderer {
		private boolean _parenthesis = false; 
		PhoneRenderer() {}

		@Override
		public String fromDisplayText(String display) {
			String value = "";
			for (int i=0; i<display.length(); i++) {
				char ch = display.charAt(i); 
				_parenthesis |= (ch == '(');
				boolean accepted = Character.isDigit(ch);
				if (accepted) {
					value += ch;
				}
			}
			return value;
		}

		@Override
		public String toDisplayText(String value) {
			String display = ""; 
			
			if (value.length() >= 10) {
				display = MessageFormat.format("({0}) {1}-{2}", new Object[] {value.substring(0, 3), value.substring(3, 6), value.substring(6, 10)}); 
				_parenthesis = false;
			} else if (value.length() >= 6) {
				display = MessageFormat.format("({0}) {1}-{2}", new Object[] {value.substring(0, 3), value.substring(3, 6), value.substring(6)}); 
				_parenthesis = false;
			} else if (value.length() >= 3) {
				display = MessageFormat.format("({0}) {1}", new Object[] {value.substring(0, 3), value.substring(3)}); 
				_parenthesis = false;
			} else {
				display = _parenthesis ? "(" + value : value;
			}
			
			return display;
		}
	}
	
	private class NumericPadPopupPanel extends PopupPanel {
		private AbsolutePanel _numericPad;
		
		NumericPadPopupPanel() {
			super(true);
			_numericPad = new NumericPadAbsolute(this);
			setWidget(_numericPad);
		}
	}
	
	private class NumericPadAbsolute extends AbsolutePanel implements ClickHandler {
		private PopupPanel _popup;
		private GridPanel _numericPad;
		
		NumericPadAbsolute(PopupPanel popup) {
			_popup = popup;
			_numericPad = new NumericPad();
			add(_numericPad); 
		}

		@Override
		public void onClick(ClickEvent event) {
			_popup.hide();
		}
	}
	
	private class NumericPad extends GridPanel implements MouseOverHandler, MouseOutHandler {
		NumericPad() {
			for (int i=0; i<9; i++) {
				int row = i/3;
				int col = i%3;
				NumericKey key = new NumericKey(i+1); 
				_grid.setWidget(row, col, key);
				_grid.getFlexCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
				key.addMouseOverHandler(this); 
				key.addMouseOutHandler(this); 
			}
			
			NumericKey key = new NumericKey(0); 
			_grid.setWidget(3, 1, key);
			_grid.getFlexCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_CENTER);
			key.addMouseOverHandler(this); 
			key.addMouseOutHandler(this); 
		}
		
		@Override
		public void onMouseOver(MouseOverEvent ev) {
			NumericKey key = (NumericKey)ev.getSource(); 
			key.getElement().getStyle().setBackgroundColor(Color.GREY_LIGHT.toString());
			key.getElement().getStyle().setCursor(Cursor.POINTER);
		}

		@Override
		public void onMouseOut(MouseOutEvent ev) {
			NumericKey key = (NumericKey)ev.getSource(); 
			key.getElement().getStyle().setBackgroundColor(Color.WHITE.toString());
			key.getElement().getStyle().setCursor(Cursor.DEFAULT);
		}
	}
	
	private class NumericKey extends GridFocusPanel implements ClickHandler {
		private final String[] LETTERS = new String[] {
			"+", "", "ABC", "DEF", "GHI", "JKL", "MNO", "PQRS", "TUV", "WXYZ"};
		
		private int _key;
		NumericKey(int key) {
			_key = key;
			_grid.setCellPadding(0);
			_grid.setCellSpacing(0);
			setSize("40px", "32px");
			
			Label digitLbl = new Label(Integer.toString(key));
			digitLbl.getElement().getStyle().setFontSize(200, Unit.PCT);
			_grid.setWidget(0, 0, digitLbl);
			
			Label lettersLbl = new Label(LETTERS[key]);
			boolean smaller = (key == 7) || (key == 9); 
			lettersLbl.getElement().getStyle().setFontSize(smaller ? 60 : 75, Unit.PCT);
			_grid.setWidget(0, 1, lettersLbl);
			_grid.getFlexCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_BOTTOM);

			addClickHandler(this);  
		}

		@Override
		public void onClick(ClickEvent event) {
			if (_renderer != null) {
				String value = _renderer.fromDisplayText(_textBox.getValue());
				value = value + _key;
				String display = _renderer.toDisplayText(value); 
				_textBox.setText(display);
			}
		}
	} //end NumericKey
}
