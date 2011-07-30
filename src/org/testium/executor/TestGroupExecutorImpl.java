package org.testium.executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;

import org.testtoolinterfaces.testresult.TestCaseResultLink;
import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testresult.TestGroupResultLink;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresultinterface.TestGroupResultWriter;
import org.testtoolinterfaces.testsuite.TestCaseLink;
import org.testtoolinterfaces.testsuite.TestEntry;
import org.testtoolinterfaces.testsuite.TestEntryArrayList;
import org.testtoolinterfaces.testsuite.TestGroup;
import org.testtoolinterfaces.testsuite.TestGroupLink;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestStepArrayList;
import org.testtoolinterfaces.testsuiteinterface.TestGroupReader;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.Warning;

public class TestGroupExecutorImpl implements TestGroupExecutor
{
	private static final String		TYPE = "tti";

	private TestStepMetaExecutor	myTestStepExecutor;
	private TestGroupResultWriter 	myTestGroupResultWriter;

	private TestGroupReader			myTestGroupReader;
	private TestGroupMetaExecutor	myTestGroupExecutor;
	private TestCaseMetaExecutor	myTestCaseExecutor;

	/**
	 * @param aTestRunResultWriter 
	 * @param myTestStepExecutor
	 * @param myTestCaseScriptExecutor
	 */
	public TestGroupExecutorImpl( TestStepMetaExecutor aTestStepMetaExecutor,
	                              TestCaseMetaExecutor aTestCaseMetaExecutor,
	                              TestGroupMetaExecutor aTestGroupMetaExecutor,
	                              TestGroupResultWriter aTestGroupResultWriter )
	{
		myTestStepExecutor = aTestStepMetaExecutor;
		myTestCaseExecutor = aTestCaseMetaExecutor;
		myTestGroupExecutor = aTestGroupMetaExecutor;
		myTestGroupResultWriter = aTestGroupResultWriter;

		myTestGroupReader = new TestGroupReader( myTestStepExecutor.getInterfaces(), true );
	}

	@Override
	public void execute( TestGroupLink aTestGroupLink,
	                     File aLogDir,
	                     TestGroupResult aTestGroupResult,
	                     RunTimeData aRTData )
	{
		if ( !aLogDir.isDirectory() )
		{
			FileNotFoundException exc = new FileNotFoundException("Directory does not exist: " + aLogDir.getPath());
			throw new IOError( exc );
		}
		Trace.println(Trace.EXEC, "execute( " 
				+ aTestGroupLink.getId() + ", "
	            + aLogDir.getPath() + ", "
	            + aTestGroupResult.getId() + ", "
				+ aRTData.size() + " Variables )", true );

		File testGroupFile = aTestGroupLink.getLink();
		File scriptDir = testGroupFile.getParentFile();
		TestGroup testGroup = myTestGroupReader.readTgFile(testGroupFile);

		File groupLogDir = new File(aLogDir, aTestGroupLink.getId());
		groupLogDir.mkdir();

		File logFile = new File(groupLogDir, aTestGroupLink.getId() + "_log.xml");
		TestGroupResult tgResult = new TestGroupResult( testGroup );
    	myTestGroupResultWriter.write( tgResult, logFile );

    	TestGroupResultLink tgResultLink = new TestGroupResultLink( aTestGroupLink,
    	                                                            logFile );
    	tgResult.register(tgResultLink);
    	aTestGroupResult.addTestGroup(tgResultLink);

		execute( testGroup, scriptDir, groupLogDir, tgResult, aRTData );
	}

	@Override
	public void execute( TestGroup aTestGroup,
	                     File aScriptDir,
	                     File aLogDir,
	                     TestGroupResult aTestGroupResult,
	                     RunTimeData aRTData )
	{
		Trace.println( Trace.EXEC, "execute( " 
						+ aTestGroup.getId() + ", "
						+ aScriptDir.getPath() + ", "
						+ aLogDir.getPath() + ", "
						+ aTestGroupResult.getId() + ", "
						+ aRTData.size() + " Variables )", true );

    	TestStepArrayList prepareSteps = aTestGroup.getPrepareSteps();
    	executePrepareSteps(prepareSteps, aTestGroupResult, aScriptDir, aLogDir, aRTData);

    	TestEntryArrayList execSteps = aTestGroup.getExecutionEntries();
		executeExecSteps(execSteps, aTestGroupResult, aScriptDir, aLogDir, aRTData);

    	TestStepArrayList restoreSteps = aTestGroup.getRestoreSteps();
    	executeRestoreSteps(restoreSteps, aTestGroupResult, aScriptDir, aLogDir, aRTData);
	}

	public void executePrepareSteps( TestStepArrayList aPrepareSteps,
	                                 TestGroupResult aResult,
	                                 File aScriptDir,
	                                 File aLogDir,
	                                 RunTimeData aRTData )
	{
		Trace.println(Trace.EXEC_PLUS, "executePrepareSteps( " 
						+ aPrepareSteps + ", "
						+ aResult + ", "
			            + aLogDir.getPath()
			            + " )", true );

		for (int key = 0; key < aPrepareSteps.size(); key++)
    	{
    		TestStep step = aPrepareSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir, aRTData);
			aResult.addInitialization(tsResult);
    	}
	}

	public void executeExecSteps( TestEntryArrayList anExecEntries,
	                              TestGroupResult aResult,
	                              File aScriptDir,
	                              File aLogDir,
	                              RunTimeData aRTData )
	{
		Trace.println(Trace.EXEC_PLUS, "executeExecSteps( " 
						+ anExecEntries + ", "
						+ aResult + ", "
			            + aScriptDir.getPath() + ", "
			            + aLogDir.getPath() + ", "
						+ aRTData.size() + " Variables )", true );

		for (int key = 0; key < anExecEntries.size(); key++)
    	{
			TestEntry entry = anExecEntries.get(key);
			RunTimeData rtData = new RunTimeData( aRTData );
			try
			{
				if ( entry.getType() == TestEntry.TYPE.GroupLink )
				{
					TestGroupLink tgLink = (TestGroupLink) entry;
					tgLink.setLinkDir( aScriptDir );

					myTestGroupExecutor.execute(tgLink, aLogDir, aResult, rtData );
				}
				else if ( entry.getType() == TestEntry.TYPE.CaseLink )
				{
					if ( myTestCaseExecutor == null )
					{
						throw new Error("No Executor is defined for TestCaseLinks");
					}
					
					TestCaseLink tcLink = (TestCaseLink) entry;
					tcLink.setLinkDir( aScriptDir );

					TestCaseResultLink tcResult = myTestCaseExecutor.execute(tcLink, aLogDir, rtData);
					aResult.addTestCase(tcResult);
				}
				else
				{
					throw new InvalidTestTypeException( entry.getType().toString(), "Cannot execute execution entries of type " + entry.getType() );
				}
			}
			catch (InvalidTestTypeException e)
			{
				String message = "Execution of " + entry.getType() + " " + entry.getId() + " failed:\n"
					+ e.getMessage()
					+ "\nTrying to continue, but this may affect further execution...";
				aResult.addComment(message);
				Warning.println(message);
				Trace.print(Trace.ALL, e);
			}
    	}
	}

	public void executeRestoreSteps( TestStepArrayList aRestoreSteps,
	                                 TestGroupResult aResult,
	                                 File aScriptDir,
	                                 File aLogDir,
	                                 RunTimeData aRTData )
	{
		Trace.println(Trace.EXEC_PLUS, "executeRestoreSteps( " 
						+ aRestoreSteps + ", "
						+ aResult + ", "
			            + aLogDir.getPath()
			            + " )", true );

		for (int key = 0; key < aRestoreSteps.size(); key++)
    	{
    		TestStep step = aRestoreSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir, aRTData);
			aResult.addRestore(tsResult);
    	}
	}
	
	@Override
	public String getType()
	{
		return TYPE;
	}
}
