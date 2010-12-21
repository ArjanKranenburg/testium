package org.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.TestStepSimple;


public interface TestStepCommandExecutor
{
	String getCommand();
	
	TestStepResult execute( TestStepSimple aStep, File aScriptDir, File aLogDir );
}