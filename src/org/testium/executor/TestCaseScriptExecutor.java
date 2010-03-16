package org.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestCaseResult;
import org.testtoolinterfaces.testsuite.TestCaseLink;


public interface TestCaseScriptExecutor
{
	public void execute( TestCaseLink aTestCaseLink,
						 File aScriptDir,
						 File aLogDir,
						 TestCaseResult aResult );
	
	public String getScriptType();
}