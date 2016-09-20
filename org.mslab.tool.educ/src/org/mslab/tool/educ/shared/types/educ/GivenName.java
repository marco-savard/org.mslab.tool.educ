package org.mslab.commons.shared.types.educ;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class GivenName implements Serializable {
	private String _name;
	
	//GWT required
	@SuppressWarnings("unused")
	private GivenName() {}

	public GivenName(String text) {
		if (_givenNameList == null) {
			_givenNameList = Arrays.asList(GIVEN_NAMES); 
		}
		
		int idx = _givenNameList.indexOf(text);
		_name = (idx == -1) ? text : Integer.toString(idx); 
	}
	
	public static GivenName fromString(String value) {
		GivenName name = new GivenName(); 
		name._name = value;
		return name;
	} 
	
	@Override
	public String toString() {
		return _name;
	}

	public String toDisplayString() {
		String displayString; 
		
		try {
			int idx = Integer.parseInt(_name); 
			displayString = GIVEN_NAMES[idx]; 
		} catch (NumberFormatException ex) {
			displayString = _name;
		}
		
		return displayString;
	}
	
	private static List<String> _givenNameList = null;
	private static final String[] GIVEN_NAMES = new String[] {
		//most common
		"Alain",
		"André", 
		"Annie", 
		"Brigitte", 
		"Caroline",
		"Chantal", 
		"Christine", 
		"Daniel", 
		"Denis", 
		"Diane", 
		"Éric", 
		"France", 
		"François", 
		"Geneviève",
		"Hélène", 
		"Isabelle", 
		"Johanne", 
		"Josée", 
		"Julie", 
		"Louise", 
		"Manon", 
		"Marie-Claude", 
		"Marie-Josée", 
		"Martin", 
		"Martine", 
		"Michel", 
		"Nancy", 
		"Nathalie", 
		"Patrick", 
		"Pierre", 
		"Richard", 
		"Serge", 
		"Sophie", 
		"Stéphane", 
		"Sylvain", 
		"Sylvie", 
		"Vincent", 
		"Yves",
	
	}; 
}
