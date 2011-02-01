/**
 * (c) 2007 M.Winkler, M�nchen, Germany
 */
package net.micwin.yajl.core.config;

import java.util.Collection;
import java.util.Map;

/**
 * A set of boolean flags.
 * 
 * @author ichael.winkler@micwin.net
 * 
 */
public interface IFlagtSet {

    /**
         * Checks wether or not a specified flag is set.
         * 
         * @param flag
         * @return
         */
    boolean isSet(Enum flag);

    /**
         * Checks wether or not a specified flag is cleared.
         * 
         * @param flag
         * @return
         */
    boolean isCleared(Enum flag);

    /**
         * Sets a specified flag to a specified state.
         * 
         * @param flag
         * @return
         */
    IFlagtSet setState(Enum flag, boolean state);

    /**
         * Sets a specified flag to state "set".
         * 
         * @param flag
         * @return
         */
    IFlagtSet set(Enum flag);

    /**
         * Sets a specified flag to state "Cleared".
         * 
         * @param flag
         * @return
         */
    IFlagtSet clear(Enum flag);

    /**
         * Puts all names of all flags having the specified state into
         * <code>target</code>. If <code>target</code> is null, then create
         * a new collection and return that one.
         * 
         * @param state
         *                a desired state
         * @param target
         *                Optional. A target collection to put the names to.
         *                Optional <code>null</code>.
         * 
         * @return
         */
    Collection<String> getFlags(boolean state, Collection<String> target);

    /**
         * Puts all flags and their states into a map.
         * 
         * @param target
         *                A target map to put the names and states to. If
         *                <code>null</code>, then a new map will be created.
         * @return
         */
    Map<String, Boolean> getAllFlags(Map<String, Boolean> target);

}
