package org.testium;

import java.io.File;
import java.util.Calendar;

import org.testium.configuration.ConfigurationException;
import org.testium.configuration.KEYS;
import org.testium.executor.TestExecutionException;
import org.testium.executor.TestSuiteExecutor;
import org.testium.plugins.PluginCollection;
import org.testtoolinterfaces.testsuite.TestGroup;
import org.testtoolinterfaces.testsuiteinterface.TestGroupReader;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;

public class Testium
{
	private TestGroupReader myTestGroupReader;

	private TestSuiteExecutor	myTestSuiteExecutor;
	
	public Testium(PluginCollection aPlugins, RunTimeData aRtData ) throws ConfigurationException
	{
		myTestGroupReader = aPlugins.getTestGroupReader();
		myTestSuiteExecutor = aPlugins.getTestSuiteExecutor();
		
	}

	public TestGroup readTestGroup( File aTestGroup )
	{
		Trace.println(Trace.EXEC_PLUS, "validate( " + aTestGroup.getName() + " )", true);

		return myTestGroupReader.readTgFile(aTestGroup);
	}

	public void execute( TestGroup aTestGroup, File aBaseExecutionDir, RunTimeData aRtData ) throws TestExecutionException
	{
		Trace.println(Trace.EXEC, "execute( " + aTestGroup.getId() + " )", true);

    	// TODO Move date as start-date to run-time data
		Calendar date = Calendar.getInstance();
		File logDir = (File) aRtData.getValue(KEYS.RESULT_BASE_DIR.toString());
		myTestSuiteExecutor.execute( aTestGroup,
    	                             aBaseExecutionDir,
    	                             logDir,
    	                             date );
	}
}
