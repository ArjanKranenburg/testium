package org.testium.plugins;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.testium.TestGroupResultStdOutWriter;
import org.testium.TestRunResultStdOutWriter;
import org.testium.Testium;
import org.testium.configuration.ConfigurationException;
import org.testium.configuration.TestResultInterfaceConfiguration;
import org.testium.configuration.TestResultInterfaceConfigurationXmlHandler;
import org.testtoolinterfaces.testresultinterface.Configuration;
import org.testtoolinterfaces.testresultinterface.TestCaseResultXmlWriter;
import org.testtoolinterfaces.testresultinterface.TestGroupResultXmlWriter;
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
		File configDir = (File) aRtData.getValue(Testium.CONFIGDIR);
		if ( configDir == null )
		{
			throw new ConfigurationException( Testium.CONFIGDIR + " is not set" );
		}
		File trConfigFile = new File( configDir, "testResultConfiguration.xml" );
		TestResultInterfaceConfiguration trConfig = readConfigFile( trConfigFile, aRtData );
		
		// Executors
		
		// Input and ouput interfaces
    	if ( trConfig.getStdOutEnabled() )
    	{
    		TestGroupResultStdOutWriter stdOutGroupWriter = new TestGroupResultStdOutWriter( 2 );
    		aPluginCollection.addTestGroupResultWriter( stdOutGroupWriter );
 
    		TestRunResultStdOutWriter stdOutRunWriter = new TestRunResultStdOutWriter();
    		aPluginCollection.addTestRunResultWriter( stdOutRunWriter );
    	}

    	if ( trConfig.getFileEnabled() )
    	{
        	Configuration ttiConfig = trConfig.getTtiConfig();
    		TestCaseResultXmlWriter tcXmlWriter = new TestCaseResultXmlWriter(ttiConfig);
    		aPluginCollection.addTestCaseResultWriter( tcXmlWriter );

        	TestGroupResultXmlWriter tgXmlWriter = new TestGroupResultXmlWriter(ttiConfig);
    		aPluginCollection.addTestGroupResultWriter( tgXmlWriter );
    		
    		String testEnvironment = aRtData.getValueAs(String.class, Testium.TESTENVIRONMENT);
    		String testPhase = aRtData.getValueAs(String.class, Testium.TESTPHASE);

        	TestRunResultXmlWriter xmlWriter = new TestRunResultXmlWriter( ttiConfig, testEnvironment, testPhase );
        	aPluginCollection.addTestRunResultWriter(xmlWriter);
    	}
	}
	
	public TestResultInterfaceConfiguration readConfigFile( File aConfigFile, RunTimeData aRtData ) throws ConfigurationException
	{
		Trace.println(Trace.UTIL, "readConfigFile( " + aConfigFile.getName() + " )", true );
        // create a parser
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(false);
        TestResultInterfaceConfigurationXmlHandler handler = null;
        
		try
		{
			SAXParser saxParser = spf.newSAXParser();
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
