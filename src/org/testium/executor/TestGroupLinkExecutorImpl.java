package org.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testsuite.TestGroup;
import org.testtoolinterfaces.testsuite.TestGroupLink;
import org.testtoolinterfaces.testsuiteinterface.TestGroupReader;


public class TestGroupLinkExecutorImpl implements TestGroupLinkExecutor
{
	TestGroupReader myTestGroupReader;
	TestGroupExecutor myTestGroupExecutor;

	/**
	 * @param aTestGroupReader
	 * @param aTestGroupExecutor
	 */
	public TestGroupLinkExecutorImpl(TestGroupReader aTestGroupReader, TestGroupExecutor aTestGroupExecutor)
	{
		myTestGroupReader = aTestGroupReader;
		myTestGroupExecutor = aTestGroupExecutor;
	}

	public TestGroupResult execute(TestGroupLink aTestGroupLink, File aScriptDir, File aLogDir)
	{
		File script = new File( aScriptDir + File.separator + aTestGroupLink.getTestGroupScript().getExecutionScript() );
		TestGroup testGroup = myTestGroupReader.readTgFile(script);
		
		File newScriptDir = new File( script.getParent() );

//		String logSubDir = script.getName();
//		logSubDir = logSubDir.substring(0, logSubDir.lastIndexOf('.'));
		
//		File newLogDir = new File (aLogDir.getAbsoluteFile() + File.separator + logSubDir);
//		newLogDir.mkdir();
		
		return myTestGroupExecutor.execute(testGroup, newScriptDir, aLogDir);
	}
	
	public void setTestGroupReader(TestGroupReader aTestGroupReader)
	{
		myTestGroupReader = aTestGroupReader;
	}
	
}
