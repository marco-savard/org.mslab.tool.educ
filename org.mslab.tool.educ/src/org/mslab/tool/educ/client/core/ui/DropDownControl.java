package org.mslab.tool.educ.client.core.ui;

import org.mslab.tool.educ.client.core.ui.panels.FocusVerticalPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

//used by FontPicker
public class DropDownControl extends FocusVerticalPanel implements MouseOverHandler, MouseOutHandler, ClickHandler {
	private static final String SPINNER_BOTTOM_BG_COLOR_NORMAL = "#dddddd";
	private static final String SPINNER_BG_COLOR_HOVER = "#aaaaaa";
	private static final String SPINNER_BG_COLOR_DISABLED = "#ffffff";
	private static final String ARROW_DOWN = "&#x25bc;";
	
	private Widget _owner;
	private PopupPanel _popup;
	private boolean _enabled = true;
	
	public DropDownControl(Widget owner, PopupPanel popup) {
		_owner = owner;
		_popup = popup;
		
		HTML arrow = new HTML(ARROW_DOWN); 
		arrow.setWidth("100%");
		arrow.setHeight("100%");
		arrow.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER); 
		add(arrow); 
		
		getElement().getStyle().setBackgroundColor(SPINNER_BOTTOM_BG_COLOR_NORMAL);
		
		addMouseOverHandler(this); 
		addMouseOutHandler(this); 
		addClickHandler(this); 
		refresh();
	}
	
	public void setEnabled(boolean enabled) {
		_enabled = enabled;
		refresh();
	}

	public void refresh() {
		String bgColor = _enabled ? SPINNER_BOTTOM_BG_COLOR_NORMAL : SPINNER_BG_COLOR_DISABLED;
		DOM.setStyleAttribute(getElement(), "backgroundColor", bgColor);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (_enabled) {
			_popup.show();
			int left = _owner.getAbsoluteLeft(); 
			int bottom = _owner.getAbsoluteTop() + _owner.getOffsetHeight();
	        _popup.setPopupPosition(left, bottom);
		}
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		refresh();
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		String bgColor = _enabled ? SPINNER_BG_COLOR_HOVER : SPINNER_BG_COLOR_DISABLED;
		DOM.setStyleAttribute(getElement(), "backgroundColor", bgColor);
	}

	

}
