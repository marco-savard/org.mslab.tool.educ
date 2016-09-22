package org.mslab.tool.educ.shared.types.educ;

import java.io.Serializable;

import org.mslab.tool.educ.shared.text.StringExt;
import org.mslab.tool.educ.shared.text.Text;

@SuppressWarnings("serial")
public class Person implements Serializable {
	public enum Gender {MALE, FEMALE, UNKNOWN}
	
	//required by GWT
	@SuppressWarnings("unused")
	private Person() {}
	
	public Person(Gender gender, String givenName, String familyName, String title, Organization organization) {
		_gender = gender;
		_givenName = Text.capitalizeWords(givenName.toLowerCase());
		_familyName = Text.capitalizeWords(familyName.toLowerCase());
		_title = title;
		_organization = organization;
	}
	
	private Gender _gender;
	public Gender getGender() {return _gender; }
	
	private String _givenName;
	public String getGivenName() {return _givenName; }
	public void setGivenName(String givenName) {_givenName = givenName; }
	
	private String _familyName;
	public String getFamilyName() {return _familyName; }
	public void setFamilyName(String familyName) {_familyName = familyName; }

	private String _title;
	public String getTitle() {	return _title; }
	
	private Organization _organization;
	public Organization getOrganization() {	return _organization; }
	
	public String getFullName() { 
		String fullName = (StringExt.isNullOrWhitespace(_givenName) && StringExt.isNullOrWhitespace(_givenName)) ? 
			null : 
		   _givenName + " " + _familyName;	
		return fullName; 
	}
	
	@Override
	public String toString() {return getFullName(); }




}
