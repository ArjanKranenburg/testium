package net.sf.testium.executor;

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
	                     TestGroupResult aTestGroupResult ,
	                     ExecutionEnvironment aParentEnv );

	public void execute( TestGroup aTestGroup,
	                     TestGroupResult aTestGroupResult,
	                     ExecutionEnvironment aParentEnv );

	@Deprecated
	public void execute( TestGroupLink aTestGroupLink,
            File aLogDir,
            TestGroupResult aTestGroupResult ,
            RunTimeData aRTData );

	@Deprecated
	public void execute( TestGroup aTestGroup,
            File aScriptDir,
            File aLogDir,
            TestGroupResult aTestGroupResult,
            RunTimeData aRTData );

	public String getType();
}