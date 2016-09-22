package org.mslab.tool.educ.client.core.ui;

import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;

public class SelectionButton extends MouseOverHTML implements ClickHandler {
	private SelectionButtonGroup _owner;
	private Style _style;
	//private int _idx;
	private boolean _selected = false;
	
	public SelectionButton(SelectionButtonGroup owner, String html) {
		super(html);
		_owner = owner;
		
		_style = getElement().getStyle();
		_style.setBorderStyle(BorderStyle.SOLID);
		_style.setBorderWidth(2, Unit.PX);
		_style.setPadding(2, Unit.PX);
		
		addClickHandler(this); 
		refresh();
	}
	
	public void setSelected(boolean selected) {
		_selected = selected;
		refresh();
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		super.onMouseOver(event);
		_style.setBackgroundColor(_owner.getSelectionColor().toString());
		_style.setColor(Color.WHITE.toString());
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		super.onMouseOut(event);
		refresh();
	}

	@Override
	public void onClick(ClickEvent event) {
		_owner.setSelectedItem(this);
	}
	
	private void refresh() {
		Color color = _selected ? Color.GREY_DARK : Color.GREY_SILVER;
		_style.setColor(color.toString());
		_style.setBackgroundColor(Color.WHITE.toString());
	}

	
}
