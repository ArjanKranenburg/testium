package org.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testsuite.TestGroup;
import org.testtoolinterfaces.testsuite.TestGroupLink;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * @author Arjan
 *
 */
public interface TestGroupExecutor
{
	public void execute( TestGroupLink aTestGroupLink,
	                     File aLogDir,
	                     TestGroupResult aTestGroupResult ,
	                     RunTimeData aRTData );

	public void execute( TestGroup aTestGroup,
	                     File aScriptDir,
	                     File aLogDir,
	                     TestGroupResult aTestGroupResult,
	                     RunTimeData aRTData );

	public String getType();
}