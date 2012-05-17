package net.sf.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.TestStepScript;


public interface TestStepScriptExecutor
{
	/**
	 * @return the script type for which this executor is used
	 */
	String getType();
	
	TestStepResult execute( TestStepScript aStep, File aScriptDir, File aLogDir );
}