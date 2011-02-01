package net.micwin.tools4j.instances;

import net.micwin.tools4j.exceptions.TechException;

public interface IFactory<T> {

    /**
         * Creates a new Instance of the specified class.
         * 
         * @return
         * @throws TechException
         */
    public T newInstance() throws TechException;

}
