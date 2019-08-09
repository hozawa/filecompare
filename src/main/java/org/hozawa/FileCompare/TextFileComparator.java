package org.hozawa.FileCompare;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Text file compare class.
 * 
 * @author hitoshi ozawa
 * @version 1.0.0
 * @since 2019/08/02
 */
public class TextFileComparator {
	private final static Logger logger = LogManager.getLogger(TextFileComparator.class);

	/**
	 * Compare files specified in configuration.
	 * @param config
	 * @throws IOException when specified file can not be processed
	 */
	public void compareTextFiles(Config config) throws IOException {
		compareTextFiles(config.getOutputWriter(), new File(config.getFullFilepath1()), new File(config.getFullFilepath2()), config.getEncoding(), config.getAppendFlg(), config.getIncludeHeaderFlg(), config.getReadAheadLineCnt(), config.getBufferSize(), config.getMaxDiffLineCnt(), config.getColumnSeparator(), config.getMaxColumnDiffLength());
	}
	
	/**
	 * 
	 * @param bw
	 * @param file1
	 * @param file2
	 * @param encoding
	 * @param appendOutput
	 * @param includeHeader
	 * @param readAheadCnt
	 * @param bufferSize
	 * @param maxDiffLineCnt
	 * @param columnSeparator
	 * @param maxDiffChars
	 * @throws IOException
	 */
	public void compareTextFiles(BufferedWriter bw, File file1, File file2, String encoding, boolean appendOutput, boolean includeHeader, int readAheadCnt, int bufferSize, int maxDiffLineCnt, String columnSeparator, int maxDiffChars) throws IOException {
		if (file1 == null) {
			log(bw, columnSeparator, Messages.getString("TextFileComparator.null"), "", 0, 0, "", "", 0, Messages.getString("TextFileComparator.null_is_specified_as_file_1_name")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			return;
		}
		if (file2 == null) {
			log(bw, columnSeparator, "", Messages.getString("TextFileComparator.null"), 0, 0, "", "", 0, Messages.getString("TextFileComparator.null_is_specified_as_file2_name")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			return;
		}
		if (includeHeader) {
			logHeader(bw, columnSeparator);
		}

		boolean isSame = true;	// true if 2 files are the same. false otherwise
		int diffCnt = 0;
		
		BufferedReader reader1 = null;
		BufferedReader reader2 = null;
		
		try {
			reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(file1), encoding));
			reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(file2), encoding));
         
			String file1Line = reader1.readLine();
			String file2Line = reader2.readLine();
			String tmpLine;
         
			int file1LineNo = 1;
			int file2LineNo = 1;
         
			while (file1Line != null || file2Line != null) {
				if (file1Line == null) {						// file 1 is missing lines at end of file
					log(bw, columnSeparator, file1.getName(), file2.getName(), file1LineNo, file2LineNo-1, "", file2Line, maxDiffChars, Messages.getString("TextFileComparator.file_2_has_additional_ending_lines")); //$NON-NLS-1$ //$NON-NLS-2$
					diffCnt++;
					if (diffCnt > maxDiffLineCnt) {
						return;
					}
					isSame = false;
					// output all additional lines in file 2
					file2Line = reader2.readLine();
					file2LineNo++;
				} else if (file2Line == null) {				// file 2 is missing lines at end of file
					log(bw, columnSeparator, file1.getName(), file2.getName(), file1LineNo-1, file2LineNo, file1Line, "", maxDiffChars, Messages.getString("TextFileComparator.file_2_has_additional_ending_lines")); //$NON-NLS-1$ //$NON-NLS-2$
					diffCnt++;
					if (diffCnt > maxDiffLineCnt) {
						return;
					}
					isSame = false;
					// output all additional lines in file 1
					file1Line = reader1.readLine();
					file1LineNo++;
				} else if (file1Line.equals(file2Line)){	// lines are the same
					file1Line = reader1.readLine();
					file2Line = reader2.readLine();
					file1LineNo++;
					file2LineNo++;
				} else {										// lines are different
					isSame = false;
					logger.debug("unmatched line found - file1 line:{} file2 line:{}", file1Line, file2Line); //$NON-NLS-1$
					
					// try moving through searching file2 to matching line
					logger.debug("moving through file 2 to find matching line"); //$NON-NLS-1$
					reader2.mark(bufferSize);
					Queue<LineInfo> queue = new LinkedList<LineInfo>();
					for (int readAheadLine = 0; readAheadLine < readAheadCnt; readAheadLine++) {
						tmpLine = reader2.readLine();
						if (tmpLine == null) {
							break;
						}
						queue.add(new LineInfo(file2LineNo + readAheadLine, tmpLine));
						if (file1Line.equals(tmpLine)) {	// found matching line. Skip to this line
							log(bw, columnSeparator, file1.getName(), file2.getName(), file1LineNo, file2LineNo, "", file2Line, maxDiffChars, Messages.getString("TextFileComparator.file_2_has_additional_lines")); //$NON-NLS-1$ //$NON-NLS-2$
							diffCnt++;
							if (diffCnt > maxDiffLineCnt) {
								return;
							}
							// output all lines in file2 between file2LineNo and file2LineNo+readAheadLine
							for (int j = 0; j < readAheadLine; j++) {
								LineInfo lineInfo = queue.poll();
								if (lineInfo != null) {
									logger.debug("---- line 2:{} {}", lineInfo.getLineNo() + 1,lineInfo.getContent()); //$NON-NLS-1$
									log(bw, columnSeparator, file1.getName(), file2.getName(), file1LineNo, lineInfo.getLineNo()+1, "", lineInfo.getContent(), maxDiffChars, Messages.getString("TextFileComparator.file_2_has_additional_lines")); //$NON-NLS-1$ //$NON-NLS-2$
									diffCnt++;
									if (diffCnt > maxDiffLineCnt) {
										return;
									}
								}
							}
							
							file2LineNo += readAheadLine + 1;
							file2Line = tmpLine;
							logger.debug("trying to find matching line in file1. Found. fileLine1:{} fileLine2:{} file2 line#:{}", file1Line, tmpLine, file2LineNo); //$NON-NLS-1$
							break;
						}
					}
					logger.debug("======================"); //$NON-NLS-1$
//					logger.debug("** checked file2 fileLine1:{} file2Line:{}", file1Line, file2Line);
					if (file1Line.equals(file2Line)) {	// if matched line found, move to next line
						file1Line = reader1.readLine();
						file2Line = reader2.readLine();
						file1LineNo++;
						file2LineNo++;
						logger.debug("found matching line so moving to next line. file1 #:{} file2 #:{}", file1LineNo, file2LineNo); //$NON-NLS-1$
					} else {	// try to search matching line in file 1
						reader2.reset();						// reset file 2 past matching line
						reader1.mark(bufferSize);
						boolean foundUnmatch = false;
						logger.debug("moving through file 1 to find matching line"); //$NON-NLS-1$
						queue = new LinkedList<LineInfo>();
						for (int readAheadLine = 0; readAheadLine < readAheadCnt; readAheadLine++) {
							tmpLine = reader1.readLine();
//							logger.debug("++ fileLine1:{} fileLine2:{}", tmpLine, file2Line);
							if (tmpLine == null) {	// reached end of file1 without finding matched line
								logger.debug("unable to find matching line in file1. file2: {}", file2Line); //$NON-NLS-1$
								log(bw, columnSeparator, file1.getName(), file2.getName(), file1LineNo, file2LineNo, file1Line, file2Line, maxDiffChars, Messages.getString("TextFileComparator.lines_do_not_match")); //$NON-NLS-1$
								diffCnt++;
								if (diffCnt > maxDiffLineCnt) {
									return;
								}
								foundUnmatch = true;
								reader1.reset();
								break;
							}
							queue.add(new LineInfo(file1LineNo + readAheadLine, tmpLine));
							if (tmpLine.equals(file2Line)) {
								logger.debug("found matching line in file1. line: {}", readAheadLine); //$NON-NLS-1$
								log(bw, columnSeparator, file1.getName(), file2.getName(), file1LineNo, file2LineNo, file1Line, "", maxDiffChars, Messages.getString("TextFileComparator.file_2_is_missing_line"));  //$NON-NLS-1$//$NON-NLS-2$
								diffCnt++;
								if (diffCnt > maxDiffLineCnt) {
									return;
								}
								
								for (int j = 0; j < readAheadLine; j++) {
									LineInfo lineInfo = queue.poll();
									if (lineInfo != null) {
										logger.debug("---- line 2:{} {}", lineInfo.getLineNo() + 1,lineInfo.getContent()); //$NON-NLS-1$
										log(bw, columnSeparator, file1.getName(), file2.getName(), lineInfo.getLineNo()+1, file2LineNo, lineInfo.getContent(), "", maxDiffChars, Messages.getString("TextFileComparator.file_2_has_missing_lines")); //$NON-NLS-1$ //$NON-NLS-2$
										diffCnt++;
										if (diffCnt > maxDiffLineCnt) {
											return;
										}
									}
								}
								file1LineNo += readAheadLine + 1;
								foundUnmatch = true;
								break;
							}
						}
						logger.debug("** resetting reader1 file1:{} file2:{}", file1Line, file2Line); //$NON-NLS-1$
						if (!foundUnmatch) {
							logger.debug("** resetting reader1 file1:{} file2:{}", file1Line, file2Line); //$NON-NLS-1$
							log(bw, columnSeparator, file1.getName(), file2.getName(), file1LineNo, file2LineNo, file1Line, "", maxDiffChars, Messages.getString("TextFileComparator.unmatched_line")); //$NON-NLS-1$ //$NON-NLS-2$
							diffCnt++;
							if (diffCnt > maxDiffLineCnt) {
								return;
							}
							reader1.reset();
						}
						file1Line = reader1.readLine();
						file2Line = reader2.readLine();
						file1LineNo++;
						file2LineNo++;
						
						logger.debug("***** moving to next line:" + file1Line + " " + file2Line); //$NON-NLS-1$ //$NON-NLS-2$
					}
				} 

        	}
	        
	       if (isSame) {
	            log(bw, columnSeparator, file1.getName(), file2.getName(), 0, 0, "", "", maxDiffChars, Messages.getString("TextFileComparator.files_are_the_same")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	       }
		} finally {
			if (reader1 != null) {
				reader1.close();
			}
			if (reader2 != null) {
				reader2.close();
			}
		}	
	}  

	/**
	 * Output messages pertaining to lines in files to output.
	 * @param bw
	 * @param columnSeparator
	 * @param file1Name
	 * @param file2Name
	 * @param file1No
	 * @param file2No
	 * @param file1Content
	 * @param file2Content
	 * @param maxDiffChars
	 * @param message
	 * @throws IOException
	 */
	private void log(BufferedWriter bw, String columnSeparator, String file1Name, String file2Name, int file1No, int file2No, String file1Content, String file2Content, int maxDiffChars, String message) throws IOException {
//		String strFormat = "%s,%s,%d,%d,%s,%s,%s" + Consts.NEW_LINE;
		String strFormat = "%s,%s,%d,%d,%s,%s" + Consts.NEW_LINE; //$NON-NLS-1$
		strFormat = strFormat.replace(",", columnSeparator); //$NON-NLS-1$

		String diff = ""; //$NON-NLS-1$
		if (maxDiffChars > 0) {
			diff = StringUtils.difference(file1Content, file2Content);
			if (diff.length() > 0) {
				diff = diff.substring(0, Math.min(diff.length(), maxDiffChars));
			}
		}
//		String str = String.format(strFormat, file1Name, file2Name, file1No, file2No, file1Content, file2Content,message);
		String str = String.format(strFormat, file1Name, file2Name, file1No, file2No, diff, message);
		if (bw == null) {
			logger.info(str);
		} else {
			bw.write(str);
		}
	}
	
	/**
	 * Log missing file message to output.
	 * @param bw
	 * @param columnSeparator
	 * @param filenames
	 * @param maxDiffChars
	 * @param message
	 * @throws IOException
	 */
	public void logMissingFiles(BufferedWriter bw, String columnSeparator, Collection<String> filenames, int maxDiffChars, String message) throws IOException {
		for (Iterator<String> iterator = filenames.iterator(); iterator.hasNext();) {
			log(bw, columnSeparator, iterator.next(), "", 0, 0, "", "", 0, Messages.getString("TextFileComparator.files_missing_in_directory2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}
	
	/**
	 * Output additional file message to output. 
	 * @param bw
	 * @param columnSeparator
	 * @param filenames
	 * @param maxDiffChars
	 * @param message
	 * @throws IOException
	 */
	public void logAdditionalFiles(BufferedWriter bw, String columnSeparator, Collection<String> filenames, int maxDiffChars, String message) throws IOException {
		for (Iterator<String> iterator = filenames.iterator(); iterator.hasNext();) {
			log(bw, columnSeparator, "", iterator.next(), 0, 0, "", "", 0, message); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}
	
	/**
	 * Output message when both specified directories are empty.
	 * @param bw
	 * @param columnSeparator
	 * @throws IOException
	 */
	public void logBothDirEmpty(BufferedWriter bw, String columnSeparator) throws IOException {
		log(bw, columnSeparator, "", "", 0, 0, "", "", 0, Messages.getString("TextFileComparator.both_directories_are_empty")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}
	
	/**
	 * Output header row.
	 * @param bw
	 * @param columnSeparator
	 * @throws IOException
	 */
	private void logHeader(BufferedWriter bw, String columnSeparator) throws IOException {
//		String strFormat = "%s,%s,%s,%s,%s,%s,%s" + Consts.NEW_LINE;
		String strFormat = "%s,%s,%s,%s,%s,%s" + Consts.NEW_LINE; //$NON-NLS-1$
		strFormat = strFormat.replace(",", columnSeparator); //$NON-NLS-1$
//		String str = String.format(strFormat, "Production Filename", "Test Filename", "Prod Line No", "Test Line No", "Prod Content", "Test Content", "Message");
		String str = String.format(strFormat, Messages.getString("TextFileComparator.file_1"), Messages.getString("TextFileComparator.file_2"), Messages.getString("TextFileComparator.file_1_line_no"), Messages.getString("TextFileComparator.file_2_line_no"), Messages.getString("TextFileComparator.content_differences"), Messages.getString("TextFileComparator.message")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		if (bw == null) {
			logger.info(str);
		} else {
			bw.write(str);
		}
	}
	
	/**
	 * Configuration class holding all settings.
	 * 
	 * @author hitoshi ozawa
	 * @version 1.0.0
	 * @since 2019/08/02
	 */
	private final static class LineInfo {
		private final int lineNo;
		private final String content;
		
		protected LineInfo(final int lineNo, final String content) {
			this.lineNo = lineNo;
			this.content = content;
		}
		protected int getLineNo() {
			return this.lineNo;
		}
		protected String getContent() {
			return this.content;
		}
	}
}
