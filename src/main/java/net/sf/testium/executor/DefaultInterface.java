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

		add(new AddToList(this));
		add(new CheckListSize(this));
		add(new CheckVariable( this ));
		add(new Comment( this ));
		add(new Fail( this ));
		add(new GetListItem(this));
		add(new GetListSize(this));
		add(new PrintVars( this ));
		add(new RemoveFromList( this));
		add(new SetDate( this));
		add(new SetList( this));
		add(new SetVariable( this));
		add(new Sum(this));
		add(new Substract(this));
		add(new SubString(this));
		add(new Wait( this ));
	}
}
