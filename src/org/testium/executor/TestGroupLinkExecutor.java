package org.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testsuite.TestGroupLink;


public interface TestGroupLinkExecutor
{
	TestGroupResult execute( TestGroupLink aTestGroupLink, File aScriptDir, File aLogDir );
}