package org.testium.configuration;

import java.io.File;
import java.util.ArrayList;

import org.testtoolinterfaces.utils.GenericTagAndBooleanXmlHandler;
import org.testtoolinterfaces.utils.GenericTagAndStringXmlHandler;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;


public class TestResultInterfaceConfigurationXmlHandler extends XmlHandler
{
	private static final String START_ELEMENT = "TestResultWriter";

	private static final String	CFG_STDOUT_ENABLED	= "toStdOut";
	private static final String	CFG_FILE_ENABLED	= "toFile";
	private static final String	CFG_OUTPUT_FILENAME	= "fileName";
	private static final String	CFG_XSLDIR_FILE	= "xslDir";

	private boolean myStdOutEnabled = true;
	private boolean myFileEnabled = true;
	private String myTempFileName = "result.xml";
	private File myTempXslDir;

	public TestResultInterfaceConfigurationXmlHandler(XMLReader anXmlReader)
	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.LEVEL.CONSTRUCTOR);
	    reset();

	    ArrayList<XmlHandler> xmlHandlers = new ArrayList<XmlHandler>();
	    xmlHandlers.add(new GenericTagAndBooleanXmlHandler(anXmlReader, CFG_STDOUT_ENABLED));
	    xmlHandlers.add(new GenericTagAndBooleanXmlHandler(anXmlReader, CFG_FILE_ENABLED));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_XSLDIR_FILE));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_OUTPUT_FILENAME));

	    for (XmlHandler handler : xmlHandlers)
	    {
			this.addStartElementHandler(handler.getStartElement(), handler);
			handler.addEndElementHandler(handler.getStartElement(), this);
	    }
	}

	@Override
	public void handleStartElement(String aQualifiedName)
	{
		// nop
	}

	@Override
	public void handleCharacters(String aValue)
	{
		// nop
	}

	@Override
	public void handleEndElement(String aQualifiedName)
	{
		// nop
	}

	@Override
	public void processElementAttributes(String aQualifiedName, Attributes att)
	{
		// nop
	}

	@Override
	public void handleGoToChildElement(String aQualifiedName)
	{
		// nop
	}

	@Override
	public void handleReturnFromChildElement(String aQualifiedName, XmlHandler aChildXmlHandler)
	{
	    Trace.println(Trace.LEVEL.UTIL, "handleReturnFromChildElement( " + 
	    	      aQualifiedName + " )", true);
	    
		if (aQualifiedName.equalsIgnoreCase(CFG_STDOUT_ENABLED))
    	{
			myStdOutEnabled = aChildXmlHandler.getValue().equalsIgnoreCase("true");
			aChildXmlHandler.reset();
    	}
		if (aQualifiedName.equalsIgnoreCase(CFG_FILE_ENABLED))
    	{
			myFileEnabled = aChildXmlHandler.getValue().equalsIgnoreCase("true");
			aChildXmlHandler.reset();
    	}
		if (aQualifiedName.equalsIgnoreCase(CFG_XSLDIR_FILE))
    	{
			myTempXslDir = new File( aChildXmlHandler.getValue() );
			aChildXmlHandler.reset();
    	}
		if (aQualifiedName.equalsIgnoreCase(CFG_OUTPUT_FILENAME))
    	{
			myTempFileName = aChildXmlHandler.getValue();
			aChildXmlHandler.reset();
    	}

   		aChildXmlHandler.reset();
	}
	
	public TestResultInterfaceConfiguration getConfiguration() throws ConfigurationException
	{
		return new TestResultInterfaceConfiguration( myStdOutEnabled, myFileEnabled, myTempXslDir, myTempFileName );
	}
	
	public void reset()
	{
		myTempXslDir = null;
	}

}
