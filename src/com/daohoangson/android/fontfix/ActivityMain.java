package com.daohoangson.android.fontfix;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.daohoangson.android.fontfix.util.UtilCommandLine;
import com.daohoangson.android.fontfix.util.UtilFile;

public class ActivityMain extends SherlockListActivity {

	static private String TAG = "ActivityMain";

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mni_fix:
			doFix();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		
	}

	private void doFix() {
		Map<String, Integer> fontResIds = Fonts.resIds();

		Map<String, String> fontPaths = doFix_getPaths(fontResIds);
		if (fontPaths.size() == 0) {
			Log.i(TAG, "backupResult=0");
			return;
		} else {
			Log.d(TAG, String.format("fontPaths.size=%d", fontPaths.size()));
		}

		boolean backupResult = doFix_backup(fontPaths.values());
		if (backupResult == false) {
			Log.e(TAG, "backupResult=false");
			return;
		} else {
			Log.d(TAG, String.format("backupResult=%s", backupResult));
		}

		boolean rwResult = doFix_remountSystem(true);
		if (rwResult == false) {
			Log.e(TAG, "rwResult=false");
			return;
		} else {
			Log.d(TAG, String.format("rwResult=%s", rwResult));
		}

		boolean copyResult = doFix_copy(fontPaths, fontResIds);
		Log.d(TAG, String.format("copyResult=%s", copyResult));

		boolean roResult = doFix_remountSystem(false);
		Log.d(TAG, String.format("roResult=%s", roResult));
	}

	private Map<String, String> doFix_getPaths(Map<String, Integer> fontResIds) {
		Map<String, String> fontPaths = new HashMap<String, String>();
		SparseArray<String> hashes = Fonts.hashes();

		for (String fontName : fontResIds.keySet()) {
			String fontPath = Fonts.getPath(fontName);

			if (fontPath != null) {
				String fontHash = UtilFile.calcHash(fontPath);
				String resHash = hashes.get(fontResIds.get(fontName));

				if (!fontHash.equals(resHash)) {
					Log.v(TAG, String.format("font %s: %s", fontName, fontPath));
					fontPaths.put(fontName, fontPath);
				} else {
					Log.v(TAG, String.format("font %s: up to date (hash=%s)",
							fontName, fontHash));
				}
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
