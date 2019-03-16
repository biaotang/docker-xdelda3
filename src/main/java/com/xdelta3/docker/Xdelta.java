package com.xdelta3.docker;

public class Xdelta {
	
	//generate xpack file
	public static void xdelta_diff(String... args) throws Exception {
		if (args.length < 4) {
			throw new RuntimeException("diff must have four params, first: const value 'diff'; second: old image; third: new image; fourth: xfilepath, default '/com/xdelta3';");
		}
		DiffStep diffStep = new DiffStep();
		diffStep.oldImage = args[1];
		diffStep.newImage = args[2];
		if (args[3] != null && !args[3].isEmpty()) {
			diffStep.defaultRootPath = args[3];
		}
		try {
			diffStep.init();
			diffStep.saveImages();
			diffStep.generatePatchFile();
		} finally {
			diffStep.clean();
		}
		System.out.println(diffStep.resultFile);
	}
	
	//recover from xpack file
	public static void xdelta_recover(String... args) throws Exception {
		if (args.length < 3) {
			throw new RuntimeException("recover must have three params at least, first: const value 'recover'; second: old image; third: patchfilepath; fourth: newImageFilePath;");
		}
		RecoverStep recoverStep = new RecoverStep();
		recoverStep.oldImage = args[1];
		recoverStep.patchFile = args[2];
		if (args[3] != null && !args[3].isEmpty()) {
			recoverStep.defaultRootPath = args[3];
		}
		try {
			recoverStep.init();
			recoverStep.saveImages();
			recoverStep.patch();
			recoverStep.loadImage();
		} finally {
			recoverStep.clean();
		}
	}
	
	public static void main(String[] args) {
		String command = args[0];
		if (!"diff".equals(command) && !"recover".equals(command)) {
			throw new RuntimeException("Command error, only allow ('diff'、'recover')");
		}
		try {
			if ("diff".equals(command)) {
				xdelta_diff(args);
			} else if ("recover".equals(command)) {
				xdelta_recover(args);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
