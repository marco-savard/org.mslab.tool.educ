package org.mslab.tool.educ.server.schools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CsvReader {
	private CsvStructure _structure = new CsvStructure(); 
	private InputStream _input;
	private String _format; 
	
	public CsvReader(InputStream input, String format) {	
		_input = input;
		_format = format; 
	}
	
	public CsvStructure read() throws IOException {
		InputStreamReader ir = new InputStreamReader(_input, _format); 
		BufferedReader br = new BufferedReader(ir); 
		readHeader(br); 
		
		while (true) {
			String line = br.readLine();

			if (line == null) {
				break;
			} else {
				readRow(line);
			}
		}
		
		ir.close();
		return _structure;
	}

	private void readRow(String line) {
		_structure.addRow(line);
	}

	private void readHeader(BufferedReader br)  throws IOException {
		String line = br.readLine();
		String[] fields = line.split(";"); 	
		int idx = 0; 
		
		for (String field : fields) {
			String column = trimQuotes(field);
			_structure.addColumn(column, idx); 
			idx++;
		}
	}
	
	private String trimQuotes(String original) {
		String trimmed = original.substring(1, original.length()-1); 
		return trimmed;
	}
	
	
	
	public int getIndexOf(String column) {
		Integer value = _structure.getColumnIdx(column); 
		int idx = (value == null) ? -1 : value; 
		return idx;
	}
}
