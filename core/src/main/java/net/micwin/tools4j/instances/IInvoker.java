package net.micwin.tools4j.instances;

import net.micwin.tools4j.exceptions.TechException;

public interface IInvoker <T> {

    T invoke() throws TechException;

}
