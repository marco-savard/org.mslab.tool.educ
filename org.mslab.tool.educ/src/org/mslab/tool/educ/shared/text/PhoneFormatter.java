package org.mslab.tool.educ.shared.text;

public class PhoneFormatter {
	public static void main(String[] args) {
		String phoneFormat = "{0} {1}-{2}"; 
		System.out.println(formatPhoneNumber("4", phoneFormat) + ".");
		System.out.println(formatPhoneNumber("41", phoneFormat) + ".");
		System.out.println(formatPhoneNumber("4a", phoneFormat) + ".");
		System.out.println(formatPhoneNumber("418", phoneFormat) + ".");
		System.out.println(formatPhoneNumber("418 ", phoneFormat) + ".");
		System.out.println(formatPhoneNumber("418-", phoneFormat) + ".");
		System.out.println(formatPhoneNumber("418a", phoneFormat) + ".");
		System.out.println(formatPhoneNumber("4186", phoneFormat) + ".");
		System.out.println(formatPhoneNumber("41868", phoneFormat) + ".");
		System.out.println(formatPhoneNumber("418683", phoneFormat) + ".");
		System.out.println(formatPhoneNumber("4186831", phoneFormat) + ".");
		System.out.println(formatPhoneNumber("418683112", phoneFormat) + ".");
		
		
		System.out.println(formatPhoneNumber("418 6", phoneFormat) + ".");
	}
	
	public static String formatPhoneNumber(String original, String phoneFormat) {
		String formatted; 
		
		try {
			original = filterOutLetters(original);
			String filtered = filterCharacters(original); 
			int len = filtered.length();
			
			String areaCode = filtered.substring(0, Math.min(3, len));
			String start = (len > 3) ? filtered.substring(3, Math.min(6, len)) : "ZZZ"; 
			String end = (len > 6) ? filtered.substring(6, Math.min(10, len)) : "ZZZZ";
			
			areaCode += "ZZZ".substring(0, 3 - areaCode.length());
			start += "ZZZ".substring(0, 3 - start.length());
			end += "ZZZZ".substring(0, 4 - end.length());
			
			formatted = MessageFormat.format(phoneFormat, new Object[] {areaCode, start, end});
			int idx = formatted.indexOf('Z'); 
			formatted = (idx == -1) ? formatted : formatted.substring(0, idx); 
			len = differsAt(original, formatted, phoneFormat);
			formatted = formatted.substring(0, len);
		} catch (RuntimeException ex) {
			formatted = original;
		}
		
		return formatted;
	}
	
	private static int differsAt(String original, String formatted, String phoneFormat) {
		int c1 = 0, c2 = 0;
		boolean stop; 
		
		do {
			char ch1 = original.charAt(c1);
			char ch2 = formatted.charAt(c2);
			
			if (ch1 != ch2) {
				if (! Character.isDigit(ch2) && phoneFormat.indexOf(ch2) != -1) {
					c2++;
				} else {
					c1++; c2++; 
				}
			} else {
				c1++; c2++; 
			}
			
			stop = (c1 >= original.length());
			stop |= (c2 >= formatted.length());
		} while (! stop); 
		
		return c2;
	}

	private static boolean isValid(char ch, String phoneFormat) {
		boolean valid = Character.isDigit(ch);
		valid |= phoneFormat.indexOf(ch) != -1;
		return valid;
	}
	
	private static String filterOutLetters(String text) {
		String value = "";
		for (int i=0; i<text.length(); i++) {
			char ch = text.charAt(i); 
			boolean accepted = ! Character.isLetter(ch);
			
			if (accepted) {
				value += ch;
			}
		}
		return value;
	}

	
	private static String filterCharacters(String text) {
		String value = "";
		for (int i=0; i<text.length(); i++) {
			char ch = text.charAt(i); 
			boolean accepted = Character.isDigit(ch);
			
			if (accepted) {
				value += ch;
			}
		}
		return value;
	}
}
