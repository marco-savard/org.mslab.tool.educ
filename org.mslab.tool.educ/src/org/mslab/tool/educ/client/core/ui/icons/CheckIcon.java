package org.mslab.tool.educ.client.core.ui.icons;

import org.mslab.tool.educ.shared.text.MessageFormat;
import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;

public class CheckIcon extends AbstractIcon {

	public CheckIcon() {
		String pat = "<span style=\"color:{0}\" class=\"fa fa-check fa-lg \"></span>";
		String html = MessageFormat.format(pat, new Object[] {Color.GREEN_DARK.toString()}); 
		super.setHTML(html);
	}
}
