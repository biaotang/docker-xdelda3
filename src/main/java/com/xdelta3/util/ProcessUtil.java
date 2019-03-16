package com.xdelta3.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

/**
 * @author	biao.tang
 * 2019年3月15日
 */
public class ProcessUtil {

	public final static int BufferSize = 4096;
	
	public static boolean exec(String[] cmdarray) throws Exception {
		Process process = Runtime.getRuntime().exec(cmdarray);
		process.waitFor();
		return process.exitValue() == 0 ? true : false;
	}
	
	public static boolean exec(String command) throws Exception {
		Process process = Runtime.getRuntime().exec(command);
		process.waitFor();
		return process.exitValue() == 0 ? true : false;
	}
	
	public static String system(String commandLine) throws IOException {
		LinkedList<String> list = new LinkedList<>();
		StringBuilder buffer = new StringBuilder();
		boolean inQuote = false;
		boolean inString = false;

		for (int i = 0; i < commandLine.length(); ++i) {
			char theChar = commandLine.charAt(i);
			switch (theChar) {
				case '\t' :
				case ' ' :
					if (inQuote) {
						buffer.append(theChar);
					} else if (inString) {
						list.add(buffer.toString());
						buffer.setLength(0);
						inString = false;
					}
					break;
				case '"' :
					if (inQuote) {
						list.add(buffer.toString());
						buffer.setLength(0);
					}

					inQuote = !inQuote;
					break;
				default :
					buffer.append(theChar);
					if (!inQuote) {
						inString = true;
					}
			}
		}

		if (buffer.length() > 0) {
			list.add(buffer.toString());
		}
		return system((String[]) list.toArray(new String[list.size()]));
	}
	
	public static String system(String[] cmdArray) throws IOException {
		Process proc = Runtime.getRuntime().exec(cmdArray);
		byte[][] results = execute(proc);
		return new String(results[0]) + new String(results[1]);
	}
	
	private static byte[][] execute(Process proc) throws IOException {
		try (InputStream stdout = proc.getInputStream();
				InputStream stderr = proc.getErrorStream();
				ByteArrayOutputStream bufIn = new ByteArrayOutputStream();
				ByteArrayOutputStream bufErr = new ByteArrayOutputStream();
				){
			int num = BufferSize;
			byte[] buffer = new byte[num];
			boolean isOutput = true;

			while (isOutput) {
				num = stdout.read(buffer);
				if (num >= 0) {
					bufIn.write(buffer, 0, num);
				} else {
					isOutput = false;
				}

				if (stderr.available() > 0) {
					num = stderr.read(buffer);
					if (num >= 0) {
						bufErr.write(buffer, 0, num);
					}
				}
			}
			return new byte[][]{bufIn.toByteArray(), bufErr.toByteArray()};
		}

	}
	
}
