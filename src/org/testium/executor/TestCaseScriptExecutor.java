package org.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestCaseResult;
import org.testtoolinterfaces.testsuite.TestCaseLink;


public interface TestCaseScriptExecutor
{
	TestCaseResult execute( TestCaseLink aTestCaseLink, File aScriptDir, File aLogDir );
	
	String getScriptType();
}