package org.hozawa.FileCompare;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Text file comparison tool.
 * 
 * @author hitoshi ozawa
 * @version 1.0.0
 * @since 2019/08/02
 */
public class FileCompare {
    public static void main(String[] args) {
    	Config config = null;
    	
    	final Option appendOption = Option.builder("a") //$NON-NLS-1$
    			.required(false)
    	       .longOpt("append") //$NON-NLS-1$
    	       .desc(Messages.getString("FileCompare.append_to_existing_output_file")) //$NON-NLS-1$
    	       .build();
    	final Option bufferOption = Option.builder("b") //$NON-NLS-1$
    			.required(false)
    			.hasArgs()
    	       .longOpt("buffer") //$NON-NLS-1$
    	       .desc(Messages.getString("FileCompare.read_ahead_buffer_size")) //$NON-NLS-1$
    	       .build();
    	final Option confOption = Option.builder("c") //$NON-NLS-1$
    			.required(false)
    	       .hasArgs()
    	       .longOpt("conf") //$NON-NLS-1$
    	       .desc(Messages.getString("FileCompare.configuration_file_to_use")) //$NON-NLS-1$
    	       .build();
    	final Option maxDiffLineCntOption = Option.builder("d") //$NON-NLS-1$
    			.required(false)
    	       .hasArgs()
    	       .type(Integer.class)
    	       .longOpt("diffCnt") //$NON-NLS-1$
    	       .desc(Messages.getString("FileCompare.max_number_of_differing_lines_per_file")) //$NON-NLS-1$
    	       .build();
    	final Option encodingOption = Option.builder("e") //$NON-NLS-1$
    			.required(false)
    	       .hasArgs()
    	       .longOpt("encoding") //$NON-NLS-1$
    	       .desc(Messages.getString("FileCompare.file_encoding")) //$NON-NLS-1$
    	       .build();
    	final Option file1Option = Option.builder("f") //$NON-NLS-1$
    			.required(false)
    	       .hasArgs()
    	       .longOpt("file1") //$NON-NLS-1$
    	       .desc(Messages.getString("FileCompare.input_file_1")) //$NON-NLS-1$
    	       .build();
    	final Option file2Option = Option.builder("g") //$NON-NLS-1$
    			.required(false)
    	       .hasArgs()
    	       .longOpt("file2") //$NON-NLS-1$
    	       .desc(Messages.getString("FileCompare.input_file_2")) //$NON-NLS-1$
    	       .build();
    	final Option dir1Option = Option.builder("i") //$NON-NLS-1$
    			.required(false)
    	       .hasArgs()
    	       .longOpt("dir1") //$NON-NLS-1$
    	       .desc(Messages.getString("FileCompare.input_dir_1")) //$NON-NLS-1$
    	       .build();
    	final Option dir2Option = Option.builder("j") //$NON-NLS-1$
    			.required(false)
    	       .hasArgs()
    	       .longOpt("dir2") //$NON-NLS-1$
    	       .desc(Messages.getString("FileCompare.input_dir_2")) //$NON-NLS-1$
    	       .build();
    	final Option outputOption = Option.builder("o") //$NON-NLS-1$
    			.required(false)
    	       .hasArgs()
    	       .longOpt("output") //$NON-NLS-1$
    	       .desc(Messages.getString("FileCompare.output_log_full_path")) //$NON-NLS-1$
    	       .build();
    	final Option quietOption = Option.builder("q") //$NON-NLS-1$
    			.required(false)
    	       .longOpt("quiet") //$NON-NLS-1$
    	       .desc(Messages.getString("FileCompare.hide_console_messages")) //$NON-NLS-1$
    	       .build();
    	final Option readAheadOption = Option.builder("r") //$NON-NLS-1$
    			.required(false)
    	       .hasArgs()
    	       .type(Integer.class)
    	       .longOpt("readahead") //$NON-NLS-1$
    	       .desc(Messages.getString("FileCompare.number_of_rows_to_read_ahead_to_find_matching_line")) //$NON-NLS-1$
    	       .build();
    	final Option columnSeparatorOption = Option.builder("s") //$NON-NLS-1$
    			.required(false)
    	       .hasArgs()
    	       .longOpt("separator") //$NON-NLS-1$
    	       .desc(Messages.getString("FileCompare.output_column_separator_character")) //$NON-NLS-1$
    	       .build();
    	final Option titleOption = Option.builder("t") //$NON-NLS-1$
    			.required(false)
    	       .longOpt("title") //$NON-NLS-1$
    	       .desc(Messages.getString("FileCompare.output_column_header_row")) //$NON-NLS-1$
    	       .build();
    	final Option helpOption = Option.builder("h") //$NON-NLS-1$
    			.required(false)
    	       .longOpt("help") //$NON-NLS-1$
    	       .desc(Messages.getString("FileCompare.display_this_help")) //$NON-NLS-1$
    	       .build();
    	final Option versionOption = Option.builder("v") //$NON-NLS-1$
    			.required(false)
    	       .longOpt("version") //$NON-NLS-1$
    	       .desc(Messages.getString("FileCompare.version")) //$NON-NLS-1$
    	       .build();
    	
    	final Options options = new Options();
    	options.addOption(appendOption);
    	options.addOption(bufferOption);
    	options.addOption(confOption);
    	options.addOption(dir1Option);
    	options.addOption(dir2Option);
    	options.addOption(maxDiffLineCntOption);
    	options.addOption(file1Option);
    	options.addOption(file2Option);
    	options.addOption(encodingOption);
    	options.addOption(outputOption);
    	options.addOption(quietOption);
    	options.addOption(readAheadOption);
    	options.addOption(columnSeparatorOption);
    	options.addOption(titleOption);
    	options.addOption(helpOption);
    	options.addOption(versionOption);
    	
    	DefaultParser parser = new DefaultParser();
    	CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);

	    	if (cmd.hasOption("c")) { //$NON-NLS-1$
	    		config = new Config(cmd.getOptionValue("c")); //$NON-NLS-1$
	    	} else {
	    		config = new Config();
	    	}
	    	if (cmd.hasOption("a")) { //$NON-NLS-1$
	    		config.setAppendFlg(true);
	    	}
	    	if (cmd.hasOption("b")) { //$NON-NLS-1$
	    		config.setBufferSize(cmd.getOptionValue("b")); //$NON-NLS-1$
	    	}
	    	if (cmd.hasOption("d")) { //$NON-NLS-1$
	    		config.setMaxDiffLineCnt(cmd.getOptionValue("d")); //$NON-NLS-1$
	    	}
	    	if (cmd.hasOption("e")) { //$NON-NLS-1$
	    		config.setEncoding(cmd.getOptionValue("e")); //$NON-NLS-1$
	    	}
	    	if (cmd.hasOption("f")) { //$NON-NLS-1$
	    		config.setFile1Name(cmd.getOptionValue("f")); //$NON-NLS-1$
	    	}
	    	if (cmd.hasOption("g")) { //$NON-NLS-1$
	    		config.setFile2Name(cmd.getOptionValue("g")); //$NON-NLS-1$
	    	}
	    	if (cmd.hasOption("i")) { //$NON-NLS-1$
	    		config.setFile1Dir(cmd.getOptionValue("i")); //$NON-NLS-1$
	    	}
	    	if (cmd.hasOption("j")) { //$NON-NLS-1$
	    		config.setFile2Dir(cmd.getOptionValue("j")); //$NON-NLS-1$
	    	}
	    	if (cmd.hasOption("o")) { //$NON-NLS-1$
	    		config.setOutputFilename(cmd.getOptionValue("o")); //$NON-NLS-1$
	    	}
	    	if (cmd.hasOption("q")) { //$NON-NLS-1$
	    		config.setQuietFlg(true);
	    	}
	    	if (cmd.hasOption("r")) { //$NON-NLS-1$
	    		config.setReadAheadLineCnt(cmd.getOptionValue("r")); //$NON-NLS-1$
	    	}
	    	if (cmd.hasOption("s")) { //$NON-NLS-1$
	    		config.setColumnSeparator(cmd.getOptionValue("s")); //$NON-NLS-1$
	    	}
	    	if (cmd.hasOption("t")) { //$NON-NLS-1$
	    		config.setIncludeHeaderFlg(true);
	    	}

	    	if (cmd.hasOption("h")) { //$NON-NLS-1$
	    		showHelp(options);
		    	return;
	    	}
	    	if (cmd.hasOption("v")) { //$NON-NLS-1$
		    	System.out.println(Messages.getString("FileCompare.version_colon") + Consts.VERSION); //$NON-NLS-1$
		    	return;
	    	}
	    	if (config.getFile1Dir() == null && config.getFile1Name() == null) {
	    		System.out.println(Messages.getString("FileCompare.please_specify_either_file1_or_dir1")); //$NON-NLS-1$
	    		return;
	    	}
	    	if (config.getFile2Dir() == null && config.getFile2Name() == null) {
	    		System.out.println(Messages.getString("FileCompare.please_specify_either_file2_or_dir2")); //$NON-NLS-1$
	    		return;
	    	}
	    	if (config.getFile1Name() == null && config.getFile2Dir() == null) {
	    		System.out.println(Messages.getString("FileCompare.please_specify_either_file1_or_dir2")); //$NON-NLS-1$
	    		return;
	    	}

	    	TextFileComparator comparator = new TextFileComparator();
	    	try {
	    		config.setOutputWriter();
	    		if (config.getFile1Name() == null && config.getFile2Name() == null && config.getFile1Dir() != null && config.getFile2Dir() != null) {
	    			// scan file1 directory to get all file and compare with file2 directory
	    			Collection<String> file1Names = Utility.listFilesInDir(new File(config.getFile1Dir()));
	    			Collection<String> file2Names = Utility.listFilesInDir(new File(config.getFile2Dir()));
	    			if (file1Names != null && file1Names.size() > 0) {
	    				if (file2Names != null && file2Names.size() > 0) {
	    					// process files in both directories
	    					Collection<String> common = new HashSet<String>(file1Names);
	    					common.retainAll(file2Names);
	    					for (Iterator<String> iterator = common.iterator(); iterator.hasNext();) {
	    						String filename = iterator.next();
	    						config.setFile1Name(filename);
	    						config.setFile2Name(filename);
	    						comparator.compareTextFiles(config);
	    						config.setIncludeHeaderFlg(false);		// only include header once
	    					}
	    					// process files missing in directory 2
	    					Collection<String> filesInDir1 = new HashSet<String>(file1Names);
	    					filesInDir1.removeAll(file2Names);
	    					comparator.logMissingFiles(config.getOutputWriter(), config.getColumnSeparator(), filesInDir1, config.getMaxColumnDiffLength(), Messages.getString("FileCompare.files_missing_in_directory_2")); //$NON-NLS-1$
	    					// process files only in directory 2
	    					Collection<String> filesInDir2 = new HashSet<String>(file2Names);
	    					filesInDir2.removeAll(file1Names);
	    					comparator.logAdditionalFiles(config.getOutputWriter(), config.getColumnSeparator(), filesInDir2, config.getMaxColumnDiffLength(), Messages.getString("FileCompare.files_extra_in_directory_2")); //$NON-NLS-1$
	    				} else {	// directory 2 is empty
	    					comparator.logMissingFiles(config.getOutputWriter(), config.getColumnSeparator(), file1Names, config.getMaxColumnDiffLength(), Messages.getString("FileCompare.directory_2_is_empty_file_only_in_directory_1")); //$NON-NLS-1$
	    				}
	    			} else { 		// directory 1 is empty
	    				if (file2Names != null && file2Names.size() > 0) {	// files only in directory2
	    					comparator.logAdditionalFiles(config.getOutputWriter(), config.getColumnSeparator(), file2Names, config.getMaxColumnDiffLength(), Messages.getString("FileCompare.directory_1_is_empty_file_extra_in_directory_2")); //$NON-NLS-1$
	    				} else {	// both directories are empty
	    					comparator.logBothDirEmpty(config.getOutputWriter(), config.getColumnSeparator());
	    				}
	    			}
	    		} else {
	    			comparator.compareTextFiles(config);
	    		}
	    	} catch (FileNotFoundException e) {
	    		System.out.println(Messages.getString("FileCompare.file_not_found") + e.getMessage()); //$NON-NLS-1$
//	    		e.printStackTrace();
//	    		showHelp(options);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				config.closeOutputWriter();
			}
		} catch (org.apache.commons.cli.ParseException e) {
			System.out.println(Messages.getString("FileCompare.unable_to_parse_commandline_argument") + e.getMessage()); //$NON-NLS-1$
		} catch (IOException e1) {
			System.out.println(Messages.getString("FileCompare.unable_to_open_configuration_file") + e1.getMessage()); //$NON-NLS-1$
		}
    	
		if (config == null || !config.getQuietFlg()) {
			System.out.println(Messages.getString("FileCompare.finish")); //$NON-NLS-1$
		}
    }
    private static void showHelp(Options options) {
    	HelpFormatter helpFormatter = new HelpFormatter();
    	helpFormatter.printHelp(Consts.PROJECT_NAME, options, true);
    }
}
