package org.mslab.commons.client.tool.educ;

import org.mslab.commons.client.core.ui.GradientBackground;
import org.mslab.commons.client.core.ui.StyleUtil;
import org.mslab.commons.client.core.ui.panels.GridPanel;
import org.mslab.commons.client.core.ui.theme.AbstractTheme;
import org.mslab.commons.client.core.ui.theme.ThematicColors;
import org.mslab.commons.client.core.ui.theme.ThemeChangeEvent;
import org.mslab.commons.client.core.ui.theme.ThemeChangeHandler;
import org.mslab.commons.shared.text.MessageFormat;
import org.mslab.commons.shared.types.Color;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

public class EducHeader extends GridPanel implements ThemeChangeHandler {
	
	public static final int HEIGHT = 75;
	private HeaderLabel _title;

	public EducHeader() {
		AbstractTheme theme = AbstractTheme.getTheme();
		theme.addThemeChangeHandler(this);
		setHeight(HEIGHT + "px"); 
		_grid.setWidth("100%");
		_grid.setHeight("100%");
		
		_title = new HeaderLabel("Les écoles du Québec"); 
		_grid.setWidget(0, 0, _title);
		_grid.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		_grid.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		
		refresh();
	}

	@Override
	public void onThemeChange(ThemeChangeEvent event) {
		refresh();
	}
	
	void refresh() {
		AbstractTheme theme = AbstractTheme.getTheme();
		Color bg = theme.getPrimaryBgColor();
		GradientBackground.setGradient(this, bg, bg.blendWith(Color.WHITE));
		_title.refresh();
	}
	
	public void update() {
		_title.update();
	}
	
	
	private static class HeaderLabel extends HTML {
		HeaderLabel(String text) {
			super(text);
			removeStyleName("gwt-HTML");
			Style style = getElement().getStyle();
			AbstractTheme theme = EducTheme.getTheme();
			style.setProperty("fontFamily", theme.getFontFamily());
			style.setFontWeight(FontWeight.LIGHTER);
			style.setColor(ThematicColors.COLOR_INFO_FG.toString());
			style.setFontSize(300, Unit.PCT);
			refresh();
		}

		public void update() {
		}

		public void refresh() {
			Style style = getElement().getStyle();
			style.setFontSize(300, Unit.PCT);
		}
	}




}
