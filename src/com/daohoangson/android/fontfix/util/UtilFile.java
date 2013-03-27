package com.daohoangson.android.fontfix.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

	static public String calcHash(String path) {
		MessageDigest md = null;
		InputStream is = null;

		try {
			md = MessageDigest.getInstance("MD5");
			is = new FileInputStream(path);
			is = new DigestInputStream(is, md);

			byte[] buf = new byte[65536];
			int len;
			while ((len = is.read(buf)) > 0) {
				// do nothing
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO log?
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO log?
			e.printStackTrace();
		} catch (IOException e) {
			// TODO log?
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO log?
					e.printStackTrace();
				}
			}
		}

		if (md != null) {
			StringBuilder sb = new StringBuilder();
			for (byte b : md.digest()) {
				sb.append(String.format("%02x", b & 0xff));
			}
			
			return sb.toString();
		} else {
			return "";
		}
	}
}
