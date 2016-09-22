package org.mslab.tool.educ.shared.types;

import java.io.Serializable;

import org.mslab.tool.educ.shared.util.Random;

@SuppressWarnings("serial")
public class PostalCode implements Serializable {
	public static final String VALID_LETTERS = "ABCEGHJKLMNPRSTVXY";
	private String _postalCode;
	
	//required by GWT
	@SuppressWarnings("unused")
	private PostalCode() {}

	public PostalCode(String postalCode) {
		postalCode = (postalCode == null) ? null : postalCode.replaceAll(" ", "").toUpperCase();
		_postalCode = postalCode;
	}

	public int length() {
		return _postalCode.length();
	}

	public void add(char ch) {
		_postalCode += ch;
	}
	
	@Override 
	public String toString() {
		//format?
		return _postalCode;
	}

	public void setText(String text) {
		_postalCode = text.replaceAll(" ", "").toUpperCase();
	}

	public String toDisplayString() {
		int len = (_postalCode == null) ? 0 : _postalCode.length(); 
		String display = _postalCode; 
		
		if (len > 3) {
			display = _postalCode.substring(0, 3) + " " + _postalCode.substring(3); 
		}
		
		return display;
	}
	
	public int compareTo(PostalCode postalCode) {
		int comparison = _postalCode.compareTo(postalCode._postalCode);
		return comparison;
	}

	public boolean isRural() {
		char ch = _postalCode.charAt(1); 
		boolean rural = (ch == '0');
		return rural;
	}

	public boolean isValid() {
		return _postalCode.length() == 6;
	}
	
	public static PostalCode random() {
		int r = Random.nextInt(PostalCode.VALID_LETTERS.length());
		char letter = PostalCode.VALID_LETTERS.charAt(r); 
		r = Random.nextInt(10);
		String code = letter + Integer.toString(r);
		
		r = Random.nextInt(PostalCode.VALID_LETTERS.length());
		letter = PostalCode.VALID_LETTERS.charAt(r); 
		r = Random.nextInt(10);
		code += letter + Integer.toString(r);
		
		r = Random.nextInt(PostalCode.VALID_LETTERS.length());
		letter = PostalCode.VALID_LETTERS.charAt(r); 
		r = Random.nextInt(10);
		code += letter + Integer.toString(r);
		
		PostalCode postalCode = new PostalCode(code);
		return postalCode;
	}


	public String getProvince() {
		char ch = _postalCode.charAt(0); 
		String prov;
		
		switch (ch) {
		case 'A':
			prov = "NL";
			break;
		case 'B':
			prov = "NS";
			break;
		case 'C':
			prov = "PE";
			break;
		case 'E':
			prov = "NB";
			break;
		case 'G':
		case 'H':
		case 'J':
			prov = "QC";
			break;
		case 'K':
		case 'L':
		case 'M':
		case 'N':
		case 'P':
			prov = "ON";
			break;
		case 'R':
			prov = "MB";
			break;
		case 'S':
			prov = "SK";
			break;
		case 'T':
			prov = "AB";
			break;
		case 'V':
			prov = "BC";
			break;
		case 'X':
			prov = getTerritory();
			break;
		case 'Y':
			prov = "YK";
			break;
		default :
			prov = "?";
		}
		return prov;
	}

	private String getTerritory() {
		String territory; 
		
		if (_postalCode.startsWith("X0A")) {
			territory = "NU";
		} else {
			territory = "NT";
		}
		return territory;
	}

	public String getRegion() {
		String region = null;
		
		char ch = _postalCode.charAt(0); 
		
		switch (ch) {
		case 'A':
			region = "Newfoundland";
			break;
		case 'B':
			region = "Nova Scotia";
			break;
		case 'C':
			region = "Prince Edouard Island";
			break;
		case 'E':
			region = "New Brunswick";
			break;
		case 'G':
			region = getRegionEasternQuebec();
			break;
		case 'H':
			region = "Montreal Area";
			break;
		case 'J':
			region = getRegionWesternQuebec();
			break;
		case 'K':
			region = "Eastern Ontario";
			break;
		case 'L':
			region = "Central Ontario";
			break;
		case 'M':
			region = "Toronto Area";
			break;
		case 'N':
			region = "Western Ontario";
			break;
		case 'P':
			region = "Northern Ontario";
			break;
		case 'R':
			region = "Manitoba";
			break;
		case 'S':
			region = "Saskatchewan";
			break;
		case 'T':
			region = getRegionAlberta();
			break;
		case 'V':
			region = getRegionBC();
			break;
		case 'X':
			region = getRegionNT();
			break;
		case 'Y':
			region = "Yukon";
			break;
		}
				
		return region;
	}
	
	private String getRegionEasternQuebec() {
		String region; 
		
		if (isRural()) {
			region = getRegionEasternQuebecRural();
		} else {
			region = getRegionEasternQuebecUrban();
		}
		
		return region;
	}
	
	private String getRegionEasternQuebecRural() {
		String region; 
		
		if (_postalCode.startsWith("G0C")) {
			region = "Gaspesie";
		} else if (_postalCode.startsWith("G0C")) {
			region = "Gaspesie";
		} else if (_postalCode.startsWith("G0E")) {
			region = "Gaspesie";
		} else if (_postalCode.startsWith("G0G")) {
			region = "North Shore";
		} else if (_postalCode.startsWith("G0H")) {
			region = "North Shore";
		} else if (_postalCode.startsWith("G0J")) {
			region = "Gaspesie";
		} else if (_postalCode.startsWith("G0K")) {
			region = "Gaspesie";
		} else if (_postalCode.startsWith("G0L")) {
			region = "Gaspesie";
		} else if (_postalCode.startsWith("G0M")) {
			region = "Chaudiere-App.";
		} else if (_postalCode.startsWith("G0N")) {
			region = "Chaudiere-App.";
		} else if (_postalCode.startsWith("G0P")) {
			region = "Central Quebec";
		} else if (_postalCode.startsWith("G0R")) {
			region = "Chaudiere-App.";
		} else if (_postalCode.startsWith("G0S")) {
			region = "Chaudiere-App.";
		} else if (_postalCode.startsWith("G0T")) {
			region = "Saguenay";
		} else if (_postalCode.startsWith("G0V")) {
			region = "Saguenay";
		} else if (_postalCode.startsWith("G0W")) {
			region = "Saguenay";
		} else if (_postalCode.startsWith("G0X")) {
			region = "Mauricie";
		} else if (_postalCode.startsWith("G0Y")) {
			region = "Chaudiere-App.";
		} else if (_postalCode.startsWith("G0Z")) {
			region = "Central Quebec";
		} else {
			region = "Capitale Nationale";
		}
		
		return region;
	}
	
	private String getRegionEasternQuebecUrban() {
		String region; 
		
		if (_postalCode.startsWith("G8P")) {
			region = "Northern Quebec";
		} else if (_postalCode.startsWith("G7B")) {
			region = "Saguenay";
		} else if ((compareTo(new PostalCode("G7G1A1")) >= 0) && (compareTo(new PostalCode("G8P1A1")) < 0)) {
			region = "Saguenay";
		} else if (compareTo(new PostalCode("G8T1A1")) >= 0) {
			region = "Mauricie";
		} else if ((compareTo(new PostalCode("G4R1A1")) >= 0) && (compareTo(new PostalCode("G5T1A1")) < 0)) {
			region = getRegionGaspeNothShore();
		} else {
			region = "Capitale Nationale";
		}
		
		return region;
	}
	
	private String getRegionGaspeNothShore() {
		String region; 
		
		if (_postalCode.startsWith("G5B")) {
			region = "North Shore Quebec";
		} else if (_postalCode.startsWith("G5C")) {
			region = "North Shore Quebec";
		} else if (compareTo(new PostalCode("G4T1A1")) >= 0) {
			region = "North Shore Quebec";
		} else {
			region = "Gaspesie";
		}
		
		return region;
	}

	private String getRegionWesternQuebec() {
		String region; 
		
		if (isRural()) {
			region = getRegionWesternQuebecRural();
		} else {
			region = getRegionWesternQuebecUrban();
		}
		
		return region;
	}

	private String getRegionWesternQuebecRural() {
		String region; 
		
		if (_postalCode.startsWith("J0A") || _postalCode.startsWith("J0C")) {
			region = "Central Quebec";
		} else if (_postalCode.startsWith("J0G") || _postalCode.startsWith("J0H")) {
			region = "Central Quebec";
		} else if (_postalCode.startsWith("J0B") || _postalCode.startsWith("J0E")) {
			region = "Eastern Townships";
		} else if (_postalCode.startsWith("J0J") || _postalCode.startsWith("J0L")) {
			region = "Montreregie";
		} else if (_postalCode.startsWith("J0K")) {
			region = "Lanaudiere";
		} else if (_postalCode.startsWith("J0M")) {
			region = "Northern Quebec";
		} else if (_postalCode.startsWith("J0N")) {
			region = "Laurentides";
		} else if (_postalCode.startsWith("J0P")) {
			region = "Montreregie";
		} else if (_postalCode.startsWith("J0R")) {
			region = "Laurentides";
		} else if (_postalCode.startsWith("J0S")) {
			region = "Montreregie";
		} else if (_postalCode.startsWith("J0T")) {
			region = "Laurentides";
		} else {
			region = "Western Quebec";
		}
		
		return region;
	}

	private String getRegionWesternQuebecUrban() {
		String region; 
		
		if (_postalCode.startsWith("J3T")) {
			region = "Central Quebec"; //Nicolet
		} else if (_postalCode.startsWith("J5R")) {
			region = "Monteregie"; //La Prairie
		} else if (_postalCode.startsWith("J5V")) {
			region = "Mauricie"; //Louiseville
		} else if (_postalCode.startsWith("J9L")) {
			region = "Laurentides"; //Mont-Laurier
		} else if (compareTo(new PostalCode("J1Z1A1")) < 0) {
			region = "Eastern Townships";
		} else if (compareTo(new PostalCode("J2G1A1")) < 0) {
			region = "Central Quebec";
		} else if (compareTo(new PostalCode("J5J1A1")) < 0) {
			region = "Monteregie";
		} else if (compareTo(new PostalCode("J5T1A1")) < 0) {
			region =  "Laurentides";
		} else if (compareTo(new PostalCode("J6J1A1")) < 0) {
			region =  "Lanaudiere";
		} else if (compareTo(new PostalCode("J6V1A1")) < 0) {
			region =  "Laurentides";
		} else if (compareTo(new PostalCode("J6V1A1")) < 0) {
			region =  "Monteregie";
		} else if (compareTo(new PostalCode("J6Z1A1")) < 0) {
			region =  "Lanaudiere";
		} else if (compareTo(new PostalCode("J7K1A1")) < 0) {
			region =  "Laurentides";
		} else if (compareTo(new PostalCode("J7N1A1")) < 0) {
			region =  "Lanaudiere";
		} else if (compareTo(new PostalCode("J7T1A1")) < 0) {
			region =  "Laurentides";
		} else if (compareTo(new PostalCode("J7Y1A1")) < 0) {
			region =  "Monteregie";
		} else if (compareTo(new PostalCode("J8L1A1")) < 0) {
			region =  "Laurentides";
		} else if (compareTo(new PostalCode("J9L1A1")) < 0) {
			region =  "Outaouais";
		} else {
			region = "Abitibi";
		}
		
		return region;
	}

	private String getRegionAlberta() {
		String region; 
		
		int cmp = compareTo(new PostalCode("T4C1A1"));
		if (cmp < 0) {
			region = "Southern Alberta";
		} else {
			boolean north = _postalCode.startsWith("T8S"); 
			north |= _postalCode.startsWith("T8V"); 
			north |= _postalCode.startsWith("T8W"); 
			north |= _postalCode.startsWith("T8X"); 
			north |= _postalCode.startsWith("T9H"); 
			north |= _postalCode.startsWith("T9J"); 
			north |= _postalCode.startsWith("T9K"); 
			north |= _postalCode.startsWith("T9S"); 
			
			region = north ? "Northern Alberta" : "Central Alberta";
		}
		
		return region;
	}

	private String getRegionBC() {
		String region; 
		
		int cmp = compareTo(new PostalCode("V8K1A1"));
		if (cmp < 0) {
			region = "Main Land BC";
		} else {
			region = "Vancouver Island";
		}
		
		return region;
	}
	
	private String getRegionNT() {
		String territory; 
		
		if (_postalCode.startsWith("X0A")) {
			territory = "Nunavuk";
		} else {
			territory = "North West Territories";
		}
		return territory;
	}



	

	
	

}
