package org.testium.plugins;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.testium.TestRunResultStdOutWriter;
import org.testium.configuration.ConfigurationException;
import org.testium.configuration.KEYS;
import org.testium.configuration.TestResultInterfaceConfiguration;
import org.testium.configuration.TestResultInterfaceConfigurationXmlHandler;
import org.testtoolinterfaces.testresultinterface.TestRunResultXmlWriter;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 * @author Arjan Kranenburg
 *
 */
public final class TestResultInterfacePlugin implements Plugin
{
	public TestResultInterfacePlugin()
	{
		// nop
	}
	
	public void loadPlugIn(PluginCollection aPluginCollection, RunTimeData aRtData) throws ConfigurationException
	{
		File configDir = (File) aRtData.getValue(KEYS.CONFIG_DIRECTORY.toString());
		File trConfigFile = new File( configDir, "testResultConfiguration.xml" );
		TestResultInterfaceConfiguration trConfig = readConfigFile( trConfigFile, aRtData );
		
		// Factories

		// Executors
		
		// Input and ouput interfaces
    	if ( trConfig.getStdOutEnabled() )
    	{
    		TestRunResultStdOutWriter stdOutWriter = new TestRunResultStdOutWriter();
    		aPluginCollection.addTestRunResultWriter( stdOutWriter );
    	}

    	if ( trConfig.getFileEnabled() )
    	{
    		File xslDir = trConfig.getXslDir();
    		String fileName = trConfig.getFileName();
    		String testEnvironment = (String) aRtData.getValue(KEYS.TEST_ENVIRONMENT.toString());
    		String testPhase = (String) aRtData.getValue(KEYS.TEST_PHASE.toString());
    		File logDir = (File) aRtData.getValue(KEYS.RESULT_BASE_DIR.toString());
        	File resultFile = new File( logDir.getAbsolutePath(), fileName );

        	TestRunResultXmlWriter xmlWriter = new TestRunResultXmlWriter( resultFile, xslDir, testEnvironment, testPhase );
    		aPluginCollection.addTestRunResultWriter( xmlWriter );
    	}
	}
	
	public TestResultInterfaceConfiguration readConfigFile( File aConfigFile, RunTimeData aRtData ) throws ConfigurationException
	{
		Trace.println(Trace.UTIL, "readConfigFile( " + aConfigFile.getName() + " )", true );
        // create a parser
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(false);
        SAXParser saxParser;
        TestResultInterfaceConfigurationXmlHandler handler = null;
		try
		{
			saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();

	        // create a handler
			handler = new TestResultInterfaceConfigurationXmlHandler(xmlReader, aRtData);

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
		
		TestResultInterfaceConfiguration myConfiguration = handler.getConfiguration();
		
		return myConfiguration;
	}
}
