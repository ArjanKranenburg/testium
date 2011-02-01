package org.testium.executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.util.ArrayList;
import java.util.Hashtable;

import org.testtoolinterfaces.testresult.TestCaseResult;
import org.testtoolinterfaces.testresult.TestCaseResultLink;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testresultinterface.TestCaseResultWriter;
import org.testtoolinterfaces.testsuite.TestCase;
import org.testtoolinterfaces.testsuite.TestCaseImpl;
import org.testtoolinterfaces.testsuite.TestCaseLink;
import org.testtoolinterfaces.testsuite.TestStepArrayList;
import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan
 *
 */
public abstract class TestCaseAbstractExecutor implements TestCaseExecutor
{
	private TestCaseResultWriter myTestCaseResultWriter;

	abstract public void executeScript( File anExecutable, File aLogFile, TestCaseResult aResult );

	/**
	 * @param myTestCaseResultWriter
	 */
	public TestCaseAbstractExecutor( TestCaseResultWriter aTestCaseResultWriter )
	{
		myTestCaseResultWriter = aTestCaseResultWriter;
	}

	abstract public String getType();

    /**
     * Executes the Test Case as a perl script on the Operating System
     * 
	 * @param aBaseLogDir the baseDir for log-files
     */
    public TestCaseResultLink execute( TestCaseLink aTestCaseLink,
                                       File aLogDir )
    {
    	String tcId = aTestCaseLink.getId();
		Trace.println(Trace.EXEC, "execute( "
						+ tcId + ", "
			            + aLogDir.getPath()
			            + " )", true );

		if ( !aLogDir.isDirectory() )
		{
			FileNotFoundException exc = new FileNotFoundException("Directory does not exist: " + aLogDir.getAbsolutePath());
			throw new IOError( exc );
		}

		String description = ""; // TODO try to get a description from the shell script
		ArrayList<String> requirements = new ArrayList<String>(); // TODO try to get a requirements from the shell script

		File caseLogDir = new File(aLogDir, tcId);
		caseLogDir.mkdir();
		File logFile = new File( caseLogDir, tcId + ".xml" );
		TestCase testCase = new TestCaseImpl( tcId,
		                                      new Hashtable<String, String>(),
		                                      description,
		                                      requirements,
		                                      new TestStepArrayList(),
		                                      new TestStepArrayList(),
		                                      new TestStepArrayList(),
		                                      new Hashtable<String, String>());
		TestCaseResult result = new TestCaseResult( testCase );
    	myTestCaseResultWriter.write( result, logFile );

		File executable = aTestCaseLink.getLink();
    	if ( ! executable.canExecute() )
    	{
    		result.setResult( VERDICT.ERROR );
    		result.addComment( "Script can not be found or is not executable: " + executable.getPath() );
    	}
    	else
    	{
    		executeScript(executable, logFile, result);
    	}
    	
		return new TestCaseResultLink( aTestCaseLink,
		                               result.getResult(),
		                               logFile );
    }
}
