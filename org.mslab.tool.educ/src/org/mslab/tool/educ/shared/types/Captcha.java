package org.mslab.tool.educ.shared.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Captcha implements Serializable {
	private int _width;
	private int _height;
	private int _nbChars;
	private long _captchaHash;
	private String _captchaCode;
	private List<Line> _lines = new ArrayList<Line>();
	
	//GWT shared types require a parameterless constructor
	public Captcha() {}
	
	public Captcha(int width, int height, int nbChars, String code, long captchaHash) {
		_width = width;
		_height = height;
		_nbChars = nbChars;
		_captchaCode = code;
		_captchaHash = captchaHash;
	}

	public String getCode() {
		return _captchaCode;
	}

	public int getHeigth() {
		return _height;
	}

	public int getWidth() {
		return _width;
	}

	public int getMaxNbChars() {
		return _nbChars;
	}
	
	public void addLine(Line line) {
		_lines.add(line);
	}

	public List<Line> getLines() {
		return _lines;
	}

	public boolean validate(long captchaHash) {
		boolean valid = captchaHash == _captchaHash;
		return valid;
	}

	
}
