package org.mslab.tool.educ.client.tool.educ;

import org.mslab.tool.educ.client.core.ui.theme.AbstractTheme;
import org.mslab.tool.educ.client.core.ui.theme.ThematicColors;
import org.mslab.tool.educ.shared.types.Color;

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
