package org.mslab.commons.client.tool.educ.people.viewer;

import org.mslab.commons.client.tool.educ.people.viewer.FilteringOption.SearchField;

public class FilteringOption {
	public enum SearchField {ALL_FIELDS, GIVEN_NAME, FAMILY_NAME, PHONE_NUMBER};
	
	public String _filteringText;
	public SearchField _searchField;
	
	public FilteringOption(String filteringText, SearchField searchField) {
		_filteringText = filteringText;
		_searchField = searchField;
	}

}
