package org.mslab.tool.educ.client.core.ui;

import org.mslab.tool.educ.client.core.ui.panels.GridPanel;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

public class WordTag extends GridPanel {
	protected HTML _html;
	private int _nbOccurrences = 1;
	
	public WordTag(String text, int nbOccurrences) {
		_html = new HTML(text);
		_nbOccurrences = nbOccurrences;
		_grid.setHeight("100%");
		_grid.setWidget(0, 0, _html);
		_grid.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
	}

	//public void setNumberOfOccurences(int nbOccurrences) {
	//	_nbOccurrences = nbOccurrences;
	//}
	
	@Override
	public String toString() {
		String text = _html.getText();
		return text;
	}

	public int getNbOccurrences() {
		return _nbOccurrences;
	}

	public Label getLabel() {
		return _html;
	}

}
