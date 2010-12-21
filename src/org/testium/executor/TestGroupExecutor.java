package org.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testresult.TestGroupResultLink;
import org.testtoolinterfaces.testsuite.TestGroup;
import org.testtoolinterfaces.testsuite.TestGroupLink;

public interface TestGroupExecutor
{
	public TestGroupResultLink execute( TestGroupLink aTestGroupLink,
	                                    File aLogDir );

	public TestGroupResult execute( TestGroup aTestGroup,
	                                File aScriptDir,
	                                File aLogFile );

	public String getType();
}