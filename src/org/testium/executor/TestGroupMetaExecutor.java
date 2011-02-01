package org.testium.executor;

import java.io.File;
import java.util.Hashtable;

import org.testtoolinterfaces.testresult.ResultSummary;
import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testresult.TestGroupResultLink;
import org.testtoolinterfaces.testsuite.TestGroupLink;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.Warning;

public class TestGroupMetaExecutor
{
	private Hashtable<String, TestGroupExecutor> myExecutors;

	/**
	 */
	public TestGroupMetaExecutor()
	{
		Trace.println( Trace.CONSTRUCTOR );
		
		myExecutors = new Hashtable<String, TestGroupExecutor>();
	}

	public void put(String aType, TestGroupExecutor aTestGroupExecutor)
	{
		myExecutors.put(aType, aTestGroupExecutor);
	}

	public void execute( TestGroupLink aTestGroupLink,
	                     File aLogDir,
	                     TestGroupResult aResult )
	{
		Trace.println(Trace.EXEC, "execute( " 
						+ aTestGroupLink.getId() + ", "
			            + aLogDir.getPath() + ", "
			            + aResult.getId() + " )", true );

		if ( myExecutors.containsKey( aTestGroupLink.getGroupType() ) )
		{
			TestGroupExecutor executor = myExecutors.get( aTestGroupLink.getGroupType() );
			
			executor.execute(aTestGroupLink, aLogDir, aResult);
		}
		else
		{
			TestGroupResultLink result = new TestGroupResultLink( aTestGroupLink,
			                                  new ResultSummary(0, 0, 0, 0),
			                                  null );

			String message = "Cannot execute test case scripts of type " + aTestGroupLink.getGroupType() + "\n";
			result.addComment(message);
			Warning.println(message);
			Trace.print(Trace.ALL, "Cannot execute " + aTestGroupLink.getId());
			
			aResult.addTestGroup(result);
		}
	}
}
