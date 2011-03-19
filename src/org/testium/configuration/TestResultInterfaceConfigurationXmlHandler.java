package org.testium.configuration;

import java.io.File;
import java.util.ArrayList;

import org.testium.Testium;
import org.testtoolinterfaces.testresultinterface.Configuration;
import org.testtoolinterfaces.utils.GenericTagAndBooleanXmlHandler;
import org.testtoolinterfaces.utils.GenericTagAndStringXmlHandler;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;


public class TestResultInterfaceConfigurationXmlHandler extends XmlHandler
{
	private static final String START_ELEMENT = "TestResultWriter";

	private static final String	CFG_STDOUT_ENABLED	= "toStdOut";
	private static final String	CFG_FILE_ENABLED	= "toFile";
	private static final String CFG_OUTPUT_BASE_DIRECTORY = "OutputBaseDirectory";
	private static final String	CFG_OUTPUT_FILENAME	= "fileName";
	private static final String	CFG_RUN_XSLDIR	= "xslRunDir";
	private static final String	CFG_GROUP_XSLDIR	= "xslGroupDir";
	private static final String	CFG_CASE_XSLDIR	= "xslCaseDir";

	private boolean myStdOutEnabled;
	private boolean myFileEnabled;
	private String myFileName;
	private File myRunXslDir;
	private File myGroupXslDir;
	private File myCaseXslDir;

	private RunTimeData myRunTimeData;

	public TestResultInterfaceConfigurationXmlHandler(XMLReader anXmlReader, RunTimeData anRtData)
	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);
	    reset();

		myRunTimeData = anRtData;

	    ArrayList<XmlHandler> xmlHandlers = new ArrayList<XmlHandler>();
	    xmlHandlers.add(new GenericTagAndBooleanXmlHandler(anXmlReader, CFG_STDOUT_ENABLED));
	    xmlHandlers.add(new GenericTagAndBooleanXmlHandler(anXmlReader, CFG_FILE_ENABLED));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_RUN_XSLDIR));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_GROUP_XSLDIR));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_CASE_XSLDIR));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_OUTPUT_FILENAME));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_OUTPUT_BASE_DIRECTORY));

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
	    Trace.println(Trace.UTIL, "handleReturnFromChildElement( " + 
	    	      aQualifiedName + " )", true);
	    
		if (aQualifiedName.equalsIgnoreCase(CFG_OUTPUT_BASE_DIRECTORY))
    	{
			// If it is set, it was set by personal configuration. We will not overwrite it.
			if ( ! myRunTimeData.containsKey(Testium.RESULTBASEDIR) )
			{
				String resultBaseDirName = myRunTimeData.substituteVars( aChildXmlHandler.getValue() );
				File resultBaseDir = new File( resultBaseDirName );
				RunTimeVariable rtVar = new RunTimeVariable(Testium.RESULTBASEDIR, resultBaseDir);
				myRunTimeData.add( rtVar );
			}
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_STDOUT_ENABLED))
    	{
			myStdOutEnabled = aChildXmlHandler.getValue().equalsIgnoreCase("true");
			aChildXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_FILE_ENABLED))
    	{
			myFileEnabled = aChildXmlHandler.getValue().equalsIgnoreCase("true");
			aChildXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_RUN_XSLDIR))
    	{
			String xslDirName = myRunTimeData.substituteVars( aChildXmlHandler.getValue() );
			myRunXslDir = new File( xslDirName );
			aChildXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_GROUP_XSLDIR))
    	{
			String xslDirName = myRunTimeData.substituteVars( aChildXmlHandler.getValue() );
			myGroupXslDir = new File( xslDirName );
			aChildXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_CASE_XSLDIR))
    	{
			String xslDirName = myRunTimeData.substituteVars( aChildXmlHandler.getValue() );
			myCaseXslDir = new File( xslDirName );
			aChildXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_OUTPUT_FILENAME))
    	{
			myFileName = myRunTimeData.substituteVars( aChildXmlHandler.getValue() );
			aChildXmlHandler.reset();
    	}

   		aChildXmlHandler.reset();
	}
	
	public TestResultInterfaceConfiguration getConfiguration() throws ConfigurationException
	{
		Configuration ttiConfiguration = new Configuration( myRunXslDir, myGroupXslDir, myCaseXslDir );
		return new TestResultInterfaceConfiguration( myStdOutEnabled, myFileEnabled, ttiConfiguration, myFileName );
	}
	
	public void reset()
	{
		myStdOutEnabled = true;
		myFileEnabled = true;
		myFileName = "result.xml";

		myRunXslDir = null;
		myGroupXslDir = null;
		myCaseXslDir = null;
	}

}
