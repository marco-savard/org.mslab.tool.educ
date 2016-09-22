package org.mslab.tool.educ.client.core.ui;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBox;

public class NumberTextBox extends TextBox implements KeyUpHandler {
	private static final String INTEGER_REGEX = "[-+]?[0-9]*"; 
	private static final String FLOAT_REGEX = "[-+]?[0-9]*\\.?[0-9]+"; 
	
	private String _integerText = "";
	private String _regex;
	private Class<?> _numberClass;
	
	public NumberTextBox(Class<?> numberClass) {
		addKeyUpHandler(this);
		_numberClass = numberClass;
		
		if (int.class.equals(numberClass)) {
			_regex = INTEGER_REGEX;
		} else if (short.class.equals(numberClass)) {
			_regex = INTEGER_REGEX;
		} else if (long.class.equals(numberClass)) {
			_regex = INTEGER_REGEX;
		} else if (float.class.equals(numberClass)) {
			_regex = FLOAT_REGEX;
		} else if (double.class.equals(numberClass)) {
			_regex = FLOAT_REGEX;
		} else {
			String text = numberClass + " : not supported";
			throw new UnsupportedOperationException(text);
		}
	}
	
	public void setValue(Number number) {
		String text = (number == null) ? "0" : number.toString();
		setText(text);
	}
	
	public Number getNumberValue() {
		String literal = getValue();
		Number number = null;
		
		if (int.class.equals(_numberClass)) {
			number = Integer.valueOf(literal);
		} else if (short.class.equals(_numberClass)) {
			number = Short.valueOf(literal);
		} else if (long.class.equals(_numberClass)) {
			number = Long.valueOf(literal);
		} else if (float.class.equals(_numberClass)) {
			number = Float.valueOf(literal);
		} else if (double.class.equals(_numberClass)) {
			number = Double.valueOf(literal);
		}
		
		return number;
	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		String text = getText();
		boolean isInteger = text.matches(_regex); 
		
		if (isInteger) {
			_integerText = text;
		} else {
			setText(_integerText); 
		}
	} //end onKeyUp()



}
