package org.mslab.commons.server.services;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.mslab.commons.client.tool.services.GeoService;
import org.mslab.commons.server.commonnames.CommonNameFinder;
import org.mslab.commons.server.postalcodes.PostalCodeFinder;
import org.mslab.commons.server.schools.OrganizationFinder;
import org.mslab.commons.server.schools.OrganizationFinderOld;
import org.mslab.commons.shared.text.HtmlEncoder;
import org.mslab.commons.shared.text.HtmlEncoder2;
import org.mslab.commons.shared.text.StringExt;
import org.mslab.commons.shared.text.Text;
import org.mslab.commons.shared.text.UnicodeEncoder;
import org.mslab.commons.shared.types.Address;
import org.mslab.commons.shared.types.Captcha;
import org.mslab.commons.shared.types.Contact;
import org.mslab.commons.shared.types.ContactSuggestNames;
import org.mslab.commons.shared.types.GeoLocation;
import org.mslab.commons.shared.types.PhoneNumber;
import org.mslab.commons.shared.types.PostalCode;
import org.mslab.commons.shared.types.educ.Organization;
import org.mslab.commons.shared.util.Random;

import com.google.gwt.user.server.Base64Utils;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class GeoServiceImpl extends RemoteServiceServlet implements GeoService {
	private static final String DEFAULT_LAT_LON = "46.8,-71.2"; 
	private static final String DEFAULT_POSTAL_CODE = "G1A1A3"; 
	private static final String DEFAULT_CITY = "Quebec"; 
	
	public static void main(String[] args) {
		GeoServiceImpl service = new GeoServiceImpl();  
		
		for (int i=0; i<10; i++) {
			GeoLocation code;
			
			do {
				PostalCode postalCode = PostalCode.random(); 
				code = service.findPostalCode(postalCode.toString());
			} while (code == null); 
			
			String s = code.toString();
			System.out.println(s);
		}
	}
	
	@Override
	public GeoLocation getClientLocation() throws IllegalArgumentException {
		HttpServletRequest request = getThreadLocalRequest(); 
		String country=null, region=null, city=null, cityLatLon=null;
		
		if (request != null) {
			country = request.getHeader("X-AppEngine-Country"); 
			region = request.getHeader("X-AppEngine-Region"); 
			city = request.getHeader("X-AppEngine-City"); 
			cityLatLon = request.getHeader("X-AppEngine-CityLatLong"); 
		}
		
		cityLatLon = (cityLatLon == null) ? DEFAULT_LAT_LON : cityLatLon;
		city = (city == null) ? DEFAULT_CITY : city;
		int idx = cityLatLon.indexOf(",");
		
		double lat = Double.parseDouble(cityLatLon.substring(0, idx));
		double lon = Double.parseDouble(cityLatLon.substring(idx+1));
		PostalCode postalCode = new PostalCode(DEFAULT_POSTAL_CODE);
		GeoLocation location = new GeoLocation(lat, lon, postalCode, city);
		return location;
	}
	

	@Override
	public GeoLocation findPostalCode(String code) throws IllegalArgumentException {
		//normalize
		code = new Text(code).removeBlanks().toString().toUpperCase();
		
		//validate code
		int len = code.length();
		if (len != 6) {
			IllegalArgumentException ex = new IllegalArgumentException();
			throw ex;
		}
		
		PostalCodeFinder finder = new PostalCodeFinder();
		GeoLocation postalCode = finder.find(code);
		return postalCode;
	}
	
	@Override
	public List<GeoLocation> findPostalCodes(List<String> postalCodes)
			throws IllegalArgumentException {
		
		List<GeoLocation> locations = new ArrayList<GeoLocation>(); 
		for (String code : postalCodes) {
			if (! StringExt.isNullOrWhitespace(code)) {
				GeoLocation location = findPostalCode(code); 
				if (location.getLatitude() >= 10) {
					locations.add(location); 
				}
			}
		}
		
		return locations;
	}

	@Override
	public ContactSuggestNames getSuggestNames() throws IllegalArgumentException {
		ContactSuggestNames suggestNames = new ContactSuggestNames();
		
		CommonNameFinder finder = new CommonNameFinder(); 
		List<String> commonMaleNames = finder.getMaleGivenNames();
		List<String> commonFemaleNames = finder.getFemaleGivenNames();
		List<String> commonFamilyNames = finder.findCommonFamilyNames();	
		suggestNames.getMaleGivenNames().addAll(commonMaleNames); 
		suggestNames.getFemaleGivenNames().addAll(commonFemaleNames); 
		suggestNames.getFamilyNames().addAll(commonFamilyNames); 
		
		return suggestNames;
	}

	@Override
	public List<Contact> getContacts(int nbContacts) throws IllegalArgumentException {
		CommonNameFinder finder = new CommonNameFinder();
		List<Contact> contacts = new ArrayList<Contact>();
		
		for (int i=0; i<nbContacts; i++) {
			Contact contact = createTestContact(finder);
			contacts.add(contact); 
		}
		
		return contacts;
	}

	private Contact createTestContact(CommonNameFinder finder) {
		//choose gender and names
		boolean isMale = Random.nextInt(10) >= 5;
		List<String> givenNames = isMale ? finder.getMaleGivenNames() : finder.getFemaleGivenNames();
		int r = Random.nextInt(givenNames.size());
		String givenName = givenNames.get(r); 
		
		List<String> familyNames = finder.findCommonFamilyNames();
		r = Random.nextInt(familyNames.size());
		String familyName = familyNames.get(r); 
		
		//get valid postal code
		PostalCode postalCode;
		GeoLocation location = null;
		do {
			postalCode = PostalCode.random(); 
			location = findPostalCode(postalCode.toString());
		} while (location == null); 
		
		
		r = (1+Random.nextInt(10)) * (1+Random.nextInt(10)) * (1+Random.nextInt(10));
		String civicNo = Integer.toString(r); 
		
		String streetName = getTestStreetName(postalCode.getProvince());
		Address address = new Address(civicNo, streetName, "", location, postalCode); 

		Contact contact = new Contact(isMale, givenName, familyName, address);
		String areaCode = PhoneNumber.getAreaCodeFor(postalCode);
		contact.addPhoneNumber(PhoneNumber.random(areaCode));
		
		return contact;
	}

	private String getTestStreetName(String province) {
		String streetName; 
		
		if ("QC".equals(province)) {
			int r = Random.nextInt(ODONYMS_FR.length);
			streetName = ODONYMS_FR[r]; 
		} else {
			int r = Random.nextInt(ODONYMS_EN.length);
			streetName = ODONYMS_EN[r]; 
		}
		return streetName;
	}
	
	private static final String[] ODONYMS_FR = new String[] {
		"Hochelaga",
		"Champlain", "Jacques-Cartier", "Maisonneuve", "Jeanne-Mance", "Frontenac", "Jean-Talon", "Roberval",
		"Louis XIV", "Richelieu",
		"Papineau", "De Lorimier",
		"Saint-Charles", "Saint-Jean", "Saint-Denis", "Saint-Joseph", "Saint-Laurent", "Saint-Louis", "Saint-Pierre", 
		"Sainte-Croix", "Sainte-Marie", "Sainte-Catherine", "Sainte-Anne",
		"Notre-Dame", "De l'Eglise",
		"Laurier", "Tachereau", "Mercier", "Henri-Bourassa", 
		"Ren�-Levesque", "Robert-Bourassa",
		"Hamel", "Talbot", "Gouin", "Charest", "Racine"
	};
	
	private static final String[] ODONYMS_EN = new String[] {
		"Main",
		"Maple", "Cedar", "Elm", "Oak", "Pine", "Walnut", "Chestnut", "Hickory", "Spruce",
		"Bay", "Hill", "Lakeview", "Lakeshore", "Meadow", "Riverside", "Riverbend", "Ridge",
		"King", "Queen", "Sussex", "Oxford", "Cambridge", "Wellington", "Windsor", "Victoria",
		"Amherst", "Champlain", "Cavendish",
		"Bishop", "Church", "Monk", "Sherbrooke"
	};

	@Override
	public Captcha createCaptcha(int captchaWidth, int captchaHeight)
			throws IllegalArgumentException {
		Captcha captcha; 
		
		try {
			CaptchaBuilder builder = new CaptchaBuilder(captchaWidth, captchaHeight); 
			builder.chooseWord(6);
			builder.addNoise(6);
			captcha = builder.build();	
		} catch (RuntimeException caught) {
			String userMessage = caught.toString(); 
			//String report = buildReport(db, caught); 
			IllegalArgumentException ex = new IllegalArgumentException(userMessage); 
			throw ex;
		}
		
		return captcha;
	}

	@Override
	public List<Organization> getListOrganizations()
			throws IllegalArgumentException {
		OrganizationFinder finder = new OrganizationFinder(); 
		List<Organization> organizations = finder.getOrganizationRepository().getOrganizations();
		return organizations;
	}

	

   
}
