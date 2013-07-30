package net.sf.testium.executor;

import java.io.File;
import java.util.ArrayList;

import net.sf.testium.executor.general.SpecifiedParameter;

import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestStepCommand;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;


public interface TestStepCommandExecutor
{
	String getCommand();
	
	TestStepResult execute( TestStepCommand aStep,
	                        RunTimeData aVariables,
	                        File aLogDir ) throws TestSuiteException;

	boolean verifyParameters(ParameterArrayList aParameters) throws TestSuiteException;

	String getDescription();
	
	ArrayList<SpecifiedParameter> getParameterSpecs();

}