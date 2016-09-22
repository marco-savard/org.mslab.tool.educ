package org.mslab.tool.educ.server.commonnames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.Base64Utils;

public class CommonNameFinder {
	private List<String> _maleGivenNames = null;
	private List<String> _femaleGivenNames = null;
	
	public static void main(String[] args) {
		CommonNameFinder finder = new CommonNameFinder(); 
		List<String> maleGivenNames = finder.getMaleGivenNames(); 
		for (int i=0; i<10; i++) {
			System.out.println(maleGivenNames.get(i)); 
		}
	}
	
	public List<String> getMaleGivenNames() {
		if (_maleGivenNames == null) {
			init();
		}
		
		return _maleGivenNames;
	}
	
	public List<String> getFemaleGivenNames() {
		if (_femaleGivenNames == null) {
			init();
		}
		
		return _femaleGivenNames;
	}
	
	private void init() {
		_maleGivenNames = new ArrayList<String>();
		_femaleGivenNames = new ArrayList<String>();
		
		String resname = "givenNames.txt"; 
		List<String> entries = readFromResource(resname); 
		for (String entry : entries) {
			String[] words = entry.split(";");
			String gender = words.length == 2 ? words[1].trim() : null;
			if ("m".equals(gender)) {
				_maleGivenNames.add(words[0].trim());
			} else if ("f".equals(gender)) { 
				_femaleGivenNames.add(words[0].trim());
			}
		}
	} //end init()
	
	public List<String> findCommonFamilyNames() {
		String resname = "familyNames.txt"; 
		List<String> commonFamilyNames = readFromResource(resname); 
		return commonFamilyNames;
	}
	

	
	private List<String> readFromResource(String resname) {
		URL url = CommonNameFinder.class.getResource(resname); 
		List<String> commonGivenNames;
		
		try {
			InputStream input = url.openStream();
			commonGivenNames = findInResource(input); 
			input.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			commonGivenNames = null;
		}
		
		return commonGivenNames;
	}
	
	

	
	private List<String> findInResource(InputStream input) throws IOException {
		InputStreamReader ir = new InputStreamReader(input, "UTF-8"); 
		BufferedReader br = new BufferedReader(ir); 
		List<String> entries = new ArrayList<String>();
		int bestMatch = 0;
		
		while (true) {
			String line = br.readLine();
			
			if (line == null) {
				break;
			} else {
				entries.add(line);
			}
			
			if (bestMatch == 6) {
				break;
			}
		}
		
		
		return entries;
	}





	

}
