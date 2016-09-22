package org.mslab.tool.educ.client.core.ui.icons;

import org.mslab.tool.educ.shared.types.Color;

public class CheckBoxIcon extends AbstractStackIcon {
	
	public CheckBoxIcon() {
		addLayer("fa-square-o", Color.BLACK.toString());
		addLayer("fa-check", Color.GREEN_DARK.toString(), 2, -2);
		render();
	}

}
