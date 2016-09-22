package org.mslab.tool.educ.shared.types;

import java.io.Serializable;

import org.mslab.tool.educ.shared.text.MessageFormat;
import org.mslab.tool.educ.shared.text.StringExt;
import org.mslab.tool.educ.shared.util.Random;


@SuppressWarnings("serial")
public class PhoneNumber implements Serializable {
	public enum Category {HOME, OFFICE, FAX, OTHER}
	private Category _category = Category.OFFICE;
	
	private String _areaCode, _number, _extension; 
	
	
	//required by GWT
	private PhoneNumber() {}
	
	public PhoneNumber(Category category, String fullNumber) {
		this(category, fullNumber, null);
	}
		
	public PhoneNumber(Category category, String fullNumber, String extension) {
		String digits = StringExt.removeNonDigit(fullNumber); 
		
		_category = category;
		_areaCode = digits.substring(0, 3); 
		_number = digits.substring(3); 
		_extension = extension;
	}
	
	public Category getCategory() { return _category; }
	public String getAreaCode() { return _areaCode; }
	public String getNumber() { return _number; }
	public String getExtension() { return _extension; }
	
	public String getNumberStart() { return _number.substring(0, 3); }
	public String getNumberEnd() { return _number.substring(3); }

	
	public static PhoneNumber parse(String text) {
		StringBuffer parsed = new StringBuffer(); 
		
		for (int i=0; i<text.length(); i++) {
			char ch = text.charAt(i); 
			if (Character.isDigit(ch)) {
				parsed.append(ch);
			}
		}
		text = parsed.toString();
		
		int len = Math.min(text.length(), 3);
		String rc = text.substring(0,  len); 
		String number = text.substring(len); 
		PhoneNumber phone = new PhoneNumber(PhoneNumber.Category.OTHER, rc, number);
		return phone;
	}
	
	@Override
	public String toString() {
		String s = _areaCode + _number; 
		return s;
	}
	
	public String toDisplayString() {
		String s = MessageFormat.format("{0} {1}-{2}", new Object[] {
			_areaCode, _number.substring(0, 3), _number.substring(3) }); 
		return s;
	}

	public static String getAreaCodeFor(PostalCode postalCode) {
		char c = postalCode.toString().charAt(0);
		int idx = c - 'A'; 
		String areaCode = AREA_CODES[idx];
		return areaCode;
	}
	private static final String[] AREA_CODES = new String[] {
		"709", //A 
		"782", //B 
		"782", //C
		null, 
		"506", //E
		null,
		"418", //G
		"514", //H
		null,
		"819", //J
		"613", //K
		"613", //L
		"416", //M
		"416", //N
		null,
		"807", //P
		null,
		"431", //R
		"636", //S
		"587", //T
		null,
		"236", //V
		null,
		"867", //X
		null,
		null
	};

	public static PhoneNumber random(String areaCode) {
		StringBuffer sf = new StringBuffer();
		sf.append(2 + Random.nextInt(8)); 
		sf.append(Random.nextInt(10)); 
		sf.append(Random.nextInt(10)); 
		
		sf.append(Random.nextInt(10)); 
		sf.append(Random.nextInt(10)); 
		sf.append(Random.nextInt(10)); 
		sf.append(Random.nextInt(10)); 
		
		PhoneNumber phoneNumber = new PhoneNumber(PhoneNumber.Category.OTHER, areaCode, sf.toString());
		return phoneNumber;
	}
	
	public int compareTo(PhoneNumber phoneNumber) {
		String s1 = toString();
		String s2 = phoneNumber.toString(); 
		int comparison = s1.compareTo(s2); 
		return comparison;
	}
	
	public static void main(String[] args) {
		for (int i=0; i<10; i++) {
			PostalCode postalCode = PostalCode.random();
			String areaCode = PhoneNumber.getAreaCodeFor(postalCode);
			PhoneNumber phone = PhoneNumber.random(areaCode);
			System.out.println(MessageFormat.format("Postal Code:{0}; Phone:{1}", new Object[] {postalCode.toDisplayString(), phone.toString()}));
		}
	}






	


	
}
