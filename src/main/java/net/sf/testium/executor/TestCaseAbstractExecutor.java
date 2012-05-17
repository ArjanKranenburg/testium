package net.sf.testium.executor;

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
import org.testtoolinterfaces.testsuite.TestStepSequence;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan
 *
 */
public abstract class TestCaseAbstractExecutor implements TestCaseExecutor
{
	public enum LOGTYPE
	{
		/** The log is written as log, i.e. text only */						LOGONLY,
		/** The log is written in XML using the TTI standard */					TTI,
		/** No log */ 															NONE;
		
		public String toString()
		{
			return super.toString().toLowerCase();
		}
	};

	private TestCaseResultWriter myTestCaseResultWriter;
	private LOGTYPE myLogType;

	abstract public void executeScript( File anExecutable, File aLogFile, TestCaseResult aResult );

	/**
	 * @param myTestCaseResultWriter
	 * @param aLogType 
	 */
	public TestCaseAbstractExecutor( TestCaseResultWriter aTestCaseResultWriter, LOGTYPE aLogType )
	{
		myTestCaseResultWriter = aTestCaseResultWriter;
		myLogType = aLogType;
	}

	abstract public String getType();

    /**
     * Executes the Test Case as a perl script on the Operating System
     * 
	 * @param aBaseLogDir the baseDir for log-files
	 * @param aLogDir
	 * @param anRTData
     */
    public TestCaseResultLink execute( TestCaseLink aTestCaseLink,
                                       File aLogDir,
                                       RunTimeData anRTData )
    {
    	String tcId = aTestCaseLink.getId();
		Trace.println(Trace.EXEC, "execute( "
						+ tcId + ", "
			            + aLogDir.getPath() + ", "
			            + anRTData.size() + " Variables )", true );

		if ( !aLogDir.isDirectory() )
		{
			FileNotFoundException exc = new FileNotFoundException("Directory does not exist: " + aLogDir.getAbsolutePath());
			throw new IOError( exc );
		}

		String description = ""; // TODO try to get a description from the shell script
		ArrayList<String> requirements = new ArrayList<String>(); // TODO try to get a requirements from the shell script

		File caseLogDir = new File(aLogDir, tcId);
		caseLogDir.mkdir();
		File resultFile = new File( caseLogDir, tcId + ".xml" );

		TestCase testCase = new TestCaseImpl( tcId,
		                                      description,
		                                      0,
		                                      requirements,
		                                      new TestStepSequence(),
		                                      new TestStepSequence(),
		                                      new TestStepSequence(),
		                                      new Hashtable<String, String>(),
		                                      new Hashtable<String, String>());
		TestCaseResult result = new TestCaseResult( testCase );
    	myTestCaseResultWriter.write( result, resultFile );

		File executable = aTestCaseLink.getLink();
    	if ( ! executable.canExecute() )
    	{
    		result.setResult( VERDICT.ERROR );
    		result.addComment( "Script can not be found or is not executable: " + executable.getPath() );
    	}
    	else
    	{
    		if ( myLogType.equals(LOGTYPE.LOGONLY) )
    		{
    			File logFile = new File( caseLogDir, tcId + "_run.log" );
        		executeScript(executable, logFile, result);
        		result.addTestLog("log", logFile.getPath());
    		}
    		else // LOGTYPE.NONE or LOGTYPE.TTI
    		{
        		executeScript(executable, resultFile, result);
    		}
    	}
    	
		return new TestCaseResultLink( aTestCaseLink,
		                               result.getResult(),
		                               resultFile );
    }
}
