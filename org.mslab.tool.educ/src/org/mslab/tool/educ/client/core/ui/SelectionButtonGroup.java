package org.mslab.tool.educ.client.core.ui;

import java.util.ArrayList;
import java.util.List;

import org.mslab.tool.educ.client.core.ui.panels.GridPanel;
import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public abstract class SelectionButtonGroup extends GridPanel implements ClickHandler {
	private List<SelectionButton> _buttons = new ArrayList<SelectionButton>();
	
	//selectio  idx
	private int _selectedIdx = 0;
	public int getSelectedIndex() { return _selectedIdx; }

	//selection color
	private Color _selectionColor;
	public Color getSelectionColor() { return _selectionColor; }
	public void setColor(Color color) { _selectionColor = color; }
	
	protected SelectionButtonGroup(Color selectionColor) {
		_selectionColor = selectionColor;
		//_grid.setCellPadding(0);
		//_grid.setCellSpacing(0);
		//refresh();
	}
	
	protected void addItem(SelectionButton selectionButton) {
		_buttons.add(selectionButton); 
		selectionButton.addClickHandler(this); 
		refresh();
	}
	
	private void refresh() {
		_grid.clear(true);
		
		for (int i=0; i<_buttons.size(); i++) {
			SelectionButton btn = _buttons.get(i); 
			btn.setSelected(_selectedIdx == i);
			_grid.setWidget(0, i, btn);
			//_grid.getFlexCellFormatter().setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
			//_grid.getFlexCellFormatter().setVerticalAlignment(0, i, HasVerticalAlignment.ALIGN_MIDDLE);
			
		}
	}

	public void setSelectedItem(int index) {
		_selectedIdx = index;
		refresh();
	}
	
	public boolean isSelectedItem(int selectedIdx) {
		boolean selected = (_selectedIdx == selectedIdx); 
		return selected;
	}
	
	public void setItemSize(String width, String height) {
		for (SelectionButton btn : _buttons) {
			btn.setSize(width, height);
		}
	}
	
	public void setSelectedItem(SelectionButton selectionButton) {
		_selectedIdx = _buttons.indexOf(selectionButton);
		refresh();
	}
	
	@Override
	public void onClick(ClickEvent ev) {
		Object src = ev.getSource();
		if (src instanceof SelectionButton) {
			SelectionButton selectionButton = (SelectionButton)src;
			_selectedIdx = _buttons.indexOf(selectionButton);
			onSelectionChanged();
			
			for (SelectionChangeHandler handler : _handlers) {
				handler.handleSelectionChange();
			}
		}
	}
	
	public abstract void onSelectionChanged();
	
	public void addSelectionChangeHandler(SelectionChangeHandler handler) {
		_handlers.add(handler); 
	}
	private List<SelectionChangeHandler> _handlers = new ArrayList<SelectionChangeHandler>();
	
	public static interface SelectionChangeHandler {

		void handleSelectionChange();
		
	}
}
