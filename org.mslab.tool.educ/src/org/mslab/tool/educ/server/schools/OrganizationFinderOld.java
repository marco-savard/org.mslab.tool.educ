package org.mslab.tool.educ.server.schools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.mslab.tool.educ.shared.text.StringExt;
import org.mslab.tool.educ.shared.types.Address;
import org.mslab.tool.educ.shared.types.GeoLocation;
import org.mslab.tool.educ.shared.types.PhoneNumber;
import org.mslab.tool.educ.shared.types.PostalCode;
import org.mslab.tool.educ.shared.types.RegionAdministrative;
import org.mslab.tool.educ.shared.types.educ.Organization;
import org.mslab.tool.educ.shared.types.educ.Person;
import org.mslab.tool.educ.shared.types.educ.Person.Gender;

public class OrganizationFinderOld {
	
	public static void main(String[] args) {
		OrganizationFinderOld finder = new OrganizationFinderOld(); 
		List<Organization> organizations = finder.getListOrganizations();
		for (Organization org : organizations) {
			System.out.println(org.toString()); 
		}
	}

	public List<Organization> getListOrganizations() {
		List<Organization> organizations; 
	
		String resname = "RechercheOrganisme2014_11_25_10h49_15.csv"; 
		URL url = OrganizationFinderOld.class.getResource(resname); 
		try {
			InputStream input = url.openStream();
			organizations = findInResource(input); 
			input.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			organizations = null;
		}
		
		return organizations;
	}

	private List<Organization> findInResource(InputStream input) throws IOException {
		List<Organization> organizations = new ArrayList<Organization>(); 
		
		//read resources
		//PostalCodeFinder postalCodeFinder = new PostalCodeFinder();
		
		InputStreamReader ir = new InputStreamReader(input, "iso-8859-1"); 
		BufferedReader br = new BufferedReader(ir); 
		int bestMatch = 0;
		
		br.readLine(); //skip header
		
		while (true) {
			String line = br.readLine();

			if (line == null) {
				break;
			} else {
				Organization organization = findInResourceLine(line); 
				
				if (organization != null) {
					organizations.add(organization); 
				}
			}
			
			if (bestMatch == 6) {
				break;
			}
		}
		
		return organizations;
	}

	private Organization findInResourceLine(String line) {
		String[] fields = line.split(";"); 	
		String code = trimQuotes(fields[0]);
		String name = trimQuotes(fields[1]); 
		String codeResponsable = trimQuotes(fields[3]); 
		
		String email = trimQuotes(fields[5]); 
		String web = trimQuotes(fields[6]); 
		String ordreApp = trimQuotes(fields[9]); 
		String reseau = trimQuotes(fields[10]);
		String typeOrganisme = trimQuotes(fields[11]); 
		
		String phoneNumberText = trimQuotes(fields[12]); 
		String phoneExt = trimQuotes(fields[13]); 
		String faxText = trimQuotes(fields[14]); 
		
		String directorGivenName = trimQuotes(fields[30]); 
		String directorFamilyName = trimQuotes(fields[29]); 
		String directorGender = trimQuotes(fields[28]); 
		String directorTitle = trimQuotes(fields[26]); 
		String directorFunction = trimQuotes(fields[25]); 
		String circonscription = trimQuotes(fields[23]); 
		
		String french = trimQuotes(fields[38]); 
		String english = trimQuotes(fields[39]); 
		String other = trimQuotes(fields[40]); 
		
		String numberAndStreet = trimQuotes(fields[15]); 
		Address address = null; 
		int idx = (numberAndStreet.length() <= 2) ? -1 : numberAndStreet.indexOf(','); 
		
		if (idx > 1) {
			String civicNumber = numberAndStreet.substring(0, idx);
			String street = numberAndStreet.substring(idx+2);
			String city = trimQuotes(fields[16]);
			PostalCode postalCode = new PostalCode(trimQuotes(fields[18]));
			GeoLocation location = new GeoLocation(postalCode, city);
			
			address = new Address(civicNumber, street, (String)null, location, postalCode);
		}
		 
		
		String regionAdmin = trimQuotes(fields[8]);
		
		
		RegionAdministrative region = new RegionAdministrative(regionAdmin);
		Organization organization = null;
		
		if ("Cégep".equals(typeOrganisme)) {
			//organization = new School(code, name, codeResponsable, address, region, ordreApp); 
		}
		
		if ("Commission scolaire".equals(typeOrganisme)) {
			//organization = new SchoolBoard(code, name, address, region); 
		}
		
		if ("École".equals(typeOrganisme)) {
			//organization = new School(code, name, codeResponsable, address, region, ordreApp); 
		}
		
		if ("Installation".equals(typeOrganisme)) {
			//organization = new School(code, name, codeResponsable, address, region, ordreApp); 
		}
		
		if ("Université".equals(typeOrganisme)) {
			//organization = new School(code, name, codeResponsable, address, region, ordreApp); 
		}
		
		if (organization != null) {
			if (! StringExt.isNullOrWhitespace(phoneNumberText)) {
				//String areaCode = phoneNumberText.substring(1, 4);
				//String number = phoneNumberText.substring(6); 
				PhoneNumber phoneNumber = new PhoneNumber(PhoneNumber.Category.OFFICE, phoneNumberText);
				organization.addPhoneNumber(phoneNumber);
			}
			
			if (! StringExt.isNullOrWhitespace(faxText)) {
				String areaCode = faxText.substring(1, 4);
				String number = faxText.substring(6); 
				PhoneNumber fax = new PhoneNumber(PhoneNumber.Category.FAX, faxText);
				organization.addPhoneNumber(fax);
			}

			
			if (! StringExt.isNullOrWhitespace(directorFamilyName)) {				
				Person.Gender gender = findGender(directorGender, directorGivenName); 
				String title = StringExt.isNullOrWhitespace(directorTitle) ? directorFunction : directorTitle;
				organization.setDirector(gender, directorGivenName, directorFamilyName, title);
			}
			
			organization.setEmail(email);
			organization.setWeb(web);
			organization.setCirconscription(circonscription); 
			organization.setPublic("Public".equals(reseau)); 
			organization.setFrench("Oui".equals(french)); 
			organization.setEnglish("Oui".equals(english)); 
		}
		
		return organization;
	}

	private Gender findGender(String directorGender, String directorGivenName) {
		Gender gender; 
		
		if ("M.".equals(directorGender)) {
			gender = Gender.MALE; 
		} else if ("Mme.".equals(directorGender)) {
			gender = Gender.FEMALE; 
		} else {
			gender = Gender.UNKNOWN; 
		}
		
		//char finalCh = directorGivenName.charAt(directorGivenName.length() - 1); 
		//Gender gender = (finalCh == 'e') || (finalCh == 'a') ? 
		//	Gender.FEMALE : 
		//	Gender.MALE;
		return gender;
	}

	private String trimQuotes(String original) {
		String trimmed = original.substring(1, original.length()-1); 
		return trimmed;
	}

}
