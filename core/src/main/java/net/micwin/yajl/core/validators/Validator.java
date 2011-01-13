package net.micwin.yajl.core.validators;

import java.util.Collection;

/**
 * A Validator is a entity that checks wether another entity is valid - thats
 * all. The context in which the checked entity is valid is implemented by this
 * validator entity.
 * 
 * @author MicWin
 * 
 */
public interface Validator<T> {

	/**
	 * Checks wether <code>entitytoCheck is valid</code>. If so, return
	 * <code>true</code>.
	 * 
	 * @param entityToCheck
	 *            An entity to check.
	 * @return
	 */
	public boolean isValid(T entityToCheck);

	/**
	 * Checks wether or not the specified entity is valid. If not, an exception
	 * is thrown.
	 * 
	 * @param entityToCheck
	 *            The entity to check.
	 * @throws ValidationException
	 *             Throw when entity is not valid.
	 */
	public void checkValid(T entityToCheck) throws ValidationException;

	/**
	 * Collect all validation errors in one collection. Dont throw any
	 * exceptions and dont return a result.
	 * 
	 * @param entityToCheck
	 * @param collectionToPutProblemsIn
	 */
	public abstract void validate(T entityToCheck,
			Collection<String> collectionToPutProblemsIn);

}
