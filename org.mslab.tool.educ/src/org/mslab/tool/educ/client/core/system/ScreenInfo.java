package org.mslab.tool.educ.client.core.system;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;

public class ScreenInfo {
	private int _width, _height, _diagonal;
	private int _area;
	private String _aspectRatio;

	public static ScreenInfo create() {
		ScreenInfo info = new ScreenInfo();
		return info;
	}
	
	private ScreenInfo() {
		_width = Window.getClientWidth();
		_height = Window.getClientHeight();
		_area = _width * _height;
		_diagonal = (int)Math.sqrt((_width * _width) + (_height * _height));
		
		double heightRatio = (double)_height /_width;
		NumberFormat f = NumberFormat.getFormat("0.00");
		String s = f.format(heightRatio); 
		_aspectRatio = "1:" + s;
	}

	public int getWidth() {
		return _width;
	}

	public int getHeight() {
		return _height;
	}

	public int computeDiagonal() {
		return _diagonal;
	}
	
	public int computeArea() {
		return _area;
	}

	public String computeAspectRatio() {
		return _aspectRatio;
	}

}
