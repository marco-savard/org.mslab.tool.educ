package org.mslab.tool.educ.shared.types;

import java.io.Serializable;

//A type-safe color type
//Replaces java.awt.Color, which is not supported in GWT
@SuppressWarnings("serial")
public class Color implements Serializable {
	private static final double RED_LUMINENCE = 0.299; 
	private static final double GREEN_LUMINENCE = 0.587; 
	private static final double BLUE_LUMINENCE = 0.114;
	
	private int _r, _g, _b;
	private double _alpha;
	private String _name = null;
	private ColorCategory.Category _category = null;
	
	//required by GWT
	private Color() {}
	
	public Color(int r, int g, int b) {
		this(r, g, b, 1.0); 
	}
	
	public Color(int r, int g, int b, double alpha) {
		_r = r;
		_g = g; 
		_b = b;
		_alpha = alpha;
	}

	public Color(String name, ColorCategory.Category category, int code) {
		_name = name;
		_category = category;
		_r = (code & 0xff0000) >> 16;
		_g = (code & 0x00ff00) >> 8; 
		_b = (code % 256);
	}

	public Color(String rgb) {
		_r = Integer.parseInt(rgb.substring(0, 2), 16);
		_g = Integer.parseInt(rgb.substring(2, 4), 16);
		_b = Integer.parseInt(rgb.substring(4, 6), 16);
	}

	public String toRGBString() {
		String rgbString = "rgb(" + _r + "," + _g + "," + _b + ")"; 
		return rgbString;
	}
	
	public String toRGBAString() {
		String rgbString = "rgba(" + _r + "," + _g + "," + _b + "," + _alpha + ")"; 
		return rgbString;
	}
	
	@Override
	public String toString() {
		String r = Integer.toHexString(0x100 | _r).substring(1);
		String g = Integer.toHexString(0x100 | _g).substring(1);
		String b = Integer.toHexString(0x100 | _b).substring(1);
		String str = "#" + r + g + b;
		return str;
	}
	
	public static Color fromString(String value) {
		Color color;
		
		try {
			String r = value.substring(1, 3);
			String g = value.substring(3, 5);
			String b = value.substring(5, 7);
			
			int red = Integer.parseInt(r, 16); 
			int green = Integer.parseInt(g, 16); 
			int blue = Integer.parseInt(b, 16); 
			color = new Color(red, green, blue); 
		} catch (RuntimeException ex) {
			color = null;
		}
		
		return color;
	}
	
	@Override
	public boolean equals(Object o) {
		Color that = (o instanceof Color) ? (Color)o : null;
		boolean equal = that != null;
		
		equal &= (that._r == _r);
		equal &= (that._g == _g);
		equal &= (that._b == _b);
		
		return equal;
	}
	
	@Override
	public int hashCode() {
		int code = (_r * 7*7) + (_g * 7) + _b;
		return code; 
	}

	public Color getGrayscale() {
		int luminence = (int)((RED_LUMINENCE * _r) + (GREEN_LUMINENCE * _g) + (BLUE_LUMINENCE * _b));
		return new Color(luminence, luminence, luminence);
	}
	
	public Color brighter() {
		return brighter(1.2); 
	}
	
	public Color brighter(double factor) {
		float[] hsb = toHSB();
		int hue = (int)(radToDegree(hsb[0])); 
		int saturation = (int)(100 * hsb[1]); 
		int brightness = (int)(100 * hsb[2] * factor);
		brightness = (brightness > 100) ? 100 : brightness;
		
		Color brighterColor = Color.createFromHsl(hue, saturation, brightness); 
		return brighterColor; 
		
		/*
		int r = (int)(_r * br);
		int g = (int)(_g * br);
		int b = (int)(_b * br);
		r = Math.min(r, 255); 
		g = Math.min(g, 255); 
		b = Math.min(b, 255); 
		return new Color(r, g, b);*/
	}
	
	private double radToDegree(double radian) {
		double degrees = (radian * 180) / Math.PI; 
		degrees = (degrees > 0) ? (degrees % 360) : (degrees + 360); 
		return degrees;
	}

	public Color darker() {
		return darker(1.2); 
	}
	
	public Color darker(double factor) {
		Color darkerColor = brighter(1.0 / factor); 
		return darkerColor;
		/*
		int r = (int)(_r / dark);
		int g = (int)(_g / dark);
		int b = (int)(_b / dark);
		return new Color(r, g, b);
		*/
	}
	
	//return a number between 0 (no contrast) and 1 (full contrast)
	public double constrastWith(Color otherColor) {
		double thisLuminence = (RED_LUMINENCE * _r) + (GREEN_LUMINENCE * _g) + (BLUE_LUMINENCE * _b);
		double otherLuminence = (RED_LUMINENCE * otherColor._r) + (GREEN_LUMINENCE * otherColor._g) + (BLUE_LUMINENCE *otherColor. _b);
		double contrast = Math.abs(thisLuminence - otherLuminence) / 255.0;
		return contrast;
	}

	public Color getComplimentaryColor() {
		int r = 255 - _r; 
		int g = 255 - _g;
		int b = 255 - _b;
		return new Color(r, g, b);
	}

	//saturation, hue, 
	//unit test
	
	//named colors: //http://html-color-codes.info/color-names/
	
	//reds
	public static final Color RED_INDIAN = new Color("Indian Red", ColorCategory.Category.RED, 0xcd5c5c);
	public static final Color RED_LIGHT_CORAL = new Color("Light Coral", ColorCategory.Category.RED, 0xf08080);
	public static final Color RED_SALMON = new Color("Salmon", ColorCategory.Category.RED, 0xfa8072);
	public static final Color RED_DARK_SALMON = new Color("Dark Salmon", ColorCategory.Category.RED, 0xe9967a);
	public static final Color RED_LIGHT_SALMON = new Color("Light Salmon", ColorCategory.Category.RED, 0xffa07a);
	public static final Color RED_CRIMSON = new Color("Crimson", ColorCategory.Category.RED, 0xdc143c);
	public static final Color RED = new Color("Red", ColorCategory.Category.RED, 0xff0000);
	public static final Color RED_FIRE_BRICK = new Color("Fire Brick", ColorCategory.Category.RED, 0xb22222);
	public static final Color RED_DARK = new Color("Dark Red", ColorCategory.Category.RED, 0x8b0000);
	
	//pinks
	public static final Color PINK = new Color("Pink", ColorCategory.Category.PINK, 0xffc0cb);
	public static final Color PINK_LIGHT = new Color("Light Pink", ColorCategory.Category.PINK, 0xffb6c1);
	public static final Color PINK_HOT = new Color("Hot Pink", ColorCategory.Category.PINK, 0xff69b4);
	public static final Color PINK_DEEP = new Color("Deep Pink", ColorCategory.Category.PINK, 0xff1494);
	public static final Color PINK_MEDIUM_VIOLET_RED = new Color("Medium Violet Red", ColorCategory.Category.PINK, 0xc71585);
	public static final Color PINK_PALE_VIOLET_RED = new Color("Pale Violet Red", ColorCategory.Category.PINK, 0xdb7093);
	
	//oranges
	public static final Color ORANGE_CORAL = new Color("Coral", ColorCategory.Category.ORANGE, 0xff7f50);
	public static final Color ORANGE_TOMATO = new Color("Tomato", ColorCategory.Category.ORANGE, 0xff6347);
	public static final Color ORANGE_RED = new Color("Orange Red", ColorCategory.Category.ORANGE, 0xff4500);
	public static final Color ORANGE_DARK = new Color("Dark Orange", ColorCategory.Category.ORANGE, 0xff8c00);
	public static final Color ORANGE = new Color("Orange", ColorCategory.Category.ORANGE, 0xffa500);
	
	//yellows
	public static final Color YELLOW_GOLD = new Color("Gold", ColorCategory.Category.YELLOW, 0xffd700);
	public static final Color YELLOW = new Color("Yellow", ColorCategory.Category.YELLOW, 0xffff00);
	public static final Color YELLOW_LIGHT = new Color("Light Yellow", ColorCategory.Category.YELLOW, 0xffffe0);
	public static final Color YELLOW_LEMON_CHIFFON = new Color("Lemon Chiffon", ColorCategory.Category.YELLOW, 0xfffacd);
	public static final Color YELLOW_LIGHT_GOLDENROD = new Color("Light Goldenrod Yellow", ColorCategory.Category.YELLOW, 0xfafad2);
	public static final Color YELLOW_PAPAYA_WHIP = new Color("Papaya Whip", ColorCategory.Category.YELLOW, 0xffefd5);
	public static final Color YELLOW_MOCCASIN = new Color("Moccasin", ColorCategory.Category.YELLOW, 0xffe4b5);
	public static final Color YELLOW_PEACH_PUFF = new Color("Peach Puff", ColorCategory.Category.YELLOW, 0xffdab9);
	public static final Color YELLOW_PALE_GOLDENROD = new Color("Pale Goldenrod", ColorCategory.Category.YELLOW, 0xeee8aa);
	public static final Color YELLOW_KHAKI = new Color("Khaki", ColorCategory.Category.YELLOW, 0xf0e68c);
	public static final Color YELLOW_DARK_KHAKI = new Color("Dark Khaki", ColorCategory.Category.YELLOW, 0xbdb76b);
	
	//greens
	public static final Color GREEN_YELLOW = new Color("Green Yellow", ColorCategory.Category.GREEN, 0xadff2f);
	public static final Color GREEN_CHARTREUSE = new Color("Chartreuse", ColorCategory.Category.GREEN, 0x7fff00);
	public static final Color GREEN_LAWN = new Color("Lawn Green", ColorCategory.Category.GREEN, 0x7cfc00);
	public static final Color GREEN_LIME = new Color("Lime", ColorCategory.Category.GREEN, 0x00ff00);
	public static final Color GREEN_LIME_GREEN = new Color("Lime Green", ColorCategory.Category.GREEN, 0x32cd32);
	public static final Color GREEN_PALE = new Color("Pale Green", ColorCategory.Category.GREEN, 0x98fb98);
	public static final Color GREEN_LIGHT = new Color("Light Green", ColorCategory.Category.GREEN, 0x90ee90);
	public static final Color GREEN_MEDIUM_SPRING = new Color("Medium Spring Green", ColorCategory.Category.GREEN, 0x00fa9a);
	public static final Color GREEN_SPRING = new Color("Spring Green", ColorCategory.Category.GREEN, 0x00ff7f);
	
	public static final Color GREEN_MEDIUM_SEA = new Color("Medium Sea Green", ColorCategory.Category.GREEN, 0x3cb371);
	public static final Color GREEN_SEA = new Color("Sea Green", ColorCategory.Category.GREEN, 0x2e8b57);
	public static final Color GREEN_FOREST = new Color("Forest Green", ColorCategory.Category.GREEN, 0x228b22);
	public static final Color GREEN = new Color("Green", ColorCategory.Category.GREEN, 0x008000);
	public static final Color GREEN_DARK = new Color("Dark Green", ColorCategory.Category.GREEN, 0x006400);
	public static final Color GREEN_YELLOW_GREEN = new Color("Yellow Green", ColorCategory.Category.GREEN, 0x9acd32);
	public static final Color GREEN_OLIVE_DRAB = new Color("Olive Drab", ColorCategory.Category.GREEN, 0x6b8e23);
	public static final Color GREEN_OLIVE = new Color("Olive", ColorCategory.Category.GREEN, 0x808000);
	public static final Color GREEN_DARK_OLIVE = new Color("Dark Olive Green", ColorCategory.Category.GREEN, 0x526b2f);
	
	public static final Color GREEN_MEDIUM_AQUAMARINE = new Color("Medium Aquamarine", ColorCategory.Category.GREEN, 0x66cdaa);
	public static final Color GREEN_DARK_SEA = new Color("Dark Sea Green", ColorCategory.Category.GREEN, 0x8fbc8f);
	public static final Color GREEN_LIGHT_SEA = new Color("Light Sea Green", ColorCategory.Category.GREEN, 0x20b2aa);
	public static final Color GREEN_DARK_CYAN = new Color("Dark Cyan", ColorCategory.Category.GREEN, 0x008b8b);
	public static final Color GREEN_TEAL = new Color("Teal", ColorCategory.Category.GREEN, 0x008080);
	
	//blues
	public static final Color BLUE_CYAN = new Color("Cyan", ColorCategory.Category.BLUE, 0x00ffff);
	public static final Color BLUE_LIGHT_CYAN = new Color("Light Cyan", ColorCategory.Category.BLUE, 0xe0ffff);
	public static final Color BLUE_PALE_TURQUOISE = new Color("Pale Turquoise", ColorCategory.Category.BLUE, 0xafeeee);
	public static final Color BLUE_AQUA_MARINE = new Color("Aqua Marine", ColorCategory.Category.BLUE, 0x7fffd4);
	public static final Color BLUE_TURQUOISE = new Color("Turquoise", ColorCategory.Category.BLUE, 0x40e0d0);
	public static final Color BLUE_MEDIUM_TURQUOISE = new Color("Medium Turquoise", ColorCategory.Category.BLUE, 0x48d1cc);
	public static final Color BLUE_DARK_TURQUOISE = new Color("Dark Turquoise", ColorCategory.Category.BLUE, 0x00ced1);
	public static final Color BLUE_CADET = new Color("Cadet Blue", ColorCategory.Category.BLUE, 0x5f9ea0);
	public static final Color BLUE_STEEL = new Color("Steel Blue", ColorCategory.Category.BLUE, 0x4682b4);
	
	public static final Color BLUE_LIGHT_STEEL = new Color("Light Steel Blue", ColorCategory.Category.BLUE, 0xb0c4de);
	public static final Color BLUE_POWDER = new Color("Powder Blue", ColorCategory.Category.BLUE, 0xb0e0e6);
	public static final Color BLUE_LIGHT = new Color("Light Blue", ColorCategory.Category.BLUE, 0xadd8e6);
	public static final Color BLUE_SKY = new Color("Sky Blue", ColorCategory.Category.BLUE, 0x87ceeb);
	public static final Color BLUE_LIGHT_SKY = new Color("Light Sky Blue", ColorCategory.Category.BLUE, 0x87cefa);
	public static final Color BLUE_DEEP_SKY = new Color("Deep Sky Blue", ColorCategory.Category.BLUE, 0x00bfff);
	public static final Color BLUE_DODGER_SKY = new Color("Dodger Blue", ColorCategory.Category.BLUE, 0x1e90ff);
	public static final Color BLUE_CORNFLOWER = new Color("Cornflower Blue", ColorCategory.Category.BLUE, 0x6495ed);
	
	public static final Color BLUE_MEDIUM_STEEL = new Color("Medium Slate Blue", ColorCategory.Category.BLUE, 0x7b68ee);
	public static final Color BLUE_ROYAL = new Color("Royal Blue", ColorCategory.Category.BLUE, 0x4169e1);
	public static final Color BLUE = new Color("Blue", ColorCategory.Category.BLUE, 0x0000ff);
	public static final Color BLUE_MEDIUM = new Color("Medium Blue", ColorCategory.Category.BLUE, 0x0000cd);
	public static final Color BLUE_DARK = new Color("Dark Blue", ColorCategory.Category.BLUE, 0x00008b);
	public static final Color BLUE_NAVY = new Color("Navy Blue", ColorCategory.Category.BLUE, 0x000080);
	public static final Color BLUE_MIDNIGHT = new Color("Midnight Blue", ColorCategory.Category.BLUE, 0x191970);
	
	//purples
	public static final Color PURPLE_LAVENDER = new Color("Lavender", ColorCategory.Category.PURPLE, 0xe6e6fa);
	public static final Color PURPLE_THISTLE = new Color("Thistle", ColorCategory.Category.PURPLE, 0xd8bfd8);
	public static final Color PURPLE_PLUM = new Color("Plum", ColorCategory.Category.PURPLE, 0xdda0dd);
	public static final Color PURPLE_VIOLET = new Color("Violet", ColorCategory.Category.PURPLE, 0xee82ee);
	public static final Color PURPLE_ORCHID = new Color("Orchid", ColorCategory.Category.PURPLE, 0xda70d6);
	public static final Color PURPLE_MAGENTA = new Color("Magenta", ColorCategory.Category.PURPLE, 0xff00ff);
	public static final Color PURPLE_MEDIUM_ORCHID = new Color("Medium Orchid", ColorCategory.Category.PURPLE, 0xba55d3);
	public static final Color PURPLE_MEDIUM = new Color("Medium Purple", ColorCategory.Category.PURPLE, 0x9370db);
	public static final Color PURPLE_AMETHYST = new Color("Amethyst", ColorCategory.Category.PURPLE, 0x9966cc);
	
	public static final Color PURPLE_BLUE_VIOLET = new Color("Blue Violet", ColorCategory.Category.PURPLE, 0x8a2be2);
	public static final Color PURPLE_DARK_VIOLET = new Color("Dark Violet", ColorCategory.Category.PURPLE, 0x9400d3);
	public static final Color PURPLE_DARK_ORCHID = new Color("Dark Orchid", ColorCategory.Category.PURPLE, 0x9932cc);
	public static final Color PURPLE_DARK_MAGENTA = new Color("Dark Magenta", ColorCategory.Category.PURPLE, 0x8b008b);
	public static final Color PURPLE = new Color("Purple", ColorCategory.Category.PURPLE, 0x800080);
	public static final Color PURPLE_INDIGO = new Color("Indigo", ColorCategory.Category.PURPLE, 0x4b0082);
	public static final Color PURPLE_SLATE_BLUE = new Color("Slate Blue", ColorCategory.Category.PURPLE, 0x6a5acd);
	public static final Color PURPLE_DARK_SLATE_BLUE = new Color("Dark Slate Blue", ColorCategory.Category.PURPLE, 0x483d8b);
	public static final Color PURPLE_MEDIUM_SLATE_BLUE = new Color("Medium Slate Blue", ColorCategory.Category.PURPLE, 0x7b68ee);
	
	//browns
	public static final Color BROWN_CORNSILK = new Color("Cornsilk", ColorCategory.Category.BROWN, 0xfff8dc);
	public static final Color BROWN_BLANCHED_ALMOND = new Color("Blanched Almond", ColorCategory.Category.BROWN, 0xffebcd);
	public static final Color BROWN_BISQUE = new Color("Bisque", ColorCategory.Category.BROWN, 0xffe4c4);
	public static final Color BROWN_NAVAJO_WHITE = new Color("Navajo White", ColorCategory.Category.BROWN, 0xffdead);
	public static final Color BROWN_WHEAT = new Color("Wheat", ColorCategory.Category.BROWN, 0xf5deb3);
	public static final Color BROWN_BURLY_WOOD = new Color("Burly Wood", ColorCategory.Category.BROWN, 0xdeb887);
	public static final Color BROWN_TAN = new Color("Tan", ColorCategory.Category.BROWN, 0xd2b48c);
	public static final Color BROWN_ROSY = new Color("Rosy Brown", ColorCategory.Category.BROWN, 0xbc8f8f);
	
	public static final Color BROWN_SANDY = new Color("Sandy Brown", ColorCategory.Category.BROWN, 0xf4a460);
	public static final Color BROWN_GOLDENROD = new Color("Goldenrod", ColorCategory.Category.BROWN, 0xdaa520);
	public static final Color BROWN_DARK_GOLDENROD = new Color("Dark Goldenrod", ColorCategory.Category.BROWN, 0xb8860b);
	public static final Color BROWN_PERU = new Color("Peru", ColorCategory.Category.BROWN, 0xcd853f);
	public static final Color BROWN_CHOCOLATE = new Color("Chocolate", ColorCategory.Category.BROWN, 0xd2691e);
	public static final Color BROWN_SADDLE = new Color("Saddle Brown", ColorCategory.Category.BROWN, 0x8b4513);
	public static final Color BROWN_SIENNA = new Color("Sienna", ColorCategory.Category.BROWN, 0xa0522d);
	public static final Color BROWN = new Color("Brown", ColorCategory.Category.BROWN, 0xa52a2a);
	public static final Color BROWN_MARRON = new Color("Marron", ColorCategory.Category.BROWN, 0x800000);
	
	//whites
	public static final Color WHITE = new Color("White", ColorCategory.Category.WHITE, 0xffffff);
	public static final Color WHITE_SNOW = new Color("Snow", ColorCategory.Category.WHITE, 0xfffafa);
	public static final Color WHITE_HODERDEY = new Color("Honey Dew", ColorCategory.Category.WHITE, 0xf0fff0);
	public static final Color WHITE_MINT_CREAM = new Color("Mint Cream", ColorCategory.Category.WHITE, 0xf5fffa);
	public static final Color WHITE_AZURE = new Color("Azure", ColorCategory.Category.WHITE, 0xf0ffff);
	public static final Color WHITE_ALICE_BLUE = new Color("Alice Blue", ColorCategory.Category.WHITE, 0xf0f8ff);
	public static final Color WHITE_GHOST = new Color("Ghost White", ColorCategory.Category.WHITE, 0xf8f8ff);
	public static final Color WHITE_SMOKE = new Color("White Smoke", ColorCategory.Category.WHITE, 0xf5f5f5);
	public static final Color WHITE_SEASHELL = new Color("Seashell", ColorCategory.Category.WHITE, 0xfff5ee);
	public static final Color WHITE_BEIGE = new Color("Beige", ColorCategory.Category.WHITE, 0xf5f5dc);
	
	public static final Color WHITE_OLD_LACE = new Color("Old Lace", ColorCategory.Category.WHITE, 0xfdf5e6);
	public static final Color WHITE_FLORAL = new Color("Floral White", ColorCategory.Category.WHITE, 0xfffaf0);
	public static final Color WHITE_IVORY = new Color("Ivory", ColorCategory.Category.WHITE, 0xfffff0);
	public static final Color WHITE_ANTIQUE = new Color("Antique White", ColorCategory.Category.WHITE, 0xfaebd7);
	public static final Color WHITE_LINEN = new Color("Linen", ColorCategory.Category.WHITE, 0xfaf0e6);
	public static final Color WHITE_LAVENDER_BLUSH = new Color("Lavender Blush", ColorCategory.Category.WHITE, 0xfff0f5);
	public static final Color WHITE_MISTY_ROSE = new Color("Misty Rose", ColorCategory.Category.WHITE, 0xffe4e1);
	
	//greys
	public static final Color GREY_GAINBORO = new Color("Gainboro", ColorCategory.Category.GREY, 0xdcdcdc);
	public static final Color GREY_LIGHT = new Color("Light Grey", ColorCategory.Category.GREY, 0xd3d3d3);
	public static final Color GREY_SILVER = new Color("Silver", ColorCategory.Category.GREY, 0xc0c0c0);
	public static final Color GREY_DARK = new Color("Dark Grey", ColorCategory.Category.GREY, 0x090909);
	public static final Color GREY = new Color("Grey", ColorCategory.Category.GREY, 0x808080);
	public static final Color GREY_DIM = new Color("Dim Grey", ColorCategory.Category.GREY, 0x696969);
	public static final Color GREY_LIGHT_SLATE = new Color("Light Slate Gray", ColorCategory.Category.GREY, 0x778899);
	public static final Color GREY_SLATE = new Color("Slate Grey", ColorCategory.Category.GREY, 0x708090);
	public static final Color GREY_DARK_SLATE = new Color("Dark Slate Grey", ColorCategory.Category.GREY, 0x2f4f4f);
	public static final Color BLACK = new Color("Black", ColorCategory.Category.GREY, 0x000000);
	
	public String getName() {
		return _name;
	}

	public int getValue() {
		int value = _r << 16 | _g << 8 | _b;
		return value;
	}

	public int getRed() {
		return _r;
	}
	
	public int getGreen() {
		return _g;
	}
	
	public int getBlue() {
		return _b;
	}
	
	public void setAlpha(double alpha) {
		_alpha = alpha;
	}

	public String getHexCode() {
		StringBuffer buf = new StringBuffer("#"); 
		buf.append((_r <= 9) ? "0" : ""); 
		buf.append(Integer.toHexString(_r)); 
		
		buf.append((_g <= 9) ? "0" : ""); 
		buf.append(Integer.toHexString(_g)); 
		
		buf.append((_b <= 9) ? "0" : ""); 
		buf.append(Integer.toHexString(_b)); 
		
		String hex = buf.toString().toUpperCase();  
		return hex;
	}

	public Color blendWith(Color otherColor) {
		int r = (_r + otherColor._r) / 2;
		int g = (_g + otherColor._g) / 2;
		int b = (_b + otherColor._b) / 2;
		
		Color color = new Color(r, g, b);
		return color;
	}
	
	public Color blendWith(Color thatColor, int thatPercent) {
		int thisPercent = 100 - thatPercent;
		int r = (_r * thisPercent + thatColor._r * thatPercent) / 100;
		int g = (_g * thisPercent + thatColor._g * thatPercent) / 100;
		int b = (_b * thisPercent + thatColor._b * thatPercent) / 100;

		Color color = new Color(r, g, b);
		return color;
	}
	
	public float[] toHSB() {
		double y = Math.sqrt(3.0) * (_g - _b);
		double x = (2.0 * _r) - _g - _b;
		double h = Math.atan2(y, x);
		
		int min = Math.min(Math.min(_r, _g), _b);
	    int max = Math.max(Math.max(_r, _g), _b);
	    int delta = max - min;
	    double s = (max == 0) ? 0 : delta / (double)max;
	    
	    double b = (((max+min) / 2) / 255.0);
		
		float[] hsb = new float[] {(float)h, (float)s, (float)b};
		return hsb;
	}

	public int getHue() {
		float[] hsb = toHSB();
		double degrees = (hsb[0] * (180/Math.PI));
		degrees = (degrees + 360.0) % 360;
		return (int)degrees;
	}
	
	public void setHue(int hueDegree) {
		float[] hsb = toHSB();
		Color color = createFromHsl(hueDegree, (int)(100 * hsb[1]), (int)(100 * hsb[2]));
		_r = color.getRed();
		_g = color.getGreen();
		_b = color.getBlue();
		
	}

	public int getSaturation() {
		float[] hsb = toHSB();
		return (int)(hsb[1] * 100);
	}
	
	public int getBrightness() {
		float[] hsb = toHSB();
		return (int)(hsb[2] * 100);
	}

	public float[] getCMYK() {
		double rp = _r / 255.0; 
		double gp = _g / 255.0; 
		double bp = _b / 255.0; 
	
		double k = 1.0 - Math.max(rp, Math.max(gp, bp));
		double kr = 1.0 - k;
		double c = (1.0 - rp - k) / kr;
		double m = (1.0 - gp - k) / kr;
		double y = (1.0 - bp - k) / kr;
		
		float[] cmyk = new float[] {(float)c, (float)m, (float)y, (float)k};
		return cmyk;
	}
	
	public static Color createFromHsl(int hue, int saturation, int lightness) {
		hue = (hue >= 360) ? hue % 360 : hue;
		int h = (int)((hue/360.0) * 6);
	    double f = (hue/360.0) * 6 - h;
	    double v = lightness/100.0;
	    double p = v * (1 - (saturation/100.0));
	    double q = v * (1 - f * (saturation/100.0));
	    double t = v * (1 - (1 - f) * (saturation/100.0));
	    double r, g, b;
	    
	    switch (h) {
	      case 0: {r=v; g=t; b=p;} break;
	      case 1: {r=q; g=v; b=p;} break;
	      case 2: {r=p; g=v; b=t;} break;
	      case 3: {r=p; g=q; b=v;} break;
	      case 4: {r=t; g=p; b=v;} break;
	      case 5: {r=v; g=p; b=p;} break;
	      
	      default: throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + v);
	    }
	    
	    Color color = new Color((int)(r*255), (int)(g*255), (int)(b*255));
	    return color;
	}

	public Color getContrastColor() {
		int brightness = getBrightness();
		Color contrast = (brightness > 50) ? Color.BLACK : Color.WHITE;
		return contrast;
	}

	public Color setSaturation(int saturation) {
		int hue = getHue();
		int lightness = getBrightness();
		Color newColor = Color.createFromHsl(hue, saturation, lightness); 
		return newColor;
	}

	public Color setBrightness(int brightness) {
		int hue = getHue();
		int saturation = getSaturation();
		Color newColor = Color.createFromHsl(hue, saturation, brightness);
		return newColor;
	}

	public Hue getHueCode() {
		int value = getHue();
		Hue hue = Hue.RED; 
		
		if (value < 25) {
			hue = Hue.RED;
		} else if (value < 45) { 
			hue =  Hue.ORANGE;
		} else if (value < 60) { 
			hue = Hue.YELLOW;
		} else if (value < 150) { 
			hue = Hue.GREEN;
		} else if (value < 180) { 
			hue = Hue.CYAN;
		} else if (value < 240) { 
			hue = Hue.BLUE;
		} else if (value < 270) { 
			hue =  Hue.PURPLE;
		} 
		return hue;
	}
	public enum Hue {RED, ORANGE, YELLOW, GREEN, CYAN, BLUE, PURPLE}; 
	

	

	
	

	
	
}
