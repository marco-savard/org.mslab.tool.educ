package org.mslab.commons.client.tool.educ.school.viewer.content;

import org.mslab.commons.client.tool.educ.EducContext;
import org.mslab.commons.client.tool.educ.settings.pref.Preferences;
import org.mslab.commons.client.tool.snippets.SnippetContext;
import org.mslab.commons.shared.text.MessageFormat;
import org.mslab.commons.shared.text.StringExt;
import org.mslab.commons.shared.text.Text;
import org.mslab.commons.shared.types.Address;
import org.mslab.commons.shared.types.CityOld;
import org.mslab.commons.shared.types.GeoLocation;
import org.mslab.commons.shared.types.PhoneNumber;
import org.mslab.commons.shared.types.PostalCode;
import org.mslab.commons.shared.types.educ.City;
import org.mslab.commons.shared.types.educ.Organization;
import org.mslab.commons.shared.types.educ.Person;

import com.google.gwt.i18n.client.NumberFormat;

public class OrganizationRenderer {
	private Organization _organization;
	public enum Field { NAME, 
		ADDRESS_LINE_1, ADDRESS_LINE_2, POSTAL_CODE, DISTANCE, 
		PHONE_NUMBER_EXT, FAX, EMAIL, WEB,
		DIRECTION, CIRCONSCRIPTION};
	
	OrganizationRenderer(Organization organization) {
		_organization = organization;
	}

	public int indexOf(Field field, String filterText) {
		String rendered = render(field);
		int idx = -1; 
		
		if (rendered != null && filterText != null) {
			filterText = Text.toLowerCase(filterText).toUnaccentued().toLowerCase().toString();
			String plainText = Text.removeTags(rendered).toLowerCase().toUnaccentued().toString();
			idx = plainText.indexOf(filterText); //search in plain tags, outside tags
			if (idx != -1) {
				String lowercase = Text.toLowerCase(rendered).toUnaccentued().toString();
				int fromIndex = 0;
				idx = lowercase.indexOf(filterText, fromIndex); //return the actual index 
				while (isWithinTags(lowercase, idx)) {
					fromIndex = 1+idx;
					idx = lowercase.indexOf(filterText, fromIndex); //return the actual index 
				}
			}
		}
		
		return idx;
	}

	private boolean isWithinTags(String lowercase, int idx) {
		boolean withinTags = false;
		
		if (idx > 1) {
			lowercase = lowercase.substring(0, idx-1);
			int nbOpen = Text.count(lowercase, '<'); 
			int nbClose = Text.count(lowercase, '>'); 
			withinTags = nbOpen > nbClose; 
		}
		
		return withinTags;
	}

	String render(Field field) {
		String rendered = null;
		Address address = _organization.getAddress();
		GeoLocation location = (address == null) ? null : address.getLocation();
		GeoLocation clientLocation =  EducContext.getInstance().getClientLocation(); 
		City city = (location == null) ? null : location.getCity();
		String provCode = (location == null) ? null : location.getProvinceCode();
		String provName = (location == null) ? null : location.getProvinceName();
		PostalCode postalCode = (address == null) ? null : address.getPostalCode();
		
		if (field == Field.NAME) {
			rendered = _organization.getName();
		} else if (field == Field.ADDRESS_LINE_1) {
			rendered = (address == null) ? null : MessageFormat.format("{0}, {1}", new Object[] {address.getCivicNumber(), address.getStreet()});
		} else if (field == Field.ADDRESS_LINE_2) {
			String cityFormat = Preferences.getInstance().getCityFormat();
			rendered = (city == null) ? null : MessageFormat.format(cityFormat, 
				new Object[] {city.getName(), provName, provCode, postalCode.toDisplayString()});
		} else if (field == Field.POSTAL_CODE) {
			rendered = (postalCode == null) ? null : MessageFormat.format("{0}", new Object[] {postalCode.toDisplayString()});
		} else if (field == Field.DISTANCE) {
			double distance = location.computeDistanceFrom(clientLocation);
			String text = NumberFormat.getFormat("0.00").format(distance); 
			rendered = (postalCode == null) ? null : MessageFormat.format("Distance: {0} km", new Object[] {text});
		} else if (field == Field.PHONE_NUMBER_EXT) {
			PhoneNumber office = _organization.getPhoneNumber(PhoneNumber.Category.OFFICE); 
			
			if (office != null) {
				String phoneArea = office.getAreaCode();
				String phoneNumberStart = office.getNumberStart();
				String phoneNumberEnd = office.getNumberEnd();
				String phoneExt = office.getExtension();
				String phoneFormat = Preferences.getInstance().getPhoneFormat();
				String phone = MessageFormat.format(phoneFormat,
						phoneArea, phoneNumberStart, phoneNumberEnd); 
				rendered = "<i class=\"fa fa-phone\">" + " " + phone;	
				
		}
			
		} else if (field == Field.FAX) {
			PhoneNumber fax = _organization.getPhoneNumber(PhoneNumber.Category.FAX); 
			
			if (fax != null) {
				String phoneArea = fax.getAreaCode();
				String phoneNumberStart = fax.getNumberStart();
				String phoneNumberEnd = fax.getNumberEnd();
				
				String phoneFormat = Preferences.getInstance().getPhoneFormat();
				String phone = MessageFormat.format(phoneFormat,
						phoneArea, phoneNumberStart, phoneNumberEnd); 
				rendered = "<i class=\"fa fa-fax\">" + " " + phone;	
				
			}
		} else if (field == Field.EMAIL) {
			String email = _organization.getEmail();
			rendered = StringExt.isNullOrWhitespace(email) ? null : MessageFormat.format("<i class=\"fa fa-at\"></i> {0}", new Object[] {email});
		} else if (field == Field.WEB) {
			String web = _organization.getWeb();
			rendered = StringExt.isNullOrWhitespace(web) ? null : MessageFormat.format("<i class=\"fa fa-globe \"></i> {0}", new Object[] {web});
		} else if (field == Field.DIRECTION) {
			Person director = _organization.getDirector();
			if (director != null) {
				String givenName = director.getGivenName();
				String familyName = director.getFamilyName();
				String title = director.getTitle();
				title = (title.length() > 30) ? title.substring(0, 30) + ".." : title;
				title = (StringExt.isNullOrWhitespace(title) ? "Directeur" : title); 
				
				String nameFormat = Preferences.getInstance().getNameFormat();
				String fullName = MessageFormat.format(nameFormat, givenName,familyName);
				
				rendered = MessageFormat.format("{0} : {1}", new Object[] {title, fullName}); 
			}
			
		} else if (field == Field.CIRCONSCRIPTION) {
			String circonscription = _organization.getCirconscription();
			rendered = MessageFormat.format("Circonscription : {0}", new Object[] {circonscription}); 
		}
		
		return rendered;
	}
}
