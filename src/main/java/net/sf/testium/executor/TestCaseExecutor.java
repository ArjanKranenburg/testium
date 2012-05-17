package net.sf.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestCaseResultLink;
import org.testtoolinterfaces.testsuite.TestCaseLink;
import org.testtoolinterfaces.utils.RunTimeData;

public interface TestCaseExecutor
{
	public TestCaseResultLink execute( TestCaseLink aTestCaseLink,
	                                   File aLogDir,
	                                   RunTimeData aRTData ) throws TestCaseLinkExecutionException;

	public String getType();
}