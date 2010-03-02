package org.testium.plugins;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.testium.configuration.Configuration;
import org.testium.configuration.ConfigurationException;
import org.testium.configuration.TestResultInterfaceConfiguration;
import org.testium.configuration.TestResultInterfaceConfigurationXmlHandler;
import org.testtoolinterfaces.testresultinterface.TestRunResultXmlWriter;
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
	
	public void loadPlugIn(PluginCollection aPluginCollection, Configuration aConfig) throws ConfigurationException
	{
		String configDir = aConfig.getConfigDirectory();
		File configFile = new File( configDir + File.separator + "testResultConfiguration.xml" );
		TestResultInterfaceConfiguration config = readConfigFile( configFile );
		
		// Factories

		// Executors
		
		// Input and ouput interfaces
		File xslDir = config.getXslDir();
		String testEnvironment = aConfig.getTestEnvironment();
		String testPhase = aConfig.getTestPhase();
		TestRunResultXmlWriter aWriter = new TestRunResultXmlWriter( xslDir, testEnvironment, testPhase );
		aPluginCollection.addTestRunResultWriter( aWriter );
	}
	
	public TestResultInterfaceConfiguration readConfigFile( File aConfigFile ) throws ConfigurationException
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
			handler = new TestResultInterfaceConfigurationXmlHandler(xmlReader);

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
