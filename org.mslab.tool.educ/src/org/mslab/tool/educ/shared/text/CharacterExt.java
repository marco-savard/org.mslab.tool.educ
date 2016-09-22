package org.mslab.tool.educ.shared.text;

import java.util.HashMap;
import java.util.Map;

public class CharacterExt {
	
	public static boolean isISOControl(char ch) {
		boolean control = (ch < ' ') || (ch == 127); 
		return control;
	}
	
	public static int getType(char code) {
		return 0;
	}

	
	public static String toString(char ch) {
		String s;
		
		if (isISOControl(ch)) {
			s = getControlAbbr(ch); 
		} else {
			s = Character.toString(ch);
		}
		
		return s;
	}

	public static String getControlAbbr(int code) {
		String s; 
		
		if (code == 0) {
			s = "NUL";
		} else if (code == 1) {
			s = "SOH";
		} else if (code == 2) {
			s = "STX";
		} else if (code == 3) {
			s = "ETX";
		} else if (code == 4) {
			s = "EOT";
		} else if (code == 5) {
			s = "ENQ";
		} else if (code == 6) {
			s = "ACK";
		} else if (code == 7) {
			s = "BEL";
		} else if (code == 8) {
			s = "BS";
		} else if (code == 9) {
			s = "TAB";
		} else if (code == 10) {
			s = "LF";
		} else if (code == 11) {
			s = "VT";
		} else if (code == 12) {
			s = "FF";
		} else if (code == 13) {
			s = "CR";
		} else if (code == 14) {
			s = "SO";
		} else if (code == 15) {
			s = "SI";
		} else if (code == 16) {
			s = "DLE";
		} else if (code == 17) {
			s = "DC1";
		} else if (code == 18) {
			s = "DC2";
		} else if (code == 19) {
			s = "DC3";
		} else if (code == 20) {
			s = "DC4";
		} else if (code == 21) {
			s = "NAK";
		} else if (code == 22) {
			s = "SYN";
		} else if (code == 23) {
			s = "ETB";
		} else if (code == 24) {
			s = "CAN";
		} else if (code == 25) {
			s = "EM";
		} else if (code == 26) {
			s = "SUB";
		} else if (code == 27) {
			s = "ESC";
		} else if (code == 28) {
			s = "FS";
		} else if (code == 29) {
			s = "GS";
		} else if (code == 30) {
			s = "RS";
		} else if (code == 31) {
			s = "US";
		} else if (code == 127) {
			s = "DEL";
		} else {
			s = " "; 
		}
		
		return s;
	}

	public static String getAltText(int code) {
		if (_altTexts == null) {
			_altTexts = buildAltTexts(); 
		}
		
		String altText = _altTexts.get(code);		
		
		if (altText == null) {
			altText = getHtmlEntityCode(code);
		}
		
		return altText;
	}
	
	private static Map<Integer, String> _altTexts = null;
	private static Map<Integer, String> buildAltTexts() {
		Map<Integer, String> altTexts = new HashMap<Integer, String>();
		altTexts.put(0, "\\0");
		altTexts.put(7, "\\a");
		altTexts.put(8, "\\b");
		altTexts.put(9, "\\t");
		altTexts.put(10, "\\n");
		altTexts.put(12, "\\f");
		altTexts.put(13, "\\r");
		altTexts.put(127, "^?");

		return altTexts;
	}
	
	public static boolean isWhitespace(char code) {
		boolean whitespace = (WHITESPACES.indexOf(code) >= 0);
		whitespace |= (code >= 0x2000) && (code <= 0x200a);
		whitespace |= (code == 0x2028);
		whitespace |= (code == 0x2029);
		whitespace |= (code == 0x202f);
		whitespace |= (code == 0x205f);
		whitespace |= (code == 0x3000);
		return whitespace;
	}
	private static final String WHITESPACES = " \t\n\r\f\u000b\u0085\u00a0\u1680\u180e";
	
	public static boolean isPunctuation(char code) {
		boolean punctuation = (PUNCTUATIONS.indexOf(code) >= 0);
		punctuation |= (code >= 0x055a) && (code <= 0x055f);
		punctuation |= (code == 0x0589);
		punctuation |= (code == 0x05c0);
		punctuation |= (code == 0x05c3);
		punctuation |= (code == 0x05c6);
		punctuation |= (code == 0x05f3);
		punctuation |= (code == 0x05f4);
		punctuation |= (code == 0x0609);
		punctuation |= (code == 0x060a);
		punctuation |= (code == 0x060c);
		punctuation |= (code == 0x060d);
		punctuation |= (code == 0x061b);
		punctuation |= (code == 0x061e);
		punctuation |= (code == 0x061f);
		punctuation |= (code == 0x066a);
		punctuation |= (code == 0x066b);
		punctuation |= (code == 0x066c);
		punctuation |= (code == 0x066d);
		punctuation |= (code == 0x06d4);
		return punctuation;
	}
	private static final String PUNCTUATIONS = "!\"#%&\'*,./:;?@\\\u00a1\u00a7\u00b6\u00b7\u00bf\u037e\u0387";

	public static boolean isMathSymbol(char code) {
		boolean mathSymbol = (MATHS.indexOf(code) >= 0);
		mathSymbol |= (code >= 0x2200) && (code <= 0x22ff);
		mathSymbol |= (code >= 0x27c0) && (code <= 0x27ef);
		mathSymbol |= (code >= 0x2980) && (code <= 0x29ff);
		mathSymbol |= (code >= 0x2a00) && (code <= 0x2aff);
		mathSymbol |= (code >= 0x2100) && (code <= 0x214f);
		mathSymbol |= (code >= 0x2308) && (code <= 0x230b);
		mathSymbol |= (code >= 0x25a0) && (code <= 0x25ff);
		mathSymbol |= (code >= 0x2b30) && (code <= 0x2b4c);
		mathSymbol |= (code >= 0x1d400) && (code <= 0x1d7ff);
		return mathSymbol;
	}
	private static final String MATHS = "+-<=>\u00b1\u00bc\u00bd\u00be\u00d7\u00f7";

	public static String getHtmlEntityCode(int code) {
		
		if (_entityCodes == null) {
			_entityCodes = buildEntityCodes(); 
		}
		
		String entityCode = _entityCodes.get(code);		
		return entityCode;
	}
	
	private static Map<Integer, String> _entityCodes = null;
	private static Map<Integer, String> buildEntityCodes() {
		Map<Integer, String> entityCodes = new HashMap<Integer, String>();
		entityCodes.put(34, "&quot;");
		entityCodes.put(38, "&amp;");
		entityCodes.put(39, "&apos;");
		
		entityCodes.put(60, "&lt;");
		entityCodes.put(62, "&gt;");
		
		entityCodes.put(160, "&nbsp;");
		entityCodes.put(161, "&iexcl;");
		entityCodes.put(162, "&cent;");
		entityCodes.put(163, "&pound;");
		entityCodes.put(164, "&curren;");
		entityCodes.put(165, "&yen;");
		entityCodes.put(166, "&brvbar;");
		entityCodes.put(167, "&sect;");
		entityCodes.put(168, "&uml;");
		entityCodes.put(169, "&copy;");
		entityCodes.put(170, "&ordf;");
		entityCodes.put(171, "&laquo;");
		entityCodes.put(172, "&not;");
		entityCodes.put(173, "&shy;");
		entityCodes.put(174, "&reg;");
		entityCodes.put(175, "&macr;");
		entityCodes.put(176, "&deg;");
		entityCodes.put(177, "&plusmn;");
		entityCodes.put(178, "&sup2;");
		entityCodes.put(179, "&sup3;");
		entityCodes.put(180, "&acute;");
		entityCodes.put(181, "&micro;");
		entityCodes.put(182, "&para;");
		entityCodes.put(183, "&middot;");
		entityCodes.put(184, "&cedil;");
		entityCodes.put(185, "&sup1;");
		entityCodes.put(186, "&ordm;");
		entityCodes.put(187, "&raquo;");
		entityCodes.put(188, "&frac14;");
		entityCodes.put(189, "&frac12;");
		entityCodes.put(190, "&frac13;");
		entityCodes.put(191, "&iquest;");
		
		entityCodes.put(215, "&times;");
		entityCodes.put(247, "&divide;");
		
		entityCodes.put(192, "&Agrave;");
		entityCodes.put(193, "&Aacute;");
		entityCodes.put(194, "&ACirc;");
		entityCodes.put(196, "&Auml;");
		entityCodes.put(200, "&Egrave;");
		entityCodes.put(201, "&Eacute;");
		entityCodes.put(202, "&Ecirc;");
		entityCodes.put(203, "&Euml;");
		entityCodes.put(206, "&Icirc;");
		entityCodes.put(207, "&Iuml;");
		entityCodes.put(212, "&Ocirc;");
		entityCodes.put(140, "&OElig;");
		entityCodes.put(217, "&Ugrave;");
		entityCodes.put(219, "&Ucirc;");
		entityCodes.put(220, "&Uuml;");
		entityCodes.put(159, "&Yuml;");

		entityCodes.put(224, "&agrave;");
		entityCodes.put(226, "&aCirc;");
		entityCodes.put(228, "&auml;");
		entityCodes.put(232, "&egrave;");
		entityCodes.put(233, "&eacute;");
		entityCodes.put(234, "&ecirc;");
		entityCodes.put(235, "&euml;");
		entityCodes.put(238, "&icirc;");
		entityCodes.put(239, "&iuml;");
		entityCodes.put(244, "&ocirc;");
		entityCodes.put(156, "&oElig;");
		entityCodes.put(250, "&ugrave;");
		entityCodes.put(251, "&ucirc;");
		entityCodes.put(252, "&uuml;");
		entityCodes.put(255, "&yuml;");
		
		entityCodes.put(199, "&Ccedil;");
		entityCodes.put(231, "&ccedil");
		entityCodes.put(8249, "&lsaquo;");
		entityCodes.put(8250, "&rsaquo");
		entityCodes.put(8364, "&euro;");
		
		return entityCodes;
	}

	public static boolean isVowel(char charAt) {
		char lower = (char)Character.toLowerCase(charAt); 
		boolean vowel = "aeiou".indexOf(lower) >= 0;
		return vowel;
	}

	public static boolean isAlphabetic(char ch) {
		char lower = (char)Character.toLowerCase(ch); 
		boolean alpha = (lower >= 'a') && (lower <= 'z');
		return alpha;
	}
	

	
}
