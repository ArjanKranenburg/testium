package org.testium.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import org.testium.executor.TestExecutionException;
import org.testtoolinterfaces.utils.DateFormat;


public class Configuration
{
	private File myTestResultBaseDir;
	private String myPluginLoaders = "";
	private String myPluginsDirectory = "";
	private File myConfigDirectory;
	private String myEnvironment = "Unknown";
	private String myTestPhase = "Unknown";
	private String myDateStamp;
	private String mySettingsFileName;

	/**
	 * @param aTestResultBaseDir
	 * @param aPluginLoaders
	 * @param aPluginsDirectory
	 * @param aConfigDir
	 * @param anEnvironment 
	 * @param aTestPhase
	 */
	public Configuration( File aTestResultBaseDir, 
						  String aPluginLoaders,
						  String aPluginsDirectory,
						  File aConfigDir,
						  String anEnvironment,
						  String aTestPhase,
						  String aSettingsFileName )
	{
		myTestResultBaseDir = aTestResultBaseDir;
		myPluginLoaders = aPluginLoaders;
		myPluginsDirectory = aPluginsDirectory;
		myConfigDirectory = aConfigDir;
		myEnvironment = anEnvironment;
		myTestPhase = aTestPhase;
		mySettingsFileName = aSettingsFileName;
		
		myDateStamp = DateFormat.getDateTimeStamp( Calendar.getInstance() );
	}

	/**
	 * @return the PluginLoaders
	 */
	public ArrayList<String> getPluginLoaders()
	{
		ArrayList<String> pluginLoaders = new ArrayList<String>();
    	if ( ! myPluginLoaders.isEmpty() )
    	{
        	String[] classNames = myPluginLoaders.trim().split(";");
        	if ( classNames.length != 0 )
        	{
            	for ( String className : classNames )
            	{
            		className = className.replace('\t', ' ').trim();
            		pluginLoaders.add( className );
            	}
        	}
    	}

		return pluginLoaders;
	}

	/**
	 * @return the configuration directory
	 */
	public String getConfigDirectory()
	{
		return myConfigDirectory.getAbsolutePath();
	}

	/**
	 * @return the PluginsDirectory
	 */
	public String getPluginsDirectory()
	{
		return myPluginsDirectory;
	}

	/**
	 * @return the TestResultBaseDir + datestamp
	 * @throws TestExecutionException 
	 */
	public File getTestResultBaseDir()
	{
		// TODO Get datestamp from Run-time data (as start-date)
		File logDir = new File( myTestResultBaseDir.getAbsolutePath(), myDateStamp );

		if( ! logDir.isDirectory() )
		{
			logDir.mkdir();
		}
		
		return logDir;
	}

	/**
	 * @return the Test Environment
	 */
	public String getTestEnvironment()
	{
		return myEnvironment;
	}

	public String getTestPhase()
	{
		return myTestPhase;
	}

	public String getSettingsFileName()
	{
		return mySettingsFileName;
	}
}
