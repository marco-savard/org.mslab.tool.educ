package org.mslab.tool.educ.shared.text;

import java.util.Arrays;
import java.util.List;

public class StringExt {
	private static final Character[] VOYELS = new Character[] {'a', 'e', 'i', 'o', 'u'};

	public static boolean isNullOrWhitespace(String text) {
		boolean nullOrWhiteSpace = (text == null);
		
		if (text != null) {
			nullOrWhiteSpace = text.trim().isEmpty();
		}
		
		return nullOrWhiteSpace;
	}

	public static boolean isVoyel(char c) {
		c = Character.toLowerCase(c); 
		List<Character> voyels = Arrays.asList(VOYELS); 
		boolean voyel = voyels.contains(c);
		return voyel;
	}

	public static String removeNonDigit(String text) {
		StringBuffer sb = new StringBuffer(); 
		int nb = text.length();
		
		for (int i=0; i<nb; i++) {
			char ch = text.charAt(i); 
			if (Character.isDigit(ch)) {
				sb.append(ch); 
			}
		}
		
		return sb.toString();
	}

}
