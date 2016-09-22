package org.mslab.tool.educ.client.core.ui.icons;

import org.mslab.tool.educ.shared.types.Color;

public class DeleteIcon extends AbstractStackIcon {
	
	public DeleteIcon() {
		addLayer("fa-trash fa-lg", Color.GREEN_CHARTREUSE.toString());
		addLayer("fa-trash-o fa-lg", Color.BLACK.toString());
		render();
	}

}
