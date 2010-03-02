package org.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.TestStep;


public interface TestStepExecutor
{
	String getCommand();
	
	TestStepResult execute( TestStep aStep, File aScriptDir, File aLogDir );
}