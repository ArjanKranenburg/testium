package org.testium.configuration;
/**
 * 
 */

import org.testtoolinterfaces.testresultinterface.Configuration;
import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan Kranenburg
 *
 */
public class TestResultInterfaceConfiguration
{
	private boolean myStdOutEnabled;
	private boolean myFileEnabled;
	private Configuration myTtiConfiguration;
	private String myFileName;

	/**
	 * @param myFileEnabled 
	 * @param myStdOutEnabled 
	 * @param aXslDir
	 * @param aFileName 
	 */
	public TestResultInterfaceConfiguration( boolean aStdOutEnabled,
											 boolean aFileEnabled,
											 Configuration aTtiConfiguration,
											 String aFileName )
	{
	    Trace.println(Trace.CONSTRUCTOR);

	    myStdOutEnabled = aStdOutEnabled;
	    myFileEnabled = aFileEnabled;
	    myTtiConfiguration = aTtiConfiguration;
	    myFileName = aFileName;
	}

	/**
	 * @return the myXslDir
	 */
	public Configuration getTtiConfig()
	{
		return myTtiConfiguration;
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
