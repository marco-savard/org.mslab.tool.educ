package org.mslab.tool.educ.shared.types;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Line implements Serializable {
	int _x1, _y1, _x2, _y2;
	
	//GWT shared types require a parameterless constructor
	public Line() {}
	
	public Line(int x1, int y1, int x2, int y2) {
		_x1 = x1;
		_y1 = y1;
		_x2 = x2;
		_y2 = y2;
	}
	
	public int getHigherX() {
		return Math.max(_x1, _x2);
	}
	
	public int getHigherY() {
		return Math.max(_y1, _y2);
	}

	public int getX1() {
		return _x1;
	}

	public int getY1() {
		return _y1;
	}

	public int getX2() {
		return _x2;
	}

	public int getY2() {
		return _y2;
	}
}
