package net.micwin.yajl.core.instances;

/**
 * A Permutator has the ability of permutating through a set of terminals for a
 * given amount of digits.
 * 
 * @author michael.winkler@micwin.net
 * 
 */
public class Permutator {

    final Object[] terminals;

    int activeState[];

    boolean overflow = false;

    /**
         * Creates a new permutator for given terminals and specified length.
         * 
         * @param terminals
         * @param length
         */
    public Permutator(Object[] terminals, int length) {
	if (terminals == null) {
	    throw new IllegalArgumentException("argument 'terminals' is null");
	} else if (terminals.length < 2) {
	    throw new IllegalArgumentException(
		    "argument 'terminals' has too few elements ("
			    + terminals.length + ")");
	}

	if (length < 1) {
	    throw new IllegalArgumentException(
		    "argument 'length' is too small (" + length + ")");
	}
	this.terminals = terminals;
	activeState = new int[length];
    }

    public Object[] terminals(Object[] dest) {

	// check state (are we already through?!?)
	if (overflow) {
	    throw new IllegalStateException("Permutator out of range");
	}

	// adjust target array
	if (dest == null || dest.length < activeState.length) {
	    dest = new Object[activeState.length];
	}

	// transfer terminals according to active state
	for (int i = 0; i < dest.length; i++) {
	    dest[i] = terminals[activeState[i]];
	}

	// return target array
	return dest;
    }

    public void restart() {
	for (int i = 0; i < activeState.length; i++) {
	    activeState[i] = 0;
	}
	overflow = false;
    }

    /**
         * Permutate one further.
         * 
         * @return true if the reached state is valid.
         */
    public boolean raise() {

	for (int index = 0; index < activeState.length; index++) {
	    activeState[index]++;
	    if (activeState[index] >= terminals.length) {
		// rollover, reset actual value to 0
		activeState[index] = 0;

		if (index == activeState.length - 1) {
		    // uh oh overflow
		    overflow = true;
		}

	    } else {
		// no rollover, break ;
		break;
	    }
	}

	return !overflow;
    }
}
