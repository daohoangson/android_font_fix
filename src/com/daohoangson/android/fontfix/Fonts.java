package com.daohoangson.android.fontfix;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Fonts {
	public static Map<String, Integer> resIds() {
		Map<String, Integer> mapping = new HashMap<String, Integer>();
		
		mapping.put("Roboto-Black.ttf", R.raw.roboto_black);
		mapping.put("Roboto-BlackItalic.ttf", R.raw.roboto_blackitalic);
		mapping.put("Roboto-Bold.ttf", R.raw.roboto_bold);
		mapping.put("Roboto-BoldCondensed.ttf", R.raw.roboto_boldcondensed);
		mapping.put("Roboto-BoldCondensedItalic.ttf", R.raw.roboto_blackitalic);
		mapping.put("Roboto-BoldItalic.ttf", R.raw.roboto_bolditalic);
		mapping.put("Roboto-Condensed.ttf", R.raw.roboto_condensed);
		mapping.put("Roboto-CondensedItalic.ttf", R.raw.roboto_condenseditalic);
		mapping.put("Roboto-Italic.ttf", R.raw.roboto_italic);
		mapping.put("Roboto-Light.ttf", R.raw.roboto_light);
		mapping.put("Roboto-LightItalic.ttf", R.raw.roboto_lightitalic);
		mapping.put("Roboto-Medium.ttf", R.raw.roboto_medium);
		mapping.put("Roboto-MediumItalic.ttf", R.raw.roboto_mediumitalic);
		mapping.put("Roboto-Regular.ttf", R.raw.roboto_regular);
		mapping.put("Roboto-Thin.ttf", R.raw.roboto_thin);
		mapping.put("Roboto-ThinItalic.ttf", R.raw.roboto_thinitalic);
		
		// alternative names as found in CM10.1 (mako build)
		mapping.put("RobotoCondensed-Bold.ttf", R.raw.roboto_boldcondensed);
		mapping.put("RobotoCondensed-BoldItalic.ttf", R.raw.roboto_boldcondenseditalic);
		mapping.put("RobotoCondensed-Italic.ttf", R.raw.roboto_condenseditalic);
		mapping.put("RobotoCondensed-Regular.ttf", R.raw.roboto_condensed);

		return mapping;
	}
	
	public static String getPath(String name) {
		String[] candidates = new String[] {
			"/system/fonts/%s"
		};
		
		for (String candidate : candidates) {
			String path = String.format(candidate, name);
			File file = new File(path);
			
			if (file.exists()) {
				return path;
			}
		}
		
		return null;
	}
}
