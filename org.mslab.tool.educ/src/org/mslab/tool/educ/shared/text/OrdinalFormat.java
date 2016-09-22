package org.mslab.tool.educ.shared.text;

public class OrdinalFormat {
	private String _locale;

	public static OrdinalFormat getFormat(String locale) {
		OrdinalFormat fmt = new OrdinalFormat(locale); 
		return fmt;
	}
	
	private OrdinalFormat(String locale) {
		_locale = locale;
	}
	
	public String format(int cardinal) {
		if (_locale.startsWith("fr")) {
			return formatFr(cardinal);
		} else {
			return formatEn(cardinal);
		}
	}
	
	public String formatFr(int cardinal) {
		String ordinal; 
		
		if (cardinal == 1) {
			ordinal = cardinal + "er"; 
		} else {
			ordinal = cardinal + "e"; 
		}
		
		return ordinal;
	}
	
	public String formatEn(int cardinal) {
		String ordinal; 
		
		if (((cardinal % 10) == 1) && ((cardinal % 100) != 11)) {
			ordinal = cardinal + "st"; 
		} else if (((cardinal % 10) == 2) && ((cardinal % 100) != 12)) {
			ordinal = cardinal + "nd"; 
		} else if (((cardinal % 10) == 3) && ((cardinal % 100) != 13)) {
			ordinal = cardinal + "rd"; 
		} else {
			ordinal = cardinal + "th"; 
		}
		
		return ordinal;
	}

	

}
