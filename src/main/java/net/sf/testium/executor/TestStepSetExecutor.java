package net.sf.testium.executor;

import java.io.File;
import java.util.Iterator;

import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testresult.TestStepResultBase;
import org.testtoolinterfaces.testresult.TestStepResultList;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestStepSequence;
import org.testtoolinterfaces.utils.RunTimeData;

public class TestStepSetExecutor
{
	TestStepMetaExecutor myTestStepExecutor;
	
	public TestStepSetExecutor(TestStepMetaExecutor testStepExecutor) {
		myTestStepExecutor = testStepExecutor;
	}

	public void execute( TestStepSequence steps,
			TestStepResultList stepResultSet,
            File aScriptDir,
            File aLogDir,
            RunTimeData aRTData ) {

		Iterator<TestStep> stepsItr = steps.iterator();
		while (stepsItr.hasNext()) {
			TestStep step = stepsItr.next();
			TestStepResultBase tsResult = myTestStepExecutor.execute(step,
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
			TestStepResultBase tsResult = myTestStepExecutor.execute(step,
					aScriptDir, aLogDir, aRTData);
			stepResultSet.add(tsResult);

			if (tsResult.getResult().equals(VERDICT.ERROR)) {
				return;
			}
		}
	}
}
