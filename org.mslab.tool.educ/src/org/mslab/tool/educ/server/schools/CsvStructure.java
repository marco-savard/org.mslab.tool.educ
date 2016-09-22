package org.mslab.tool.educ.server.schools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvStructure {
	private Map<String, Integer> _columns = new HashMap<String, Integer>(); 
	private List<String> _columnList = new ArrayList<String>();
	private List<String> _rows = new ArrayList<String>();
	
	public void addRow(String line) {
		_rows.add(line); 
	}
	
	public void addColumn(String column, int idx) {
		_columns.put(column, idx); 
		_columnList.add(column);
	}

	public List<String> getRows() {
		return _rows;
	}

	public Integer getColumnIdx(String column) {
		return _columns.get(column);
	}

	public List<String> getColumns() {
		return _columnList;
	}


}
