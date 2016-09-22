package org.mslab.tool.educ.client.core.ui;

import org.mslab.tool.educ.client.core.ui.theme.ThematicColors;
import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Button;

public class CloseButton2 extends Button implements MouseOverHandler, MouseOutHandler {
	
	public CloseButton2() {
		removeStyleName("gwt-Button");
		String html = "<i class=\"fa fa-times\" fa-fw/>";
		setHTML(html);
		
		StyleUtil.setBorderRadius(this, "50%");
		Style style = getElement().getStyle();
		style.setBorderStyle(BorderStyle.SOLID);
		style.setBorderWidth(1, Unit.PX);
		style.setPadding(0, Unit.PX);
		
		addMouseOverHandler(this);
		addMouseOutHandler(this);
		refresh();
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		Style style = getElement().getStyle();
		style.setBackgroundColor(ThematicColors.COLOR_ALERT_FG.toString());
		style.setBorderColor(ThematicColors.COLOR_ALERT_FG.toString());
		style.setColor(Color.WHITE.toString());
		style.setCursor(Cursor.POINTER);
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		refresh();
	}

	private void refresh() {
		Style style = getElement().getStyle();
		style.setBackgroundColor(Color.WHITE_FLORAL.toString());
		style.setBorderColor(Color.GREY_LIGHT.toString());
		style.setColor(Color.GREY_DARK.toString());
		style.setCursor(Cursor.DEFAULT);
		
	}


}
