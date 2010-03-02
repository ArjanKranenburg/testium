package org.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testsuite.TestGroup;


public interface TestGroupExecutor
{
	TestGroupResult execute( TestGroup aTestGroup, File aScriptDir, File aLogDir );
	
	public void setTestCaseExecutor( TestCaseExecutor aTestCaseExecutor );
	
	public void setTestGroupLinkExecutor ( TestGroupLinkExecutor aTestGroupLinkExecutor );
}