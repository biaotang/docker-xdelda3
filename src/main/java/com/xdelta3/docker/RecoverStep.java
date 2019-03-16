package com.xdelta3.docker;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;

import com.xdelta3.util.DateTimeUtil;
import com.xdelta3.util.FileHelper;
import com.xdelta3.util.ProcessUtil;

/**
 * 
 * @author	biao.tang
 * 2019年3月16日
 * 
 */
public class RecoverStep {
	public String oldImage = "";
	public String patchFile = "";
	public String defaultRootPath = "/com/xdelta3";
	
	public String oldImageFile = "";
	public String tag = "";
	public String newImageFile = "";
	
	public RecoverStep() throws Exception {
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
		tag = DateTimeUtil.format(new Date(), DateTimeUtil.formatOne);
		newImageFile = defaultRootPath + File.separator + tag + ".tar";
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
	}
	
	//xdelta3 patch file
	public void patch() throws Exception {
		String command = "xdelta3 -v -e -s %s %s %s";
		
		command = String.format(command, oldImageFile, patchFile, newImageFile);
		boolean success = ProcessUtil.exec(new String[] {"/bin/bash", "-c", command});
		if (!success) {
			throw new RuntimeException("xdelta3 patch file failed ! command: " + command);
		}
	}
	
	//load image
	public void loadImage() throws Exception {
		String command = "docker load -i %s";
		command = String.format(command, newImageFile);
		boolean success = ProcessUtil.exec(new String[] {"/bin/bash", "-c", command});
		if (!success) {
			throw new RuntimeException("load image failed ! command: " + command);
		}
	}
	
	//clean
	public void clean() throws Exception {
		Files.deleteIfExists(Paths.get(oldImageFile));
	}
}
