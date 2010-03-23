package org.testium.configuration;
/**
 * 
 */

import java.io.File;

import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan Kranenburg
 *
 */
public class TestResultInterfaceConfiguration
{
	private boolean myStdOutEnabled;
	private boolean myFileEnabled;
	private File myXslDir;
	private String myFileName;

	/**
	 * @param myFileEnabled 
	 * @param myStdOutEnabled 
	 * @param aXslDir
	 * @param aFileName 
	 */
	public TestResultInterfaceConfiguration( boolean aStdOutEnabled,
											 boolean aFileEnabled,
											 File aXslDir,
											 String aFileName )
	{
	    Trace.println(Trace.LEVEL.CONSTRUCTOR);

	    myStdOutEnabled = aStdOutEnabled;
	    myFileEnabled = aFileEnabled;
	    myXslDir = aXslDir;
	    myFileName = aFileName;
	}

	/**
	 * @return the myXslDir
	 */
	public File getXslDir()
	{
		return myXslDir;
	}

	/**
	 * @return the myFileName
	 */
	public String getFileName()
	{
		return myFileName;
	}

	/**
	 * @return the myStdOutEnabled
	 */
	public boolean getStdOutEnabled()
	{
		return myStdOutEnabled;
	}

	/**
	 * @return the myFileEnabled
	 */
	public boolean getFileEnabled()
	{
		return myFileEnabled;
	}
}
