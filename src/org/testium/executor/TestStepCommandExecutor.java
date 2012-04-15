package org.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;


public interface TestStepCommandExecutor
{
	String getCommand();
	
	TestStepResult execute( TestStep aStep,
	                        RunTimeData aVariables,
	                        File aLogDir ) throws TestSuiteException;

	boolean verifyParameters(ParameterArrayList aParameters) throws TestSuiteException;
}