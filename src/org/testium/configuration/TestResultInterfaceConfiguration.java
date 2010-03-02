package org.testium.configuration;
/**
 * 
 */

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.testtoolinterfaces.utils.Trace;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 * @author Arjan Kranenburg
 *
 */
public class TestResultInterfaceConfiguration
{
	private File myXslDir;

	/**
	 * @param aXslDir
	 */
	public TestResultInterfaceConfiguration( File aXslDir )
	{
	    Trace.println(Trace.LEVEL.CONSTRUCTOR);

	    myXslDir = aXslDir;
	}

	public void readConfigFile( File aConfigFile )
	{
	    Trace.println(Trace.UTIL, "readConfigFile( " + aConfigFile + " )", true);
	    SAXParser saxParser;
	
	    SAXParserFactory spf = SAXParserFactory.newInstance();
	    spf.setNamespaceAware(false);
	
	    TestResultInterfaceConfigurationXmlHandler handler = null;
	    try
	    {
		    saxParser = spf.newSAXParser();
		    XMLReader xmlReader = saxParser.getXMLReader();
	
		    handler = new TestResultInterfaceConfigurationXmlHandler(xmlReader);
	
		    xmlReader.setContentHandler(handler);
	
		    xmlReader.parse(aConfigFile.getAbsolutePath());
	    }
	    catch (ParserConfigurationException e)
	    {
	    	e.printStackTrace();
	    }
	    catch (SAXException e)
	    {
	    	e.printStackTrace();
	    }
	    catch (IOException e)
	    {
	    	e.printStackTrace();
	    }
	}

	/**
	 * @return the myXslDir
	 */
	public File getXslDir()
	{
		return myXslDir;
	}
}
