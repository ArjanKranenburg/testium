package org.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testsuite.TestGroup;
import org.testtoolinterfaces.testsuite.TestGroupLink;

/**
 * @author Arjan
 *
 */
public interface TestGroupExecutor
{
	public void execute( TestGroupLink aTestGroupLink,
	                     File aLogDir,
	                     TestGroupResult aResult );

	public void execute( TestGroup aTestGroup,
	                     File aScriptDir,
	                     File aLogDir,
	                     TestGroupResult aTestGroupResult );

	public String getType();
}