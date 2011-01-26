package net.micwin.yajl.core.contexts;

import net.micwin.yajl.core.instances.IUniqueIdProvider;
import net.micwin.yajl.core.timedate.IClock;

/**
 * A continuum is a specific logical group of configurations, rules, entities
 * and forces. Its a far above abstraction of "the space" in which we live. An
 * IContinum ist stateful.
 * 
 * @author MicWin
 * 
 */
public interface IContinuum {

	/**
	 * The unique ID provider to create unique IDs that really really really are
	 * unique in the context of the continuum.
	 * 
	 * @return
	 */
	public IUniqueIdProvider getUniqueIdProvider();

	/**
	 * Return the time measurement insrtrument for this continuum.
	 * 
	 * @return
	 */
	public IClock getClock();

}
