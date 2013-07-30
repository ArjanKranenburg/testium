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
	private File outputBaseDir;
	private File xslSourceDir;

	/**
	 * @param myFileEnabled 
	 * @param myStdOutEnabled 
	 * @param aXslDir
	 * @param aFileName 
	 */
	public KeywordDefinitionsConfiguration( File outputBaseDir, File xslSourceDir )
	{
	    Trace.println(Trace.CONSTRUCTOR);

	    this.outputBaseDir = outputBaseDir;
	    this.xslSourceDir = xslSourceDir;
	}

	public File getOutputBaseDir() {
		return outputBaseDir;
	}

	public File getXslSourceDir() {
		return xslSourceDir;
	}
}
