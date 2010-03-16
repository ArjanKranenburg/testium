package org.testium.executor;

import java.io.File;

import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testresultinterface.TestRunResultWriter;
import org.testtoolinterfaces.testsuite.TestGroup;
import org.testtoolinterfaces.testsuite.TestGroupLink;
import org.testtoolinterfaces.testsuiteinterface.TestGroupReader;


public class TestGroupLinkExecutorImpl implements TestGroupLinkExecutor
{
	private TestGroupReader myTestGroupReader;
	private TestGroupExecutor myTestGroupExecutor;
	private TestRunResultWriter myTestRunResultWriter;

	/**
	 * @param aTestGroupReader
	 * @param aTestGroupExecutor
	 * @param aTestRunResultWriter 
	 */
	public TestGroupLinkExecutorImpl( TestGroupReader aTestGroupReader,
									  TestGroupExecutor aTestGroupExecutor,
									  TestRunResultWriter aTestRunResultWriter )
	{
		myTestGroupReader = aTestGroupReader;
		myTestGroupExecutor = aTestGroupExecutor;
		myTestRunResultWriter = aTestRunResultWriter;
	}

	@Override
	public void execute( TestGroupLink aTestGroupLink,
						 File aScriptDir,
						 File aLogDir,
						 TestGroupResult aResult )
	{
		File script = new File( aScriptDir + File.separator + aTestGroupLink.getTestGroupScript().getExecutionScript() );
		TestGroup testGroup = myTestGroupReader.readTgFile(script);
		
		File newScriptDir = new File( script.getParent() );

//		String logSubDir = script.getName();
//		logSubDir = logSubDir.substring(0, logSubDir.lastIndexOf('.'));
		
//		File newLogDir = new File (aLogDir.getAbsoluteFile() + File.separator + logSubDir);
//		newLogDir.mkdir();
		
		myTestGroupExecutor.execute(testGroup, newScriptDir, aLogDir, aResult);
		myTestRunResultWriter.intermediateWrite();
	}
	
	public void setTestGroupReader(TestGroupReader aTestGroupReader)
	{
		myTestGroupReader = aTestGroupReader;
	}
}
