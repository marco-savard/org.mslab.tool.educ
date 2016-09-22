package org.mslab.tool.educ.client.core.ui.icons;

import org.mslab.tool.educ.shared.types.Color;

public class FolderIcon extends AbstractStackIcon {
	
	public FolderIcon() {
		addLayer("fa-folder-open", Color.YELLOW_KHAKI.toString());
		addLayer("fa-folder-open-o", Color.BLACK.toString());
		render();
	}

}
