package org.testium.configuration;

import java.io.File;
import java.util.ArrayList;

import org.testium.Testium;
import org.testtoolinterfaces.utils.GenericTagAndBooleanXmlHandler;
import org.testtoolinterfaces.utils.GenericTagAndStringXmlHandler;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.testtoolinterfaces.utils.RunTimeVariable;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;


/**
 * @author Arjan Kranenburg 
 * 
 *  <Configuration>
 *    <TestResultOutputBaseDirectory>...</TestResultOutputBaseDirectory>
 *    <TraceEnabled>...</TraceEnabled>
 *    <TestEnvironment>...</TestEnvironment>
 *    <TestPhase>...</TestPhase>
 *  ...
 *  </Configuration>
 * 
 */
public class PersonalConfigurationXmlHandler extends XmlHandler
{
	public static final String START_ELEMENT = "Configuration";

	public static final String CFG_TEST_RESULT_OUTPUT_BASE_DIRECTORY = "TestResultOutputBaseDirectory";
	public static final String CFG_TESTENVIRONMENT = "TestEnvironment";
	public static final String CFG_TESTPHASE = "TestPhase";

	private static final String CFG_TRACE_ENABLED = "TraceEnabled";
	private static final String CFG_TRACE_CLASS = "TraceClass";
	private static final String CFG_TRACE_LEVEL = "TraceLevel";
	private static final String CFG_TRACE_DEPTH = "TraceDepth";

	private RunTimeData myRunTimeData;
	
	public PersonalConfigurationXmlHandler( XMLReader anXmlReader, RunTimeData anRtData )
	{
		super(anXmlReader, START_ELEMENT);
		Trace.println(Trace.CONSTRUCTOR);
		
		myRunTimeData = anRtData;

	    ArrayList<XmlHandler> xmlHandlers = new ArrayList<XmlHandler>();
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_TEST_RESULT_OUTPUT_BASE_DIRECTORY));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_TESTENVIRONMENT));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_TESTPHASE));

	    xmlHandlers.add(new GenericTagAndBooleanXmlHandler(anXmlReader, CFG_TRACE_ENABLED));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_TRACE_CLASS));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_TRACE_LEVEL));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_TRACE_DEPTH));

	    for (XmlHandler handler : xmlHandlers)
	    {
			this.addStartElementHandler(handler.getStartElement(), handler);
			handler.addEndElementHandler(handler.getStartElement(), this);
	    }
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
		//nop
    }

	@Override
	public void handleGoToChildElement(String aQualifiedName)
	{
		//nop
	}

	@Override
	public void handleReturnFromChildElement(String aQualifiedName, XmlHandler aChildXmlHandler)
	{
		Trace.println(Trace.UTIL, "handleReturnFromChildElement( " 
	            + aQualifiedName + " )", true );
		RunTimeVariable rtVar = null;
		if (aQualifiedName.equalsIgnoreCase(CFG_TEST_RESULT_OUTPUT_BASE_DIRECTORY))
    	{
			String resultBaseDirName = myRunTimeData.substituteVars( aChildXmlHandler.getValue() );
			File resultBaseDir = new File( resultBaseDirName );
			rtVar = new RunTimeVariable(Testium.RESULTBASEDIR, resultBaseDir);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TESTENVIRONMENT))
    	{
			String testEnvironment = aChildXmlHandler.getValue();
			rtVar = new RunTimeVariable(Testium.TESTENVIRONMENT, testEnvironment);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TESTPHASE))
    	{
			String testPhase = aChildXmlHandler.getValue();
			rtVar = new RunTimeVariable(Testium.TESTPHASE, testPhase);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TRACE_ENABLED))
    	{
			boolean traceEnabled = aChildXmlHandler.getValue().equalsIgnoreCase("true");
			Trace.getInstance().setEnabled( traceEnabled );
			rtVar = new RunTimeVariable(Testium.TRACEENABLED, traceEnabled);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TRACE_CLASS))
    	{
			String traceClass = aChildXmlHandler.getValue();
			Trace.getInstance().setTraceClass(traceClass);
			rtVar = new RunTimeVariable(Testium.TRACECLASS, traceClass);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TRACE_LEVEL))
    	{
			Trace.LEVEL traceLevel = Trace.LEVEL.valueOf( aChildXmlHandler.getValue() );
			Trace.getInstance().setTraceLevel(traceLevel);
			rtVar = new RunTimeVariable(Testium.TRACELEVEL, traceLevel);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TRACE_DEPTH))
    	{
			int traceDepth = (new Integer(aChildXmlHandler.getValue())).intValue();
			Trace.getInstance().setDepth( traceDepth );
			rtVar = new RunTimeVariable(Testium.TRACEDEPTH, traceDepth);
    	}
		else
		{
			String value = aChildXmlHandler.getValue();
			rtVar = new RunTimeVariable(aQualifiedName.toLowerCase(), value);
		}

		if (rtVar != null)
		{
			myRunTimeData.add( rtVar );
		}
		aChildXmlHandler.reset();
	}
	
	/**
	 * @return the PluginLoaders
	 */
	public ArrayList<String> convertStringToPluginLoaders( String aPluginLoadersString )
	{
		ArrayList<String> pluginLoaders = new ArrayList<String>();
    	if ( ! aPluginLoadersString.isEmpty() )
    	{
        	String[] classNames = aPluginLoadersString.trim().split(";");
        	if ( classNames.length != 0 )
        	{
            	for ( String className : classNames )
            	{
            		className = className.replace('\t', ' ').trim();
            		pluginLoaders.add( className );
            	}
        	}
    	}

		return pluginLoaders;
	}
}
