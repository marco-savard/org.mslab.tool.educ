package org.mslab.tool.educ.client.core.ui;

import org.mslab.tool.educ.client.tool.educ.EducTheme;
import org.mslab.tool.educ.shared.text.MessageFormat;
import org.mslab.tool.educ.shared.text.Text;

import com.google.gwt.user.client.ui.HTML;

public class RangeHTML extends HTML {
	private String _selectionColor; 
	
	public RangeHTML(String selectionColor) {
		_selectionColor = selectionColor;
		super.setWordWrap(false);
		EducTheme theme = (EducTheme)EducTheme.getTheme();
		String fontFamily = theme.getFontFamily();
		this.getElement().getStyle().setProperty("fontFamily", fontFamily);
	}
	
	public void setRangeHTML(String text) {
		setRangeHTML(text, -1, 0);
	}	
	
	public void setRangeHTML(String text, int idx, int len) {
		if (idx == -1) {
			String html = Text.toHtml(text); 
			super.setHTML(html);
		} else {
			len = (idx + len > text.length()) ? 0 : len;
			idx = (idx >= text.length()) ? text.length() - 1 : idx;
			String prefix = text.substring(0, idx);
			String middle = text.substring(idx, idx+len);
			String suffix = text.substring(idx+len);
			text = MessageFormat.format("{0}<span style=\"background-color: {1};font-weight:bold;text-decoration:underline\">{2}</span>{3}",
				new Object[] {prefix, _selectionColor, middle, suffix});
			String html = Text.toHtml(text); 
			super.setHTML(html);
		}
	}


} //end RangeLabel
