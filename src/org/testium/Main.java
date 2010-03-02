package org.testium;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.testium.configuration.Configuration;
import org.testium.configuration.ConfigurationException;
import org.testium.configuration.ConfigurationXmlHandler;
import org.testium.executor.TestExecutionException;
import org.testium.plugins.PluginClassLoader;
import org.testium.plugins.PluginCollection;
import org.testtoolinterfaces.testsuite.TestGroup;
import org.testtoolinterfaces.utils.Trace;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


public class Main
{
	private static final String APPLICATIONNAME = "Testium";

	private static final String SWITCH_TESTSUITE_FILENAME = "-f";
	private static final String SWITCH_CONFIG_FILENAME =    "-c";
	private static final String SWITCH_PLUGINS_DIRECTORY =  "-p";
	private static final String SWITCH_SETTINGS_FILENAME =  "-s";

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		File testGroupFile = null;
		File testiumBaseDir = null;
		String settingsFileName = "";

		try
		{
			File jarFile = new File(  org.testium.Main.class.getProtectionDomain()
			                         						.getCodeSource()
			                         						.getLocation()
			                         						.toURI() );
			testiumBaseDir = jarFile.getParentFile().getParentFile();
		}
		catch (URISyntaxException exc)
		{
			System.out.println("ERROR: Cannot determine " + APPLICATIONNAME + "'s basedir: ");
			System.out.println( exc.getMessage() );
//			exc.printStackTrace();
		}
		
		File configDir = new File( testiumBaseDir, "config" );
		File configFile = new File( configDir, "global.xml" );

		String pluginDirectoryName = "";
		Trace.getInstance().addBaseClass("org.testium");
		Trace.getInstance().addBaseClass("org.testtoolinterfaces");

		// First we parse the command line parameters
		Trace.print(Trace.UTIL, "Parsing commandline parameters( ");
		if (args.length > 0) 
		{
			for (int index=0; index<args.length; index++)
			{
				Trace.print(Trace.UTIL, args[index] + " " + args[index+1] );
				if( args[index].equals(SWITCH_TESTSUITE_FILENAME) )
				{
					testGroupFile = new File( args[++index] );
				}
				else if( args[index].equals(SWITCH_CONFIG_FILENAME) )
				{
					configFile = new File( args[++index] );
				}
				else if( args[index].equals(SWITCH_PLUGINS_DIRECTORY) )
				{
					pluginDirectoryName = args[++index];
				}
				else if( args[index].equals(SWITCH_SETTINGS_FILENAME) )
				{
					settingsFileName = args[++index];
				}
				else
				{
					System.out.println( "ERROR: Unknown switch " + args[index] );
					printUsage();
					return;
				}
			}
		}
		Trace.println(Trace.UTIL, " )");

		if ( testGroupFile == null )
		{
			System.out.println( "ERROR: Missing testgroup filename (" + SWITCH_TESTSUITE_FILENAME +")" );
			printUsage();
			return;
		}

		Testium testium;
		try
		{
			// Read in the Global Configuration file
			Configuration config = readConfigFile( configFile );

			// Load plugins
			PluginCollection plugins = loadPlugins(testiumBaseDir, pluginDirectoryName, config);

			File settingsFile = getSettingsFile( settingsFileName, config );
			testium = new Testium( plugins, config, settingsFile );
		}
		catch (ConfigurationException e)
		{
			System.out.println( "ERROR: " + APPLICATIONNAME + " could not be configured." );
			System.out.println( e.getMessage() );
			Trace.print(Trace.UTIL, e);
			return;
		}

		// Read the Test Group
		TestGroup testGroup;
		try
		{
			testGroup = testium.readTestGroup( testGroupFile );
		}
		catch (IOError e)
		{
			System.out.println( "ERROR: TestGroup could not be read." );
			System.out.println( e.getMessage() );
			Trace.print(Trace.UTIL, e);
			return;
		}
		
		// Execute the Test Group
		try
		{
			testium.execute( testGroup, testGroupFile.getParentFile() );
		}
		catch (TestExecutionException e)
		{
			System.out.println( "ERROR: Execution failed." );
			System.out.println( e.getMessage() );
			Trace.print(Trace.UTIL, e);
			return;
		}
	}

	private static File getSettingsFile( String aSettingsFileName,
	                                     Configuration aConfig )
	{
		File settingsFile;
		if ( ! aSettingsFileName.isEmpty() )
		{
			settingsFile = new File( aSettingsFileName );
		}
		else
		{
			String settingsFileName = aConfig.getSettingsFileName();
			settingsFile = new File( settingsFileName );
		}

		if ( ! settingsFile.isAbsolute() )
		{
			File userHome = new File( System.getProperty("user.home") );
			settingsFile = new File( userHome, settingsFile.getName() );
		}
		return settingsFile;
	}

	private static PluginCollection loadPlugins( File aTestiumBaseDir,
	                                             String aPluginDirectoryName,
	                                             Configuration aConfig ) throws ConfigurationException
	{
		File pluginDirectory = null;
		if ( ! aPluginDirectoryName.isEmpty() )
		{
			pluginDirectory = new File( aPluginDirectoryName );
		}
		else if( ! aConfig.getPluginsDirectory().isEmpty() )
		{
			pluginDirectory = new File( aConfig.getPluginsDirectory() );
		}
		else
		{
			pluginDirectory = new File( "plugins" );
		}

		if ( ! pluginDirectory.isAbsolute() )
		{
			pluginDirectory = new File( aTestiumBaseDir, aConfig.getPluginsDirectory() );
		}

		if ( ! pluginDirectory.isDirectory() )
		{
			throw new ConfigurationException( "Plugin Directory is not valid: " + pluginDirectory.getAbsolutePath() );
		}
		
		PluginCollection plugins = PluginClassLoader.loadPlugins( pluginDirectory, aConfig );

		return plugins;
	}

	private static Configuration readConfigFile( File aConfigFile ) throws ConfigurationException
	{
		Trace.println(Trace.UTIL, "readConfigFile( " + aConfigFile.getName() + " )", true );
        // create a parser
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(false);
        SAXParser saxParser;
        ConfigurationXmlHandler handler = null;
		try
		{
			saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();

	        // create a handler
			handler = new ConfigurationXmlHandler(xmlReader, aConfigFile.getParentFile());

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
		
		return handler.getConfiguration();
	}
	
	private static void printUsage()
	{
		System.out.println( );
		System.out.println( "Usage:" );
		System.out.println( APPLICATIONNAME + " "
							+ SWITCH_TESTSUITE_FILENAME + " <testgroup filename> "
							+ "[" + SWITCH_SETTINGS_FILENAME + " <settings filename>] "
							+ "[" + SWITCH_CONFIG_FILENAME +    " <configuration filename>] "
							+ "[" + SWITCH_PLUGINS_DIRECTORY +  " <plugin directory>] " );
	}
}
