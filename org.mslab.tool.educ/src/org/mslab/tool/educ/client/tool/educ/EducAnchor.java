package org.mslab.commons.client.tool.educ;

import org.mslab.commons.shared.types.Color;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Anchor;

public class EducAnchor extends Anchor implements MouseOverHandler, MouseOutHandler {
	
	public EducAnchor() {
		this("");
	}

	public EducAnchor(String html) {
		this.setHTML(html);
		EducTheme theme = (EducTheme)EducTheme.getTheme();
		Color fgColor = theme.getPrimaryFgColor();
		getElement().getStyle().setColor(fgColor.toString());
		addMouseOverHandler(this); 
		addMouseOutHandler(this); 
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		getElement().getStyle().setCursor(Cursor.DEFAULT);
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		getElement().getStyle().setCursor(Cursor.POINTER);
	}

}
