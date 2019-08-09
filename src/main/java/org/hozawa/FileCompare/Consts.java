package org.hozawa.FileCompare;

/**
 * Constant class.
 * 
 * @author hitoshi ozawa
 * @version 1.0.0
 * @since 2019/08/02
 */
public final class Consts {
	public static final String PROJECT_NAME = "FileCompare";	// project name
	public static final String VERSION = "1.0.0";					// version
	
	public static final String NEW_LINE = System.getProperty("line.separator");
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	public static final String PATH_SEPARATOR = System.getProperty("path.separator");
	
	public static final String DEFAULT_ENCODING = "Shift-JIS";	// "UTF-8"
	public static final int BUFFER_SIZE = 1000;					// read ahead buffer size
	public static final int MAX_READ_AHEAD_LINE_CNT = 10;			// number of lines to read ahead to check for matching line
	public static final int MAX_DIFF_LINE_CNT = 10;				// max number of different lines in a file before terminating
	
	public static final String COLUMN_SEPARATOR = ",";			// output csv field separator character
	public static final int MAX_COLUMN_DIFF_LENGTH = 10;			// max length of differences string between between lines
	
	public static final String DEFAULT_OUTPUT_FILENAME = "src/test/resources/output/diff.csv";	// default output file full path
	
	private Consts() {
		throw new AssertionError();
	}
}
