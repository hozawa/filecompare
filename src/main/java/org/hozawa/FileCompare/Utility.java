package org.hozawa.FileCompare;

import java.io.File;
import java.util.HashSet;

/**
 * Utility class for FileCompare tool.
 * 
 * @author hitoshi ozawa
 * @version 1.0.0
 * @since 2019/08/02
 */
public final class Utility {
	private Utility() {
		throw new AssertionError();
	}
	
	public static boolean isWindows() {
		String OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		String OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		String OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
	}

	public static boolean isSolaris() {
		String OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("sunos") >= 0);
	}

	public static String getFilePath(String path) {
		if (path == null || path.length() < 1) {
			return null;
		}
		if (isWindows()) {
			path = path.replace("/", Consts.FILE_SEPARATOR);
		} else {
			path = path.replace("\\", Consts.FILE_SEPARATOR);
		}
		if (!path.endsWith(Consts.FILE_SEPARATOR)) {
			path += Consts.FILE_SEPARATOR;
		}
		return path;
	}
	public static String getFullFilename(String filename) {
		if (filename == null) {
			return null;
		}
		if (isWindows()) {
			filename = filename.replace("/", Consts.FILE_SEPARATOR);
		} else {
			filename = filename.replace("\\", Consts.FILE_SEPARATOR);
		}
		return filename;
	}
	
    public static HashSet<String> listFilesInDir(final File dir) {
    	try {
	    	if (dir == null) {
	    		return null;
	    	}
	    	File[] files = dir.listFiles();
	    	if (files == null) {
	    		return null;
	    	} else {
		    	HashSet<String> fileNames = new HashSet<String>();
				for (final File fileEntry : files) {
					if (!fileEntry.isDirectory()) {
						fileNames.add(fileEntry.getName());
				    } 
				}
				if (fileNames.size() < 1) {
					return null;
				}
				return fileNames;
	    	}
    	} catch (SecurityException e) {
    		return null;
    	}
    }
}
