package org.mslab.commons.client.tool.educ.people.viewer.content;

import org.mslab.commons.client.tool.educ.settings.pref.Preferences;
import org.mslab.commons.shared.text.MessageFormat;
import org.mslab.commons.shared.text.Text;
import org.mslab.commons.shared.types.PhoneNumber;
import org.mslab.commons.shared.types.PhoneNumber.Category;
import org.mslab.commons.shared.types.educ.Organization;
import org.mslab.commons.shared.types.educ.Person;

public class PersonRenderer {
	private Person _person; 
	public enum Field { FULL_NAME, GIVEN_NAME, FAMILY_NAME, TITLE, SCHOOL, PHONE, EMAIL};  

	public PersonRenderer(Person person) {
		_person = person;
	}

	public String render(Field field) {
		String rendered = null;
		
		if (field == Field.FULL_NAME) {
			String nameFormat = Preferences.getInstance().getNameFormat();
			rendered = MessageFormat.format(nameFormat, _person.getGivenName(), _person.getFamilyName());
		} else if (field == Field.GIVEN_NAME) {
			rendered =  _person.getGivenName();
		} else if (field == Field.FAMILY_NAME) {
			rendered =  _person.getFamilyName();
		} else if (field == Field.TITLE) {
			Organization organization = _person.getOrganization();
			rendered = MessageFormat.format("{0},&nbsp;", _person.getTitle());
		} else if (field == Field.SCHOOL) {
			Organization organization = _person.getOrganization();
			rendered =  organization.getName();
		} else if (field == Field.PHONE) {
			Organization organization = _person.getOrganization();
			PhoneNumber phoneNumber = organization.getPhoneNumber(Category.OFFICE);
			String phoneFormat = Preferences.getInstance().getPhoneFormat();
			String phone = MessageFormat.format(phoneFormat,
					phoneNumber.getAreaCode(), phoneNumber.getNumberStart(), phoneNumber.getNumberEnd()); 
			rendered = "<i class=\"fa fa-phone\">" + " " + phone;		
		} else if (field == Field.EMAIL) {
			Organization organization = _person.getOrganization();
			String email = organization.getEmail();
			rendered = "<i class=\"fa fa-envelope-o\">" + " " + email;
		} else {
			rendered = null;
		}
		
		return rendered;
	} //end render()

	public boolean filter(Field field, String filterText) {
		int idx = indexOf(field, filterText);
		boolean accepted = (idx >= 0); 
		return accepted;
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
}
