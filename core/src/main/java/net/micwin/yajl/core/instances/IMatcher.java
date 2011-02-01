package net.micwin.yajl.core.instances;

/**
 * A strategy that checks instances of a certain type to match a criteria specified
 * by its implementation.
 * 
 * @author MicWin@micwin.net
 * 
 */
public interface IMatcher <T> {

    boolean matches (T cancdidate) ;
}
