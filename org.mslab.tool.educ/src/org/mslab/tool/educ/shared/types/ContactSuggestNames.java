package org.mslab.tool.educ.shared.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ContactSuggestNames implements Serializable {
	
	private List<String> _suggestMaleGivenNames = new ArrayList<String>();
	public List<String> getMaleGivenNames() {return _suggestMaleGivenNames; }
	public void setMaleGivenNames(List<String> givenNames) {_suggestMaleGivenNames = givenNames; }
	
	private List<String> _suggestFemaleGivenNames = new ArrayList<String>();
	public List<String> getFemaleGivenNames() {return _suggestFemaleGivenNames; }
	public void setFemaleGivenNames(List<String> givenNames) {_suggestFemaleGivenNames = givenNames; }

	private List<String> _suggestFamilyNames = new ArrayList<String>();
	public List<String> getFamilyNames() {return _suggestFamilyNames; }
	public void setFamilyNames(List<String> familyNames) {_suggestFamilyNames = familyNames; }
}
