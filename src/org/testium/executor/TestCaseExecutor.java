package org.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestCaseResultLink;
import org.testtoolinterfaces.testsuite.TestCaseLink;

public interface TestCaseExecutor
{
	public TestCaseResultLink execute( TestCaseLink aTestCaseLink,
	                                   File aLogDir );

	public String getType();
}