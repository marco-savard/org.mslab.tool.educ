package org.mslab.tool.educ.shared.text;

public class UnicodeEncoder {
	
	public static UnicodeEncoder getInstance() {
		if (_encoder == null) {
			_encoder = new UnicodeEncoder();
		}
		return _encoder; 
	}
	
	private static UnicodeEncoder _encoder;

	private UnicodeEncoder() {
		
	}

	public String encode(String original) {
		int nb = original.length();
		StringBuffer buf = new StringBuffer(); 
		
		for (int i=0; i<nb; i++) {
			char ch = original.charAt(i);
			boolean isLetter = Character.isLetter(ch);
			String text = MessageFormat.format("{0}", new Object[] {Character.toString(ch)});
			buf.append(text);
		}
		
		for (int i=0; i<nb; i++) {
			char ch = original.charAt(i);
			boolean isLetter = Character.isLetter(ch);
			String text = isLetter ? 
				MessageFormat.format("({0});", new Object[] {Integer.toString((int)ch)}) : 
				MessageFormat.format("{0};", new Object[] {Integer.toString((int)ch)});
			buf.append(text);
		}
		
		return buf.toString();
	}
}
