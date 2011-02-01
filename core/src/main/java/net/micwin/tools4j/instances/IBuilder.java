package net.micwin.tools4j.instances;

/**
 * A builder is a sort of factory, but with the specific speciality that it is
 * used only once.
 * 
 * @author micwin@micwin.net
 * 
 * @param <T>
 */
public interface IBuilder<T> {

    /**
         * After have filled in all values, create the effective instance.
         * 
         * @return
         */
    T create();

}
