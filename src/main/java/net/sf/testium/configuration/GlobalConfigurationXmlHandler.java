package net.sf.testium.configuration;

import java.io.File;
import java.util.ArrayList;

import net.sf.testium.Testium;

import org.testtoolinterfaces.utils.GenericTagAndStringXmlHandler;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;


/**
 * @author Arjan Kranenburg 
 * 
 *  <GlobalConfiguration>
 *    <TestResultOutputBaseDirectory>...</TestResultOutputBaseDirectory>
 *    <DefaultUserConfigurationFile>...</DefaultUserConfigurationFile>
 *    <TraceBaseClass>...</TraceBaseClass>
 *    <PluginLoaders>...</PluginLoaders>
 *    <PluginsDirectory>...</PluginsDirectory>
 *    <TestEnvironment>...</TestEnvironment>
 *    <TestPhase>...</TestPhase>
 *    <ProjectDirectory>...</ProjectDirectory>
 *    <TestFile>...</TestFile>
 *    <ExitCodeOnFailures>...</ExitCodeOnFailures>
 *    <ExitCodeOnErrors>...</ExitCodeOnErrors>
// *    <Interfaces>...</Interfaces>
 *  ...
 *  </GlobalConfiguration>
 * 
 */
public class GlobalConfigurationXmlHandler extends XmlHandler
{
	public static final String START_ELEMENT = "GlobalConfiguration";

	public static final String CFG_PLUGIN_LOADERS = "PluginLoaders";
	public static final String CFG_PLUGINS_DIRECTORY = "PluginsDirectory";
	public static final String CFG_TESTENVIRONMENT = "TestEnvironment";
	public static final String CFG_TESTPHASE = "TestPhase";
	public static final String CFG_DEFAULT_CONFIGFILE = "DefaultUserConfigurationFile";
	public static final String CFG_USER_CONFIGDIR = "UserConfigurationDirectory";
	public static final String CFG_CONFIGDIR = "ConfigurationDirectory";
	public static final String CFG_PROJECTDIR = "ProjectDirectory";
	public static final String CFG_TESTFILE = "TestFile";
	public static final String CFG_EXITCODE_ONFAILURES = "ExitCodeOnFailures";
	public static final String CFG_EXITCODE_ONERRORS = "ExitCodeOnErrors";

	// Trace
	private static final String CFG_TRACE_BASECLASS = "TraceBaseClass";
	private static final String CFG_TRACE_CLASS = "TraceClass";
	private static final String CFG_TRACE_LEVEL = "TraceLevel";
	private static final String CFG_TRACE_DEPTH = "TraceDepth";

	private RunTimeData myRunTimeData;
	
	public GlobalConfigurationXmlHandler( XMLReader anXmlReader, RunTimeData aRtData )
	{
		super(anXmlReader, START_ELEMENT);
		Trace.println(Trace.CONSTRUCTOR);
		
		myRunTimeData = aRtData;

		this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_PLUGIN_LOADERS));
		this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_PLUGINS_DIRECTORY));
	    this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_TESTENVIRONMENT));
	    this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_TESTPHASE));
	    this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_DEFAULT_CONFIGFILE));
	    this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_USER_CONFIGDIR));
	    this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_CONFIGDIR));
	    this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_PROJECTDIR));
	    this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_TESTFILE));
	    this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_EXITCODE_ONFAILURES));
	    this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_EXITCODE_ONERRORS));

	    this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_TRACE_BASECLASS));
	    this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_TRACE_CLASS));
	    this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_TRACE_LEVEL));
	    this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_TRACE_DEPTH));
	    
//	    this.addElementHandler(new InterfacesXmlHandler(anXmlReader, aRtData, null, null));
	    
	    // Set default values
		myRunTimeData.add( new RunTimeVariable(Testium.EXITCODEONFAILURES, new Integer(0)) );
		myRunTimeData.add( new RunTimeVariable(Testium.EXITCODEONERRORS, new Integer(1)) );
	    
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
		if ( ! GenericTagAndStringXmlHandler.class.isInstance(aChildXmlHandler) )
		{
			throw new Error( "ChildXmlHandler (" + aChildXmlHandler.getClass().toString() + ") must be of type GenericTagAndStringXmlHandler" );
		}
		GenericTagAndStringXmlHandler childXmlHandler = (GenericTagAndStringXmlHandler) aChildXmlHandler;

		RunTimeVariable rtVar = null;
		if (aQualifiedName.equalsIgnoreCase(CFG_PLUGIN_LOADERS))
    	{
			String pluginLoaderStr = childXmlHandler.getValue();
			ArrayList<String> pluginLoaders = convertStringToPluginLoaders(pluginLoaderStr);
			rtVar = new RunTimeVariable(Testium.PLUGINLOADERS, pluginLoaders);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_PLUGINS_DIRECTORY))
    	{
			String pluginsDirName = myRunTimeData.substituteVars( childXmlHandler.getValue() );
			File pluginsDirectory = new File( pluginsDirName );
			rtVar = new RunTimeVariable(Testium.PLUGINSDIR, pluginsDirectory);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TESTENVIRONMENT))
    	{
			String testEnvironment = myRunTimeData.substituteVars( childXmlHandler.getValue() );
			rtVar = new RunTimeVariable(Testium.TESTENVIRONMENT, testEnvironment);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TESTPHASE))
    	{
			String testPhase = myRunTimeData.substituteVars( childXmlHandler.getValue() );
			rtVar = new RunTimeVariable(Testium.TESTPHASE, testPhase);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_DEFAULT_CONFIGFILE))
    	{
			String defaultConfigFileName = myRunTimeData.substituteVars( childXmlHandler.getValue() );
			File defaultConfigFile = new File( defaultConfigFileName );
			rtVar = new RunTimeVariable(Testium.DEFAULTCONFIGFILE, defaultConfigFile);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_USER_CONFIGDIR))
    	{
			String userConfigDirName = myRunTimeData.substituteVars( childXmlHandler.getValue() );
			File userConfigDir = new File( userConfigDirName );
			rtVar = new RunTimeVariable(Testium.USERCONFIGDIR, userConfigDir);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_CONFIGDIR))
    	{
			String configDirName = myRunTimeData.substituteVars( childXmlHandler.getValue() );
			File configDir = new File( configDirName );
			rtVar = new RunTimeVariable(Testium.CONFIGDIR, configDir);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_PROJECTDIR))
    	{
			String projectDirName = myRunTimeData.substituteVars( childXmlHandler.getValue() );
			File projectDir = new File( projectDirName );
			rtVar = new RunTimeVariable(Testium.PROJECTDIR, projectDir);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TESTFILE))
    	{
			String testFileName = myRunTimeData.substituteVars( childXmlHandler.getValue() );
			File testFile = new File( testFileName );
			rtVar = new RunTimeVariable(Testium.TESTFILE, testFile);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_EXITCODE_ONFAILURES))
    	{
			Integer exitCodeOnFailures = new Integer( childXmlHandler.getValue() );
			rtVar = new RunTimeVariable(Testium.EXITCODEONFAILURES, exitCodeOnFailures);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_EXITCODE_ONERRORS))
    	{
			Integer exitCodeOnErrors = new Integer( childXmlHandler.getValue() );
			rtVar = new RunTimeVariable(Testium.EXITCODEONERRORS, exitCodeOnErrors);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TESTFILE))
    	{
			String testFileName = myRunTimeData.substituteVars( childXmlHandler.getValue() );
			File testFile = new File( testFileName );
			rtVar = new RunTimeVariable(Testium.TESTFILE, testFile);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TRACE_BASECLASS))
    	{
			String traceBaseClass = childXmlHandler.getValue();
			Trace.getInstance().addBaseClass(traceBaseClass);
			String pkgBases = (String) myRunTimeData.getValue(Testium.TRACEPKGBASES);
			pkgBases += ";" + traceBaseClass;
			rtVar = new RunTimeVariable(Testium.TRACEPKGBASES, pkgBases);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TRACE_CLASS))
    	{
			String traceClass = childXmlHandler.getValue();
			Trace.getInstance().setTraceClass(traceClass);
			rtVar = new RunTimeVariable(Testium.TRACECLASS, traceClass);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TRACE_LEVEL))
    	{
			Trace.LEVEL traceLevel = Trace.LEVEL.valueOf( childXmlHandler.getValue() );
			Trace.getInstance().setTraceLevel(traceLevel);
			rtVar = new RunTimeVariable(Testium.TRACELEVEL, traceLevel);
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_TRACE_DEPTH))
    	{
			int traceDepth = (new Integer(childXmlHandler.getValue())).intValue();
			Trace.getInstance().setDepth( traceDepth );
			rtVar = new RunTimeVariable(Testium.TRACEDEPTH, traceDepth);
    	}
		else
		{
			String value = childXmlHandler.getValue();
			rtVar = new RunTimeVariable(aQualifiedName, value);
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
