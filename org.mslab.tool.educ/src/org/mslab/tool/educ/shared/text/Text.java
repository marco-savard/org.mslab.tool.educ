package org.mslab.tool.educ.shared.text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("serial")
public class Text implements Serializable {
	private String _text; 
	
	//required by GWT
	@SuppressWarnings("unused")
	private Text() {}
	
	public Text(String text) {
		_text = text; 
	}

	public Text trimQuotes() {
		_text = (_text.charAt(0) == '\"') ? _text.substring(1) : _text;
		int len = _text.length(); 
		_text = (_text.charAt(len - 1) == '\"') ? _text.substring(0, len-1) : _text;
		return new Text(_text);
	}
	
	@Override
	public String toString() {
		return _text; 
	}

	public Text removeBlanks() {
		int nb = _text.length();
		String dest = "";
		
		for (int i=0; i<nb; i++) {
			char ch = _text.charAt(i); 
			
			if (! CharacterExt.isWhitespace(ch)) {
				dest += ch;
			}
		}
		
		return new Text(dest);
	}

	public Text capitalize() {
		Text text = this; 
		
		if (_text.length() > 0) {
			text = new Text(Character.toUpperCase(_text.charAt(0)) + _text.substring(1)); 
		}
		
		return text;
	}
	
	public static String capitalizeWords(String lowerCase) {
		boolean capital = true; 
		String capitalized = ""; 
		
		for (int i=0; i<lowerCase.length(); i++) {
			char ch = lowerCase.charAt(i); 
			capitalized += capital ? Character.toUpperCase(ch) : ch;
			capital = (ch == ' ') || (ch == '-');
		}
		return capitalized;
	}

	public Text capitalizeWords() {
		String capitalized = capitalizeWords(_text);
		return new Text(capitalized);
	}

	public static int indexOfIgnoreCase(String text, String substring) {
		int idx = (substring == null) ? -1 : toUnaccentued(text.toLowerCase()).indexOf(substring); 
		return idx;
	}
	
	public static boolean isNullOrEmpty(String str) {
		boolean nullOrEmptry = (str == null) ? true : str.isEmpty(); 
		return nullOrEmptry;
	}
	
	public static int lastIndexOfIgnoreCase(String text, String substring) {
		int idx = (substring == null) ? -1 : toUnaccentued(text.toLowerCase()).lastIndexOf(substring); 
		return idx;
	}

	public static Text toLowerCase(String filterText) {
		Text text = new Text(filterText).toLowercase();
		return text;
	}
	
	public Text toLowerCase() {
		String lowerCase = _text.toLowerCase();
		return new Text(lowerCase);
	}
	
	//note String s = String.replace("a", "b") does not work client-side
	public Text toUnaccentued() {
		String unaccentued = toUnaccentued(_text);
		return new Text(unaccentued);
	}
	
	public static String toUnaccentued(String text) {
		StringBuffer buf = new StringBuffer();
		int nb = (text == null) ? 0 : text.length();
		
		for (int i=0; i<nb; i++) {
			char ch = text.charAt(i);
			ch = toUnaccentued(ch);
			buf.append(ch);
		}
		
		text = buf.toString();
		return text;
	}
	
	public static String toHtml(String text) {
		StringBuffer buf = new StringBuffer();
		int nb = (text == null) ? 0 : text.length();
		
		for (int i=0; i<nb; i++) {
			char ch = text.charAt(i);
			String html = toHtml(ch);
			buf.append(html);
		}
		
		text = buf.toString();
		return text;
	}

	private static String toHtml(char ch) {
		int code = (int)ch; 
		String html = Character.toString(ch); 
		
		if (code == 233) {
			html = "&eacute;";
		}
		/*
		switch(code) {
		case 232:
			html = "&egrave;";
			break;
		case 233:
			html = "&eacute;";
			break;
		case 234:
			html = "&ecirc;";
			break;
		case 235:
			html = "&euml;";
			break;
		default:
			html = Character.toString(ch);
		}
*/
		return html;
	}

	private static char toUnaccentued(char ch) {
		int code = (int)ch; 
		
		if ((code == 224) || (code == 226)) { //��
			ch = 'a';
		} else if ((code == 231)) { //�
			ch = 'c';
		} else if ((code == 232) || (code == 233) || (code == 234) || (code == 235)) { //����
			ch = 'e';
		} else if ((code == 238) || (code == 239)) { //��
			ch = 'i';
		} else if ((code == 244) || (code == 246)) { //��
			ch = 'o';
		} else if ((code == 249) || (code == 250) || (code == 251)) { //��
			ch = 'o';
		}
	
		return ch;
	}

	//buggy on client-side
	private static String toUnaccentuedOld(String text) {
		text = text.replace("�", "a");
		text = text.replace("�", "a");
		text = text.replace("�", "c");
		text = text.replace("�", "e");
		text = text.replace("�", "e");
		text = text.replace("�", "e");
		text = text.replace("�", "e");
		text = text.replace("�", "i");
		text = text.replace("�", "i");
		text = text.replace("�", "o");
		text = text.replace("�", "o");
		text = text.replace("�", "u");
		text = text.replace("�", "u");
		text = text.replace("�", "u");
		
		return text;
	}

	public Text toLowercase() {
		return new Text(_text.toLowerCase());
	}

	public Text toUnaccented() {
		return new Text(Text.toUnaccentued(_text));
	}
		
	public static class TextComparator implements Comparator<String> {
		@Override
		public int compare(String s1, String s2) {
			s1 = s1.toLowerCase();
			s2 = s2.toLowerCase(); 
		
			s1 = Text.toUnaccentued(s1);
			s2 = Text.toUnaccentued(s2); 
			return s1.compareTo(s2);
		}
	}

	public int compareTo(Text t2) {
		int comparison = _text.compareTo(t2.toString());
		return comparison;
	}
	
	public static void main(String[] args) {
		String s1 = "fr�d�rik";
		s1 = toUnaccentued(s1);
		System.out.println(s1);
		
		/*
		String[] WORDS = new String[] {"alice", "zo�", "�milie"};
		List<String> words = Arrays.asList(WORDS);
		TextComparator comparator = new TextComparator(); 
		Collections.sort(words, comparator);

        for (int i=0; i<words.size(); i++) {
        	System.out.println(words.get(i));
        }
	    */
	}

	public static int getNbLines(String sourceCode) {
		int nbLines = 0;
		
		int nb = sourceCode.length();
		for(int i=0; i<nb; i++) {
			nbLines += sourceCode.charAt(i) == '\n' ? 1 : 0;
		}
		
		return nbLines;
	}

	public static Text removeTags(String html) {
		String plainText = html.replaceAll("\\<.*?>","");
		return new Text(plainText);
	}

	public static int count(String text, char ch) {
		int nb = text.length();
		int count = 0;
		
		for (int i=0; i<nb; i++) {
			count += (text.charAt(i) == ch) ? 1 : 0; 
		}
		
		return count;
	}

	




	




}
