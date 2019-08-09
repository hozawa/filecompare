package org.hozawa.FileCompare;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

/**
 * Configuration class holding all settings.
 * 
 * @author hitoshi ozawa
 * @version 1.0.0
 * @since 2019/08/02
 */
public class Config {
	// default property values
	private static final String PROPERTY_FILE = "/filecompare.properties"; //$NON-NLS-1$
	
	private static final String APPEND_FLG = "false"; //$NON-NLS-1$
	private static final String INCLUDE_HEADER_FLG = "false"; //$NON-NLS-1$
	
	private static final String OUTPUT_FILENAME = Consts.DEFAULT_OUTPUT_FILENAME;
	
	private static final String ENCODING = Consts.DEFAULT_ENCODING;
	private static final int READ_AHEAD_LINE_CNT = Consts.MAX_READ_AHEAD_LINE_CNT;
	private static final int BUFFER_SIZE = Consts.BUFFER_SIZE;
	private static final int MAX_DIFF_LINE_CNT = Consts.MAX_DIFF_LINE_CNT;
	private static final String COLUMN_SEPARATOR = Consts.COLUMN_SEPARATOR;
	private static final int MAX_COLUMN_DIFF_LENGTH = Consts.MAX_COLUMN_DIFF_LENGTH;
	
	private static final String QUIET_FLG = "false"; //$NON-NLS-1$
	
	private static final Properties config = new Properties();	// properties are read from properties file if found
	
	// properties variables
	private boolean append_flg = Boolean.parseBoolean(APPEND_FLG);
	private boolean include_header_flg = Boolean.parseBoolean(INCLUDE_HEADER_FLG);
	
	private String file1_dir;
	private String file2_dir;
	private String file1_name;
	private String file2_name;
	private String output_filename = OUTPUT_FILENAME;
	private BufferedWriter bw = null;
	
	private String encoding = ENCODING;
	private int read_ahead_line_cnt = READ_AHEAD_LINE_CNT;
	private int buffer_size = BUFFER_SIZE;
	private int max_diff_line_cnt = MAX_DIFF_LINE_CNT;
	private String column_separator = COLUMN_SEPARATOR;
	private int max_column_diff_length = MAX_COLUMN_DIFF_LENGTH;
	
	private boolean quiet_flg = Boolean.parseBoolean(QUIET_FLG);
	
	/**
	 * Default constructor
	 * 
	 * @throws IOException
	 */
	public Config() throws IOException{
		initDefault(PROPERTY_FILE);
	}
	
	/**
	 * Constructor
	 * 
	 * @param propertyFilename	property file to use
	 * @throws IOException		property file not found
	 */
	public Config(String propertyFilename) throws IOException {
		if (propertyFilename == null) {
			initDefault(PROPERTY_FILE);
		} else {
			initFromFile(propertyFilename);
		}
	}

	/**
	 * Initialize using specified configuration.
	 *   1. load specified configuration file
	 *   2. if not able to load, switch to using default configuration
	 * 
	 * @param propertyFile
	 * @throws IOException
	 */
	private void initFromFile(String propertyFile) throws IOException {
		InputStream is = null;
		try {
			is = new BufferedInputStream( new FileInputStream(propertyFile));

			config.load(is);
			setFromConfigFile();
		} catch (FileNotFoundException e) {
			System.out.println(Messages.getString("Config.file") + propertyFile + Messages.getString("Config.was_not_found_Using_default_configuration"));  //$NON-NLS-1$//$NON-NLS-2$
			initDefault(PROPERTY_FILE);
		} finally {
			if(is != null){
				is.close();
			}
		}
	}
	
	/**
	 * Initialize using default configuration.
	 * 
	 * @param propertyFile
	 * @throws IOException
	 */
	private void initDefault(String propertyFile) throws IOException {
		InputStream is = null;
		try {
			is = Config.class.getResourceAsStream(propertyFile);
			if (is != null) {
				config.load(is);
				setFromConfigFile();
			}
		} catch (FileNotFoundException e1) {
		} catch (IOException e2) {
		} finally {
			if(is != null){
				is.close();
			}
		}
	}
	
	/**
	 * Read and set properties from property file.
	 */
	private void setFromConfigFile() {
		setAppendFlg(config.getProperty("append_flg")); //$NON-NLS-1$
		setIncludeHeaderFlg(config.getProperty("include_header_flg")); //$NON-NLS-1$
		setFile1Dir(config.getProperty("file1_dir")); //$NON-NLS-1$
		setFile2Dir(config.getProperty("file2_dir")); //$NON-NLS-1$
		setFile1Name(config.getProperty("file1_name")); //$NON-NLS-1$
		setFile2Name(config.getProperty("file2_name")); //$NON-NLS-1$
		setOutputFilename(config.getProperty("output_filename")); //$NON-NLS-1$
		setEncoding(config.getProperty("encoding")); //$NON-NLS-1$
		setReadAheadLineCnt(config.getProperty("read_ahead_line_cnt")); //$NON-NLS-1$
		setBufferSize(config.getProperty("buffer_size")); //$NON-NLS-1$
		setMaxDiffLineCnt(config.getProperty("max_diff_line_cnt")); //$NON-NLS-1$
		setColumnSeparator(config.getProperty("column_separator")); //$NON-NLS-1$
		setMaxColumnDiffLength(config.getProperty("max_column_diff_length")); //$NON-NLS-1$
		setQuietFlg(config.getProperty("quiet")); //$NON-NLS-1$
	}
	
	/**
	 * Get append flag.
	 * @return append flag.
	 */
	public boolean getAppendFlg() {
		return this.append_flg;
	}
	/**
	 * Set append flag.
	 * @param String true: append to existing file, false: erase previous content
	 */
	public void setAppendFlg(String append_flg) {
		if (append_flg == null) {
			append_flg = APPEND_FLG;
		}
		setAppendFlg(Boolean.parseBoolean(append_flg));
	}
	public void setAppendFlg(boolean append_flg) {
		this.append_flg = append_flg;
	}
	
	/**
	 * Get include header flag.
	 * @return
	 */
	public boolean getIncludeHeaderFlg() {
		return this.include_header_flg;
	}
	/**
	 * Set include header flag
	 * @param String "true": output column header, otherwise: do not output column header
	 */
	public void setIncludeHeaderFlg(String include_header_flg) {
		if (include_header_flg == null) {
			include_header_flg = INCLUDE_HEADER_FLG;
		}
		setIncludeHeaderFlg(Boolean.parseBoolean(include_header_flg));
	}
	/**
	 * Set include header flag.
	 * @param include_header_flg boolean true: output column header, otherwise: do not output column header
	 */
	public void setIncludeHeaderFlg(boolean include_header_flg) {
		this.include_header_flg = include_header_flg;
	}
	
	/**
	 * Get file 1 directory.
	 * @return file 1 directory.
	 */
	public String getFile1Dir() {
		return this.file1_dir;
	}
	/**
	 * Set file 1 directory.
	 * @param file1_dir String file 1 directory
	 */
	public void setFile1Dir(String file1_dir) {
		this.file1_dir = Utility.getFilePath(file1_dir);
	}
	/**
	 * 
	 * @return
	 */
	public String getFile2Dir() {
		return this.file2_dir;
	}
	/**
	 * 
	 * @param file2_dir
	 */
	public void setFile2Dir(String file2_dir) {
		this.file2_dir = Utility.getFilePath(file2_dir);
	}
	/**
	 * 
	 * @return
	 */
	public String getFile1Name() {
		return this.file1_name;
	}
	/**
	 * 
	 * @return
	 */
	public String getFullFilepath1() {
		if (this.file1_dir == null) {
			return this.file1_name;
		} else {
			return this.file1_dir + this.file1_name;
		}
	}
	/**
	 * 
	 * @param file1_name
	 */
	public void setFile1Name(String file1_name) {
		this.file1_name = file1_name;
	}
	/**
	 * 
	 * @return
	 */
	public String getFile2Name() {
		return this.file2_name;
	}
	/**
	 * 
	 * @return
	 */
	public String getFullFilepath2() {
		if (this.file2_dir == null) {
			return this.file2_name;
		} else {
			return this.file2_dir + this.file2_name;
		}
	}
	/**
	 * 
	 * @param file2_name
	 */
	public void setFile2Name(String file2_name) {
		this.file2_name = file2_name;
	}
	/**
	 * 
	 * @return
	 */
	public String getOutputFilename() {
		return this.output_filename;
	}
	/**
	 * 
	 * @param output_filename
	 */
	public void setOutputFilename(String output_filename) {
		if (output_filename == null || output_filename.length() < 1) {
			this.output_filename = OUTPUT_FILENAME;
		} else {
			String tmpFilename = Utility.getFullFilename(output_filename);
			if (tmpFilename.endsWith("/") || tmpFilename.endsWith("\\")) { //$NON-NLS-1$ //$NON-NLS-2$
				this.output_filename = tmpFilename + OUTPUT_FILENAME;
			} else {
				this.output_filename = tmpFilename;
			}
		}
	}
	/**
	 * 
	 * @return
	 */
	public BufferedWriter getOutputWriter() {
		return this.bw;
	}
	/**
	 * 
	 * @throws IOException
	 */
	public void setOutputWriter() throws IOException {
		if (this.output_filename != null) {
			this.bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.output_filename, this.append_flg), this.encoding));
		} else {
			throw new IOException(Messages.getString("Config.output_filename_is_null_please_specify_output_filename")); //$NON-NLS-1$
		}
	}
	/**
	 * 
	 */
	public void closeOutputWriter() {
		if (this.bw != null) {
			try {
				this.bw.close();
			} catch (IOException e) {
			}
		}
	}
	/**
	 * 
	 * @return
	 */
	public String getEncoding() {
		return this.encoding;
	}
	/**
	 * 
	 * @param encoding
	 */
	public void setEncoding(String encoding) {
		if (encoding == null) 
			this.encoding = ENCODING;
		else {
			this.encoding = encoding;
		}
	}
	/**
	 * 
	 * @return
	 */
	public int getReadAheadLineCnt() {
		return this.read_ahead_line_cnt;
	}
	/**
	 * 
	 * @param strRead_ahead_line_cnt
	 */
	public void setReadAheadLineCnt(String strRead_ahead_line_cnt) {
		int read_ahead_line_cnt = Consts.MAX_READ_AHEAD_LINE_CNT;
		if (strRead_ahead_line_cnt == null) {
			read_ahead_line_cnt = READ_AHEAD_LINE_CNT;
		} else {
			try {
				read_ahead_line_cnt = Integer.parseInt(strRead_ahead_line_cnt);
			} catch (NumberFormatException e) {
			}
		}
		setReadAheadLineCnt(read_ahead_line_cnt);
	}
	/**
	 * 
	 * @param read_ahead_line_cnt
	 */
	public void setReadAheadLineCnt(int read_ahead_line_cnt) {
		if (read_ahead_line_cnt < 1) {
			read_ahead_line_cnt = READ_AHEAD_LINE_CNT;
		}
		this.read_ahead_line_cnt = read_ahead_line_cnt;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getBufferSize() {
		return this.buffer_size;
	}
	/**
	 * 
	 * @param strRead_ahead_line_cnt
	 */
	public void setBufferSize(String strBuffer_size) {
		int buffer_size = Consts.BUFFER_SIZE;
		if (strBuffer_size == null) {
			buffer_size = BUFFER_SIZE;
		} else {
			try {
				read_ahead_line_cnt = Integer.parseInt(strBuffer_size);
			} catch (NumberFormatException e) {
			}
		}
		setBufferSize(buffer_size);
	}
	/**
	 * 
	 * @param read_ahead_line_cnt
	 */
	public void setBufferSize(int buffer_size) {
		if (buffer_size < 1) {
			buffer_size = BUFFER_SIZE;
		}
		this.buffer_size = buffer_size;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMaxDiffLineCnt() {
		return this.max_diff_line_cnt;
	}
	/**
	 * 
	 * @param strRead_ahead_line_cnt
	 */
	public void setMaxDiffLineCnt(String strMax＿diff_line_cnt) {
		int max_diff_line_cnt = MAX_DIFF_LINE_CNT;
		if (strMax＿diff_line_cnt != null) {
			try {
				max_diff_line_cnt = Integer.parseInt(strMax＿diff_line_cnt);
			} catch (NumberFormatException e) {
			}
		}
		setMaxDiffLineCnt(max_diff_line_cnt);
	}
	/**
	 * 
	 * @param read_ahead_line_cnt
	 */
	public void setMaxDiffLineCnt(int max_diff_line_cnt) {
		if (max_diff_line_cnt < 1) {
			max_diff_line_cnt = MAX_DIFF_LINE_CNT;
		}
		this.max_diff_line_cnt = max_diff_line_cnt;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getColumnSeparator() {
		return this.column_separator;
	}
	/**
	 * 
	 * @param column_separator
	 */
	public void setColumnSeparator(String column_separator) {
		if (column_separator != null && column_separator.length() > 0) {
			this.column_separator = column_separator;
		}
	}
	
	
	/**
	 * Get maximum characters to show differences between lines.
	 * @return maximum characters to show differences between lines
	 */
	public int getMaxColumnDiffLength() {
		return this.max_column_diff_length;
	}
	/**
	 * Set maximum characters to show differences between lines.
	 * @param strRead_ahead_line_cnt maximum characters to show differences between lines
	 */
	public void setMaxColumnDiffLength(String strMax_column_diff_length) {
		int max_column_diff_length = Consts.MAX_COLUMN_DIFF_LENGTH;
		if (strMax_column_diff_length != null) {
			try {
				max_column_diff_length = Integer.parseInt(strMax_column_diff_length);
			} catch (NumberFormatException e) {
			}
		}
		setMaxColumnDiffLength(max_column_diff_length);
	}
	/**
	 * Set maximum characters to show differences between lines.
	 * @param max_column_diff_length maximum characters to show differences between lines
	 */
	public void setMaxColumnDiffLength(int max_column_diff_length) {
		if (max_column_diff_length >= 0) {
			this.max_column_diff_length = max_column_diff_length;
		}
	}
	
	/**
	 * Get quiet flag.
	 * @return quiet flag.
	 */
	public boolean getQuietFlg() {
		return this.quiet_flg;
	}
	/**
	 * Set quiet flag.
	 * @param quiet_flg true: do not output messages to console
	 */
	public void setQuietFlg(String quiet_flg) {
		if (quiet_flg == null) {
			quiet_flg = QUIET_FLG;
		}
		setQuietFlg(Boolean.parseBoolean(quiet_flg));
	}
	/**
	 * 
	 * @param quiet_flg true: do not output messages to console
	 */
	public void setQuietFlg(boolean quiet_flg) {
		this.quiet_flg = quiet_flg;
	}
}
