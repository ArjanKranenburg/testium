package net.sf.testium.configuration;
/**
 * 
 */

import java.io.File;

import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan Kranenburg
 *
 */
public class KeywordDefinitionsConfiguration
{
	private final File outputBaseDir;
	private final File xslSourceDir;
	private final String xslFileName;

	/**
	 * @param outputBaseDir
	 * @param xslSourceDir 
	 */
	public KeywordDefinitionsConfiguration( File outputBaseDir, File xslSourceDir,
			String xslFileName )
	{
	    Trace.println(Trace.CONSTRUCTOR);

	    this.outputBaseDir = outputBaseDir;
	    this.xslSourceDir = xslSourceDir;
	    this.xslFileName = xslFileName;
	}

	public File getOutputBaseDir() {
		return outputBaseDir;
	}

	public File getXslSourceDir() {
		return xslSourceDir;
	}

	public String getXslFileName() {
		return xslFileName;
	}
}
