package com.xdelta3.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author	biao.tang
 * 2019年3月15日
 */
public class FileHelper {
	
	public static void mkdirs(String path) {
		new File(path).mkdirs();
	}
	
	public static void deleteFolder(String folderDir) throws IOException {
		Path path = Paths.get(folderDir);
		deleteFolder(path);
	}

	public static void deleteFolder(Path path) throws IOException {
		if (!Files.exists(path)) {
			return ;
		}
		if (!Files.isDirectory(path)) {
			Files.delete(path);
			return;
		}
		Files.list(path).forEach(p -> {
			try {
				deleteFolder(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		Files.delete(path);
	}
	
	public static void append(String filePath, String line) throws IOException {
		try (FileWriter fw = new FileWriter(new File(filePath), true);
				BufferedWriter bw = new BufferedWriter(fw)){
			bw.write(line);
			bw.newLine();
			bw.flush();
		}
	}

	public static void write(String filePath, String line) throws IOException {
		try (FileWriter fw = new FileWriter(new File(filePath));
				BufferedWriter bw = new BufferedWriter(fw)){
			bw.write(line);
			bw.flush();
		}
	}
	
}
