package org.mslab.commons.client.tool.educ.people.viewer.content;

import org.mslab.commons.client.core.ui.StyleUtil;
import org.mslab.commons.shared.types.Color;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;

public class PersonIcon extends SimplePanel {
	PersonIcon(Color colorIcon) {
		String text = "<i class=\"fa fa-user fa-3x\" aria-hidden=\"true\" ></i>";
		HTML html = new HTML(text);
		html.getElement().getStyle().setColor(colorIcon.toString());
		html.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		html.getElement().getStyle().setBorderWidth(1, Unit.PX);
		html.getElement().getStyle().setBorderColor(colorIcon.toString());
		html.getElement().getStyle().setPadding(2, Unit.PX);
		StyleUtil.setBorderRadius(html, "5px");
		add(html);
	}
}
