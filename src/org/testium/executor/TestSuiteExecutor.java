package org.testium.executor;

import java.io.File;

import org.testium.systemundertest.SutControl;
import org.testtoolinterfaces.testsuite.TestGroup;
import org.testtoolinterfaces.utils.RunTimeData;


public interface TestSuiteExecutor
{
	public void execute( TestGroup aTestGroup,
	                     String testGroupId,
						 File aScriptDir,
						 RunTimeData aRtData ) throws TestExecutionException;
	
	public void setSutControl( SutControl aSutControl );
	
	public void setTestGroupExecutor( TestGroupExecutor aTestGroupExecutor );
}