package org.mslab.tool.educ.shared.types.educ;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.mslab.tool.educ.shared.text.MessageFormat;
import org.mslab.tool.educ.shared.types.Address;
import org.mslab.tool.educ.shared.types.PhoneNumber;
import org.mslab.tool.educ.shared.types.RegionAdministrative;
import org.mslab.tool.educ.shared.types.educ.Person.Gender;

@SuppressWarnings("serial")
public abstract class Organization implements Serializable, Comparable<Organization> {
	private String _code; 
	private String _name; 
	private Address _address;
	private RegionAdministrative _regionAdmin;
	
	//required by GWT
	protected Organization() {}

	public Organization(String code, OrganizationName name, Address address, RegionAdministrative regionAdmin) {
		_code = code;
		_name = name.toDisplayString();
		_address = address;
		_regionAdmin = regionAdmin;
	}
	
	public String getCode() {
		return _code;
	}
	
	@Override
	public int compareTo(Organization arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public String toString() {
		String msg = MessageFormat.format("{0} ({1})", new Object[] {_code, _name});
		return msg;
	}

	public String getName() {
		return _name;
	}
	
	public Address getAddress() {
		return _address;
	}
	
	public RegionAdministrative getRegionAdministrative() {
		return _regionAdmin;
	}
	
	//phone numbers
	private List<PhoneNumber> _phoneNumbers = new ArrayList<PhoneNumber>();
	public void addPhoneNumber(PhoneNumber phoneNumber) {_phoneNumbers.add(phoneNumber);}
	public  List<PhoneNumber> getPhoneNumbers() { return _phoneNumbers; }
	
	public PhoneNumber getPhoneNumber(PhoneNumber.Category category) {
		PhoneNumber phoneNumber = null;
		
		for (PhoneNumber phone : _phoneNumbers) {
			if (phone.getCategory() == category) {
				phoneNumber = phone;
				break;
			}
		}
		
		return phoneNumber;
	}
	
	
	public String getEmail() { return _email; }
	public void setEmail(String email) { _email = email; }
	private String _email;
	
	public String getWeb() { return _web; }
	public void setWeb(String web) { _web = web; }
	private String _web;

	public boolean isFrench() { return _french; }
	public void setFrench(boolean french) { _french = french; }
	private boolean _french;
	
	public boolean isEnglish() { return _english; }
	public void setEnglish(boolean english) { _english = english; }
	private boolean _english;

	public boolean isPublic() { return _isPublic; }
	public void setPublic(boolean isPublic) { _isPublic = isPublic; }
	private boolean _isPublic;
	
	private Person _director;
	public Person getDirector() { return _director; }
	public void setDirector(Gender gender, String givenName, String familyName, String title) {
		_director = new Person(gender, givenName, familyName, title, this);
	}

	private String _circonscription;
	public void setCirconscription(String circonscription) { _circonscription = circonscription; }
	public String getCirconscription() { return _circonscription; }


	
}
