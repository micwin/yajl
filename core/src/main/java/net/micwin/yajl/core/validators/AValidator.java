package net.micwin.yajl.core.validators;

import java.util.ArrayList;

import net.micwin.yajl.core.instances.impl.ArrayListPool;

/**
 * An abstract class to help building validators. Just overwrite
 * {@link #validate(Object, java.util.Collection)} and you're done.
 * 
 * @author MicWin
 * 
 * @param <T>
 */
public abstract class AValidator<T> implements Validator<T> {

	public boolean isValid(T entityToCheck) {
		try {
			checkValid(entityToCheck);
			return true;
		} catch (ValidationException e) {
			return false;
		}
	}

	public void checkValid(T entityToCheck) throws ValidationException {

		ArrayList<String> problems = ArrayListPool.FOR_STRINGS.aquire();
		try {
			validate(entityToCheck, problems);

			if (problems.size() > 0) {
				throw new ValidationException(problems);

			}
		} finally {
			ArrayListPool.FOR_STRINGS.release(problems);
		}
	}
}
