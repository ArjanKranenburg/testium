package org.testium;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Locale;

import org.testium.configuration.ConfigurationException;
import org.testium.executor.TestExecutionException;
import org.testium.executor.TestRunExecutorImpl;
import org.testium.plugins.PluginCollection;
import org.testium.systemundertest.SutControl;
import org.testtoolinterfaces.testresult.SutInfo;
import org.testtoolinterfaces.testresultinterface.TestRunResultWriter;
import org.testtoolinterfaces.testsuite.TestGroup;
import org.testtoolinterfaces.testsuiteinterface.TestGroupReader;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;

public class Testium
{
	public enum KEYS
	{
		/** Testium's base directory */ 										BASEDIR,
	
		/** Command to execute */ 												COMMAND,
		/** Global configuration directory */ 									CONFIGDIR,
		/** Individual configuration file */ 									CONFIGFILE,
	
		/** Default personal configuration file */ 								DEFAULTCONFIGFILE,
		
		/** Global configuration file */										GLOBALCONFIGFILE,
	
		/** Help */							 									HELP,

		/** Plugin-loaders */ 													PLUGINLOADERS,
		/** Plugins directory */ 												PLUGINSDIR,
		/** Project (Test Suite's) base directory */ 							PROJECTDIR,
	
		/** Base dir of the Test Results */ 									RESULTBASEDIR,
		
		/** Start date and time of the test */									START,
		/** Start date of the test, formatted as yyyyMMdd */					STARTDATE,
		/** Start time of the test, formatted as HHmmss */					 	STARTTIME,
	
		/** Test Case */														TESTCASE,
		/** Test Environment */ 												TESTENVIRONMENT,
		/** Test File */														TESTFILE,
		/** Test Group */														TESTGROUP,
		/** Test Phase */ 														TESTPHASE,
		/** Part of Package names that are removed from the beginning in the trace printout */ 	TRACEPKGBASES,
		/** Class that triggers tracing */ 										TRACECLASS,
		/** Depth of trace methods shown in the trace printout */ 				TRACEDEPTH,
		/** To turn on or off tracing */ 										TRACEENABLED,
		/** Only this and higher trace levels are shown in the trace printout */ 	TRACELEVEL,
	
		/** User's Configuration directory */ 									USERCONFIGDIR,
		/** User's Home directory */ 											USERHOME,
		
		/** Version */															VERSION,
		
																				ZZZ_END;
	
		public String toString()
		{
			return super.toString().toLowerCase( Locale.ENGLISH );
		}
	}

	public final static String BASEDIR			 = KEYS.BASEDIR.toString();			// As java.io.File

	public final static String CASE				 = KEYS.TESTCASE.toString();		// As String
	public final static String COMMAND 			 = KEYS.COMMAND.toString();			// As OPTION
	public final static String CONFIGDIR		 = KEYS.CONFIGDIR.toString();		// As java.io.File
	public final static String CONFIGFILE	     = KEYS.CONFIGFILE.toString();		// As java.io.File

	public final static String DEFAULTCONFIGFILE = KEYS.DEFAULTCONFIGFILE.toString(); // As java.io.File
	
	public final static String FILE  	         = KEYS.TESTFILE.toString();		// As java.io.File
	
	public final static String GLOBALCONFIGFILE  = KEYS.GLOBALCONFIGFILE.toString();// As java.io.File
	public final static String GROUP			 = KEYS.TESTGROUP.toString();		// As String

	public final static String HELP				 = KEYS.HELP.toString();			// As boolean

	public final static String PLUGINLOADERS	 = KEYS.PLUGINLOADERS.toString();	// As java.util.ArrayList
	public final static String PLUGINSDIR		 = KEYS.PLUGINSDIR.toString();		// As java.io.File
	public final static String PROJECTDIR		 = KEYS.PROJECTDIR.toString();		// As java.io.File
	
	public final static String RESULTBASEDIR	 = KEYS.RESULTBASEDIR.toString();	// As java.io.File
	
	public final static String START			 = KEYS.START.toString();			// As java.util.Date
	public final static String STARTDATE		 = KEYS.STARTDATE.toString();		// As String
	public final static String STARTTIME		 = KEYS.STARTTIME.toString();		// As String
	
	public final static String TESTCASE			 = KEYS.TESTCASE.toString();		// As String
	public final static String TESTENVIRONMENT	 = KEYS.TESTENVIRONMENT.toString();	// As String
	public final static String TESTFILE 	     = KEYS.TESTFILE.toString();		// As java.io.File
	public final static String TESTGROUP		 = KEYS.TESTGROUP.toString();		// As String
	public final static String TESTPHASE		 = KEYS.TESTPHASE.toString();		// As String
	public final static String TRACEPKGBASES	 = KEYS.TRACEPKGBASES.toString();	// As String (Class???)
	public final static String TRACECLASS		 = KEYS.TRACECLASS.toString();		// As String (class???)
	public final static String TRACEDEPTH		 = KEYS.TRACEDEPTH.toString();		// As int
	public final static String TRACEENABLED		 = KEYS.TRACEENABLED.toString();	// As Boolean
	public final static String TRACELEVEL		 = KEYS.TRACELEVEL.toString();		// As Trace.Level
	
	public final static String USERCONFIGDIR	 = KEYS.USERCONFIGDIR.toString();		// As java.io.File
	public final static String USERHOME 		 = KEYS.USERHOME.toString();		// As java.io.File

	public final static String VERSION			 = KEYS.VERSION.toString();			// As boolean


	private TestGroupReader myTestGroupReader;
	private SutControl mySutControl;
	
	private TestRunExecutorImpl myTestRunExecutor;
	
	private TestRunResultWriter myTestRunResultWriter;
	
	public Testium(PluginCollection aPlugins, RunTimeData aRtData ) throws ConfigurationException
	{
		myTestGroupReader = aPlugins.getTestGroupReader();
		mySutControl = aPlugins.getSutControl();

		myTestRunResultWriter = aPlugins.getTestRunResultWriter();
		myTestRunExecutor = new TestRunExecutorImpl( aPlugins.getTestStepExecutor(),
		                                             aPlugins.getTestCaseExecutor(),
		                                             aPlugins.getTestGroupExecutor(),
		                                             aPlugins.getTestGroupResultWriter(),
		                                             myTestRunResultWriter );
	}

	public TestGroup readTestGroup( File aTestGroup )
	{
		Trace.println(Trace.EXEC_PLUS, "validate( " + aTestGroup.getName() + " )", true);

		return myTestGroupReader.readTgFile(aTestGroup);
	}

	public void execute( TestGroup aTestGroup, File aBaseExecutionDir, RunTimeData anRtData ) throws TestExecutionException
	{
		Trace.println(Trace.EXEC, "execute( " + aTestGroup.getId() + " )", true);

		File logDir = anRtData.getValueAsFile(Testium.RESULTBASEDIR);
		if ( !logDir.isDirectory() )
		{
			logDir.mkdir();
		}

		SutInfo sut = mySutControl.getSutInfo( logDir, anRtData );

		String username = System.getProperty("user.name");
		String hostname = "Unknown";
		try
		{
			hostname = java.net.InetAddress.getLocalHost().getHostName();
		}
		catch (UnknownHostException exc)
		{
			// nop, i.e. leave it Unknown
		}

		myTestRunExecutor.execute(aTestGroup, username, hostname, sut, aBaseExecutionDir, anRtData);
	}

	public void prepare( TestGroup aTestGroup,
	                     File aParentFile,
	                     RunTimeData anRtData ) throws TestExecutionException
	{
		// TODO Auto-generated method stub
		
	}
}
