package net.sf.testium.executor;

import java.io.File;
import java.util.Iterator;

import org.testtoolinterfaces.testresult.TestResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testresult.TestStepResultList;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestStepCommand;
import org.testtoolinterfaces.testsuite.TestStepSequence;
import org.testtoolinterfaces.utils.RunTimeData;

public class TestStepSetExecutor
{
	TestStepMetaExecutor myTestStepExecutor;
	
	public TestStepSetExecutor(TestStepMetaExecutor testStepExecutor) {
		myTestStepExecutor = testStepExecutor;
	}

	public TestStepResult execute(TestStep aStep, File aScriptDir, File aLogDir)
	{
		TestStepCommand testStep = new TestStepCommand( aStep.getSequenceNr(),
		                                              "",
		                                              "set",
		                                              null, //TestInterface. TODO this will probably give an NPE
		                                              new ParameterArrayList() );
		TestStepResult result = new TestStepResult( testStep );
		result.setResult(TestResult.UNKNOWN);
		
		return result;
	}
	
	public void execute_alt( TestStepSequence steps,
			TestStepResultList stepResultSet,
            File aScriptDir,
            File aLogDir,
            RunTimeData aRTData ) {

		Iterator<TestStep> stepsItr = steps.iterator();
		while (stepsItr.hasNext()) {
			TestStep step = stepsItr.next();
			TestStepResult tsResult = myTestStepExecutor.execute(step,
					aScriptDir, aLogDir, aRTData);
			stepResultSet.add(tsResult);
		}
	}

	// Same, but breaks on error
	public void execute_fatal( TestStepSequence steps,
			TestStepResultList stepResultSet,
            File aScriptDir,
            File aLogDir,
            RunTimeData aRTData ) {

		Iterator<TestStep> stepsItr = steps.iterator();
		while (stepsItr.hasNext()) {
			TestStep step = stepsItr.next();
			TestStepResult tsResult = myTestStepExecutor.execute(step,
					aScriptDir, aLogDir, aRTData);
			stepResultSet.add(tsResult);

			if (tsResult.getResult().equals(VERDICT.ERROR)) {
				return;
			}
		}
	}
}
