package org.testium;

import java.io.File;
import java.util.Calendar;

import org.testium.configuration.Configuration;
import org.testium.configuration.ConfigurationException;
import org.testium.executor.TestExecutionException;
import org.testium.executor.TestSuiteExecutor;
import org.testium.plugins.PluginCollection;
import org.testtoolinterfaces.testsuite.TestGroup;
import org.testtoolinterfaces.testsuiteinterface.TestGroupReader;
import org.testtoolinterfaces.utils.Trace;

public class Testium
{
	private Configuration	myConfiguration;
	
	private TestGroupReader myTestGroupReader;

	private TestSuiteExecutor	myTestSuiteExecutor;
	
	public Testium(PluginCollection aPlugins, Configuration aConfig ) throws ConfigurationException
	{
		myTestGroupReader = aPlugins.getTestGroupReader();
		myTestSuiteExecutor = aPlugins.getTestSuiteExecutor();
		
		myConfiguration = aConfig;
	}

	public TestGroup readTestGroup( File aTestGroup )
	{
		Trace.println(Trace.EXEC_PLUS, "validate( " + aTestGroup.getName() + " )", true);

		return myTestGroupReader.readTgFile(aTestGroup);
	}

	public void execute( TestGroup aTestGroup, File aBaseExecutionDir ) throws TestExecutionException
	{
		Trace.println(Trace.EXEC, "execute( " + aTestGroup.getId() + " )", true);

    	// TODO Move date as start-date to run-time data
		Calendar date = Calendar.getInstance();
		File logDir = myConfiguration.getTestResultBaseDir();
    	myTestSuiteExecutor.execute( aTestGroup,
    	                             aBaseExecutionDir,
    	                             logDir,
    	                             date );
	}
}
