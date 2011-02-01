package net.micwin.tools4j.instances;

import net.micwin.yajl.core.exceptions.TechException;

public interface IInvoker <T> {

    T invoke() throws TechException;

}
