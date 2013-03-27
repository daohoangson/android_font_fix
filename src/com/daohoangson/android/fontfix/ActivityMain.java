package com.daohoangson.android.fontfix;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.daohoangson.android.fontfix.util.UtilCommandLine;
import com.daohoangson.android.fontfix.util.UtilFile;

public class ActivityMain extends Activity implements OnClickListener {

	private Button btnFix;

	static private String TAG = "ActivityMain";

	@Override
	public void onClick(View view) {
		if (view == btnFix) {
			doFix();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnFix = (Button) findViewById(R.id.btn_fix);
		btnFix.setOnClickListener(this);
	}

	private void doFix() {
		Map<String, Integer> fontResIds = Fonts.resIds();
		Set<String> fontNames = fontResIds.keySet();

		Map<String, String> fontPaths = doFix_getPaths(fontNames);
		Log.d(TAG, String.format("fontPaths.size=%d", fontPaths.size()));

		boolean backupResult = doFix_backup(fontPaths.values());
		Log.d(TAG, String.format("backupResult=%s", backupResult));

		boolean rwResult = doFix_remountSystem(true);
		Log.d(TAG, String.format("rwResult=%s", rwResult));
		
		boolean copyResult = doFix_copy(fontPaths, fontResIds);
		Log.d(TAG, String.format("copyResult=%s", copyResult));
		
		boolean roResult = doFix_remountSystem(false);
		Log.d(TAG, String.format("roResult=%s", roResult));
	}

	private Map<String, String> doFix_getPaths(Set<String> fontNames) {
		Map<String, String> fontPaths = new HashMap<String, String>();

		for (String fontName : fontNames) {
			String fontPath = Fonts.getPath(fontName);

			if (fontPath != null) {
				Log.v(TAG, String.format("font %s -> %s", fontName, fontPath));
				fontPaths.put(fontName, fontPath);
			} else {
				Log.v(TAG, String.format("font %s -> not found", fontName));
			}
		}

		return fontPaths;
	}

	private boolean doFix_backup(Collection<String> fontPaths) {
		String dataDirPath = getApplicationInfo().dataDir;

		File dataDirFile = new File(dataDirPath);
		if (!dataDirFile.canWrite()) {
			Log.e(TAG, String.format("%s is not writable", dataDirFile));
			return false;
		}

		String backupDirName = String.format("%s.backup", new SimpleDateFormat(
				"yyyyMMddHHmmss", Locale.US).format(new Date()));
		File backupDirFile = new File(dataDirFile, backupDirName);
		if (!backupDirFile.mkdir()) {
			Log.e(TAG, String.format("%s cannot be created", backupDirFile));
			return false;
		}

		for (String fontPath : fontPaths) {
			File fontFile = new File(fontPath);
			File backupFontFile = new File(backupDirFile, fontFile.getName());

			if (!UtilFile.copy(fontFile, backupFontFile)) {
				Log.e(TAG, String.format("%s cannot be backup'd to %s",
						fontFile, backupFontFile));
				return false;
			}
		}

		Log.v(TAG, String.format("backup OK to %s", backupDirFile));

		return true;
	}

	private boolean doFix_remountSystem(boolean rw) {
		String rwStr = rw ? "rw" : "ro";
		String cmd = String.format("mount -o %s,remount /system", rwStr);

		if (UtilCommandLine.execAndWait(cmd) != 0) {
			Log.e(TAG, String.format("Cannot remount /system (rw=%s)", rw));
			return false;
		}

		return true;
	}

	private boolean doFix_copy(Map<String, String> fontPaths,
			Map<String, Integer> fontResIds) {
		File cacheDirFile = getCacheDir();
		if (!cacheDirFile.canWrite()) {
			Log.e(TAG, String.format("%s is not writable", cacheDirFile));
			return false;
		}

		Set<String> fontNames = fontPaths.keySet();
		for (String fontName : fontNames) {
			String fontPath = fontPaths.get(fontName);
			Integer resId = fontResIds.get(fontName);

			if (resId == null) {
				Log.e(TAG, String.format("resId cannot be found for font %s",
						fontName));
				return false;
			}

			File cacheFile = new File(cacheDirFile, fontName);
			if (!UtilFile.copy(this, resId, cacheFile)) {
				Log.e(TAG, String.format("Cannot save resource #%s to %s",
						resId, cacheFile));
				return false;
			}

			String cmd = String.format("cp %s %s", cacheFile.getAbsolutePath(),
					fontPath);
			if (UtilCommandLine.execAndWait(cmd) != 0) {
				Log.e(TAG, String.format("Cannot replace font file %s->%s",
						cacheFile, fontPath));
				return false;
			}
		}

		return true;
	}
}
