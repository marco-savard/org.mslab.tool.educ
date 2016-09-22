package org.mslab.tool.educ.client.core.ui.panels;

import java.util.ArrayList;
import java.util.List;

import org.mslab.tool.educ.client.core.ui.SelectionButton;
import org.mslab.tool.educ.client.core.ui.SelectionButtonGroup;
import org.mslab.tool.educ.client.core.ui.StyleUtil;
import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class IconTabLayoutPanelOld extends SimplePanel {
	private IconTabLayoutPanelContent _content; 
	private int _selectedIndex = -1;
	
	private IconTabLayoutPanelOld(int barHeight, Unit barUnit, Color selectionColor) {
		_content = new IconTabLayoutPanelContent(this, barHeight, barUnit, selectionColor);
		add(_content);
	}
	
	public void add(Widget widget, String html, boolean asHtml) {
		_content.add(widget, html, asHtml);
	}
	
	public void selectTab(int index) {
		_content.selectTab(index);
		_selectedIndex = index;
	}
	
	public void addSelectionHandler(SelectionHandler<Integer> handler) {
		_content.addSelectionHandler(handler);
	}
	
	public int getSelectedIndex() {
		return _selectedIndex;
	}
	
	
	protected IconTabLayoutPanelContent getContentPanel() {
		return _content;
	}

	//
	// inner class
	// 
	public static class IconTabLayoutPanelContent extends GridPanel { 
		private IconTabLayoutPanelOld _parent;
		private TabHeader _header; 
		private SimplePanel _content; 
		private List<SelectionHandler<Integer>> _handlers = new ArrayList<SelectionHandler<Integer>>();
		private List<Widget> _children = new ArrayList<Widget>(); 
		
		IconTabLayoutPanelContent(IconTabLayoutPanelOld parent, int barHeight, Unit barUnit, Color selectionColor) {
			_parent = parent; 
			int row = 0;
			//_grid.setCellPadding(0);
			_grid.setCellSpacing(0);
			_grid.setWidth("100%");
			_grid.setHeight("100%");
			StyleUtil.setBorderRadius(this, "2px");
		
			_header = new TabHeader(this, selectionColor); 
			_grid.setWidget(row, 0, _header);
			_grid.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
			row++; 
			
			_content = new SimplePanel(); 
			_grid.setWidget(row, 0, _content);
			row++;
		}
		
		void add(Widget widget, String html, boolean asHtml) {
			_header.addTab(html); 
			_children.add(widget);
		}
		
		public void selectTab(int index) {
			Widget child = (index >= 0) ? _children.get(index) : null;
			_header.setSelectedItem(index);
			_content.setWidget(child);
		}
		
		public void addSelectionHandler(SelectionHandler<Integer> handler) {
			_handlers.add(handler);
		}

		public SimplePanel getHeaderPanel() {
			return _header;
		}
		
	}
	
	private static class TabHeader extends SelectionButtonGroup {
		private IconTabLayoutPanelContent _parent;
		
		TabHeader(IconTabLayoutPanelContent parent, Color selectionColor) {
			super(selectionColor);
			_parent = parent;
		}

		public void addTab(String html) {
			addItem(new SelectionButton(this, html));
		}

		@Override
		public void onSelectionChanged() {
			int selectedIdx = getSelectedIndex();
			_parent._parent.selectTab(selectedIdx);
		}
	}

	

}
