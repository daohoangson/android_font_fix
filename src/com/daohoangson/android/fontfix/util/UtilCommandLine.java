package com.daohoangson.android.fontfix.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import android.util.Log;

public class UtilCommandLine {

	static private String TAG = "UtilCommandLine";

	static public int execAndWait(String cmd) {
		int exitValue = -1;

		try {
			long timestampStart = new Date().getTime();
			Log.v(TAG, String.format("execAndWait -> exec(%s)", cmd));

			Process p = Runtime.getRuntime().exec("su");

			DataOutputStream stdin = new DataOutputStream(p.getOutputStream());
			stdin.writeBytes(cmd);
			stdin.writeBytes("\nexit\n");
			stdin.flush();

			exitValue = p.waitFor();
			if (exitValue != 0) {
				InputStream stdout = p.getInputStream();
				InputStream stderr = p.getErrorStream();

				String line = null;
				InputStreamReader reader = new InputStreamReader(stdout);
				BufferedReader bufferedReader = new BufferedReader(reader);

				while ((line = bufferedReader.readLine()) != null) {
					Log.w(TAG, String.format("stdout > %s", line));
				}

				reader = new InputStreamReader(stderr);
				bufferedReader = new BufferedReader(reader);
				while ((line = bufferedReader.readLine()) != null) {
					Log.w(TAG, String.format("stderr > %s", line));
				}
			}

			long timestampFinish = new Date().getTime();
			Log.v(TAG, String.format("execAndWait() = %d (%d ms)", exitValue,
					timestampFinish - timestampStart));
		} catch (IOException e) {
			Log.e(TAG, "execAndWait -> exec() failed", e);
		} catch (InterruptedException e) {
			Log.e(TAG, "execAndWait -> waitFor() failed", e);
		}

		return exitValue;
	}
}
