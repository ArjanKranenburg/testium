package net.sf.testium.executor;

import java.util.Hashtable;

import org.testtoolinterfaces.testresult.ResultSummary;
import org.testtoolinterfaces.testresult.TestGroupResult;
import org.testtoolinterfaces.testresult.TestGroupResultLink;
import org.testtoolinterfaces.testresult.impl.TestGroupResultLinkImpl;
import org.testtoolinterfaces.testsuite.TestGroupLink;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.Warning;

/**
 * Dispatches the execution of TestGroups to the right TestGroupExecutor depending on the TestGroup type.
 * 
 * @author Arjan
 *
 */
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

	public void execute(TestGroupLink aTestGroupLink,
			TestGroupResult aResult, ExecutionEnvironment anEnv) {
		Trace.println(Trace.EXEC,
				"execute( " + aTestGroupLink.getId() + ", " + aResult.getId() + " )", true);

		if (myExecutors.containsKey(aTestGroupLink.getLinkType())) {
			TestGroupExecutor executor = myExecutors.get(aTestGroupLink
					.getLinkType());

			executor.execute(aTestGroupLink, aResult, anEnv);
		} else {
			TestGroupResultLink result = new TestGroupResultLinkImpl(
					aTestGroupLink, new ResultSummary(0, 0, 0, 0), null);

			String message = "Cannot execute test group scripts of type "
					+ aTestGroupLink.getLinkType() + "\n";
			result.addComment(message);
			Warning.println(message);
			Trace.print(Trace.ALL, "Cannot execute " + aTestGroupLink.getId());

			aResult.addTestExecItemResultLink(result);
		}
	}
}
