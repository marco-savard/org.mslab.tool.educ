package org.mslab.tool.educ.client.core.ui;

import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.WhiteSpace;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;

public abstract class AbstractIconButton extends Button implements MouseOverHandler, MouseOutHandler {
	
	protected AbstractIconButton(String html, String title) {
		super(html);
		removeStyleName("gwt-Button");
		
		Style style = getElement().getStyle();
		style.setBorderStyle(BorderStyle.NONE);
		style.setBackgroundColor(Color.WHITE.toString());
		setTitle(title);
		
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