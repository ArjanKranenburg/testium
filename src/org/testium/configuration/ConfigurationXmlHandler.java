package org.testium.configuration;

import java.io.File;

import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;


/**
 * @author Arjan Kranenburg 
 * 
 * <Configuration>
 *  <GlobalConfiguration>
 *  ...
 *  </GlobalConfiguration>
 * <Configuration>
 * 
 */
public class ConfigurationXmlHandler extends XmlHandler
{
	public static final String START_ELEMENT = "Configuration";

	private GlobalConfigurationXmlHandler myGlobalConfigurationXmlHandler;

	public ConfigurationXmlHandler( XMLReader anXmlReader, File aConfigDir )
	{
		super(anXmlReader, START_ELEMENT);
		Trace.println(Trace.LEVEL.CONSTRUCTOR);

		myGlobalConfigurationXmlHandler = new GlobalConfigurationXmlHandler(anXmlReader, aConfigDir );
		this.addStartElementHandler(GlobalConfigurationXmlHandler.START_ELEMENT, myGlobalConfigurationXmlHandler);
		myGlobalConfigurationXmlHandler.addEndElementHandler(GlobalConfigurationXmlHandler.START_ELEMENT, this);
	}

	public ConfigurationXmlHandler( XMLReader anXmlReader, Configuration aGlobalConfig )
	{
		super(anXmlReader, START_ELEMENT);
		Trace.println(Trace.LEVEL.CONSTRUCTOR);

		myGlobalConfigurationXmlHandler = new GlobalConfigurationXmlHandler(anXmlReader, aGlobalConfig);
		this.addStartElementHandler(GlobalConfigurationXmlHandler.START_ELEMENT, myGlobalConfigurationXmlHandler);
		myGlobalConfigurationXmlHandler.addEndElementHandler(GlobalConfigurationXmlHandler.START_ELEMENT, this);
	}

	@Override
	public void handleStartElement(String aQualifiedName)
	{
   		//nop;
    }

	@Override
	public void handleCharacters(String aValue)
	{
		//nop
    }
    
	@Override
	public void handleEndElement(String aQualifiedName)
	{
		//nop
    }

    public void processElementAttributes(String aQualifiedName, Attributes att)
    {
		Trace.print(Trace.LEVEL.UTIL, "processElementAttributes( " 
	            + aQualifiedName, true );
     	if (aQualifiedName.equalsIgnoreCase(START_ELEMENT))
    	{
		    for (int i = 0; i < att.getLength(); i++)
		    {
	    		Trace.print( Trace.LEVEL.UTIL, ", " + att.getQName(i) + "=" + att.getValue(i) );
//		    	if (att.getQName(i).equalsIgnoreCase(ATTRIBUTE_ID))
//		    	{
//		    		myCurrentTestSuiteId = att.getValue(i);
//		    	}
		    }
    	}
		Trace.println( Trace.LEVEL.UTIL, " )" );
    }

	@Override
	public void handleGoToChildElement(String aQualifiedName)
	{
		//nop
	}

	@Override
	public void handleReturnFromChildElement(String aQualifiedName, XmlHandler aChildXmlHandler)
	{
	}

	public Configuration getConfiguration() throws ConfigurationException
	{
		Configuration configuration = myGlobalConfigurationXmlHandler.getConfiguration();
		myGlobalConfigurationXmlHandler.reset();
		return configuration;
	}
}
