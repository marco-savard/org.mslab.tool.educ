package org.mslab.tool.educ.client.core.ui.slider;

import java.util.ArrayList;
import java.util.List;

import org.mslab.tool.educ.client.core.ui.StyleUtil;
import org.mslab.tool.educ.client.core.ui.panels.GridPanel;
import org.mslab.tool.educ.shared.types.Color;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class Slider2 extends GridPanel implements MouseMoveHandler, MouseDownHandler, MouseUpHandler, MouseOverHandler, MouseOutHandler, ClickHandler {
	private FocusPanel _track, _glider;
	private int _min, _max, _currentValue; 
	private double _ratio; //between 0 and 1
	private boolean _mouseOnGlider = true, _mouseDown = false;
	private List<SliderListener2> _listeners = new ArrayList<SliderListener2>(); 
	
	public Slider2(String name, int min, int max, int initValue) {
		_min = min;
		_max = max;
		_ratio = (double)initValue / (max - min);  
		
		_grid.setWidth("100%");
		_grid.setHeight("2em");
		int row = 0;
		
		_track = new SliderTrack();
		_track.addClickHandler(this);
		_grid.setWidget(row, 0, _track);
		row++;
		
		_glider = new SliderGlider();
		_glider.getElement().getStyle().setMarginTop(-36, Unit.PX);
		
		_glider.addMouseOverHandler(this); 
		_glider.addMouseOutHandler(this);
		_grid.setWidget(row, 0, _glider);
		row++;
		
		addMouseMoveHandler(this);
		addMouseDownHandler(this);
		addMouseUpHandler(this);
	}

	public void addListener(SliderListener2 listener) {
		_listeners.add(listener); 
	}
	
	private static class SliderTrack extends FocusPanel {
		public SliderTrack() {
			setHeight("24px");
			setWidth("100%");
			Style style = getElement().getStyle();
			style.setBorderWidth(2, Unit.PX);
			style.setBorderColor(Color.GREY_SILVER.toString());
			style.setBorderStyle(BorderStyle.SOLID);
			StyleUtil.setBorderRadius(this, "5px");
		}
	}
	
	private static class SliderGlider extends FocusPanel {
		public SliderGlider() {
			setHeight("32px");
			setWidth("32px");
			Style style = getElement().getStyle();
			style.setBackgroundColor(Color.GREY_SILVER.toString());
			style.setBorderWidth(2, Unit.PX);
			style.setBorderColor(Color.GREY_DARK.toString());
			style.setBorderStyle(BorderStyle.SOLID);
			style.setCursor(Cursor.POINTER);
			StyleUtil.setBorderRadius(this, "5px");
		}
	}
	
	@Override
	public void onClick(ClickEvent event) {
		int x = event.getX();
		glideAt(x); 
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		_mouseDown = false;
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		_mouseDown = true;
	}
	
	@Override
	public void onMouseOver(MouseOverEvent event) {
		_mouseOnGlider = true; 
		StyleUtil.setBoxShadow(_glider, "3px 3px 3px #a0a0a0");
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		_mouseOnGlider = true; 
		StyleUtil.setBoxShadow(_glider, "0px 0px 0px");
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		if (_mouseOnGlider && _mouseDown) {
			int x = event.getX();
			glideAt(x); 
		}
	}
	
	private void glideAt(int x) {
		int width = getOffsetWidth();
		
		_ratio = (double)x / width;
		_ratio = (_ratio > 1.0) ? 1.0 : _ratio;
		_currentValue = (int)(_min + (_max - _min) * _ratio);
		
		fireEvent();
		refresh(); 
	}

	private void fireEvent() {
		int[] values = new int[] {_currentValue};
		SliderEvent2 event = new SliderEvent2(this, values); 
		
		for (SliderListener2 listener : _listeners) {
			listener.onSlide(event); 
		}
	}

	public void refresh() {
		int width = getOffsetWidth();
		int trackingWidth = width - 40;
		int marginLeft = (int)(_ratio * trackingWidth);
		_glider.getElement().getStyle().setMarginLeft(marginLeft, Unit.PX);	
	}




}
