/**
 * 
 */
package net.sf.testium.executor.general;

import net.sf.testium.executor.DefaultInterface;

/**
 * 
 * @author Arjan Kranenburg
 *
 * This command is deprecated because the correct (or better) spelling is subtract
 */
public class Substract extends Subtract {

	public Substract( DefaultInterface defInterface ) {
		super( defInterface );
	}

	public String getDescription() {
		return "DEPRECATED, use Subtract. Subtracts two integer values";
	}
}
