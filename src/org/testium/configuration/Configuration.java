package org.testium.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import org.testium.executor.TestExecutionException;
import org.testtoolinterfaces.utils.DateFormat;


public class Configuration
{
	private File myTestResultBaseDir;
	private ArrayList<String> myPluginLoaders;
	private File myPluginsDirectory;
	private File myConfigDirectory;
	private String myEnvironment = "Unknown";
	private String myTestPhase = "Unknown";
	private String myDateStamp;
	private String mySettingsFileName;

	/**
	 * @param aTestResultBaseDir
	 * @param myTempPluginLoaders
	 * @param myTempPluginsDirectory
	 * @param aConfigDir
	 * @param anEnvironment 
	 * @param aTestPhase
	 */
	public Configuration( File aTestResultBaseDir, 
						  ArrayList<String> myTempPluginLoaders,
						  File myTempPluginsDirectory,
						  File aConfigDir,
						  String anEnvironment,
						  String aTestPhase,
						  String aSettingsFileName )
	{
		myTestResultBaseDir = aTestResultBaseDir;
		myPluginLoaders = myTempPluginLoaders;
		myPluginsDirectory = myTempPluginsDirectory;
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
		return myPluginLoaders;
	}

	/**
	 * @return the configuration directory
	 */
	public File getConfigDir()
	{
		return myConfigDirectory;
	}

	/**
	 * @return the PluginsDirectory
	 */
	public File getPluginsDirectory()
	{
		return myPluginsDirectory;
	}

	/**
	 * @return the TestResultBaseDir + datestamp
	 * @throws TestExecutionException 
	 */
	public File getTestResultBaseDir()
	{
		if ( myTestResultBaseDir == null )
		{
			return null;
		}

		// TODO Get datestamp from Run-time data (as start-date)
		File logDir = new File( myTestResultBaseDir, myDateStamp );

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
