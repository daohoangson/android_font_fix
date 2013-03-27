package com.daohoangson.android.fontfix.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;

public class UtilFile {
	static public boolean copy(File src, File dst) {
		InputStream in = null;
		OutputStream out = null;
		boolean result = false;

		try {
			in = new FileInputStream(src);
			out = new FileOutputStream(dst);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			result = true;
		} catch (IOException e) {
			// TODO log?
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO log?
					e.printStackTrace();
				}
			}

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO log?
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	static public boolean copy(Context context, int resId, File dst) {
		InputStream in = null;
		OutputStream out = null;
		boolean result = false;

		try {
			in = context.getResources().openRawResource(resId);
			out = new FileOutputStream(dst);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			result = true;
		} catch (IOException e) {
			// TODO log?
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO log?
					e.printStackTrace();
				}
			}

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO log?
					e.printStackTrace();
				}
			}
		}

		return result;
	}
}
