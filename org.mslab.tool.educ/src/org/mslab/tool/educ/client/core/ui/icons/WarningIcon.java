package org.mslab.tool.educ.client.core.ui.icons;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;

public class WarningIcon extends AbstractIcon {

	public WarningIcon() {
		String html = "<span class=\"fa-stack fa-stack-lg\">"; 
		html += "<i class=\"fa fa-play fa-stack-2x fa-rotate-270\"></i>";
		html += "<i style=\"color:#ddbb00\" class=\"fa fa-warning fa-stack-2x\"></i>";
		html += "</span>";
		super.setHTML(html);

		getElement().getStyle().setFontSize(60, Unit.PCT);
	}
}
