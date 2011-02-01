/*
 * (C) 2003 M.Winkler All Rights reserved
 * 
 * $Log: IConfiguration.java,v $
 * Revision 1.3  2007/08/30 07:31:25  recipient00
 * reformatted to meet builtin sun java code conventions
 *
 * Revision 1.2  2007/08/30 07:18:18  recipient00
 * gone jdk 1.5 and eclipse 3.2.x
 *
 * Revision 1.1.2.1  2007/06/03 13:15:23  recipient00
 * Migrating to maven2
 *
 * Revision 1.1.2.1  2007/02/15 07:06:27  recipient00
 * Renaming ALL packages from de.micwin.* to net.micwin.tools4j.*
 *
 * Revision 1.6.2.1  2007/02/14 22:38:35  recipient00
 * Being more explicit - getPath doesnt say something about the type; now take getPathString().
 *
 * Revision 1.6  2004/04/18 23:07:24  recipient00
 * reworked comments and javadocs
 *
 * Revision 1.5  2004/04/18 16:29:21  recipient00
 * Implemented FormatException and adjusted Interfaces and abstract
 *
 * Revision 1.4  2004/04/16 12:29:24  recipient00
 * once more adjusted formattings :( and extended javadocs
 *
 * 
 * Revision 1.3 2004/01/11 19:51:29 recipient00 Translated into mere english
 * 
 * Revision 1.2 2004/01/10 20:21:39 recipient00 Added todo tags for translation
 * and legal notes
 * 
 * Revision 1.1 2004/01/07 22:34:20 recipient00 initial check in
 * 
 * Revision 1.3 2003/12/30 21:13:18 micwin pseudo-Kollisionen :(
 * 
 * Revision 1.2 2003/12/16 09:30:07 micwin Reformattings
 * 
 * Revision 1.1 2003/12/09 20:16:57 micwin initial check in
 *  
 */

package net.micwin.tools4j.config;

/**
 * Abstracts the function of a key-value based configurative group. This
 * configuration can contain string values, integer, boolean, or arrays. It also
 * can contain sub configuration groups, accessed by string keys (see
 * <code>sub(String)</code> for that).
 * 
 * The big plus of this approach his the fact that concrete implementation of
 * the underlying storate technique and structure is completely kept out for
 * client code. Also masked out is the detail <code>how</code> Integers are
 * parsed and how sub configurations are stored. Just get passed in a
 * IConfiguration, check the presence of mandatory keys via
 * <code>assertAvailable(...)</code>, and apply defaults to optional values
 * by using the <code>,defaultValue</code> -variants of getter-methods.
 * 
 * TODO add legal notes FEATURE_PROPOSAL More types like Double, Date, ...
 * FEATURE_PROPOSAL Generic type mapping
 * 
 * @author micwin@gmx.org
 * @since 08.12.2003 19:58:38
 */
public interface IConfiguration {

    /**
         * Gets a boolean with the specified name.
         * 
         * @param key
         *                The name the value is stored with. Should be chosen
         *                descriptive (dont use hierarchy separator like . or
         *                /).
         * @return The value interpreted as java.lang.Boolean.
         * @throws MissingConfigKeyException
         *                 If the specified key is not present.
         * @throws FormatException
         *                 If the value cannot be interpreted as Boolean.
         */
    Boolean getBoolean(String key) throws MissingConfigKeyException,
	    FormatException;

    /**
         * Gets the boolean with the specified name.
         * 
         * @param key
         *                The name the value is stored with. Should be chosen
         *                descriptive (dont use hierarchy separator like . or
         *                /).
         * @param defaultBool
         *                If key not present, then this one is returned.
         * @return The value interpreted as boolean.
         * @throws FormatException
         *                 If the value cannot be interpreted as Boolean.
         */
    Boolean getBoolean(String key, Boolean defaultBool) throws FormatException;

    /**
         * Gets the integer with the specified name.
         * 
         * @param key
         *                The name the value is stored with. Should be chosen
         *                descriptive (dont use hierarchy separator like . or
         *                /).
         * @return The value interpreted as java.lang.Integer.
         * @throws MissingConfigKeyException
         *                 If the specified key is not present.
         * @throws FormatException
         *                 If the value cannot be interpreted as Integer.
         */
    Integer getInteger(String key) throws MissingConfigKeyException,
	    FormatException;

    /**
         * Gets the integer with the specified name.
         * 
         * @param key
         *                The name the value is stored with. Should be chosen
         *                descriptive (dont use hierarchy separator like . or
         *                /).
         * @param defaultInt
         *                If key not present, then this one is returned.
         * 
         * @return The value interpreted as java.lang.Integer.
         * @throws FormatException
         *                 If the value cannot be interpreted as Integer.
         */
    Integer getInteger(String key, Integer defaultInt) throws FormatException;

    /**
         * Gets the String with the specified name.
         * 
         * @param key
         *                The name the value is stored with. Should be chosen
         *                descriptive (dont use hierarchy separator like . or
         *                /).
         * @return The value converted as java.lang.String.
         * @throws MissingConfigKeyException
         *                 If the specified key is not present.
         */
    String getString(String key) throws MissingConfigKeyException;

    /**
         * Gets the String with the specified name.
         * 
         * @param key
         *                The name the value is stored with. Should be chosen
         *                descriptive (dont use hierarchy separator like . or
         *                /).
         * @param defaultString
         *                If key not present, then this one is returned. *
         * @return The value interpreted as java.lang.String.
         */
    String getString(String key, String defaultString);

    /**
         * Gets the Array with the specified name. Dont worry about how this
         * array is stored- this is the job of the implementing class. You
         * either get a string array or an exception - period.
         * 
         * @param key
         *                The name the value is stored with. Should be chosen
         *                descriptive (dont use hierarchy separator like . or
         *                /).
         * @return The value converted to String [].
         * @throws MissingConfigKeyException
         *                 If the specified key is not present.
         * @throws FormatException
         *                 If the value cannot be interpreted as Array.
         */
    String[] getArray(String key) throws MissingConfigKeyException,
	    FormatException;

    /**
         * Gets the Array with the specified name. Dont worry about how this
         * array is stored- this is the job of the implementing class. You
         * either get a string array or an exception - period.
         * 
         * @param key
         *                The name the value is stored with. Should be chosen
         *                descriptive (dont use hierarchy separator like . or
         *                /).
         * @param defaultArray
         *                If the specified key is not present, then this one is
         *                returned.
         * @return The value converted as String [].
         * @throws FormatException
         *                 If the value cannot be interpreted as Array.
         */
    String[] getArray(String key, String[] defaultArray) throws FormatException;

    /**
         * Checks wether the specified keys are present in this configuration
         * group. Tgis includes configuration keys (Integer, boolean, arrays),
         * and excludes keys for subs.
         * 
         * @param keys
         *                A set of keys that is mandatory to be present in this
         *                configuration (except sub keys).
         * 
         * FEATURE_PROPOSAL type safety ?!?
         * 
         * @throws MissingConfigKeyException
         *                 Some of the keys are missing. An implementer must
         *                 ensure that all missing keys are listed in both field
         *                 and the exception's message so correction of the
         *                 configuration files is a short job.
         */
    void assertAvailable(String[] keys) throws MissingConfigKeyException;

    /**
         * Returns a good idea of the hierarchical position of this
         * configuration group.
         * 
         * @return
         */
    String getPathString();

    /**
         * Returns the named sub configuration, if present. Throws an exception
         * otherwise.
         * 
         * @param key
         *                The name of the sub configuration (part of its path).
         * @return The sub configuration.
         * @throws MissingConfigKeyException
         *                 If the specified sub configuration is not present.
         * @throws FormatException
         *                 If the value cannot be interpreted as sub
         *                 configuration. This happens if the specified key is a
         *                 simple config key.
         */
    IConfiguration sub(String key) throws MissingConfigKeyException;

    /**
         * Returns a list of all available config keys (except keys of sub
         * configs).
         * 
         * TODO rename to valueKeys()
         * 
         * FEATURE_PROPOSAL subs() FEATURE_PROPOSAL subKeys()
         * 
         * @return
         */
    String[] keys();
}