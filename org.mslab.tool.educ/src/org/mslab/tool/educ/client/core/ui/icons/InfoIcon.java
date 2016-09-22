package org.mslab.tool.educ.client.core.ui.icons;

import org.mslab.tool.educ.shared.types.Color;

public class InfoIcon extends AbstractStackIcon {
	
	public InfoIcon() {
		addLayer("fa-circle fa-lg", Color.BLUE_LIGHT_STEEL.toString());
		addLayer("fa-circle-o fa-lg", Color.BLACK.toString());
		addLayer("fa-info", Color.BLUE_DARK.toString(), 6, 0);
		render();
	}

}
