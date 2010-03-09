package org.testium;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.testium.configuration.Configuration;
import org.testium.configuration.ConfigurationException;
import org.testium.configuration.ConfigurationXmlHandler;
import org.testium.executor.TestExecutionException;
import org.testium.executor.TestSuiteExecutor;
import org.testium.plugins.PluginCollection;
import org.testtoolinterfaces.testresult.TestRunResult;
import org.testtoolinterfaces.testresultinterface.TestRunResultWriter;
import org.testtoolinterfaces.testsuite.TestGroup;
import org.testtoolinterfaces.testsuiteinterface.TestGroupReader;
import org.testtoolinterfaces.utils.Trace;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


public class Testium
{
	private Configuration	myConfiguration;
	
	private TestGroupReader myTestGroupReader;

	private TestSuiteExecutor	myTestSuiteExecutor;
	
	private ArrayList<TestRunResultWriter>	myTestResultWriters;

	/**
	 * @deprecated
	 * @param aPlugins
	 * @param aGlobalConfig
	 * @param aSettingsFile
	 * @throws ConfigurationException
	 */
	public Testium(PluginCollection aPlugins, Configuration aGlobalConfig, File aSettingsFile ) throws ConfigurationException
	{
		myTestGroupReader = aPlugins.getTestGroupReader();
		myTestResultWriters = aPlugins.getTestRunResultWriters();
		myTestSuiteExecutor = aPlugins.getTestSuiteExecutor();
		
		myConfiguration = readConfigFile( aSettingsFile, aGlobalConfig );
	}

	public Testium(PluginCollection aPlugins, Configuration aConfig ) throws ConfigurationException
	{
		myTestGroupReader = aPlugins.getTestGroupReader();
		myTestResultWriters = aPlugins.getTestRunResultWriters();
		myTestSuiteExecutor = aPlugins.getTestSuiteExecutor();
		
		myConfiguration = aConfig;
	}

	/**
	 * @deprecated
	 */
	private Configuration readConfigFile( File aConfigFile, Configuration aGlobalConfig ) throws ConfigurationException
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
			handler = new ConfigurationXmlHandler(xmlReader, aGlobalConfig);

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

	public TestGroup readTestGroup( File aTestGroup )
	{
		Trace.println(Trace.EXEC_PLUS, "validate( " + aTestGroup.getName() + " )", true);

		return myTestGroupReader.readTgFile(aTestGroup);
	}

	public void execute( TestGroup aTestGroup, File aBaseExecutionDir ) throws TestExecutionException
	{
		Trace.println(Trace.EXEC, "execute( " + aTestGroup.getId() + " )", true);
    	System.out.println("Executing " + aTestGroup.getId());

    	// TODO Move date as start-date to run-time data
		Calendar date = Calendar.getInstance();
		File logDir = myConfiguration.getTestResultBaseDir();
    	TestRunResult result = myTestSuiteExecutor.execute( aTestGroup,
    	                                                    aBaseExecutionDir,
    	                                                    logDir,
    	                                                    date );

 //   	File resultFile = new File( logDir.getAbsolutePath(), "results.xml" );
    	
	    for (TestRunResultWriter resultWriter : myTestResultWriters)
	    {
		    resultWriter.write( result );
	    }

	}
}
