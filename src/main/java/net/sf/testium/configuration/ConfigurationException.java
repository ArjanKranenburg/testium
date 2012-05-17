/**
 * 
 */
package net.sf.testium.configuration;

/**
 * @author Arjan Kranenburg
 *
 */
public class ConfigurationException extends Exception
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5059692813990178291L;

	/**
	 * @param aMessage
	 */
	public ConfigurationException(String aMessage)
	{
		super(aMessage);
	}

	/**
	 * @param aCause
	 */
	public ConfigurationException(Throwable aCause)
	{
		super(aCause);
	}

	/**
	 * @param aMessage
	 * @param aCause
	 */
	public ConfigurationException(String aMessage, Throwable aCause)
	{
		super(aMessage, aCause);
	}
}
