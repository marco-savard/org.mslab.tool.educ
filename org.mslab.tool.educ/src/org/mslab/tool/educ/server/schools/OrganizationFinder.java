package org.mslab.tool.educ.server.schools;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mslab.tool.educ.shared.text.StringExt;
import org.mslab.tool.educ.shared.types.Address;
import org.mslab.tool.educ.shared.types.GeoLocation;
import org.mslab.tool.educ.shared.types.PhoneNumber;
import org.mslab.tool.educ.shared.types.PostalCode;
import org.mslab.tool.educ.shared.types.RegionAdministrative;
import org.mslab.tool.educ.shared.types.educ.Circonscription;
import org.mslab.tool.educ.shared.types.educ.FamilyName;
import org.mslab.tool.educ.shared.types.educ.GivenName;
import org.mslab.tool.educ.shared.types.educ.OrdreAppartenance;
import org.mslab.tool.educ.shared.types.educ.Organization;
import org.mslab.tool.educ.shared.types.educ.OrganizationName;
import org.mslab.tool.educ.shared.types.educ.Person;
import org.mslab.tool.educ.shared.types.educ.Person.Gender;
import org.mslab.tool.educ.shared.types.educ.School;
import org.mslab.tool.educ.shared.types.educ.SchoolBoard;

public class OrganizationFinder {
	
	public static void main(String[] args) {
		OrganizationFinder finder = new OrganizationFinder(); 
		OrganizationRepository repository = finder.getOrganizationRepository(); 
		List<Organization> organizations = repository._organizations;
		for (Organization org : organizations) {
			System.out.println(org.toString()); 
		}
	}

	public OrganizationRepository getOrganizationRepository() {
		OrganizationRepository repository = new OrganizationRepository(); 
	
		String resname = "Organismes.csv"; 
		URL url = OrganizationFinder.class.getResource(resname); 
		try {
			//read
			InputStream input = url.openStream();
			CsvReader reader = new CsvReader(input, "iso-8859-1");
			CsvStructure structure = reader.read(); 
			
			//build school boards
			OrganizationBuilder builder = new SchoolBoardBuilder(structure, repository);
			builder.processRows();
			
			//build school
			builder = new SchoolBuilder(structure, repository);
			builder.processRows();
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return repository;
	}

    /*
	private String trimQuotes(String original) {
		String trimmed = original.substring(1, original.length()-1); 
		return trimmed;
	} */
	
	public static class OrganizationRepository {
		public List<Organization> _organizations = new ArrayList<Organization>();
		Map<String, Organization> _organizationsMap = new HashMap<String, Organization>();
		
		public List<Organization> getOrganizations() {
			return _organizations;
		}
	}
	
	private static abstract class OrganizationBuilder extends CsvProcessor {
		protected OrganizationRepository _repository; 
		
		protected OrganizationBuilder(CsvStructure structure, OrganizationRepository repository) {
			super(structure);
			_repository = repository;
		}

		@Override
		public void processHeader() {
		}
		
		protected void addOrganization(Organization organization) {
			_repository._organizations.add(organization);
			_repository._organizationsMap.put(organization.getCode(), organization); 
		}
		
		protected Organization findOrganization(String codeResponsable) {
			Organization organization = _repository._organizationsMap.get(codeResponsable); 
			return organization;
		}
		
		
		
		protected Address getAddress(String[] fields) {
			//civic number and street
			String numberAndStreet = getValue(fields, "Numéro - nom de rue et casier postal de l'adresse postale");
			int idx = (numberAndStreet.length() <= 2) ? -1 : numberAndStreet.indexOf(','); 
			String civicNumber = (idx > 1) ? numberAndStreet.substring(0, idx) : "";
			String street = (idx > 1) ? numberAndStreet.substring(idx+2) : "";
			
			//postal code and city
			double lat = Double.parseDouble(getValue(fields, "Latitude"));
			double lon = Double.parseDouble(getValue(fields, "Longitude"));
			String postalCodeText = getValue(fields, "Code postal de l'adresse géographique"); 
			PostalCode postalCode = new PostalCode(postalCodeText);
			String city = getValue(fields, "Municipalité de l'adresse géographique"); 
			GeoLocation location = new GeoLocation(lat, lon, postalCode, city);
			Address address = new Address(civicNumber, street, (String)null, location, postalCode);
			return address;
		}
		
		protected void addPhoneEmail(String[] fields, Organization organization) {
			String phoneNumberText = getValue(fields, "Numéro de téléphone"); 
			String phoneExtensionText = getValue(fields, "Numéro de poste téléphone"); 
			String faxNumberText = getValue(fields, "Numéro de télécopieur"); 
			String email = getValue(fields, "Adresse de courrier electronique");  
			String website = getValue(fields, "Adresse de site WEB");
			
			organization.setEmail(StringExt.isNullOrWhitespace(email) ? null : email);
			organization.setWeb(StringExt.isNullOrWhitespace(website) ? null : website);
			
			if (! StringExt.isNullOrWhitespace(phoneNumberText)) {
				PhoneNumber phoneNumber = new PhoneNumber(PhoneNumber.Category.OFFICE, phoneNumberText);
				organization.addPhoneNumber(phoneNumber);
			}
			
			if (! StringExt.isNullOrWhitespace(faxNumberText)) {
				PhoneNumber fax = new PhoneNumber(PhoneNumber.Category.FAX, faxNumberText);
				organization.addPhoneNumber(fax);
			}
		}
		
		protected void addDirection(String[] fields, Organization organization) {
			FamilyName directorFamilyName = FamilyName.fromString(getValue(fields, "Nom du directeur")); 
			GivenName directorGivenName = GivenName.fromString(getValue(fields, "Prénom du directeur")); 
			String directorGender = getValue(fields, "Appellation"); 
			String directorTitle = getValue(fields, "Titre de l'intervenant"); 
			String directorFunction = "Directeur"; 
			
			String circonscriptionCode = getValue(fields, "Circonscription électorale provinciale"); 
			Circonscription circonscription = Circonscription.fromCode(circonscriptionCode);
			organization.setCirconscription(circonscription == null ? null : circonscription.getName());
			
			if (directorFamilyName != null) {				
				Person.Gender gender = findGender(directorGender, directorGivenName); 
				String title = StringExt.isNullOrWhitespace(directorTitle) ? directorFunction : directorTitle;
				organization.setDirector(gender, directorGivenName.toDisplayString(), directorFamilyName.toDisplayString(), title);
			}
		}
		
		protected void addReseau(String[] fields, Organization organization) {
			String reseau = getValue(fields, "Réseau d'enseignement"); 
			String french = getValue(fields, "Français");
			String english = getValue(fields, "Anglais");
			
			organization.setPublic("Public".equals(reseau)); 
			organization.setFrench("Oui".equals(french)); 
			organization.setEnglish("Oui".equals(english)); 
		}
		
		protected void addOrganizationAttributes(String[] fields, Organization organization) {
			addReseau(fields, organization);
			addPhoneEmail(fields, organization); 
			addDirection(fields, organization); 
		}

		protected Gender findGender(String directorGender, GivenName directorGivenName) {
			Gender gender; 
			
			if ("M.".equals(directorGender)) {
				gender = Gender.MALE; 
			} else if ("Mme.".equals(directorGender)) {
				gender = Gender.FEMALE; 
			} else {
				gender = Gender.UNKNOWN; 
			}

			return gender;
		}
	} //end OrganizationBuilder
	
	private static class SchoolBoardBuilder extends OrganizationBuilder {

		public SchoolBoardBuilder(CsvStructure structure, OrganizationRepository repository) {
			super(structure, repository);
		}
		
		@Override
		protected void processRow(String row) {
			String[] fields = row.split(";"); 	
			String typeOrganisme = getValue(fields, "Type d'organisme");
			
			//create school board
			if ("Commission scolaire".equals(typeOrganisme)) {
				String code = getValue(fields, "Code d'organisme");
				OrganizationName name = OrganizationName.fromString(getValue(fields, "Nom d'organisme officiel"));
				Address address = getAddress(fields);
				String regionAdmin = getValue(fields, "Région administrative");
				RegionAdministrative region = RegionAdministrative.createFromCode(regionAdmin); 
				Organization organization = new SchoolBoard(code, name, address, region); 
				addOrganizationAttributes(fields, organization); 
				addOrganization(organization); 
			}
		}
	} //end SchoolBoardBuilder
	
	private static class SchoolBuilder extends OrganizationBuilder {

		public SchoolBuilder(CsvStructure structure, OrganizationRepository repository) {
			super(structure, repository);
		}
		
		@Override
		protected void processRow(String row) {
			String[] fields = row.split(";"); 	
			String typeOrganisme = getValue(fields, "Type d'organisme");
			
			boolean isSchool = "École".equals(typeOrganisme);
			isSchool |= "Cégep".equals(typeOrganisme);
			isSchool |= "Université".equals(typeOrganisme);
			
			if (isSchool) {
				String codeResponsable = getValue(fields, "Code organisme responsable");
				SchoolBoard sb = (SchoolBoard)findOrganization(codeResponsable); 
				
				String code = getValue(fields, "Code d'organisme");
				OrganizationName name = OrganizationName.fromString(getValue(fields, "Nom d'organisme officiel"));
				Address address = getAddress(fields);
				String regionAdmin = getValue(fields, "Région administrative");
				RegionAdministrative region = RegionAdministrative.createFromCode(regionAdmin); 
				String ordreApp = getValue(fields, "Ordre d'appartenance");
				OrdreAppartenance ordre = OrdreAppartenance.fromCode(ordreApp);
				Organization organization = new School(sb, code, name, address, region, ordre); 
				addOrganizationAttributes(fields, organization); 
				addOrganization(organization); 
			}
		}


	} //end SchoolBuilder

}
