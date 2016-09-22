package org.mslab.tool.educ.server.services;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.mslab.tool.educ.shared.types.Captcha;
import org.mslab.tool.educ.shared.types.Line;

public class CaptchaBuilder {
	private int _width, _height;
	private int _nbChars;
	private transient String _plainText; //never transmit!
	private Map<String, Point> _points = new HashMap<String, Point>();
	private List<Line> _lines = new ArrayList<Line>();

	public CaptchaBuilder(int width, int height) {
		_width = width;
		_height = height;
	}
	
	public void chooseWord(int nbChars) {
		_plainText = chooserWord(nbChars);
		_nbChars = nbChars;
		
		//draw each letter
		int nb = _plainText.length();
		for (int i=0; i<nb; i++) {
			char ch = _plainText.charAt(i);
			setOffset(i);
			drawLetter(ch); 
		} 
	}

	public void addNoise(int nb) {
		int width = 100 * nb;
		Random r = new Random();
		
		for (int i=0; i<nb*4; i++) {
			int x = (int)(r.nextDouble() * width); 
			int y = (int)(r.nextDouble() * 100); 
			int x1 = x + v(10);
			int x2 = x + v(10);
			int y1 = y + v(10);
			int y2 = y + v(10);
			Line line = new Line(x1, y1, x2, y2);
			_lines.add(line); 
		}
	} //end addNoise()

	public Captcha build() {
		String code = encode();
		long hash = SecureHashing.getInstance().hashCode(_plainText);
		_plainText = null; //erase plain text once we obtain code and hash
		
		Captcha captcha = new Captcha(_width, _height, _nbChars, code, hash);
		return captcha;
	}
	
	//
	// private methods
	//
	private String chooserWord(int nbChars) {
		StringBuffer buf = new StringBuffer(); 
		Random r = new Random(); 
		
		for (int i=0; i<nbChars; i++) {
			char ch = (char)(65 + r.nextDouble()*26);
			buf.append(ch);	
		}

		return buf.toString();
	}
	
	private int _offset;
	private void setOffset(int offset) {
		_offset = offset;
	}
	
	private void drawLetter(char ch) {
		switch (ch) {
		case 'A':
			drawLetterA();
			break;
		case 'B':
			drawLetterB();
			break;
		case 'C':
			drawLetterC();
			break;
		case 'D':
			drawLetterD();
			break;
		case 'E':
			drawLetterE();
			break;
		case 'F':
			drawLetterF();
			break;
		case 'G':
			drawLetterG();
			break;
		case 'H':
			drawLetterH();
			break;
		case 'I':
			drawLetterI();
			break;
		case 'J':
			drawLetterJ(); 
			break;
		case 'K':
			drawLetterK();
			break;
		case 'L':
			drawLetterL();
			break;
		case 'M':
			drawLetterM();
			break;
		case 'N':
			drawLetterN();
			break;
		case 'O':
			drawLetterO();
			break;
		case 'P':
			drawLetterP();
			break;
		case 'Q':
			drawLetterQ(); 
			break;
		case 'R':
			drawLetterR();
			break;
		case 'S':
			drawLetterS();
			break;
		case 'T':
			drawLetterT();
			break;
		case 'U':
			drawLetterU();
			break;
		case 'V':
			drawLetterV();  
			break;
		case 'W':
			drawLetterW();
			break;
		case 'X':
			drawLetterX(); 
			break;
		case 'Y':
			drawLetterY();
			break;
		case 'Z':
			drawLetterZ();
			break;
		default:
			break;
		}
	} //end drawLetter()
	
	private void drawLetterA() {
		addPoint("p1", 50+v(10), 20+v(10)); 
		addPoint("p2", 20+v(10), 80+v(10));
		addPoint("p3", 80+v(10), 80+v(10));
		addMidPoint("mid1", "p1", "p2");
		addMidPoint("mid2", "p1", "p3");
		
		drawLine("p1", "p2"); 
		drawLine("p1", "p3");
		drawLine("mid1", "mid2"); 
	}
	
	private void drawLetterB() {
		int x = 20;
		addPoint("ctr", x, 35); 
		drawArc("ctr", 30, 8, 0, 180); 
		
		addPoint("ctr", x, 65+v(5)); 
		drawArc("ctr", 30, 8, 0, 180); 
		
		addPoint("pt1", x, 20+v(5)); 
		addPoint("pt2", x, 80+v(5)); 
		drawLine("pt1", "pt2"); 
	}
	
	private void drawLetterC() {
		addPoint("ctr", 80+v(5), 50+v(5)); 
		drawArc("ctr", 30, 15, 180, 360); 
	}
		
	private void drawLetterD() {
		addPoint("ctr", 20, 50); 
		drawArc("ctr", 30, 15, 0, 180); 
		
		addPoint("pt1", 20+v(5), 20+v(5)); 
		addPoint("pt2", 20+v(5), 80+v(5)); 
		drawLine("pt1", "pt2"); 
	}

	private void drawLetterE() {
		addPoint("pt1", 50+v(10), 20+v(10)); 
		addPoint("pt2", 50+v(10), 80+v(10));
		addMidPoint("mid", "pt1", "pt2"); 
		
		addPoint("pt3", 80+v(10), 20); 
		addPoint("pt4", 80+v(10), 50);
		addPoint("pt5", 80+v(10), 80);
		
		drawLine("pt1", "pt2"); 
		drawLine("pt1", "pt3"); 
		drawLine("mid", "pt4"); 
		drawLine("pt2", "pt5"); 
	}
	
	private void drawLetterF() {
		addPoint("pt1", 50+v(10), 20+v(10)); 
		addPoint("pt2", 50+v(10), 80+v(10));
		addMidPoint("mid", "pt1", "pt2"); 
		
		addPoint("pt3", 80+v(10), 20+v(10)); 
		addPoint("pt4", 80+v(10), 50+v(10));
		
		drawLine("pt1", "pt2"); 
		drawLine("pt1", "pt3"); 
		drawLine("mid", "pt4"); 
	}
	
	private void drawLetterG() {
		addPoint("ctr", 70+v(5), 50+v(5)); 
		drawArc("ctr", 30, 15, 155, 360); 
		
		addPoint("pt1", 70+v(5), 50+v(5)); 
		addPoint("pt2", 90+v(5), 50+v(5)); 
		addPoint("pt3", 90, 80); 
		
		drawLine("pt1", "pt2");
		drawLine("pt2", "pt3");
	} 
	
	private void drawLetterH() {
		
		addPoint("pt1", 20+v(10), 20+v(10));
		addPoint("pt2", 20+v(10), 80+v(10));
		addMidPoint("mid1", "pt1", "pt2"); 
		
		addPoint("pt3", 80+v(10), 20+v(10));
		addPoint("pt4", 80+v(10), 80+v(10));
		addMidPoint("mid2", "pt3", "pt4"); 
		
		drawLine("pt1", "pt2");
		drawLine("pt3", "pt4");
		drawLine("mid1", "mid2");
	}
	
	private void drawLetterI() {
		addPoint("pt1", 50+v(10), 80+v(10));
		addPoint("pt2", 50+v(10), 20+v(10));
		drawLine("pt1", "pt2");
	}
	
	private void drawLetterJ() {
		int centerX = 50+v(5), centerY = 60+v(5);
		int radiusX = 15, radiusY = 10;
		addPoint("ctr", centerX, centerY);
		drawArc("ctr", radiusX, radiusY, 90, 270); 
		
		addPoint("pt1", centerX + radiusX*2 +v(5), 20+v(5));
		addPoint("pt2", centerX + radiusX*2, centerY);
		drawLine("pt1", "pt2");
		
	}
	
	private void drawLetterK() {
		addPoint("pt1", 20+v(10), 20+v(10));
		addPoint("pt2", 20+v(10), 80+v(10));
		addMidPoint("mid", "pt1", "pt2");
		
		addPoint("pt3", 80+v(10), 20+v(10));
		addPoint("pt4", 80+v(10), 80+v(10));
		
		drawLine("pt1", "pt2");
		drawLine("mid", "pt3");
		drawLine("mid", "pt4");
	}

	private void drawLetterL() {
		addPoint("pt1", 20+v(10), 80+v(10));
		addPoint("pt2", 20+v(10), 20+v(10));
		addPoint("pt3", 80+v(10), 80+v(10));
		
		drawLine("pt1", "pt2");
		drawLine("pt1", "pt3");
	}

	private void drawLetterM() {
		addPoint("pt1", 20+v(10), 80+v(10));
		addPoint("pt2", 20+v(10), 20+v(10));
		addPoint("pt3", 50+v(10), 50+v(10));
		addPoint("pt4", 80+v(10), 20+v(10));
		addPoint("pt5", 80+v(10), 80+v(10));
		
		drawLine("pt1", "pt2");
		drawLine("pt2", "pt3");
		drawLine("pt3", "pt4");
		drawLine("pt4", "pt5");
	}
	
	private void drawLetterN() {
		addPoint("pt1", 20+v(10), 80+v(10));
		addPoint("pt2", 20+v(10), 20+v(10));
		addPoint("pt3", 80+v(10), 80+v(10));
		addPoint("pt4", 80+v(10), 20+v(10));
		
		drawLine("pt1", "pt2");
		drawLine("pt2", "pt3");
		drawLine("pt3", "pt4");
	}
	
	private void drawLetterO() {
		addPoint("ctr", 50+v(5), 50+v(5));
		drawArc("ctr", 18+v(5), 15+v(5), 0, 360); //an ellipse
	}
	
	private void drawLetterP() {
		addPoint("ctr", 20, 35+v(5));
		drawArc("ctr", 30, 8, 0, 180); 
		
		addPoint("pt1", 20, 20);
		addPoint("pt2", 20+v(5), 80+v(5));
		drawLine("pt1", "pt2");
	}
	
	private void drawLetterQ() {
		addPoint("ctr", 50+v(5), 50+v(5));
		drawArc("ctr", 18+v(3), 15+v(3), 0, 360); //an ellipse
		
		addPoint("pt1", 60, 60);
		addPoint("pt2", 80, 80);
		drawLine("pt1", "pt2");
	}
		
	private void drawLetterR() {
		addPoint("ctr", 20+v(5), 35+v(5));
		drawArc("ctr", 30, 8, 0, 180); 
		
		addPoint("pt1", 20, 20);
		addPoint("pt2", 20+v(5), 80);
		drawLine("pt1", "pt2");
		
		addMidPoint("mid", "pt1", "pt2");
		addPoint("pt3", 80+v(5), 80+v(5));
		drawLine("mid", "pt3");
	}
	
	private void drawLetterS() {
		int v1 = v(5);
		int v2 = v(5);
		
		addPoint("ctr", 70+v1, 35+v2);
		drawArc("ctr", 30, 8, 190, 360); 
		
		addPoint("ctr", 20+v1, 65+v2);
		drawArc("ctr", 30, 8, 30, 180); 
	}

	private void drawLetterT() {
		addPoint("pt1", 20+v(10), 20+v(10));
		addPoint("pt2", 80+v(10), 20+v(10));
		addMidPoint("mid", "pt1", "pt2");
		addPoint("pt3", 50+v(10), 80+v(10));
		
		drawLine("pt1", "pt2");
		drawLine("mid", "pt3");
	}
	
	private void drawLetterU() {
		int centerX = 50+v(5), centerY = 60+v(5);
		int radiusX = 20+v(5), radiusY = 10+v(5);
		addPoint("ctr", centerX, centerY); 
		drawArc("ctr", radiusX, radiusY, 90, 270);
		
		addPoint("pt1", centerX + radiusX*2, 20); 
		addPoint("pt2", centerX + radiusX*2, centerY); 
		drawLine("pt1", "pt2");
		
		addPoint("pt3", centerX - radiusX*2, 20); 
		addPoint("pt4", centerX - radiusX*2, centerY); 
		drawLine("pt3", "pt4");
	}
	
	private void drawLetterV() {
		addPoint("pt1", 50+v(10), 80+v(10));
		addPoint("pt2", 20+v(10), 20+v(10));
		addPoint("pt3", 80+v(10), 20+v(10));
		
		drawLine("pt1", "pt2");
		drawLine("pt1", "pt3");
	}
	
	private void drawLetterW() {
		addPoint("pt1", 20+v(5), 20+v(5));
		addPoint("pt2", 20+v(5), 80+v(5));
		addPoint("pt3", 50+v(5), 50+v(5));
		addPoint("pt4", 80+v(5), 80+v(5));
		addPoint("pt5", 80+v(5), 20+v(5));
		
		drawLine("pt1", "pt2");
		drawLine("pt2", "pt3");
		drawLine("pt3", "pt4");
		drawLine("pt4", "pt5");
	}
	
	private void drawLetterX() {
		addPoint("pt1", 20+v(10), 20+v(10));
		addPoint("pt2", 80+v(10), 80+v(10));
		addPoint("pt3", 20+v(10), 80+v(10));
		addPoint("pt4", 80+v(10), 20+v(10));
		
		drawLine("pt1", "pt2");
		drawLine("pt3", "pt4");
	}
	
	private void drawLetterY() {
		addPoint("pt1", 50+v(10), 80+v(10));
		addPoint("pt2", 50+v(10), 50+v(10));
		addPoint("pt3", 20+v(10), 20+v(10));
		addPoint("pt4", 80+v(10), 20+v(10));
		
		drawLine("pt1", "pt2");
		drawLine("pt2", "pt3");
		drawLine("pt2", "pt4");
	}
	
	private void drawLetterZ() {
		addPoint("pt1", 20+v(10), 20+v(10));
		addPoint("pt2", 80+v(10), 20+v(10));
		addPoint("pt3", 20+v(10), 80+v(10));
		addPoint("pt4", 80+v(10), 80+v(10));
		
		drawLine("pt1", "pt2");
		drawLine("pt2", "pt3");
		drawLine("pt3", "pt4");
	}
	
	//add a variance
	private int v(int percent) {
		Random r = new Random();
		int v = (int)(r.nextDouble() * percent * 2) - percent; 
		return v;
	}
	
	public void addPoint(String name, int x, int y) {
		Point pt = new Point(x, y);
		_points.put(name, pt);
	}
	
	public void addMidPoint(String name, String pt1Name, String pt2Name) {
		Point pt1 = _points.get(pt1Name); 
		Point pt2 = _points.get(pt2Name); 
		Point mid = pt1.getMidPoint(pt2);
		_points.put(name, mid);
	}
	
	public void drawLine(String pt1Name, String pt2Name) {
		Point pt1 = _points.get(pt1Name); 
		Point pt2 = _points.get(pt2Name); 
		Line line = new Line((100 * _offset) + pt1._x, pt1._y, (100 * _offset) + pt2._x, pt2._y);
		_lines.add(line); 
	}
	
	final double NB_SEGMENTS = 12;
	public void drawArc(String ctrName, int radiusX, int radiusY, int startDeg, int endDeg) {
		Point ctr = _points.get(ctrName); 
		for (int i=0; i<NB_SEGMENTS; i++) {
			int degree = (int)(startDeg + (i/NB_SEGMENTS) * (endDeg-startDeg)); 
			degree = (-degree + 180) % 360;
			double rad = Math.PI * (degree/180.0);
			int x = ctr._x + (int)(radiusX * 2 * Math.sin(rad));
			int y = ctr._y + (int)(radiusY * 2 * Math.cos(rad));
			addPoint("pt1", x, y);
			
			degree = (int)(startDeg + ((i+1)/NB_SEGMENTS) * (endDeg-startDeg)); 
			degree = (-degree + 180) % 360;
			rad = Math.PI * (degree/180.0);
			x = ctr._x + (int)(radiusX * 2 * Math.sin(rad));
			y = ctr._y + (int)(radiusY * 2 * Math.cos(rad));
			addPoint("pt2", x, y); 
			
			drawLine("pt1", "pt2");
		} //end for
	} //end drawArc()
	
	private String encode() {
		StringBuffer bf = new StringBuffer();
		Collections.shuffle(_lines);
		
		for (Line line : _lines) {
			String x1 = Integer.toString(line.getX1()); 
			String y1 = Integer.toString(line.getY1()); 
			String x2 = Integer.toString(line.getX2()); 
			String y2 = Integer.toString(line.getY2()); 
			
			String msg = MessageFormat.format("{0},{1},{2},{3};", new Object[] {x1, y1, x2, y2}); 
			bf.append(msg);
		}
		
		String code = bf.toString();
		return code;
	} //end encode()
	
	//
	// inner classes
	//
	private static class Point {
		int _x, _y;
		
		public Point(int x, int y) {
			_x = x;
			_y = y;
		}

		public Point getMidPoint(Point that) {
			int x = (this._x + that._x) / 2;
			int y = (this._y + that._y) / 2;
			Point mid = new Point(x, y); 
			return mid;
		}
		
		@Override
		public String toString() {
			String text = MessageFormat.format("{0},{1}", new Object[] {_x, _y}); 
			return text;
		}
	} //end Point

	public String getPlainText() {
		return _plainText;
	}


	


}
