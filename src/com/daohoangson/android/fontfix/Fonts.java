package com.daohoangson.android.fontfix;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.util.SparseArray;

public class Fonts {
	static public Map<String, Integer> resIds() {
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
		mapping.put("RobotoCondensed-BoldItalic.ttf",
				R.raw.roboto_boldcondenseditalic);
		mapping.put("RobotoCondensed-Italic.ttf", R.raw.roboto_condenseditalic);
		mapping.put("RobotoCondensed-Regular.ttf", R.raw.roboto_condensed);

		return mapping;
	}

	static public SparseArray<String> hashes() {
		SparseArray<String> hashes = new SparseArray<String>();

		hashes.put(R.raw.roboto_black, "54f1ae62ecf36f565e4a91a2c39b7636");
		hashes.put(R.raw.roboto_blackitalic, "03476501edaaaf3cf84ddb79204d89be");
		hashes.put(R.raw.roboto_bold, "42872daef218940db8a9d9a7cd36341e");
		hashes.put(R.raw.roboto_boldcondensed,
				"bfff7821cf9d49d4d8286af2a908144c");
		hashes.put(R.raw.roboto_boldcondenseditalic,
				"6d3f936e2558b6514771030cc01035f6");
		hashes.put(R.raw.roboto_bolditalic, "d46f084a7db973734129dc3f74a7225f");
		hashes.put(R.raw.roboto_condensed, "6a88b5541fe504a914dda734ad6b80c6");
		hashes.put(R.raw.roboto_condenseditalic,
				"236246840647e9ba42b9cac26b1766a0");
		hashes.put(R.raw.roboto_italic, "421144505843a5c04b7b5b2d1a0e5cce");
		hashes.put(R.raw.roboto_light, "a323969419b609c2ab7b70b239f5839c");
		hashes.put(R.raw.roboto_lightitalic, "31db94bb95ce19b93621d11d921da771");
		hashes.put(R.raw.roboto_medium, "f4e3be45af0b2d460b7e227eddafde9f");
		hashes.put(R.raw.roboto_mediumitalic,
				"baf738fb292f0e1096035102d91587c6");
		hashes.put(R.raw.roboto_regular, "835f307ca4b5aec4ff0170327d83276a");
		hashes.put(R.raw.roboto_thin, "62fb3cf0b49bfbd7d07950e7d1ea931a");
		hashes.put(R.raw.roboto_thinitalic, "a6cb06d7d21721c8cfa9dee4d345d02e");

		return hashes;
	}

	static public String getPath(String name) {
		String[] candidates = new String[] { "/system/fonts/%s" };

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
