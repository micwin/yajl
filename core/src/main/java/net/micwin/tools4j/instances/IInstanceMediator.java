package net.micwin.tools4j.instances;

/**
 * Manages persistence of instances.
 * 
 * @author micwin@micwin.net
 * 
 */
public interface IInstanceMediator<T extends IManagedInstance> {

    /**
         * Returns the specified instance.
         * 
         * @param primaryKey
         *                the primary key of instance.
         * @return
         */
    T lookup(long primaryKey);
}
