package org.hozawa.FileCompare;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.hozawa.FileCompare.Config;
import org.hozawa.FileCompare.Consts;
import org.hozawa.FileCompare.Utility;

import junit.framework.TestCase;

public class ConfigTest  extends TestCase {

	/**
	 * test if appendFlg is set properly.
	 */
	public void testAppendFlg() {
		Config config;
		try {
			config = new Config();
			config.setAppendFlg("true");
			assertThat(config.getAppendFlg()).isEqualTo(true);
		} catch (IOException e) {
			fail("IO exception thrown creating Config class.");
		}
	}
	/**
	 * test if includeHeaderFlg is set properly.
	 */
	public void testIncludeHeaderFlg() {
		Config config;
		try {
			config = new Config();
			config.setIncludeHeaderFlg("true");
			assertThat(config.getIncludeHeaderFlg()).isEqualTo(true);
		} catch (IOException e) {
			fail("IO exception thrown creating Config class.");
		}
	}
	/**
	 * test if file1 directory is set properly to null.
	 */
	public void testFile1Dir() {
		Config config;
		try {
			config = new Config();
			config.setFile1Dir(null);
			assertThat(config.getFile1Dir()).isEqualTo(null);
		} catch (IOException e) {
			fail("IO exception thrown creating Config class.");
		}
	}
	/**
	 * test if file1 directory is set properly to specified directory.
	 */
	public void testFile1Dir2() {
		Config config;
		try {
			config = new Config();
			config.setFile1Dir("main/test");
			if (Utility.isWindows()) {
				assertThat(config.getFile1Dir()).isEqualTo("main\\test\\");
			} else {
			assertThat(config.getFile1Dir()).isEqualTo("main/test/");
		}
		} catch (IOException e) {
			fail("IO exception thrown creating Config class.");
		}
	}
	/**
	 * test if file2 directory is set properly to null.
	 */
	public void testFile2Dir() {
		Config config;
		try {
			config = new Config();
			config.setFile2Dir(null);
			assertThat(config.getFile2Dir()).isEqualTo(null);
		} catch (IOException e) {
			fail("IO exception thrown creating Config class.");
		}
	}
	/**
	 * test if file1 directory is set properly to specified directory.
	 */
	public void testFile2Dir2() {
		Config config;
		try {
			config = new Config();
			config.setFile2Dir("home/test/");
			if (Utility.isWindows()) {
				assertThat(config.getFile2Dir()).isEqualTo("home\\test\\");
			} else {
			assertThat(config.getFile2Dir()).isEqualTo("home/test/");
		}
		} catch (IOException e) {
			fail("IO exception thrown creating Config class.");
		}
	}
	/**
	 * test if output filename is set to default filename when null is specified.
	 */
	public void testOutputFilename() {
		Config config;
		try {
			config = new Config();
			config.setOutputFilename(null);
			assertThat(config.getOutputFilename()).isEqualTo(Consts.DEFAULT_OUTPUT_FILENAME);
		} catch (IOException e) {
			fail("IO exception thrown creating Config class.");
		}
	}
	/**
	 * test if file1 directory is set properly to specified directory
	 */
	public void testOutputFilename2() {
		Config config;
		try {
			config = new Config();
			String filename = "output_filename.csv";
			config.setOutputFilename(filename);
			assertThat(config.getOutputFilename()).isEqualTo(filename);
		} catch (IOException e) {
			fail("IO exception thrown creating Config class.");
		}
	}
	/**
	 * test if file1 directory is set properly to specified directory
	 */
	public void testOutputFilename3() {
		Config config;
		try {
			config = new Config();
			config.setOutputFilename("output_filename/");
			if (Utility.isWindows()) {
				assertThat(config.getOutputFilename()).isEqualTo("output_filename\\" + Consts.DEFAULT_OUTPUT_FILENAME);
			} else {
			assertThat(config.getOutputFilename()).isEqualTo("output_filename/" + Consts.DEFAULT_OUTPUT_FILENAME);
		}
		} catch (IOException e) {
			fail("IO exception thrown creating Config class.");
		}
	}
	/**
	 * test if encoding is set to default encoding when null is specified.
	 */
	public void testEncoding() {
		Config config;
		try {
			config = new Config();
			config.setEncoding(null);
			assertThat(config.getEncoding()).isEqualTo(Consts.DEFAULT_ENCODING);
		} catch (IOException e) {
			fail("IO exception thrown creating Config class.");
		}
	}
	/**
	 * test if encoding is set to specified encoding.
	 */
	public void testEncoding2() {
		Config config;
		try {
			config = new Config();
			config.setEncoding("UTF-16");
			assertThat(config.getEncoding()).isEqualTo("UTF-16");
		} catch (IOException e) {
			fail("IO exception thrown creating Config class.");
		}
	}
}
