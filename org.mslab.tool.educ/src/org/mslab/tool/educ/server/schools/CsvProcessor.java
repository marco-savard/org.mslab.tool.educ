package org.mslab.tool.educ.server.schools;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public abstract class CsvProcessor {
	protected CsvStructure _structure;
	protected List<String> _columns = new ArrayList<String>(); 

	protected CsvProcessor(CsvStructure structure) {
		_structure = structure;
	}

	public void processRows() {
		List<String> rows = _structure.getRows();
		for (String row : rows) {
			processRow(row); 
		}
	}

	protected abstract void processRow(String row);

	protected String getValue(String[] values, String column) {
		int idx = _structure.getColumnIdx(column); 
		String value = trim(values[idx]); 
		return value;
	}

	private String trim(String original) {
		String trimmed = original.substring(1, original.length()-1); 
		return trimmed;
	}

	public void addColumn(String column) {
		_columns.add(column);
	}
	
	protected List<String> getColumns() {
		return _columns;
	}

	public abstract void processHeader();

	
	
	


}
