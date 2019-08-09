package org.hozawa.FileCompare;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import org.hozawa.FileCompare.FileCompare;

import junit.framework.TestCase;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit test for FileCompare tool.
 * 
 * @author hitoshi ozawa
 * @version 1.0.0
 * @since 2019/08/02
 */
public class FileCompareTest extends TestCase {
	
	/**
	 * Change locale to en.US to check against messages in English.
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
    	Locale.setDefault(new Locale("en", "US"));
	}
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
/*    public FileCompareTest( String testName ) {
        super( testName );
    } */

    /**
     * @return the suite of tests being tested
     */
/*    public static Test suite() {
        return new TestSuite( FileCompareTest.class );
    }*/

	/**
	 * Test same content file
	 */
    public void testSameContent1() {
    	String file1Path = "src/test/resources/dir1/a1.txt";
    	String file2Path = "src/test/resources/dir2/a1.txt";
    	String outputFilePath = "src/test/resources/output/sameContent.txt";
    	String[] expectedResult = {"a1.txt,a1.txt,0,0,,files are the same"};
    	
    	testCompareResult(null, null, file1Path, file2Path, outputFilePath, null, expectedResult, true);
    }
    /**
     * Test unmatched content in a line
     */
    public void testUnmatchedLine() {
    	String file1Path = "src/test/resources/dir1/a1.txt";
    	String file2Path = "src/test/resources/dir2/b.txt";
    	String outputFilePath = "src/test/resources/output/unmatchedLine.txt";
    	String[] expectedResult = {"a1.txt,b.txt,2,2,h,lines do not match"};
    	
    	testCompareResult(null, null, file1Path, file2Path, outputFilePath, null, expectedResult, true);
    }
    /**
     * Test unmatched content in 2 lines
     */
    public void testUnmatched2Line() {
    	String file1Path = "src/test/resources/dir1/a1.txt";
    	String file2Path = "src/test/resources/dir2/b2.txt";
    	String outputFilePath = "src/test/resources/output/unmatched2Line.txt";
    	String[] expectedResult = {
    			"a1.txt,b2.txt,1,1,h,lines do not match",
    			"a1.txt,b2.txt,2,2,h,lines do not match"
    			};
    	
    	testCompareResult(null, null, file1Path, file2Path, outputFilePath, null, expectedResult, true);
    }
    
    /**
     * Test missing line
     */
    public void testMissingLine() {
    	String file1Path = "src/test/resources/dir1/a1.txt";
    	String file2Path = "src/test/resources/dir2/c1.txt";
    	String outputFilePath = "src/test/resources/output/missingLine.txt";
    	String[] expectedResult = {"a1.txt,c1.txt,2,2,,file 2 is missing line"};
    	
    	testCompareResult(null, null, file1Path, file2Path, outputFilePath, null, expectedResult,true);
    }
    /**
     * Test additional line
     */
    public void testAdditionalLine() {
    	String file1Path = "src/test/resources/dir1/a1.txt";
    	String file2Path = "src/test/resources/dir2/d1.txt";
    	String outputFilePath = "src/test/resources/output/additionalLine.txt";
    	String[] expectedResult = {"a1.txt,d1.txt,3,3,Additional,file 2 has additional lines"};
    	
    	testCompareResult(null, null, file1Path, file2Path, outputFilePath, null, expectedResult,true);
    }
    
    /**
     * Test changing column separator in output
     */
    public void testCsvSeparator() {
    	String file1Path = "src/test/resources/dir1/a1.txt";
    	String file2Path = "src/test/resources/dir2/a1.txt";
    	String outputFilePath = "src/test/resources/output/csvSeparator.txt";
    	String[] expectedResult = {"a1.txt:a1.txt:0:0::files are the same"};
    	
    	String strArgs = "-s,:";
    	ArrayList<String> arg = new ArrayList<String>(Arrays.asList(strArgs.split(",")));
    	testCompareResult(null, null, file1Path, file2Path, outputFilePath, arg, expectedResult, true);
    }
    
    /**
     * Test if column header is written to output
     */
    public void testOutputHeader() {
   	
    	String file1Path = "src/test/resources/dir1/a1.txt";
    	String file2Path = "src/test/resources/dir2/a1.txt";
    	String outputFilePath = "src/test/resources/output/header.txt";
    	String[] expectedResult = {
    		"File 1,File 2,File 1 Line No,File 2 Line No,Content Differences,Message",	
    		"a1.txt,a1.txt,0,0,,files are the same"
    	};
    	ArrayList<String> arg = new ArrayList<String>(Arrays.asList("-t"));
    	
    	testCompareResult(null, null, file1Path, file2Path, outputFilePath, arg, expectedResult, true);
    }
	/**
	 * Test when directory1 has no files
	 */
    public void test2File1DirIsEmpty() {
    	String file1Dir = "src/test/resources/dir0";	// does not exist
    	String file2Dir = "src/test/resources/dir2";
    	
    	String outputFilePath = "src/test/resources/output/emptyDir1.txt";
    	String[] expectedResult = {
    			",b2.txt,0,0,,directory 1 is empty. file extra in directory 2.",
    			",c2.txt,0,0,,directory 1 is empty. file extra in directory 2.",
    			",c1.txt,0,0,,directory 1 is empty. file extra in directory 2.",
    			",a1.txt,0,0,,directory 1 is empty. file extra in directory 2.",
    			",b.txt,0,0,,directory 1 is empty. file extra in directory 2."
    			};
    	
    	testCompareResult(file1Dir, file2Dir, null, null, outputFilePath, null, expectedResult, false);
    }

    private void testCompareResult(String file1Dir, String file2Dir, String file1Name, String file2Name, String outputName, ArrayList<String> argList, String[] expectedResult, boolean exactMatch) {
    	try {
	    	File file1 = null;
	    	String file1Path = null;
	    	File file2 = null;
	    	String file2Path = null;
	    	
	    	if (file1Name != null) {
	    		file1 = new File(file1Name);
	    		file1Path = file1.getAbsolutePath();
	    	}
	    	if (file2Name != null) {
	    		file2 = new File(file2Name);
	    		file2Path = file2.getAbsolutePath();
	    	}
	    	
	    	File outputFile = new File(outputName);
	    	String outputPath = outputFile.getAbsolutePath();
	    	
	    	ArrayList<String> args = new ArrayList<String>();
	    	args.add("-o");
	    	args.add(outputPath);
	    	args.add("-q");
	    	if (file1Dir != null) {
	    		args.add("-i");
	    		args.add(file1Dir);
	    	}
	    	if (file2Dir != null) {
	    		args.add("-j");
	    		args.add(file2Dir);
	    	}
	    	if (file1Path != null) {
	    		args.add("-f");
	    		args.add(file1Path);
	    	}
	    	if (file2Path != null) {
	    		args.add("-g");
	    		args.add(file2Path);
	    	}
	    	if (argList != null) {
	    		args.addAll(argList);
	    	}
	    	
	    	FileCompare.main(args.toArray(new String[0]));
	    	
	    	if (exactMatch) {
	    		assertThat(linesOf(outputFile)).containsExactly(expectedResult);
	    	} else {
	    		assertThat(linesOf(outputFile)).containsOnlyOnce(expectedResult);
	    	}
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }

}
