package org.mslab.commons.client.tool.educ.people.viewer;

import org.mslab.commons.client.tool.educ.EducContext;
import org.mslab.commons.client.tool.educ.settings.pref.Preferences;
import org.mslab.commons.shared.text.MessageFormat;
import org.mslab.commons.shared.text.PhoneFormatter;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBox;

public class EducPhoneTextBox extends TextBox implements KeyUpHandler {
	
	public EducPhoneTextBox() {
		setWidth("7em");
		getElement().getStyle().setBorderStyle(BorderStyle.NONE);
		
		addKeyUpHandler(this);
	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		refresh();
	}

	private void refresh() {
		int pos = getCursorPos(); 		
		String original = getText(); 
		Preferences prefs = Preferences.getInstance();
		String phoneFormat = prefs.getPhoneFormat();
		String formatted = PhoneFormatter.formatPhoneNumber(original, phoneFormat); 
		setText(formatted);
		int delta = formatted.length() - original.length();
		setCursorPos(pos+delta);
	}

	

	
	


}
