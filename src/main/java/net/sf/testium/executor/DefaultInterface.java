/**
 * 
 */
package net.sf.testium.executor;

import net.sf.testium.executor.general.*;
import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan Kranenburg
 *
 */
public class DefaultInterface extends CustomInterface
{
	public final static String NAME			 = "Default";

	/**
	 * 
	 */
	public DefaultInterface()
	{
		super( NAME );
		Trace.println(Trace.CONSTRUCTOR);

		add(new WaitCommand( this ));
		add(new CheckVariableCommand( this ));
		add(new Comment( this ));
		add(new SetVariable( this));
		add(new GetListItem(this));
		add(new PrintVars( this ));
		add(new CheckListSize(this));
		add(new SetDate( this));
	}
}
