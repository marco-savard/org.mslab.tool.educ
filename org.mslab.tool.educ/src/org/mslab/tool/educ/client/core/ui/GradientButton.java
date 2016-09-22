package org.mslab.tool.educ.client.core.ui;

import java.util.ArrayList;
import java.util.List;

import org.mslab.tool.educ.client.core.ui.panels.GradientPanel;
import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

public class GradientButton extends GradientPanel implements MouseOverHandler, MouseOutHandler, ClickHandler {
	private static final Color DEFAULT_BORDER_COLOR = new Color(148, 148, 148); 
	//private List<ClickHandler> _handlers = new ArrayList<ClickHandler>(); 
	private Label _label; 
	private boolean _enabled = true;
	
	public GradientButton(String text, String startGradientColor, String endGradientColor) {
		super(startGradientColor, endGradientColor);
		_grid.setSize("100%", "100%");
		
		_label = new HTML(text); 
		_grid.setWidget(0, 0, _label);
		_grid.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER); 
		_grid.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE); 
		
		DOM.setStyleAttribute(this.getElement(), "borderColor", DEFAULT_BORDER_COLOR.toString()); 
		DOM.setStyleAttribute(this.getElement(), "borderRadius", "15px"); 
		DOM.setStyleAttribute(this.getElement(), "borderStyle", "solid"); 
		DOM.setStyleAttribute(this.getElement(), "borderWidth", "1px"); 
		
		_label.addMouseOverHandler(this);
		_label.addMouseOutHandler(this);
		_label.addClickHandler(this); 
	}
	
	@Override
	public void onMouseOver(MouseOverEvent event) {
		boolean enabled = isEnabled();
		String cursor = enabled ? "pointer" : "default"; 
		DOM.setStyleAttribute(this.getElement(), "cursor", cursor);
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		DOM.setStyleAttribute(this.getElement(), "cursor", "default");
	}
	
	@Override
	public void onClick(ClickEvent event) {
		
	}
	
	/*
	@Override
	public void onClick(ClickEvent event) {
		if (isEnabled()) {
			ClickEvent newEvent = new GradientButtonClickEvent(this);
			
			for (ClickHandler handler : _handlers) {
				handler.onClick(newEvent); 
			}
		}
	}*/
	
	/*
	public void addClickHandler(ClickHandler handler) {
		_handlers.add(handler); 
	}
	*/
	
	public void setEnabled(boolean enabled) {
		_enabled = enabled;
		refreshButton();
	}

	public boolean isEnabled() {
		return _enabled;
	}
	
	public Label getLabel() {
		return _label;
	}
	
	public void setText(String text) {
		_label.setText(text); 
	}
	
	private void refreshButton() {
		// TODO Auto-generated method stub
		
	}

	//
	// inner class
	//
	private static class GradientButtonClickEvent extends ClickEvent {
		public GradientButtonClickEvent(GradientButton btn) {
			this.setSource(btn);
		}
	}
}
