package org.mslab.tool.educ.shared.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ColorCategory {
	public enum Category {RED, PINK, ORANGE, YELLOW, GREEN, BLUE, PURPLE, BROWN, WHITE, GREY};
	private String _name;
	private List<Color> _colorList = new ArrayList<Color>();
	
	private static Map<Color, ColorCategory> _caterogyMap = new HashMap<Color, ColorCategory>();
	private static List<ColorCategory> _categories;
	
	private ColorCategory(Category category, String name) {
		_name = name;
	}

	public static List<ColorCategory> getCategories() {
		if (_categories == null) {
			_categories = buildCategories();
		}
		
		return _categories;
	}
	
	public static ColorCategory getCategory(Color color) {
		ColorCategory category = _caterogyMap.get(color);
		return category;
	}

	private static List<ColorCategory> buildCategories() {
		List<ColorCategory> categoryList = new ArrayList<ColorCategory>(); 
		categoryList.add(buildRedCategory());
		categoryList.add(buildPinkCategory());
		categoryList.add(buildOrangeCategory());
		categoryList.add(buildYellowCategory());
		categoryList.add(buildGreenCategory());
		categoryList.add(buildBlueCategory());
		categoryList.add(buildPurpleCategory());
		categoryList.add(buildBrownCategory());
		categoryList.add(buildWhiteCategory());
		categoryList.add(buildGreyCategory());
		
		return categoryList;
	}

	private static ColorCategory buildRedCategory() {
		ColorCategory category = new ColorCategory(Category.RED, "Red");
		category.addColor(Color.RED);
		category.addColor(Color.RED_CRIMSON);
		category.addColor(Color.RED_DARK);
		category.addColor(Color.RED_DARK_SALMON);
		category.addColor(Color.RED_FIRE_BRICK);
		category.addColor(Color.RED_INDIAN);
		category.addColor(Color.RED_LIGHT_CORAL);
		category.addColor(Color.RED_LIGHT_SALMON);
		category.addColor(Color.RED_SALMON);
		return category;
	}
	
	private static ColorCategory buildPinkCategory() {
		ColorCategory category = new ColorCategory(Category.PINK, "Pink");
		category.addColor(Color.PINK);
		category.addColor(Color.PINK_DEEP);
		category.addColor(Color.PINK_HOT);
		category.addColor(Color.PINK_LIGHT);
		category.addColor(Color.PINK_MEDIUM_VIOLET_RED);
		category.addColor(Color.PINK_PALE_VIOLET_RED);
		return category;
	}
	
	private static ColorCategory buildOrangeCategory() {
		ColorCategory category = new ColorCategory(Category.ORANGE, "Orange");
		category.addColor(Color.ORANGE);
		category.addColor(Color.ORANGE_CORAL);
		category.addColor(Color.ORANGE_DARK);
		category.addColor(Color.ORANGE_RED);
		category.addColor(Color.ORANGE_TOMATO);
		return category;
	}
	
	private static ColorCategory buildYellowCategory() {
		ColorCategory category = new ColorCategory(Category.YELLOW, "Yellow");
		category.addColor(Color.YELLOW);
		category.addColor(Color.YELLOW_DARK_KHAKI);
		category.addColor(Color.YELLOW_GOLD);
		category.addColor(Color.YELLOW_KHAKI);
		category.addColor(Color.YELLOW_LEMON_CHIFFON);
		category.addColor(Color.YELLOW_LIGHT);
		category.addColor(Color.YELLOW_LIGHT_GOLDENROD);
		category.addColor(Color.YELLOW_MOCCASIN);
		category.addColor(Color.YELLOW_PALE_GOLDENROD);
		category.addColor(Color.YELLOW_PAPAYA_WHIP);
		category.addColor(Color.YELLOW_PEACH_PUFF);
		return category;
	}
	
	private static ColorCategory buildGreenCategory() {
		ColorCategory category = new ColorCategory(Category.GREEN, "Green");
		category.addColor(Color.GREEN);
		category.addColor(Color.GREEN_CHARTREUSE);
		category.addColor(Color.GREEN_DARK);
		category.addColor(Color.GREEN_DARK_CYAN);
		category.addColor(Color.GREEN_DARK_OLIVE);
		category.addColor(Color.GREEN_DARK_SEA);
		category.addColor(Color.GREEN_FOREST);
		category.addColor(Color.GREEN_LAWN);
		category.addColor(Color.GREEN_LIGHT);
		category.addColor(Color.GREEN_LIGHT_SEA);
		category.addColor(Color.GREEN_LIME);
		
		category.addColor(Color.GREEN_LIME_GREEN);
		category.addColor(Color.GREEN_MEDIUM_AQUAMARINE);
		category.addColor(Color.GREEN_MEDIUM_SEA);
		category.addColor(Color.GREEN_MEDIUM_SPRING);
		category.addColor(Color.GREEN_OLIVE);
		category.addColor(Color.GREEN_OLIVE_DRAB);
		category.addColor(Color.GREEN_PALE);
		category.addColor(Color.GREEN_SEA);
		category.addColor(Color.GREEN_SPRING);
		category.addColor(Color.GREEN_TEAL);
		category.addColor(Color.GREEN_YELLOW);
		category.addColor(Color.GREEN_YELLOW_GREEN);
		return category;
	}
	
	private static ColorCategory buildBlueCategory() {
		ColorCategory category = new ColorCategory(Category.BLUE, "Blue");
		category.addColor(Color.BLUE);
		category.addColor(Color.BLUE_AQUA_MARINE);
		category.addColor(Color.BLUE_CADET);
		category.addColor(Color.BLUE_CORNFLOWER);
		category.addColor(Color.BLUE_CYAN);
		category.addColor(Color.BLUE_DARK);
		category.addColor(Color.BLUE_DARK_TURQUOISE);
		category.addColor(Color.BLUE_DEEP_SKY);
		category.addColor(Color.BLUE_DODGER_SKY);
		category.addColor(Color.BLUE_LIGHT);
		category.addColor(Color.BLUE_LIGHT_CYAN);
		
		category.addColor(Color.BLUE_LIGHT_SKY);
		category.addColor(Color.BLUE_LIGHT_STEEL);
		category.addColor(Color.BLUE_MEDIUM);
		category.addColor(Color.BLUE_MEDIUM_STEEL);
		category.addColor(Color.BLUE_MEDIUM_TURQUOISE);
		category.addColor(Color.BLUE_MIDNIGHT);
		category.addColor(Color.BLUE_NAVY);
		category.addColor(Color.BLUE_PALE_TURQUOISE);
		category.addColor(Color.BLUE_POWDER);
		category.addColor(Color.BLUE_ROYAL);
		category.addColor(Color.BLUE_SKY);
		category.addColor(Color.BLUE_STEEL);
		category.addColor(Color.BLUE_TURQUOISE);
		return category;
	}
	
	private static ColorCategory buildPurpleCategory() {
		ColorCategory category = new ColorCategory(Category.PURPLE, "Purple");
		category.addColor(Color.PURPLE);
		category.addColor(Color.PURPLE_AMETHYST);
		category.addColor(Color.PURPLE_BLUE_VIOLET);
		category.addColor(Color.PURPLE_DARK_MAGENTA);
		category.addColor(Color.PURPLE_DARK_ORCHID);
		category.addColor(Color.PURPLE_DARK_SLATE_BLUE);
		category.addColor(Color.PURPLE_DARK_VIOLET);
		category.addColor(Color.PURPLE_INDIGO);
		category.addColor(Color.PURPLE_LAVENDER);
		category.addColor(Color.PURPLE_MAGENTA);
		category.addColor(Color.PURPLE_MEDIUM);
		
		category.addColor(Color.PURPLE_MEDIUM_ORCHID);
		category.addColor(Color.PURPLE_MEDIUM_SLATE_BLUE);
		category.addColor(Color.PURPLE_ORCHID);
		category.addColor(Color.PURPLE_PLUM);
		category.addColor(Color.PURPLE_SLATE_BLUE);
		category.addColor(Color.PURPLE_THISTLE);
		category.addColor(Color.PURPLE_VIOLET);
		return category;
	}
	
	private static ColorCategory buildBrownCategory() {
		ColorCategory category = new ColorCategory(Category.BROWN, "Brown");
		category.addColor(Color.BROWN);
		category.addColor(Color.BROWN_BISQUE);
		category.addColor(Color.BROWN_BLANCHED_ALMOND);
		category.addColor(Color.BROWN_BURLY_WOOD);
		category.addColor(Color.BROWN_CHOCOLATE);
		category.addColor(Color.BROWN_CORNSILK);
		category.addColor(Color.BROWN_DARK_GOLDENROD);
		category.addColor(Color.BROWN_GOLDENROD);
		category.addColor(Color.BROWN_MARRON);
		category.addColor(Color.BROWN_NAVAJO_WHITE);
		category.addColor(Color.BROWN_PERU);
		
		category.addColor(Color.BROWN_ROSY);
		category.addColor(Color.BROWN_SADDLE);
		category.addColor(Color.BROWN_SIENNA);
		category.addColor(Color.BROWN_TAN);
		category.addColor(Color.BROWN_WHEAT);
		return category;
	}
	
	private static ColorCategory buildWhiteCategory() {
		ColorCategory category = new ColorCategory(Category.WHITE, "White");
		category.addColor(Color.WHITE);
		category.addColor(Color.WHITE_ALICE_BLUE);
		category.addColor(Color.WHITE_ANTIQUE);
		category.addColor(Color.WHITE_AZURE);
		category.addColor(Color.WHITE_BEIGE);
		category.addColor(Color.WHITE_FLORAL);
		category.addColor(Color.WHITE_GHOST);
		category.addColor(Color.WHITE_HODERDEY);
		category.addColor(Color.WHITE_IVORY);
		category.addColor(Color.WHITE_LAVENDER_BLUSH);
		category.addColor(Color.WHITE_LINEN);
		
		category.addColor(Color.WHITE_MINT_CREAM);
		category.addColor(Color.WHITE_MISTY_ROSE);
		category.addColor(Color.WHITE_OLD_LACE);
		category.addColor(Color.WHITE_SEASHELL);
		category.addColor(Color.WHITE_SMOKE);
		category.addColor(Color.WHITE_SNOW);
		return category;
	}
	
	private static ColorCategory buildGreyCategory() {
		ColorCategory category = new ColorCategory(Category.GREY, "Grey");
		category.addColor(Color.GREY);
		category.addColor(Color.GREY_DARK);
		category.addColor(Color.GREY_DARK_SLATE);
		category.addColor(Color.GREY_DIM);
		category.addColor(Color.GREY_GAINBORO);
		category.addColor(Color.GREY_LIGHT);
		category.addColor(Color.GREY_LIGHT_SLATE);
		category.addColor(Color.GREY_SILVER);
		category.addColor(Color.GREY_SILVER);
		category.addColor(Color.GREY_SLATE);
		category.addColor(Color.BLACK);

		return category;
	}
	
	

	private void addColor(Color color) {
		_colorList.add(color);
		_caterogyMap.put(color, this);
	}

	public String getName() {
		return _name;
	}

	public List<Color> getColors() {
		return _colorList;
	}

	public Color getPrimaryColor() {
		Color primary = _colorList.get(0);
		return primary;
	}

	

	

}
