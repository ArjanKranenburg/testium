package org.testium.configuration;

import java.io.File;
import java.util.ArrayList;

import org.testtoolinterfaces.utils.GenericTagAndBooleanXmlHandler;
import org.testtoolinterfaces.utils.GenericTagAndStringXmlHandler;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;


/**
 * @author Arjan Kranenburg 
 * 
 *  <GlobalConfiguration>
 *    <TestResultOutputBaseDirectory>...</TestResultOutputBaseDirectory>
 *    <TraceEnabled>...</TraceEnabled>
 *    <InterfaceClasses>...</InterfaceClasses>
 *    <PluginsDirectory>...</PluginsDirectory>
 *    <TestEnvironment>...</TestEnvironment>
 *    <TestPhase>...</TestPhase>
 *  ...
 *  </GlobalConfiguration>
 * 
 */
public class GlobalConfigurationXmlHandler extends XmlHandler
{
	public static final String START_ELEMENT = "GlobalConfiguration";

	public static final String CFG_TEST_RESULT_OUTPUT_BASE_DIRECTORY = "TestResultOutputBaseDirectory";
	public static final String CFG_PLUGIN_LOADERS = "PluginLoaders";
	public static final String CFG_PLUGINS_DIRECTORY = "PluginsDirectory";
	public static final String CFG_TESTENVIRONMENT = "TestEnvironment";
	public static final String CFG_TESTPHASE = "TestPhase";
	public static final String CFG_SETTINGS_FILE = "DefaultUserSettings";

	private static final String CFG_TRACE_ENABLED = "TraceEnabled";
	private static final String CFG_TRACE_BASECLASS = "TraceBaseClass";
	private static final String CFG_TRACE_CLASS = "TraceClass";
	private static final String CFG_TRACE_LEVEL = "TraceLevel";
	private static final String CFG_TRACE_DEPTH = "TraceDepth";

	private String myTempResultBaseDir = "";
	private String myTempPluginLoaders = "";
	private String myTempPluginsDirectory = "";
	private String myTempEnvironment = "Unknown";
	private String myTempTestPhase = "Unknown";
	private String myTempSettingsFileName = ".testium";
	private File myConfigDirectory;
	
	public GlobalConfigurationXmlHandler( XMLReader anXmlReader, File aConfigDir )
	{
		super(anXmlReader, START_ELEMENT);
		Trace.println(Trace.CONSTRUCTOR);
		
		myConfigDirectory = aConfigDir;

	    ArrayList<XmlHandler> xmlHandlers = new ArrayList<XmlHandler>();
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_TEST_RESULT_OUTPUT_BASE_DIRECTORY));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_PLUGIN_LOADERS));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_PLUGINS_DIRECTORY));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_TESTENVIRONMENT));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_TESTPHASE));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_SETTINGS_FILE));

	    xmlHandlers.add(new GenericTagAndBooleanXmlHandler(anXmlReader, CFG_TRACE_ENABLED));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_TRACE_BASECLASS));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_TRACE_CLASS));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_TRACE_LEVEL));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_TRACE_DEPTH));

	    for (XmlHandler handler : xmlHandlers)
	    {
			this.addStartElementHandler(handler.getStartElement(), handler);
			handler.addEndElementHandler(handler.getStartElement(), this);
	    }
	}

	public GlobalConfigurationXmlHandler( XMLReader anXmlReader, File aConfigDir, Configuration aGlobalConfig )
	{
		this( anXmlReader, aConfigDir );
	    
		myTempResultBaseDir = "";
		myTempPluginLoaders = "";
		myTempPluginsDirectory = "";
		myTempEnvironment = aGlobalConfig.getTestEnvironment();
		myTempTestPhase = aGlobalConfig.getTestPhase();
		myTempSettingsFileName = ".testium";
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
		if (aQualifiedName.equalsIgnoreCase(CFG_TEST_RESULT_OUTPUT_BASE_DIRECTORY))
    	{
			myTempResultBaseDir = aChildXmlHandler.getValue();
			aChildXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_PLUGIN_LOADERS))
    	{
			myTempPluginLoaders = aChildXmlHandler.getValue();
			aChildXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_PLUGINS_DIRECTORY))
    	{
			myTempPluginsDirectory = aChildXmlHandler.getValue();
			aChildXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TESTENVIRONMENT))
    	{
			myTempEnvironment = aChildXmlHandler.getValue();
			aChildXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TESTPHASE))
    	{
			myTempTestPhase = aChildXmlHandler.getValue();
			aChildXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_SETTINGS_FILE))
    	{
			myTempSettingsFileName = aChildXmlHandler.getValue();
			aChildXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TRACE_ENABLED))
    	{
			boolean traceEnabled = aChildXmlHandler.getValue().equalsIgnoreCase("true");
			Trace.getInstance().setEnabled(traceEnabled);
			aChildXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TRACE_BASECLASS))
    	{
			Trace.getInstance().addBaseClass(aChildXmlHandler.getValue());
			aChildXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TRACE_CLASS))
    	{
			Trace.getInstance().setTraceClass(aChildXmlHandler.getValue());
			aChildXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TRACE_LEVEL))
    	{
			Trace.getInstance().setTraceLevel(Trace.LEVEL.valueOf( aChildXmlHandler.getValue() ));
			aChildXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TRACE_DEPTH))
    	{
			Trace.getInstance().setDepth( (new Integer(aChildXmlHandler.getValue())).intValue() );
			aChildXmlHandler.reset();
    	}
	}
	
	public Configuration getConfiguration() throws ConfigurationException
	{
//		if ( myTempResultBaseDir.isEmpty() )
//		{
//			throw new ConfigurationException( CFG_TEST_RESULT_OUTPUT_BASE_DIRECTORY + " is not set" );
//		}

		File baseResultDir = new File( myTempResultBaseDir );
//		if ( !baseResultDir.isDirectory() )
//		{
//			throw new ConfigurationException( "Directory does not exist: " + baseResultDir.getAbsolutePath() );
//		}

		return new Configuration( baseResultDir,
		                          myTempPluginLoaders,
		                          myTempPluginsDirectory,
		                          myConfigDirectory,
		                          myTempEnvironment,
		                          myTempTestPhase,
		                          myTempSettingsFileName );
	}
	
	public void reset()
	{
		myTempResultBaseDir = "";
		myTempPluginLoaders = "";
		myTempPluginsDirectory = "";
		myTempEnvironment = "Unknown";
		myTempTestPhase = "Unknown";
		myTempSettingsFileName = ".testium";
	}
}
