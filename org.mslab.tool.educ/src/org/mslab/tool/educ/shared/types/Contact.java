package org.mslab.tool.educ.shared.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.BorderStyle;

public class Contact implements Serializable {
	
	//required by GWT
	private Contact() {}
	
	public Contact(boolean isMale, String givenName, String familyName, Address address) {
		_isMale = isMale;
		_givenName = givenName;
		_familyName = familyName;
		_address = address;
	}
	
	private boolean _isMale;
	public boolean isMale() {return _isMale; }
	public void setGender(boolean isMale) {_isMale = isMale; }
	
	private String _givenName;
	public String getGivenName() {return _givenName; }
	public void setGivenName(String givenName) {_givenName = givenName; }
	
	private String _familyName;
	public String getFamilyName() {return _familyName; }
	public void setFamilyName(String familyName) {_familyName = familyName; }
	
	private Address _address;
	public Address getAddress() {return _address; }
	public void setAddress(Address address) {_address = address; }
	
	private List<PhoneNumber> _phoneNumbers = new ArrayList<PhoneNumber>();
	public List<PhoneNumber> getPhoneNumbers() {return _phoneNumbers; }
	public void addPhoneNumber(PhoneNumber phoneNumber) { _phoneNumbers.add(phoneNumber); }
	public PhoneNumber getPhoneNumber(int i) { 
		PhoneNumber phone = i < _phoneNumbers.size() ?  _phoneNumbers.get(i) : null;
		return phone; 
	}

	public String getFullName() { return _givenName + " " + _familyName; }
	
	@Override
	public String toString() {return getFullName(); }

	public void removePhoneNumbers() {
		_phoneNumbers.clear();
	}



	

	



}
