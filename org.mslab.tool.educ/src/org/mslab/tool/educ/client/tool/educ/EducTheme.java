package org.mslab.commons.client.tool.educ;

import org.mslab.commons.client.core.ui.theme.AbstractTheme;
import org.mslab.commons.client.core.ui.theme.ThematicColors;
import org.mslab.commons.shared.types.Color;

public class EducTheme extends AbstractTheme {
	private static final Color PRIMARY_COLOR = ThematicColors.COLOR_INFO_FG;
	
	public EducTheme() {
		setPrimaryFgColor(PRIMARY_COLOR);
	}

	public Color getSelectionColor() {
		return Color.YELLOW;
	}
	
	public String getFontFamily() {
		return "Roboto, sans-serif";
	}

}
