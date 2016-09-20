package org.mslab.commons.client.tool.educ.school.explorer;

import java.util.List;

import org.mslab.commons.shared.text.MessageFormat;

//
// inner classes
//
class FilteredCollection<T> {
	private String _name;
	private List<T> _elements; 
	
	FilteredCollection(String name, List<T> elements) {
		_name = name;
		_elements = elements;
	}

	public String getName() {
		return _name;
	}

	public List<T> getElements() {
		return _elements;
	}	
	
	@Override
	public String toString() {
		String text = MessageFormat.format("{0} ({1})", new Object[] {_name, _elements.size()});
		return text;
	}
}