package org.testium.configuration;

public enum KEYS 
{
	/** Testium's basedir */ 												BASE_DIR,

	/** Command to execute */ 												COMMAND,
	/** Global configuration directory */ 									CONFIG_DIRECTORY,
	/** Personal configuration filename */ 									CONFIGFILENAME,

	/** Plugin-loaders */ 													PLUGIN_LOADERS,
	/** Plugins directory */ 												PLUGINSDIRECTORY,

	/** Base dir of the Test Results */ 									RESULT_BASE_DIR,
	
	/** Start date of the test */				 							START_DATE,
	/** Start time of the test */										 	START_TIME,

	/** Test Environment */ 												TEST_ENVIRONMENT,
	/** Test Phase */ 														TEST_PHASE,
	/** Part of Package names that are removed from the beginning in the trace printout */ 	TRACE_PKG_BASES,
	/** Class that triggers tracing */ 										TRACE_CLASS,
	/** Depth of trace methods shown in the trace printout */ 				TRACE_DEPTH,
	/** To turn on or off tracing */ 										TRACE_ENABLED,
	/** Only this and higher trace levels are shown in the trace printout */ 	TRACE_LEVEL,

	/** User's Home directory */ 											USER_HOME,
	
																			ZZZ_END;
	
	public String toString()
	{
		return super.toString().toLowerCase();
	}
}
