package org.mslab.tool.educ.client.core.ui;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.HTML;

public abstract class MouseOverHTML extends HTML implements MouseOverHandler, MouseOutHandler {
	
	protected MouseOverHTML( String html) {
		super(html);
		addMouseOverHandler(this);
		addMouseOutHandler(this);
	}
	
	@Override
	public void onMouseOver(MouseOverEvent event) {
		getElement().getStyle().setCursor(Cursor.POINTER);
	}
	
	@Override
	public void onMouseOut(MouseOutEvent event) {
		getElement().getStyle().setCursor(Cursor.DEFAULT);
	}
}
