package org.mslab.commons.shared.types.educ;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class OrganizationName implements Serializable {
	private String _name;

	//GWT required
	@SuppressWarnings("unused")
	private OrganizationName() {}
	
	public OrganizationName(String text) {
		_name = text.replace("Centre d'éducation des adultes", "CEdA"); 
		_name = text.replace("Centre d'éducation aux adultes", "CEaA"); 
		_name = text.replace("Centre d'éducation professionnelle", "CEP"); 
		_name = text.replace("Centre de formation professionnelle", "CPF"); 
		_name = _name.replace("Commission scolaire", "CS"); 
		_name = _name.replace("École primaire internationale", "EPI"); 
		_name = _name.replace("École primaire internationale", "EP"); 
		_name = _name.replace("École secondaire", "ES"); 
	}
	
	@Override
	public String toString() {
		return _name;
	}

	public String toDisplayString() {
		String s = _name; 
		s = s.replace("CEdA", "Centre d'éducation des adultes"); 
		s = s.replace("CEaA", "Centre d'éducation aux adultes"); 
		s = s.replace("CEP", "Centre d'éducation professionnelle"); 
		s = s.replace("CPF", "Centre de formation professionnelle"); 
		s = s.replace("CS", "Commission scolaire"); 
		s = s.replace("EPI", "École primaire internationale"); 
		s = s.replace("EP", "École primaire internationale"); 
		s = s.replace("ES", "École secondaire"); 
		return s;
	}

	public static OrganizationName fromString(String value) {
		OrganizationName name = new OrganizationName();
		name._name = value;
		return name;
	}
	
}
