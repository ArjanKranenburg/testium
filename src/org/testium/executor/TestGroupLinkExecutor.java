package org.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testsuite.TestGroupLink;


public interface TestGroupLinkExecutor
{
	public void execute( TestGroupLink aTestGroupLink,
						 File aScriptDir,
						 File aLogDir,
						 TestGroupResult aResult );

	public void execute( TestGroupLink aTestGroupLink,
	                     String aTestGroupId,
						 File aScriptDir,
						 File aLogDir,
						 TestGroupResult aResult );
}