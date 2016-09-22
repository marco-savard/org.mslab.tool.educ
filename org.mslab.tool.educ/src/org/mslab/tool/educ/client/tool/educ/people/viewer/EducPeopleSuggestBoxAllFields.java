package org.mslab.tool.educ.client.tool.educ.people.viewer;

import java.util.List;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

public class EducPeopleSuggestBoxAllFields extends SuggestBox {
	
	EducPeopleSuggestBoxAllFields() {
		super(getGivenNameOracle());
		setWidth("9em");
		Style style = getElement().getStyle();
		style.setBorderStyle(BorderStyle.NONE);
		getElement().setPropertyString("placeholder", "Rechercher");
	}

	private static MultiWordSuggestOracle getGivenNameOracle() {
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		oracle.add("Alpha");
		oracle.add("Albert");
		oracle.add("Alex");
		return oracle;
	}

}
