package org.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestCaseResult;
import org.testtoolinterfaces.testsuite.TestCase;


public interface TestCaseExecutor
{
	TestCaseResult execute( TestCase aTestCase, File aScriptDir, File aLogDir );
}