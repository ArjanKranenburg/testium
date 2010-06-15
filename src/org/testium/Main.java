package org.testium;

import java.io.File;
import java.io.IOError;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.testium.configuration.ConfigurationException;
import org.testium.configuration.KEYS;
import org.testium.configuration.GlobalConfigurationXmlHandler;
import org.testium.configuration.PersonalConfigurationXmlHandler;
import org.testium.executor.TestExecutionException;
import org.testium.plugins.PluginClassLoader;
import org.testium.plugins.PluginCollection;
import org.testtoolinterfaces.cmdline.CmdLineExecutionParser;
import org.testtoolinterfaces.cmdline.CmdLineParser;
import org.testtoolinterfaces.cmdline.ParameterException;
import org.testtoolinterfaces.testsuite.TestGroup;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


public class Main
{
	public static final String APPLICATIONNAME = "Testium";

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Trace.getInstance().addBaseClass("org.testium");
		Trace.getInstance().addBaseClass("org.testtoolinterfaces");

		CmdLineParser cmdLine = new CmdLineExecutionParser( APPLICATIONNAME );
		RunTimeData rtData = new RunTimeData();

		try 
		{
			rtData = cmdLine.parse(args);
		}
		catch (ParameterException pe)
		{
			System.out.println( "Error on command line." );
			System.out.println( pe.getMessage() );
			cmdLine.printHelpOn(System.out);
			Trace.print(Trace.UTIL, pe);
		}

        Testium testium;

		try
		{
			// Read in the Global Configuration file
			readGlobalConfigFile( 
				(File) rtData.getValue(CmdLineExecutionParser.GLOBAL.toString()), rtData );
 
			// Read the personal Configuration file
			readPersonalConfigFile( 
				(File) rtData.getValue(KEYS.CONFIGFILENAME.toString()), rtData );

			// Load plugins
			PluginCollection plugins = loadPlugins( rtData );

			testium = new Testium( plugins, rtData );
		}
		catch (ConfigurationException e)
		{
			System.out.println( "ERROR: " + APPLICATIONNAME + " could not be configured." );
			System.out.println( e.getMessage() );
			Trace.print(Trace.UTIL, e);
			return;
		}

		// Read the Test Group
		File tgFile = (File) rtData.getValue(CmdLineExecutionParser.FILENAME.toString());
		TestGroup testGroup;
		try
		{
			testGroup = testium.readTestGroup( tgFile );
		}
		catch (IOError e)
		{
			System.out.println( "ERROR: TestGroup could not be read: " + tgFile.getAbsolutePath() );
			System.out.println( e.getMessage() );
			Trace.print(Trace.UTIL, e);
			return;
		}
		
		// Execute the Test Group
		try
		{
			testium.execute( testGroup, tgFile.getParentFile(), rtData );
		}
		catch (TestExecutionException e)
		{
			System.out.println( "ERROR: Execution failed." );
			System.out.println( e.getMessage() );
			Trace.print(Trace.UTIL, e);
			return;
		}
	}

	private static void readGlobalConfigFile( File aConfigFile, RunTimeData anRtData ) throws ConfigurationException
	{
		Trace.println(Trace.UTIL, "readConfigFile( " + aConfigFile.getName() + ", runTimeData )", true );

		// create a parser
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(false);
        SAXParser saxParser;
        GlobalConfigurationXmlHandler handler = null;
		try
		{
			saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();

	        // create a handler
			handler = new GlobalConfigurationXmlHandler(xmlReader, anRtData);

	        // assign the handler to the parser
	        xmlReader.setContentHandler(handler);

	        // parse the document
	        xmlReader.parse( aConfigFile.getAbsolutePath() );
		}
		catch (ParserConfigurationException e)
		{
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException( e );
		}
		catch (SAXException e)
		{
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException( e );
		}
		catch (IOException e)
		{
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException( e );
		}
	}

	private static void readPersonalConfigFile( File aConfigFile, RunTimeData anRtData ) throws ConfigurationException
	{
		Trace.println(Trace.UTIL, "readConfigFile( " + aConfigFile.getName() + ", runTimeData )", true );

		// create a parser
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(false);
        SAXParser saxParser;
        PersonalConfigurationXmlHandler handler = null;
		try
		{
			saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();

	        // create a handler
			handler = new PersonalConfigurationXmlHandler(xmlReader, anRtData);

	        // assign the handler to the parser
	        xmlReader.setContentHandler(handler);

	        // parse the document
	        xmlReader.parse( aConfigFile.getAbsolutePath() );
		}
		catch (ParserConfigurationException e)
		{
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException( e );
		}
		catch (SAXException e)
		{
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException( e );
		}
		catch (IOException e)
		{
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException( e );
		}
	}

	private static PluginCollection loadPlugins( RunTimeData anRtData ) throws ConfigurationException
	{
		File pluginDirectory = (File) anRtData.getValue( KEYS.PLUGINSDIRECTORY.toString() );

		if ( ! pluginDirectory.isDirectory() )
		{
			throw new ConfigurationException( "Plugin Directory is not valid: " + pluginDirectory.getAbsolutePath() );
		}
	
		PluginCollection plugins = PluginClassLoader.loadPlugins( pluginDirectory, anRtData );
	
		return plugins;
	}
}
