package org.mslab.tool.educ.client.core.ui.icons;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.HTML;

public abstract class AbstractIcon extends HTML implements MouseOverHandler  {
	
	protected AbstractIcon() {
		addMouseOverHandler(this);
	} //end AbstractIcon()
	
	@Override
	public void onMouseOver(MouseOverEvent event) {
		getElement().getStyle().setCursor(Cursor.DEFAULT);
	}


} //end AbstractIcon
