package org.mslab.tool.educ.client.core.ui;

import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;

public class ExpandWidget extends MouseOverHTML {
	private boolean _expanded = false;
	
	public ExpandWidget() {
		super("&raquo;"); 
		
		//rotating widget
		setSize("24px", "24px");
		getElement().getStyle().setFontSize(150, Unit.PCT);
		getElement().getStyle().setMarginTop(-6, Unit.PX);
		getElement().getStyle().setTextAlign(TextAlign.CENTER);
		StyleUtil.transition(this, "all 0.4s ease-in-out");	
	}

	public boolean isExpanded() {
		return _expanded;
	}
	
	public void setExpanded(boolean expanded) {
		_expanded = expanded;
		refresh();
	}
	
	private void refresh() {
		StyleUtil.rotate(this, _expanded ? 90 : 0);
	}

}
