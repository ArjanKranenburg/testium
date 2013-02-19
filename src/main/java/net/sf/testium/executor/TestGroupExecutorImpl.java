package net.sf.testium.executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.util.ArrayList;
import java.util.Iterator;

import org.testtoolinterfaces.testresult.TestCaseResult;
import org.testtoolinterfaces.testresult.TestCaseResultLink;
import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testresult.TestGroupResultLink;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresultinterface.TestGroupResultWriter;
import org.testtoolinterfaces.testsuite.TestCase;
import org.testtoolinterfaces.testsuite.TestCaseImpl;
import org.testtoolinterfaces.testsuite.TestCaseLink;
import org.testtoolinterfaces.testsuite.TestGroup;
import org.testtoolinterfaces.testsuite.TestGroupEntry;
import org.testtoolinterfaces.testsuite.TestGroupEntryIteration;
import org.testtoolinterfaces.testsuite.TestGroupEntrySelection;
import org.testtoolinterfaces.testsuite.TestGroupEntrySequence;
import org.testtoolinterfaces.testsuite.TestGroupImpl;
import org.testtoolinterfaces.testsuite.TestGroupLink;
import org.testtoolinterfaces.testsuite.TestLinkImpl;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestStepSequence;
import org.testtoolinterfaces.testsuiteinterface.TestGroupReader;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.Trace.LEVEL;
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

	/**
	 * Executes the TestGroupLink
	 * The preparation must have been done
	 * 
	 * @param aTestGroupLink
	 * @param aParentTgResult
	 * @param aParentEnv
	 */
	public void execute( TestGroupLink aTestGroupLink,
            TestGroupResult aParentTgResult,
            ExecutionEnvironment aParentEnv ) {

		TestGroup testGroup = this.readTgLink(aTestGroupLink);
		ExecutionEnvironment execEnv = this.setExecutionEnvironment(aTestGroupLink,
				aParentEnv.getLogDir(), aParentTgResult.getExecutionIdPath(), aParentEnv.getRtData());
		
		File logFile = execEnv.getLogFile( testGroup.getId() );
		TestGroupResult tgResult = createTgResult(testGroup, execEnv, logFile);
		tgResult.setExecutionPath( aParentTgResult.getExecutionIdPath() );

    	TestGroupResultLink tgResultLink = new TestGroupResultLink( aTestGroupLink,
                logFile );
    	tgResult.register(tgResultLink);
    	aParentTgResult.addTestGroup(tgResultLink);
		
		execute( testGroup, tgResult, execEnv );
	}

	private ExecutionEnvironment setExecutionEnvironment( TestGroupLink aTestGroupLink,
	                     File aLogDir, String anExecutionPath, RunTimeData aRTData ) {
		if ( !aLogDir.isDirectory() )
		{
			FileNotFoundException exc = new FileNotFoundException("Directory does not exist: " + aLogDir.getPath());
			throw new IOError( exc );
		}
		Trace.println(Trace.EXEC, "setExecutionEnvironment( " 
				+ aTestGroupLink.getId() + ", "
	            + aLogDir.getPath() + ", "
	            + anExecutionPath + ", "
				+ aRTData.size() + " Variables )", true );

		File testGroupFile = aTestGroupLink.getLink();
		File scriptDir = testGroupFile.getParentFile();

		File groupLogDir = new File(aLogDir, aTestGroupLink.getId());
		groupLogDir.mkdir();

		return new ExecutionEnvironment(scriptDir, groupLogDir, aRTData );
	}

	private TestGroup readTgLink( TestGroupLink aTestGroupLink ) {
		
		File testGroupFile = aTestGroupLink.getLink();
		return myTestGroupReader.readTgFile(testGroupFile);
	}

	/**
	 * @param testGroup
	 * @param execEnv
	 * @param logFile
	 * @return testGrouResult
	 */
	private TestGroupResult createTgResult(TestGroup testGroup,
			ExecutionEnvironment execEnv, File logFile) {
		TestGroupResult tgResult = new TestGroupResult( testGroup );
    	myTestGroupResultWriter.write( tgResult, logFile );

    	return tgResult;
	}

	@Deprecated
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
		TestGroupResult tgResult = new TestGroupResult( testGroup );

		File groupLogDir = new File(aLogDir, testGroup.getId());
		groupLogDir.mkdir();

		File logFile = new File(groupLogDir, testGroup.getId() + "_log.xml");
		tgResult.setExecutionPath( aTestGroupResult.getExecutionPath() + "." + aTestGroupResult.getId() );
    	myTestGroupResultWriter.write( tgResult, logFile );

    	TestGroupResultLink tgResultLink = new TestGroupResultLink( aTestGroupLink,
    	                                                            logFile );
    	tgResult.register(tgResultLink);
    	aTestGroupResult.addTestGroup(tgResultLink);

		execute( testGroup, scriptDir, groupLogDir, tgResult, aRTData );
	}

	public void execute(TestGroup aTestGroup, TestGroupResult aTestGroupResult, ExecutionEnvironment anEnv) {
		Trace.println(Trace.EXEC, "execute( " + aTestGroup.getId() + " )", true);

		TestStepSequence prepareSteps = aTestGroup.getPrepareSteps();
		executePrepareSteps(prepareSteps, aTestGroupResult, anEnv);

		TestGroupEntrySequence execSteps = aTestGroup.getExecutionEntries();
		executeExecSteps(execSteps, aTestGroupResult, anEnv.getScriptDir(), 
				anEnv.getLogDir(), anEnv.getRtData());

		TestStepSequence restoreSteps = aTestGroup.getRestoreSteps();
		executeRestoreSteps(restoreSteps, aTestGroupResult, anEnv);
	}

	@Deprecated
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

    	TestStepSequence prepareSteps = aTestGroup.getPrepareSteps();
    	executePrepareSteps(prepareSteps, aTestGroupResult, aScriptDir, aLogDir, aRTData);

    	TestGroupEntrySequence execSteps = aTestGroup.getExecutionEntries();
		executeExecSteps(execSteps, aTestGroupResult, aScriptDir, aLogDir, aRTData);

    	TestStepSequence restoreSteps = aTestGroup.getRestoreSteps();
    	executeRestoreSteps(restoreSteps, aTestGroupResult, aScriptDir, aLogDir, aRTData);
	}

	public void executePrepareSteps(TestStepSequence aPrepareSteps,
			TestGroupResult aResult, ExecutionEnvironment anEnv) {
		Trace.println(Trace.EXEC_PLUS, "executePrepareSteps( " + aPrepareSteps
				+ ", " + aResult + " )", true);

		Iterator<TestStep> stepsItr = aPrepareSteps.iterator();
		while (stepsItr.hasNext()) {
			TestStep step = stepsItr.next();
			TestStepResult tsResult = myTestStepExecutor.execute(step,
					anEnv.getScriptDir(), anEnv.getLogDir(), anEnv.getRtData());
			tsResult.setExecutionPath(aResult.getExecutionPath() + "."
					+ aResult.getId());
			aResult.addInitialization(tsResult);
		}
	}

	@Deprecated
	public void executePrepareSteps(TestStepSequence aPrepareSteps,
			TestGroupResult aResult, File aScriptDir, File aLogDir,
			RunTimeData aRTData) {
		Trace.println(Trace.EXEC_PLUS, "executePrepareSteps( " + aPrepareSteps
				+ ", " + aResult + ", " + aLogDir.getPath() + " )", true);

		Iterator<TestStep> stepsItr = aPrepareSteps.iterator();
		while (stepsItr.hasNext()) {
			TestStep step = stepsItr.next();
			TestStepResult tsResult = myTestStepExecutor.execute(step,
					aScriptDir, aLogDir, aRTData);
			tsResult.setExecutionPath(aResult.getExecutionPath() + "."
					+ aResult.getId());
			aResult.addInitialization(tsResult);
		}
	}
	
	public void executeExecSteps( TestGroupEntrySequence anExecEntries,
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

		Iterator<TestGroupEntry> entriesItr = anExecEntries.iterator();
		while(entriesItr.hasNext())
		{
			TestGroupEntry entry = entriesItr.next();
			RunTimeData rtData = new RunTimeData( aRTData );
			try
			{
				if (entry instanceof TestGroupLink) {
					executeTestGroupLink((TestGroupLink) entry, aResult,
							aScriptDir, aLogDir, rtData);
				} else if (entry instanceof TestCaseLink) {
					executeTestCaseLink((TestCaseLink) entry, aResult,
							aScriptDir, aLogDir, rtData);
				} else if (entry instanceof TestGroupEntryIteration) {
					executeForeach( (TestGroupEntryIteration) entry, aResult,
							aScriptDir, aLogDir, rtData);
				} else if (entry instanceof TestGroupEntrySelection) {
					executeSelection( (TestGroupEntrySelection) entry, aResult,
							aScriptDir, aLogDir, aRTData);
				} else {
					throw new InvalidTestTypeException( entry.getClass().getSimpleName(),
							"Cannot execute execution entries of type " + entry.getClass().getSimpleName() );
				}
			}
			catch (Throwable t) // wider than strictly needed, but plugins can be mal-implemented.
			{
				String message = "Execution of " + entry.getClass().getSimpleName() + " " + entry.getId() + " failed:\n"
					+ t.getMessage()
					+ "\nTrying to continue, but this may affect further execution...";
				aResult.addComment(message);
				Warning.println(message);
				Trace.print(Trace.ALL, t);
			}
    	}
	}

	/**
	 * @param aResult
	 * @param aScriptDir
	 * @param aLogDir
	 * @param tgLink
	 * @param rtData
	 */
	private void executeTestGroupLink(TestGroupLink tgLink, TestGroupResult aResult,
			File aScriptDir, File aLogDir, RunTimeData rtData) {
		try
		{
			String fileName = tgLink.getLink().getPath();
			String updatedFileName = rtData.substituteVars(fileName);
			if ( ! fileName.equals(updatedFileName) ) {
				TestLinkImpl newLink = new TestLinkImpl( updatedFileName, tgLink.getLinkType() );
				tgLink.setLink(newLink);
			} else {
				tgLink.setLinkDir( aScriptDir );
			}

			myTestGroupExecutor.execute(tgLink, aLogDir, aResult, rtData );
			
		}
		catch (Throwable t) // wider than strictly needed, but plugins could be mal-interpreted.
		{
			Trace.print(LEVEL.ALL, t);
			String tgId = tgLink.getId();
			
			TestGroup tg = new TestGroupImpl( tgId,
											  tgLink.getDescription(),
											  tgLink.getSequenceNr(),
											  new TestStepSequence(),
											  new TestGroupEntrySequence(),
											  new TestStepSequence() );
			
			TestGroupResult tgResult = new TestGroupResult( tg );
			tgResult.setResult(VERDICT.ERROR);
			tgResult.addComment(t.getMessage());
			
			File logDir = new File (aLogDir, tgId );
			logDir.mkdir();
			
			File logFile = new File( logDir, tgId + "_log.xml" );
			myTestGroupResultWriter.write( tgResult, logFile );

			TestGroupResultLink tgResultLink = new TestGroupResultLink( tgLink, logFile );
			tgResultLink.setSummary( tgResult.getSummary() );
			tgResult.register(tgResultLink);
			aResult.addTestGroup(tgResultLink);
		}
	}

	/**
	 * @param aResult
	 * @param aScriptDir
	 * @param aLogDir
	 * @param entry
	 * @param rtData
	 * @throws Error
	 */
	private void executeTestCaseLink(TestCaseLink tcLink, TestGroupResult aResult, File aScriptDir,
			File aLogDir, RunTimeData rtData)
			throws Error {
		if ( myTestCaseExecutor == null )
		{
			throw new Error("No Executor is defined for TestCaseLinks");
		}
		
		try
		{
			String fileName = tcLink.getLink().getPath();
			String updatedFileName = rtData.substituteVars(fileName);
			if ( ! fileName.equals(updatedFileName) ) {
				TestLinkImpl newLink = new TestLinkImpl( updatedFileName, tcLink.getLinkType() );
				tcLink.setLink(newLink);
			} else {
				tcLink.setLinkDir( aScriptDir );
			}

			TestCaseResultLink tcResult = myTestCaseExecutor.execute(tcLink, aLogDir, rtData);
			tcResult.setExecutionPath( aResult.getExecutionPath() + "." + aResult.getId() );
			aResult.addTestCase(tcResult);
		}
		catch (Throwable t)
		{
			Trace.print(LEVEL.ALL, t);
			String tcId = tcLink.getId();
			
			TestCase tc = new TestCaseImpl( tcId,
					  tcLink.getDescription(),
					  tcLink.getSequenceNr(),
					  new TestStepSequence(),
					  new TestStepSequence(),
					  new TestStepSequence() );

			TestCaseResult tcResult = new TestCaseResult( tc );
			tcResult.setResult(VERDICT.ERROR);
			tcResult.addComment(t.getMessage());
			
//						File logDir = new File (aLogDir, tcId );
//						logDir.mkdir();
//						
//						File logFile = new File( logDir, tcId + "_log.xml" );
//				    	myTestCaseResultWriter.write( tcResult, logFile );
//
//				    	TestCaseResultLink tcResultLink = new TestCaseResultLink( tcLink, VERDICT.ERROR, logFile );
//						aResult.addTestCase(tcResultLink);
		}
	}

	/**
	 * @param entry
	 * @param aResult
	 * @param aScriptDir
	 * @param aLogDir
	 * @param aRTData
	 * @param rtData
	 */
	private void executeForeach(TestGroupEntryIteration entryIteration, TestGroupResult aResult,
			File aScriptDir, File aLogDir, RunTimeData rtData) {
		String listName = entryIteration.getListName();
		String listElement = entryIteration.getItemName();
		@SuppressWarnings("unchecked")
		ArrayList<Object> list = rtData.getValueAs(ArrayList.class, listName);

		TestGroupEntrySequence doSteps = new TestGroupEntrySequence( entryIteration.getSequence() );

		Iterator<Object> listItr = list.iterator();
		int seqNr = entryIteration.getSequenceNr();
		int foreachSeqNr = 0;
		while (listItr.hasNext() ) {
			Object element = listItr.next();
			String tgId = entryIteration.getId() + "_" + foreachSeqNr++;

			TestGroup tg = new TestGroupImpl(tgId, seqNr,
					new TestStepSequence(), doSteps, new TestStepSequence());

			RunTimeVariable rtVariable = new RunTimeVariable( listElement, element );

			RunTimeData subRtData = new RunTimeData( rtData );
			subRtData.add(rtVariable);

			TestGroupResultLink groupResultLink = 
					executeTestGroupEntries(tg, aScriptDir, aLogDir, subRtData);
			aResult.addTestGroup(groupResultLink);
		
		}
	}

	private void executeSelection( TestGroupEntrySelection selectionEntry,
			TestGroupResult aResult, File aScriptDir, File aLogDir, RunTimeData aRTData ) {
		String tgId = selectionEntry.getId();

		TestStep ifStep = selectionEntry.getIfStep();
		TestStepResult ifResult = myTestStepExecutor.execute(ifStep, aScriptDir, aLogDir, aRTData);

		TestStepSequence prepSteps = new TestStepSequence();
		prepSteps.add(ifStep);

		boolean negator = selectionEntry.getNegator();

		TestGroupEntrySequence execEntries = new TestGroupEntrySequence();

//		String comment = "If-step evaluated to " + ifResult.getResult() + ".";
		if ( ifResult.getResult().equals(VERDICT.ERROR)
			 || ifResult.getResult().equals(VERDICT.UNKNOWN) ) {
			 // NOP, we don't execute the then or else!
			
		} else {
			
			if ( ifResult.getResult().equals( negator ? VERDICT.FAILED : VERDICT.PASSED) ) {
				execEntries = selectionEntry.getThenSteps();
//				comment += " Then-sequence executed.";

			} else {
				execEntries = selectionEntry.getElseSteps();
//				comment += execEntries.isEmpty() ? " Nothing executed." : " Else-sequence executed.";
			}
		}

		TestGroup tg = new TestGroupImpl(tgId, selectionEntry.getSequenceNr(),
				prepSteps, execEntries, new TestStepSequence());

// TODO Now the if-step is lost. Make it a sub test group. Either special or with the if-step in prepare.
//		TestGroupResultLink groupResultLink = 
//				executeTestGroupEntries(tg, aScriptDir, aLogDir, aRTData);
//		aResult.addTestGroup(groupResultLink);

		execute(tg, aScriptDir, aLogDir, aResult, aRTData);

//		TestGroupResult result = new TestGroupResult(tg);
//		
//		aResult.addTestGroup(result);
//
//		ifResult.setExecutionPath( result.getExecutionPath() + "." + result.getId() );
//		result.addInitialization(ifResult);
//
//		this.executeExecSteps(execEntries, result, aScriptDir, aLogDir, aRTData);
//		result.setComment(comment);
	}

	/**
	 * @param doSteps
	 * @param groupResult
	 * @param seqNr
	 * @param aScriptDir
	 * @param aLogDir
	 * @param aParentResult
	 * @param subRtData
	 */
	private TestGroupResultLink executeTestGroupEntries(TestGroup tg, File aScriptDir, File aLogDir, RunTimeData subRtData) {
		
		String tgId = tg.getId();
		int seqNr = tg.getSequenceNr();
		TestGroupEntrySequence doSteps = tg.getExecutionEntries();

		TestGroupResult groupResult = new TestGroupResult( tg );
		
		File logDir = new File (aLogDir, tgId );
		logDir.mkdir();

		this.executeExecSteps(doSteps, groupResult, aScriptDir, logDir, subRtData);

		TestGroupResultLink tgResultLink = writeTestGroup(groupResult, seqNr, logDir);
		
		return tgResultLink;
	}

	/**
	 * @param groupResult
	 * @param tgId
	 * @param seqNr
	 * @param logDir
	 * @return
	 */
	private TestGroupResultLink writeTestGroup(TestGroupResult groupResult,	int seqNr, File logDir) {
		
		String tgId = groupResult.getId();
		File logFile = new File( logDir, tgId + "_log.xml" );
		myTestGroupResultWriter.write( groupResult, logFile );
		
		TestGroupLink tgLink = new TestGroupLink(tgId, seqNr, logFile.getName());

		TestGroupResultLink tgResultLink = new TestGroupResultLink( tgLink, logFile );
		tgResultLink.setSummary( groupResult.getSummary() );
		groupResult.register(tgResultLink);
		return tgResultLink;
	}

	public void executeRestoreSteps(TestStepSequence aRestoreSteps,
			TestGroupResult aResult, ExecutionEnvironment anEnv ) {
		Trace.println(Trace.EXEC_PLUS, "executeRestoreSteps( " + aRestoreSteps
				+ ", " + aResult + " )", true);

		Iterator<TestStep> stepsItr = aRestoreSteps.iterator();
		while (stepsItr.hasNext()) {
			TestStep step = stepsItr.next();
			TestStepResult tsResult = myTestStepExecutor.execute(step,
					anEnv.getScriptDir(), anEnv.getLogDir(), anEnv.getRtData());
			tsResult.setExecutionPath(aResult.getExecutionPath() + "."
					+ aResult.getId());
			aResult.addRestore(tsResult);
		}
	}
	
	@Deprecated
	public void executeRestoreSteps(TestStepSequence aRestoreSteps,
			TestGroupResult aResult, File aScriptDir, File aLogDir,
			RunTimeData aRTData) {
		Trace.println(Trace.EXEC_PLUS, "executeRestoreSteps( " + aRestoreSteps
				+ ", " + aResult + ", " + aLogDir.getPath() + " )", true);

		Iterator<TestStep> stepsItr = aRestoreSteps.iterator();
		while (stepsItr.hasNext()) {
			TestStep step = stepsItr.next();
			TestStepResult tsResult = myTestStepExecutor.execute(step,
					aScriptDir, aLogDir, aRTData);
			tsResult.setExecutionPath(aResult.getExecutionPath() + "."
					+ aResult.getId());
			aResult.addRestore(tsResult);
		}
	}

	public String getType()
	{
		return TYPE;
	}
}
