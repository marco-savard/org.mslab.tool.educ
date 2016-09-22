package org.mslab.tool.educ.client.core.ui.icons;

import org.mslab.tool.educ.shared.types.Color;

public class StarIcon extends AbstractStackIcon {
	
	public StarIcon() {
		addLayer("fa-star", Color.YELLOW_KHAKI.toString());
		addLayer("fa-star-o", Color.BLACK.toString());
		render();
	}

}
