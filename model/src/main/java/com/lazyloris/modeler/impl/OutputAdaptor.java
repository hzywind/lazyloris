package com.lazyloris.modeler.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lazyloris.modeler.Env;

public class OutputAdaptor {

	private int indents;
	private String indent;
	private String outputPath;
	private String packageName;
	private String packagePath;
	private PrintWriter writer;
	private boolean debug;
	
	private static Log log = LogFactory.getLog(OutputAdaptor.class);
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setIndent(String indent) {
		this.indent = indent;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public String getPackagePath() {
		return this.packagePath;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
		this.packagePath = packageName.replace('.', File.separatorChar);
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public String upperInitial(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public String lowerInitial(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	public void increaseIndent() {
		indents++;
	}

	public void decreaseIndent() {
		indents--;
	}

	public void setOutputDir(String outputPath) {
		this.outputPath = outputPath;
	}

	public void openOutput(String fileName) {
		File dir = new File(Env.getInstance().getOutputDirectory(), outputPath);
		if (!dir.exists())
			dir.mkdirs();
		File outputFile = new File(dir, fileName);
		if (writer != null)
			closeOutput();
		try {
			//writer = new PrintWriter(new FileWriter(outputFile));
			writer = new PrintWriter(outputFile, "UTF-8");
			if (debug && log.isDebugEnabled()) {
				log.debug("outputing file " + outputFile.getName());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void closeOutput() {
		writer.flush();
		writer.close();
		writer = null;
	}

	public void println(String str) {
		indent();
		writer.println(str);
	}

	public void print(String str) {
		indent();
		writer.print(str);
	}

	public void printNoIndents(String str) {
		writer.print(str);
	}

	public void printlnNoIndents(String str) {
		writer.println(str);
	}

	private void indent() {
		for (int i = 0; i < indents; i++) {
			writer.print(indent);
		}
	}
}
