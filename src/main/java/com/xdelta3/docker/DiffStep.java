package com.xdelta3.docker;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import com.xdelta3.util.FileHelper;
import com.xdelta3.util.ProcessUtil;

/**
 * 
 * @author	biao.tang
 * 2019年3月15日
 * 
 */
public class DiffStep {
	public String oldImage = "";
	public String newImage = "";
	public String defaultRootPath = "/com/xdelta3";
	
	public String oldImageFile = "";
	public String newImageFile = "";
	public String tag = "";
	public String resultFile = "";
	
	public DiffStep() throws Exception {
		systemCheck();
	}
	
	//check system
	private void systemCheck() throws Exception {
		Properties props=System.getProperties();
		String osName = props.getProperty("os.name");
		if ("Linux".equals(osName) || "Ubuntu".equals(osName)) {
			return;
		}
		throw new RuntimeException("system not supported, only (Linux, Ubuntu) supported");
	}
	
	public void init() {
		tag = oldImage.split(":")[1] + "-2-" + newImage.split(":")[1];
		resultFile = defaultRootPath + File.separator + tag + File.separator + tag + ".tar";
		FileHelper.mkdirs(defaultRootPath + File.separator + tag);
	}
	
	//docker save images
	public void saveImages() throws Exception {
		String command = "docker save %s >> %s";
		oldImageFile = defaultRootPath + File.separator + tag + File.separator + oldImage.split(":")[1] + ".tar";
		boolean success = ProcessUtil.exec(new String[] {"/bin/bash", "-c", String.format(command, oldImage, oldImageFile)});
		if (!success) {
			throw new RuntimeException("image '" + oldImage + "' save failed");
		}
		
		newImageFile = defaultRootPath + File.separator + tag + File.separator + newImage.split(":")[1] + ".tar";
		success = ProcessUtil.exec(new String[] {"/bin/bash", "-c", String.format(command, newImage, newImageFile)});
		if (!success) {
			throw new RuntimeException("image '" + newImage + "' save failed");
		}
	}
	
	//xdelta3 generate patch file
	public void generatePatchFile() throws Exception {
		String command = "xdelta3 -v -e -s %s %s %s";
		
		command = String.format(command, oldImageFile, newImageFile, resultFile);
		boolean success = ProcessUtil.exec(new String[] {"/bin/bash", "-c", command});
		if (!success) {
			throw new RuntimeException("xdelta3 save diff file failed ! command: " + command);
		}
	}
	
	//clean
	public void clean() throws Exception {
		Files.deleteIfExists(Paths.get(oldImageFile));
		Files.deleteIfExists(Paths.get(newImageFile));
	}
	
}
