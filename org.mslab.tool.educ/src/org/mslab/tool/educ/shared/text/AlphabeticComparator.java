package org.mslab.tool.educ.shared.text;

import java.util.Comparator;

public class AlphabeticComparator implements Comparator<String> {
	
	 public int compare(String word1, String word2) {
		 word1 = Text.toUnaccentued(word1.toLowerCase());
		 word2 = Text.toUnaccentued(word2.toLowerCase());
		 int comparison = word1.compareToIgnoreCase(word2); 
		 return comparison;
	 }

}
