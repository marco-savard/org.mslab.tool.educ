package org.mslab.tool.educ.client.tool.educ.people.explorer;

import java.util.List;

import org.mslab.tool.educ.client.tool.educ.school.explorer.AbstractFilterCategory;
import org.mslab.tool.educ.shared.text.MessageFormat;
import org.mslab.tool.educ.shared.text.StringExt;
import org.mslab.tool.educ.shared.types.PhoneNumber;
import org.mslab.tool.educ.shared.types.PhoneNumber.Category;
import org.mslab.tool.educ.shared.types.educ.Organization;
import org.mslab.tool.educ.shared.types.educ.Person;
import org.mslab.tool.educ.shared.types.educ.Person.Gender;

public class PersonFilterCategory extends AbstractFilterCategory {
	//
	// gender
	//
	public static class GenderCategorizer extends AbstractCategorizer<Person> {
		public GenderCategorizer() {
			super("Sexe");
		}
		
		@Override
		public String categorize(Person person, List<Person> directory) {
			Person.Gender gender = person.getGender(); 
			String text = (gender == Gender.MALE) ? "Homme" : "Femme"; 
			return text;
		}
	}
	
	//
	// titre
	//
	public static class TitleCategorizer extends AbstractCategorizer<Person> {
		public TitleCategorizer() {
			super("Titre");
		}
		
		@Override
		public String categorize(Person person, List<Person> directory) {
			String title = person.getTitle(); 
			return title;
		}
	}
	
	//
	// family name
	//
	public static class FamilyNameCategorizer extends AbstractCategorizer<Person> {
		public FamilyNameCategorizer() {
			super("Nom de famille");
		}
		
		@Override
		public String categorize(Person person, List<Person> directory) {
			String familyName = person.getFamilyName(); 
			return familyName;
		}
	}
	
	//
	// given name
	//
	public static class GivenNameCategorizer extends AbstractCategorizer<Person> {
		public GivenNameCategorizer() {
			super("Prénom");
		}
		
		@Override
		public String categorize(Person person, List<Person> directory) {
			String familyName = person.getGivenName();
			return familyName;
		}
	}
	
	//
	// langue
	//
	public static class LanguageCategorizer extends AbstractCategorizer<Person> {
		public LanguageCategorizer() {
			super("Langue");
		}
		
		@Override
		public String categorize(Person person, List<Person> directory) {
			Organization org = person.getOrganization(); 
			String langue = org.isFrench() ? "Français" : 
				(org.isEnglish() ? "Anglais" : "Autre"); 
			return langue;
		}
	}
	
	//
	// code regional
	//
	public static class AreaCodeCategorizer extends AbstractCategorizer<Person> {
		public AreaCodeCategorizer() {
			super("Code régional");
		}
		
		@Override
		public String categorize(Person person, List<Person> directory) {
			Organization org = person.getOrganization(); 
			PhoneNumber phone = org.getPhoneNumber(Category.OFFICE); 
			String areaCode = MessageFormat.format("Code {0}", phone.getAreaCode()); 
			return areaCode;
		}
	}
	
	//
	// email domain
	//
	public static class EmailDomainCategorizer extends AbstractCategorizer<Person> {
		public EmailDomainCategorizer() {
			super("Domaine de courriel");
		}
		
		@Override
		public String categorize(Person person, List<Person> directory) {
			String domain = null;
			Organization org = person.getOrganization(); 
			String email = org.getEmail(); 
			if (! StringExt.isNullOrWhitespace(email)) {
				int idx = email.indexOf('@'); 
				domain = email.substring(idx); 
			}
		
			return domain;
		}
	}
}
