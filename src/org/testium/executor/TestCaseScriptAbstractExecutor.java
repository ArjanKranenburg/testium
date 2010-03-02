package org.testium.executor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.testtoolinterfaces.testresult.TestCaseResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresultinterface.TestCaseResultXmlHandler;
import org.testtoolinterfaces.testsuite.TestCaseLink;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestStepArrayList;
import org.testtoolinterfaces.utils.Trace;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;


abstract public class TestCaseScriptAbstractExecutor implements TestCaseScriptExecutor
{
	public enum LOGTYPE { logonly,  // only log the output
						  output,   // log the output and print it to stdout
						  xml };    // Read the results, since it is an xml file
	
	private TestStepExecutor		myTestStepExecutor;

	abstract public TestCaseResult execute(TestCaseLink aTestCaseLink, File aScriptDir, File aLogDir);
	abstract public String getScriptType();

	/**
	 * @param myTestStepExecutor
	 */
	public TestCaseScriptAbstractExecutor(TestStepExecutor aTestStepExecutor)
	{
		myTestStepExecutor = aTestStepExecutor;
	}

	public void executeInitSteps(TestStepArrayList anInitSteps, TestCaseResult aResult, File aScriptDir, File aLogDir)
	{
		for (int key = 0; key < anInitSteps.size(); key++)
    	{
    		TestStep step = anInitSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir);
			aResult.addInitialization(tsResult);

//			String message = "Initialization Step " + step.getId() + " failed:\n"
//					+ e.getMessage()
//					+ "\nTrying to continue, but this will probably affect further execution...";
//				aResult.addComment(message);
//				Warning.println(message);
//				Trace.printException(Trace.LEVEL.ALL, e);
    	}
	}

	public void executeExecSteps( TestStepArrayList anExecSteps,
	                              TestCaseResult aResult,
	                              File aScriptDir,
	                              File aLogDir )
	{
		for (int key = 0; key < anExecSteps.size(); key++)
    	{
			TestStep step = anExecSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir);
			aResult.addExecution(tsResult);
    	}
	}

	public void executeRestoreSteps(TestStepArrayList aRestoreSteps, TestCaseResult aResult, File aScriptDir, File aLogDir)
	{
		for (int key = 0; key < aRestoreSteps.size(); key++)
    	{
    		TestStep step = aRestoreSteps.get(key);
			TestStepResult tsResult = myTestStepExecutor.execute(step, aScriptDir, aLogDir);
			aResult.addRestore(tsResult);

//			String message = "Restore Step " + step.getId() + " failed:\n"
//					+ e.getMessage()
//					+ "\nTrying to continue, but this will probably affect further execution...";
//				aResult.addComment(message);
//				Warning.println(message);
//				Trace.printException(Trace.LEVEL.ALL, e);
    	}
	}

	protected TestCaseResult readResults(String aResultsFileName)
	{
	    Trace.println(Trace.LEVEL.UTIL, "readResults( " + aResultsFileName + " )", true);

	    SAXParser saxParser;
	    SAXParserFactory spf = SAXParserFactory.newInstance();
	    spf.setNamespaceAware(false);
	
	    TestCaseResultXmlHandler handler = null;
	    try
	    {
		    saxParser = spf.newSAXParser();
		    XMLReader xmlReader = saxParser.getXMLReader();
	
		    handler = new TestCaseResultXmlHandler(xmlReader);
	
		    xmlReader.setContentHandler(handler);
	
		    xmlReader.parse(aResultsFileName);
	    }
	    catch (ParserConfigurationException e)
	    {
	    	e.printStackTrace();
	    }
	    catch (SAXException e)
	    {
	    	e.printStackTrace();
	    }
	    catch (IOException e)
	    {
	    	e.printStackTrace();
	    }
	    
	    TestCaseResult result;
	    try
	    {
			result = handler.getTestCaseResult();
	    }
	    catch (SAXParseException e)
	    {
	    	throw new IOError( e );
		}
	    
	    return result;
	}
	
	protected void outputRunLog(File aRunLog)
	{
	    try
	    {
	        String thisLine;
	    	FileInputStream fis = new FileInputStream(aRunLog);
	    	InputStreamReader inputStreamReader = new InputStreamReader( fis );
			BufferedReader myInput = new BufferedReader( inputStreamReader );

			while ((thisLine = myInput.readLine()) != null)
			{
				  System.out.println(thisLine);
			}
	    	fis.close();
	    }
	    catch (Exception e)
	    {
	    	throw new IOError( e );
	    }
	}
	
	protected TestCaseResult addOrParseRunLog(TestCaseResult aResult, LOGTYPE aLogType, String aLogName)
	{
		TestCaseResult result = aResult;
		if ( aLogType == LOGTYPE.xml )
		{
			result = readResults(aLogName);
		}
		else if ( aLogType == LOGTYPE.output )
		{
			aResult.addTestLog("run", aLogName);
	    	outputRunLog(new File(aLogName) );
		}
		else if ( aLogType == LOGTYPE.logonly )
		{
			aResult.addTestLog("run", aLogName);
		}
		
		return result;
	}
}
