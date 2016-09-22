package org.mslab.tool.educ.client.tool.educ.ui;

import org.mslab.tool.educ.client.tool.educ.EducAnchor;
import org.mslab.tool.educ.client.tool.educ.EducTheme;
import org.mslab.tool.educ.shared.text.MessageFormat;
import org.mslab.tool.educ.shared.text.Text;
import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.user.client.ui.Anchor;

public class RangeAnchor<T> extends EducAnchor {
	private T _item; 
	private Color _selectionColor; 
	
	public RangeAnchor(T item, Color selectionColor) {
		_item = item;
		_selectionColor = selectionColor;
		
		EducTheme theme = (EducTheme)EducTheme.getTheme();
		Color fg = theme.getPrimaryFgColor();
		getElement().getStyle().setColor(fg.toString());
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

	public T getItem() {
		return _item;
	}


}