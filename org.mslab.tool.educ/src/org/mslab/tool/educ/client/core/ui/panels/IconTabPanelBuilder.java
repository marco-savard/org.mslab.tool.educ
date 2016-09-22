package org.mslab.tool.educ.client.core.ui.panels;

import java.util.ArrayList;
import java.util.List;

import org.mslab.tool.educ.client.core.ui.SelectionButton;
import org.mslab.tool.educ.client.core.ui.SelectionButtonGroup;
import org.mslab.tool.educ.client.core.ui.theme.AbstractTheme;
import org.mslab.tool.educ.client.tool.educ.school.viewer.content.AbstractContentViewer;
import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class IconTabPanelBuilder  {
	private List<IconTabPanelElement> _elements; 
	private SimplePanel _content; 
	
	public IconTabPanelBuilder() {
		_elements = new ArrayList<IconTabPanelElement>();
		_content = new SimplePanel();
	}

	public void add(Widget content, String text, String tooltip) {
		IconTabPanelElement element = new IconTabPanelElement(content, text, tooltip);
		_elements.add(element);	
	}
	
	public SelectionButtonGroup buildMenuPanel() {
		AbstractTheme theme = AbstractTheme.getTheme(); 
		SelectionButtonGroup selectionButtonGroup = new IconSelectionButtonGroup(theme.getPrimaryBgColor());		
		return selectionButtonGroup;
	}

	public SimplePanel getContentPanel() {
		return _content;
	}
	
	private class IconSelectionButtonGroup extends SelectionButtonGroup {

		protected IconSelectionButtonGroup(Color selectionColor) {
			super(selectionColor);

			for (IconTabPanelElement element : _elements) {
				SelectionButton btn = new SelectionButton(this, element._iconText); 
				btn.setTitle(element._tooltip);
				addItem(btn);
			}
			
			IconTabPanelElement element = _elements.get(0); 
			Widget widget = element._content; 
			_content.setWidget(widget);
		}

		@Override
		public void onSelectionChanged() {
			int selectedIdx = super.getSelectedIndex();
			IconTabPanelElement element = _elements.get(selectedIdx); 
			Widget widget = element._content; 
			_content.setWidget(widget);
			
			if (widget instanceof AbstractContentViewer) {
				AbstractContentViewer viewer = (AbstractContentViewer)widget;
			}
		}
	}
	
	private static class IconTabPanelElement {
		private Widget _content;
		private String _iconText;
		private String _tooltip; 
		
		public IconTabPanelElement(Widget content, String iconText, String tooltip) {
			_content = content;
			_iconText = iconText;
			_tooltip = tooltip;
		}
		
	}

}
