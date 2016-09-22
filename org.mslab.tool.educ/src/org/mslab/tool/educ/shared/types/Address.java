package org.mslab.tool.educ.shared.types;

import java.io.Serializable;

public class Address implements Serializable {

	//required by GWT
	private Address() {}
	
	public Address(String civicNumber, String street, String app, 
			GeoLocation location, PostalCode postalCode) {
		_civicNumber = civicNumber;
		_street = street;
		_location = location;
		_app = app;
		_postalCode = postalCode;
	}
	
	private String _civicNumber;
	public void setCivicNumber(String civicNumber) { _civicNumber = civicNumber; }
	public String getCivicNumber() { return _civicNumber; }
	
	private String _street;
	public void setStreet(String street) { _street = street; }
	public String getStreet() { return _street; }
	
	private String _app;
	public void setAppartment(String app) { _app = app; }
	public String getAppartment() { return _app; }

	private GeoLocation _location;
	public void setLocation(GeoLocation location) { _location = location; }
	public GeoLocation getLocation() { return _location; }
	
	private String _province;
	public void setProvince(String province) { _province = province; }
	public String getProvince() { return _province; }
	
	private PostalCode _postalCode;
	public void setPostalCode(PostalCode postalCode) { _postalCode = postalCode; }
	public PostalCode getPostalCode() { return _postalCode; }

	public String getRegion() {
		return _postalCode.getRegion();
	}

	


	

}
