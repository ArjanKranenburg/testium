package org.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestStepCommand;

public class TestStepSetExecutor
{
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
}
