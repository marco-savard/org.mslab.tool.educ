package org.mslab.tool.educ.client.core.ui;

import java.util.ArrayList;
import java.util.List;

import org.mslab.tool.educ.client.core.ui.interfaces.Renderer;
import org.mslab.tool.educ.client.core.ui.panels.GridPanel;
import org.mslab.tool.educ.client.core.ui.theme.AbstractTheme;
import org.mslab.tool.educ.client.core.ui.theme.ThemeChangeEvent;
import org.mslab.tool.educ.client.core.ui.theme.ThemeChangeHandler;
import org.mslab.tool.educ.shared.text.CharacterExt;
import org.mslab.tool.educ.shared.types.Color;
import org.mslab.tool.educ.shared.types.PostalCode;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

//
// A complex widget to enter a Canadian Postal Code
//
public class PostalCodePicker extends DecoratedTextBox implements KeyPressHandler, BlurHandler, ChangeHandler {
	private CharPickerPopup _charPickerPopup;
	private DropDownControl _control; 

	public PostalCodePicker(String placeHolder) {
		_postalCode = new PostalCode("");
		_charPickerPopup = new CharPickerPopup(this); 
		_control = new DropDownControl(this, _charPickerPopup);
		setPrefix(new HTML("<span class=\"fa fa-envelope-o\"></span>"));
		setSuffix(_control);
		build();
		
		TextBox textBox = super.getTextBox(); 
		textBox.getElement().getStyle().setProperty("placeHolder", "postalCode");
		textBox.getElement().setPropertyString("placeholder", placeHolder);
		textBox.addKeyPressHandler(this);
		textBox.addKeyUpHandler(this);
		textBox.addBlurHandler(this);
		
		Renderer renderer = new PostalCodeRenderer(); 
		super.setRenderer(renderer); 
	}
	
	private PostalCode _postalCode;
	public PostalCode getPostalCode() { return _postalCode; }
	public void setPostalCode(PostalCode postalCode) { 
		_postalCode = postalCode; 
		refresh();
	}
	
	@Override
	public void onKeyPress(KeyPressEvent ev) {
		char ch = ev.getCharCode(); 
		boolean accepted = filterCharacter(ch);
		
		if (! accepted) {
			ev.preventDefault();
		}
	}
	
	@Override
	public void onKeyUp(KeyUpEvent event) {
		String text = getTextBox().getText(); 
		_postalCode.setText(text);
		getTextBox().setText(_postalCode.toDisplayString()); 
		
		if (_postalCode.isValid()) {
			fireChangeEvent(); 
		}
		
		super.onKeyUp(event);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		_control.setEnabled(enabled);
	}
	
	//by default, accept letters, digits and blanks
	protected boolean filterCharacter(char ch) {
		boolean accepted = false;
		accepted |= Character.isDigit(ch);
		accepted |= Character.isLetter(ch);
		accepted |= CharacterExt.isWhitespace(ch); 
		accepted |= CharacterExt.isISOControl(ch);
		return accepted;
	}
	
	@Override
	public void onBlur(BlurEvent event) {
		normalizePostalCode();
	}
	
	private void normalizePostalCode() {
		String text = getTextBox().getText(); 
		PostalCode pc = new PostalCode(text); 
		String display = pc.toDisplayString(); 
		getTextBox().setText(display);
		
		if (pc.isValid()) {
			fireChangeEvent(); 
		}
	}
	
	private void fireChangeEvent() {
		ChangeEvent ev = new PostalCodeChangeEvent(this); 
		for (ChangeHandler handler : _changeHandlers) {
			handler.onChange(ev);
		}
	}

	public void addChangeHandler(ChangeHandler handler) {
		_changeHandlers.add(handler); 
	}
	private List<ChangeHandler> _changeHandlers = new ArrayList<ChangeHandler>();
	
	@Override
	public void onChange(ChangeEvent event) {
		if (event instanceof CharChangeEvent) {
			CharChangeEvent cce = (CharChangeEvent)event;
			onCharChange(cce); 
			refresh();
		} //end if
	} //end onChange()
	
	private void onCharChange(CharChangeEvent cce) {
		char ch = cce.getChar();
		
		if (ch == CharButton.CLEAR) {
			_postalCode = new PostalCode(""); 
			_charPickerPopup.init();
		} else {
			String postalCode = _postalCode.toString();
			
			if (postalCode.length() <= 5) {
				_postalCode.add(ch); 
				_charPickerPopup.toggle();
				postalCode = _postalCode.toString();
			} 
			
			if (postalCode.length() >= 6) {
				_charPickerPopup.hide(); 
			}
		}
	}

	private void refresh() {
		TextBox textBox = super.getTextBox(); 
		textBox.setText(_postalCode.toDisplayString()); 
		_charPickerPopup.setEnabled(_postalCode.toString().length() <= 5); 
	}
	
	//
	// inner classes
	//
	public class PostalCodeChangeEvent extends ChangeEvent {
		private PostalCodePicker _src; 
		
		public PostalCodeChangeEvent(PostalCodePicker src) {
			_src = src;
		}
		
		@Override
		public Object getSource() {
			return _src;
		}
	}
	
	private static class PostalCodeRenderer implements Renderer {
		PostalCodeRenderer() {}

		@Override
		public String fromDisplayText(String display) {
			String value = display;
			return value;
		}

		@Override
		public String toDisplayText(String value) {
			String display = value; 
			return display;
		}
	}
	
	private class CharPickerPopup extends PopupPanel {
		CharPickerPopupContent _content;
		
		public CharPickerPopup(ChangeHandler handler) {
			super(true);
			_content = new CharPickerPopupContent(this, handler);
			setWidget(_content);
		}

		public void setEnabled(boolean enabled) {
			_content.setEnabled(enabled);
		}

		public void init() {
			_content.init();
		}

		public void toggle() {
			_content.toggle();
		}
		
		@Override
		public void hide() {
			normalizePostalCode();
			super.hide();
		}
	}
	
	private class CharPickerPopupContent extends GridPanel implements ClickHandler {
		private CharPickerPopup _owner;
		private CharPad _charPad;
		
		public CharPickerPopupContent(CharPickerPopup owner, ChangeHandler handler) {
			_owner = owner;
			int row = 0;
			_charPad = new CharPad(owner); 
			_charPad.addChangeHandler(handler); 
			_grid.setWidget(row, 0, _charPad); 
			row++;
		}

		public void setEnabled(boolean enabled) {
			_charPad.setEnabled(enabled);
		}

		public void init() {
			_charPad.init();
		}

		public void toggle() {
			_charPad.toggle();
		}

		@Override
		public void onClick(ClickEvent ev) {
			_owner.hide();
		}
	}
	
	private class CharPad extends DeckPanel implements MouseOverHandler, MouseOutHandler, ClickHandler, ThemeChangeHandler {
		private boolean _letterShown; 
		private CharPickerPopup _owner;
		private LetterPad _letterPad;
		private DigitPad _digitPad;
		
		public CharPad(CharPickerPopup owner) {
			_letterShown = true;
			_owner = owner;
			
			_letterPad = new LetterPad(this); 
			add(_letterPad);
			
			_digitPad = new DigitPad(this); 
			add(_digitPad);
			
			AbstractTheme theme = AbstractTheme.getTheme();
			theme.addThemeChangeHandler(this);
			refreshPad(); 
		}	
		
		public void setEnabled(boolean enabled) {
			_letterPad.setEnabled(enabled);
			_digitPad.setEnabled(enabled);
		}

		public void init() {
			_letterShown = true;
			refreshPad(); 
		}

		public void toggle() {		
			_letterShown = ! _letterShown;
			refreshPad(); 
		}

		private void refreshPad() {
			showWidget(_letterShown ? 0 : 1); 
			int nbChars = _letterShown ? 6 : 5;
			String width = (nbChars * (CharButton.BUTTON_WIDTH_IN_PX+6)) + "px"; 
			setWidth(width);
		}
		
		public void addChangeHandler(ChangeHandler handler) {
			handlers.add(handler);
		}
		private List<ChangeHandler> handlers = new ArrayList<ChangeHandler>(); 

		@Override
		public void onMouseOver(MouseOverEvent ev) {
			Widget src = (Widget)ev.getSource(); 
			DOM.setStyleAttribute(src.getElement(), "borderColor", "#000000");
		}

		@Override
		public void onMouseOut(MouseOutEvent ev) {
			Widget src = (Widget)ev.getSource(); 
			DOM.setStyleAttribute(src.getElement(), "borderColor", "#ffffff");
		}
		
		@Override
		public void onThemeChange(ThemeChangeEvent event) {
			_letterPad.changeGradient();
			_digitPad.changeGradient();
		}

		@Override
		public void onClick(ClickEvent ev) {
			Object src = ev.getSource();
			
			if (src instanceof CharButton) {
				CharButton btn = (CharButton)src;
				char ch = btn.getChar();
				
				for (ChangeHandler handler : handlers) {
					ChangeEvent newEvent = new CharChangeEvent(ch); 
					handler.onChange(newEvent); 
				}
			}
		}
	} //end CharPad()
	
	private abstract class AbstractCharacterPad extends GridPanel {
		protected List<CharButton> _charButtons = new ArrayList<CharButton>(); 
		
		public void changeGradient() {
			for (CharButton btn : _charButtons) {
				btn.changeGradient();
			}
		}

		public void setEnabled(boolean enabled) {
			for (CharButton btn : _charButtons) {
				btn.setEnabled(enabled); 
			}
		}
	} //end AbstractCharacterPad
	
	//
	// The alphabetic pad
	//
	private class LetterPad extends AbstractCharacterPad {
		private static final String VALID_LETTERS = "ABCEGHJKLMNPRSTVXY"; //Only these letters in Canadian Postal Code
		
		LetterPad(ClickHandler handler) {
			int row=0, col; 
			
			for (int i=0; i<18; i++) {
				char ch = VALID_LETTERS.charAt(i); 
				CharButton btn = new CharButton(ch); 
				btn.addClickHandler(handler);
				_charButtons.add(btn);
				
				row = i / 6;
				col = i % 6;
				_grid.setWidget(row, col, btn);
				_grid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			}
			
			col = 5; row++;
			CharButton clearBtn = new CharButton(CharButton.CLEAR); 
			clearBtn.addClickHandler(handler);
			_grid.setWidget(row, col, clearBtn);
			_grid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			row++; 
		}
	}
	
	//
	// The numeric pad
	//
	private class DigitPad extends AbstractCharacterPad {
		private List<CharButton> _charButtons = new ArrayList<CharButton>(); 
		
		DigitPad(ClickHandler handler) {
			int row=0, col; 
			
			for (int i=0; i<10; i++) {
				char ch = (char)(48+i);
				CharButton btn = new CharButton(ch); 
				btn.addClickHandler(handler);
				_charButtons.add(btn);
				
				row = i / 5;
				col = i % 5;
				_grid.setWidget(row, col, btn);
				_grid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			}
			
			col = 4; row++;
			CharButton clearBtn = new CharButton(CharButton.CLEAR); 
			clearBtn.addClickHandler(handler);
			_grid.setWidget(row, col, clearBtn);
			_grid.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			row++; 
		}
	}
	
	private static class CharChangeEvent extends ChangeEvent {
		private char _char;
		
		CharChangeEvent(char ch) {
			_char = ch;
		}
		
		public char getChar() {
			return _char;
		}
	}
	
	//
	// A gradient button for letters and digits
	//
	public static class CharButton extends GradientButton {
		public static final char CLEAR = (char)-1;
		public static final int BUTTON_WIDTH_IN_PX = 48;
		
		private static final Color DEFAULT_TOP_GRADIENT = Color.WHITE;
		private char _char;		
		
		public CharButton(char ch) {
			super(Character.toString(ch), DEFAULT_TOP_GRADIENT.toString(), DEFAULT_TOP_GRADIENT.toString()); 
			setText(ch); 
			_char = ch;
			setSize(BUTTON_WIDTH_IN_PX + "px", "40px");
			changeGradient();
		}

		private void setText(char ch) {
			_char = ch;
			Label label = super.getLabel();
			String text = (ch == CLEAR) ? "CLR" : Character.toString(ch); 
			int fontSize = (ch == CLEAR) ? 120 : 160; 
			label.getElement().getStyle().setFontSize(fontSize, Unit.PCT);
			super.setText(text);
		}

		public char getChar() {
			return _char;
		}

		public void changeGradient() {
			super.refresh();
			
			AbstractTheme theme = AbstractTheme.getTheme();
			Color primaryColor = theme.getPrimaryFgColor();
			
			//fallback if gradient not supported by browser
			Color bgColor = primaryColor.blendWith(Color.WHITE).setSaturation(50);
			getElement().getStyle().setBackgroundColor(bgColor.toString());
			
			super.setGradientStartColor(DEFAULT_TOP_GRADIENT.toString());
			super.setGradientEndColor(bgColor.toString());
			super.setOrientation(270);
		}
	} //end CharButton



}
