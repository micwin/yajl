package net.micwin.tools4j;

/*
 * (C) 2004 M.Winkler All Rights reserved
 * 
 * 
 * $Log: StringFactory.java,v $
 * Revision 1.6  2007/12/17 22:50:39  recipient00
 * A very handy toCharArray method
 *
 * Revision 1.5  2007/11/20 10:32:00  recipient00
 * stabilized var resolution
 *
 * Revision 1.4  2007/09/06 15:57:59  recipient00
 * reformatted code, removed some code quality flaws
 *
 * Revision 1.3  2007/08/30 07:31:39  recipient00
 * reformatted to meet builtin sun java code conventions
 *
 * Revision 1.2  2007/08/30 07:18:27  recipient00
 * gone jdk 1.5 and eclipse 3.2.x
 *
 * Revision 1.1.2.1  2007/06/03 13:15:31  recipient00
 * Migrating to maven2
 *
 * Revision 1.1.2.1  2007/02/15 07:06:27  recipient00
 * Renaming ALL packages from de.micwin.* to net.micwin.tools4j.*
 *
 * Revision 1.9.2.7  2007/01/20 22:11:32  recipient00
 * EMPTY_STRING
 *
 * Revision 1.9.2.6  2006/12/29 12:03:34  recipient00
 * Removed double code, made more stable
 *
 * Revision 1.9.2.5  2006/12/28 13:40:50  recipient00
 * parameter 'reverse' for method "permutate"
 *
 * Revision 1.9.2.4  2006/12/28 13:25:37  recipient00
 * minor bug when actually giving in a message pattern to the permutate method
 *
 * Revision 1.9.2.3  2006/12/28 13:22:41  recipient00
 * A method to permutate strings.
 *
 * Revision 1.9.2.2  2006/12/28 09:27:11  recipient00
 * One more Tokenizer
 *
 * Revision 1.9.2.1  2006/09/18 08:40:29  recipient00
 * A Version of "resolveVars" for non-static use.
 *
 * Revision 1.9  2004/11/25 08:42:30  recipient00
 * method encodeUmlaut now can encode german umlauts to entities
 *
 * Revision 1.8  2004/06/04 15:41:40  recipient00
 * *** empty log message ***
 *
 * Revision 1.7  2004/05/02 10:33:47  recipient00
 * method length() added
 *
 * Revision 1.6  2004/04/27 21:13:53  recipient00
 * appendAll (Reader,String)
 *
 * Revision 1.5  2004/04/18 16:26:16  recipient00
 * implemented replace (int, String) and resolveVars (Properties)
 *
 * Revision 1.4  2004/04/15 19:02:21  recipient00
 * Messed around with malformed comments ...
 *
 * 
 * Revision 1.3 2004/01/11 16:36:49 recipient00 Translated into mere english
 * 
 *  
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * Provides a much more powerful replacement for StringBuffer. The declared aim
 * is to provide a complete replacement for StringBuffer, that is much more
 * faster, and also provides all methods of String to locate, generate and
 * modify a String.
 * 
 * TODO add legal notes
 * 
 * @author micwin@gmx.org
 * @since 09.04.2003
 * @version $Id: StringFactory.java,v 1.9.2.1 2006/09/18 08:40:29 recipient00
 *          Exp $
 */
public final class StringFactory {

    // --------------------------------------------------------------
    // -- static field
    // --------------------------------------------------------------

    /**
         * For convenience, an enmpty string array.
         */
    public static final String[] EMPTY_STRING_ARRAY = new String[] {};

    /**
         * For convenience, an enmpty string.
         */
    public static final String EMPTY_STRING = "";

    /**
         * Converts an array into a String. The elements will be converted into
         * strings using <code>toString()</code>. elements that are
         * <code>null</code> will map to "null".
         * 
         * @param prefix
         *                the first part of the resulting string.
         * @param array
         *                An array of elements that will be converted into
         *                strings and appended to each other.
         * @param separator
         *                A string placed between the elements.
         * @param postfix
         *                String to be appended to the last element.
         * @return A String displaying the contents of the array using the given
         *         constraints.
         */
    public static String toString(String prefix, Object[] array,
	    String separator, String postfix) {
	StringFactory sf = new StringFactory(array, separator);
	sf.ensureCapacity(sf._count + prefix.length() + postfix.length());
	sf.insert(prefix, 0, 1);
	sf.append(postfix);
	return sf.toString();
    }

    /**
         * Wraps a text to only consist of lines with a maximum length.
         * 
         * TODO inbtegrate into non staic field.
         * 
         * @param text
         *                text to wrap.
         * @param border
         *                max line length (column to wrap)
         * @param honorWhitespace
         *                If <code>true</code>, the wrap will occur at the
         *                last psoition containing a whitespace char. Useful to
         *                separate normal text built by words.
         * @return
         */
    public static String wrap(String text, int border, boolean honorWhitespace) {
	if (honorWhitespace) {
	    StringTokenizer st = new StringTokenizer(text, " \t\n");
	    StringBuffer sb = new StringBuffer(text.length());
	    int lengthThisLine = 0;
	    // �ber alle W�rter des Strings dr�bergehen
	    while (st.hasMoreTokens()) {
		String token = st.nextToken();
		// W�rde dieser Token die border unterschreiten?
		if (lengthThisLine + 1 + token.length() <= border) {
		    // hab ich schon was in der aktuellen Zeile?
		    if (lengthThisLine > 0)
			// Wenn ja, dann Abstandhalter
			sb.append(' ');
		    // Token anh�ngen, und Zeilenl�nge hochz�hlen
		    sb.append(token);
		    lengthThisLine += token.length() + 1;
		} else {
		    // Dieser token w�rde die Maximall�nge der Zeile
		    // �berschreiten
		    if (lengthThisLine == 0) {
			// noch nix in der Zeile, aber token trotzdem l�nger
			// ...
			// Token trotzdem anh�ngen, und sofort umbrechen
			sb.append(token).append('\n');
		    } else {
			// Ist schon was in der Zeile, drum umbrechen und mit
			// Token neue Zeile aufmachen
			sb.append('\n');
			sb.append(token);
			lengthThisLine = token.length();
		    }
		}
	    }
	    return sb.toString();
	} else {
	    // stupides umbrechen
	    StringBuffer sb = new StringBuffer(text.length() + text.length()
		    / border + 1);
	    int lastEnd = -1;
	    while (lastEnd + border < text.length()) {
		int start = lastEnd + 1;
		lastEnd = lastEnd + border;
		sb.append(text.substring(start, lastEnd + 1)).append('\n');
	    }
	    sb.append(text.substring(lastEnd + 1));
	    return sb.toString();
	}
    }

    /**
         * Left aligns <code>text</code> in <code>margin</code>.
         * 
         * TODO integrate into non staic field.
         * 
         * @param text
         *                text to align
         * @param margin
         *                The margin in which <code>text</code> should get
         *                aligned in.
         * @param strip
         *                If <code>text</code> is longer than
         *                <code>margin</code> and <code>strip==true</code>,
         *                the all exceeding chars will be deleted.
         * @return <code>margin</code> containing <code>text</code> left
         *         aligned.
         */
    public static String alignLeft(String text, String margin, boolean strip) {
	// Text l�nger als Rahmen?
	if (text.length() >= margin.length()) {
	    return strip ? text.substring(0, margin.length()) : text;
	} else {
	    return text + margin.substring(text.length());
	}
    }

    /**
         * Right aligns <code>text</code> in <code>margin</code>.
         * 
         * TODO integrate into non static field.
         * 
         * @param text
         *                text to align
         * @param margin
         *                The margin in which <code>text</code> should get
         *                aligned in.
         * @param strip
         *                If <code>text</code> is longer than
         *                <code>margin</code> and <code>strip==true</code>,
         *                the all exceeding chars will be deleted.
         * @return <code>margin</code> containing <code>text</code> right
         *         aligned.
         */
    public static String alignRight(String text, String margin, boolean strip) {
	// Text l�nger als Rahmen?
	if (text.length() >= margin.length()) {
	    return strip ? text.substring(text.length() - margin.length())
		    : text;
	} else {
	    return margin.substring(0, margin.length() - text.length()) + text;
	}
    }

    /**
         * Resolves variables of the form <code>${variable.name}</code> by
         * values of <<code>props</code>, where <code>variable.name</code>
         * is used as key.
         * 
         * <strong>Caution !</strong></br>
         * 
         * TODO Circular references not yet detected and resolved!
         * 
         * TODO Integrate in non static field
         * 
         * @param valueString
         *                The String to analyze.
         * @param props
         *                key-value-pairs to detect and resolve.
         * @returnThe resolved string.
         * @throws ParseException
         *                 If at least one closing bracelet is missing.
         */
    public static String resolveVars(String valueString, Properties props)
	    throws ParseException {
	String result = valueString;
	for (int startIndex = result.indexOf("${"); startIndex > -1; startIndex = result
		.indexOf("${")) {
	    int endIndex = result.indexOf('}', startIndex);
	    if (endIndex < startIndex)
		throw new ParseException(valueString, valueString.length());
	    String key = result.substring(startIndex + 2, endIndex);
	    String value = props.getProperty(key);
	    if (value == null || value.equals("${" + key + "}")) {
		value = "$?{" + key + "}";
	    }
	    result = result.substring(0, startIndex) + value
		    + result.substring(endIndex + 1);
	}
	return result;
    }

    /**
         * Resolves variables of the form <code>${variable.name}</code> by
         * values of <<code>map</code>, where <code>variable.name</code>
         * is used as key.
         * 
         * <strong>Caution !</strong></br>
         * 
         * TODO Circular references not yet detected and resolved!
         * 
         * TODO Integrate in non static field
         * 
         * @param valueString
         *                The String to analyze.
         * @param map
         *                key-value-pairs to detect and resolve.
         * @returnThe resolved string.
         * @throws ParseException
         *                 If at least one closing bracelet is missing.
         */
    public static String resolveVars(String valueString, Map<String, Object> map)
	    throws ParseException {

	// first transfer values from the Map to a properties instance
	Properties props = new Properties();
	for (String key : map.keySet()) {
	    props.put(key, map.get(key));
	}
	return resolveVars(valueString, props);
    }

    /**
         * Tokenizes a String and puts the tokens into an array.
         * 
         * @param line
         *                The String to split.
         * @param delims
         *                delimeter chars for the tokenizer.
         * @param dest
         *                The array to put the tokens in.If this is
         *                <code>null</code> or too short, then a new array is
         *                created and returned.
         * @param filler
         *                If <code>dest</code> is longer than needed, then all
         *                remaining indexes are set to this value (optionally
         *                set to <code>null</code>).
         * @return dest, if long enough. Otherwise a newly created array
         *         containing the tokens.
         */
    public static String[] tokenizeToArray(String line, String delims,
	    String[] dest, String filler) {
	StringTokenizer st = new StringTokenizer(line, delims);
	if ((dest == null) || (dest.length < st.countTokens())) {
	    dest = new String[st.countTokens()];
	}
	for (int i = 0; i < dest.length; i++) {
	    dest[i] = st.hasMoreElements() ? st.nextToken() : filler;
	}
	return dest;
    }

    /**
         * Splits the given line into tokens of specified length. For instance,
         * <code>StringFactory.tokenizeToArray("abcde" , 2)</code> results in
         * an array <code>{"ab" , "cd" , "e"}</code>.
         * 
         * </pre>
         * 
         * @param line
         * @param tokenLength
         *                The number of chars each token has.
         * @return
         */
    public static String[] tokenizeToArray(String line, int tokenLength) {

	if (tokenLength < 1) {
	    throw new IllegalArgumentException(
		    "argument 'tokenLength' has invalid value '" + tokenLength
			    + "'");
	}

	int length = line == null ? 0 : line.length();

	if (length < 1) {
	    // shortcut : line is null or empty -> return value is an empty
	    // array
	    return EMPTY_STRING_ARRAY;
	} else if (length <= tokenLength) {
	    // shortcut : line is only one token -> return a single token
	    // array
	    return new String[] { line };
	}

	// normal case : more than one token
	//

	// compute number of tokens
	int tokenCount = length / tokenLength;
	if (tokenLength * tokenCount < length) {
	    // there was a modulo
	    tokenCount += 1;
	}

	String[] tokens = new String[tokenCount];
	for (int i = 0; i < tokenCount; i++) {
	    int startIndex = i * tokenLength;
	    int endIndex = Math.min(startIndex + tokenLength, length);
	    tokens[i] = line.substring(startIndex, endIndex);
	}
	return tokens;

    }

    /**
         * Encodes al german umlauts (including �) to their appropriate
         * xml-entities.
         * 
         * @param string
         * @return
         */
    public static String encodeUmlauts(String string) {
	return string
		//            
		.replaceAll("�", "&auml;").replaceAll("�", "&ouml;")
		.replaceAll("�", "&uuml;").replaceAll("�", "&Auml;")
		.replaceAll("�", "&Ouml;").replaceAll("�", "&Uuml;")
		.replaceAll("�", "&szlig;");
    }

    /**
         * Permutates through repetitions of terminal strings and returns the
         * results as array of strings.
         * 
         * @param terminals
         *                The strings to permutate.
         * @param length
         *                the number of terminals to concatenate per permutation
         * @param messagePatternFormat
         * @param reverse
         *                Wether or not the terminals should be concatenated
         *                left to right (forward) or reverse. You normally want
         *                it <code>reverse == true</code>.
         */
    public static String[] permutate(String[] terminals, int length,
	    String messagePatternFormat, boolean reverse) {
	if (terminals == null || terminals.length < 2) {
	    throw new IllegalArgumentException(
		    "too few elements in argument 'terminals'");
	} else if (length < 1) {
	    throw new IllegalArgumentException("argument 'length' is invalid ("
		    + length + ").");
	}

	MessageFormat format = null;
	if (messagePatternFormat != null)
	    format = new MessageFormat(messagePatternFormat);

	Permutator permutator = new Permutator(terminals, length);
	long permCount = Math.round(Math.pow(terminals.length, length));
	if (permCount > Integer.MAX_VALUE) {
	    throw new IllegalStateException(
		    "resulting array too big for java (" + permCount + ")");
	}
	String[] result = new String[(int) permCount];
	Object[] intermediate = null;
	for (int i = 0; i < result.length; i++) {
	    intermediate = permutator.terminals(intermediate);
	    if (reverse)
		ArrayFactory.reverse(intermediate);
	    result[i] = toString("", intermediate, "", "");

	    if (format != null) {
		result[i] = format.format(new Object[] { result[i] });
	    }

	    permutator.raise();
	}

	return result;

    }

    // --------------------------------------------------------------
    // -- instance field
    // --------------------------------------------------------------
    /**
         * Internal buffer.
         */
    char[] _data;

    /**
         * Number of characters stored in the buffer.
         */
    int _count;

    /**
         * Creates a new StringFactory with an initial buffer capacity of
         * <code>initialLength</code>.
         * 
         * @param initialLength
         *                The initial capacity of the internal buffer. Good to
         *                be a good guess for the final length of the target
         *                string.
         */
    public StringFactory(int initialLength) {
	_data = new char[Math.max(10, initialLength)];
	_count = 0;
    }

    /**
         * Creates a new string factory and appends the array.
         * 
         * @param array
         *                The array elements to insert.
         * @param separator
         *                String to use to separate the elements from each
         *                other.
         */
    public StringFactory(Object[] array, String separator) {
	this(array != null ? array.length * separator.length() : 4);
	append(array, separator);
    }

    /**
         * Creates a new StringFactory with an initial buffer capacity of
         * <code>initialLength</code>, and copies <code>s</code> to this
         * buffer. If <code>s</code> is longer than <code>initialLength</code>,
         * then <code>s.length()</code> is taken as initial length instead.
         * 
         * @param initialLength
         *                The initial capacity of the internal buffer. Good to
         *                be a good guess for the final length of the target
         *                string.
         * @param s
         *                initial string to be put into the buffer.
         */
    public StringFactory(String s, int initialLength) {
	this(s != null ? Math.max(initialLength, s.length()) : initialLength);
	append(s);
    }

    /**
         * Creates a new StringFactory and loads the buffer with the contents of
         * the specified file. The file is read linewise using a BufferedReader.
         * If multiple lines are found, then <code>lineSeparator</code> is
         * appended between those lines.
         * 
         * @see BufferedReader
         * @param source
         *                The file to append to the buffer.
         * @param lineSeparator
         *                appended between lines as sort of line separator.
         * @throws IOException
         *                 If something is wrong with the specified file.
         */
    public StringFactory(File source, String lineSeparator) throws IOException {
	this((int) (source.length() / 80));
	append(source, lineSeparator);
    }

    /**
         * Appends the contents of the specified file into the buffer. The file
         * is read linewise using a BufferedReader. If multiple lines are found,
         * then <code>lineSeparator</code> is appended between those lines.
         * 
         * @see BufferedReader
         * @param source
         *                The file to append to the buffer.
         * @param lineSeparator
         *                appended between lines as sort of line separator.
         * @throws IOException
         *                 If something is wrong with the specified file.
         */
    public StringFactory append(File source, String lineSeparator)
	    throws IOException {
	// Gr��e sch�tzen und kapazit�t sicherstellen
	int estimatedLength = (int) (source.length() * (1 + ((double) lineSeparator
		.length()) / 80));
	ensureCapacity(_count + estimatedLength);

	// Zeilenweise laden und anh�ngen
	final FileReader reader = new FileReader(source);

	appendAll(reader, lineSeparator);
	// Datei schlie�en
	reader.close();

	return this;
    }

    /**
         * Creates a new StringFactory and appends the specified String into the
         * buffer.
         * 
         * @param s
         *                An initial string.
         */
    public StringFactory(String s) {
	this(s, 10);
    }

    /**
         * Appends <code>s</code> to the end of the buffer.
         * 
         * @param s
         *                A string to append. if <code>null</code>, then
         *                "null" will be appended.
         * @return this
         */
    public StringFactory append(String s) {
	// wenn s == null, dann 'null' anh�ngen
	if (s == null)
	    return append("null");
	// wenns nix anzuh�ngen gibt,...
	if (s.length() < 1)
	    return this;
	// Sicherstellen der Kapazit�t
	ensureCapacity(_count + s.length());
	// Anh�ngen
	s.getChars(0, s.length(), _data, _count);
	_count += s.length();
	// thats it
	return this;
    }

    /**
         * Appends <code>o.toString()</code> to the end of the buffer.
         * 
         * @param s
         *                An object to append. if <code>null</code>, then
         *                "null" will be appended.
         * @return this
         */
    public StringFactory append(Object o) {
	return o == null ? append("null") : append(o.toString());
    }

    /**
         * Ensures than the buffer can hold at lease <code>targetCapacity</code>
         * characters.
         * 
         * @param targetCapacity
         *                A number to hold at least.
         */
    public void ensureCapacity(int targetCapacity) {
	// Shortcut : hab ich schon so viel wie ben�tigt ?!?
	if (targetCapacity <= _data.length)
	    return;
	// neue Gr��e bestimmen
	int newSize = _data.length;
	while (newSize < targetCapacity) {
	    newSize *= 1.5;
	}
	// Neuen Buffer anlegen, ...
	char[] newData = new char[newSize];
	// Daten kopieren
	System.arraycopy(_data, 0, newData, 0, _count);
	// Referenz auf alten Array ersetzen
	_data = newData;
    }

    /**
         * Returns the actual capacity of the buffer.
         * 
         * @return
         */
    public int capacity() {
	return _data.length;
    }

    /**
         * Reverses the order of the characters between <code>fromIndex</code>
         * and <code>toIndex</code>.
         * 
         * @param fromIndex
         *                first character to include.
         * @param toIndex
         *                last characer to include.
         * @throws IllegalArgumentException
         *                 If fromIndex $gt; toIndex or toIndex $gt; size().
         */
    public StringFactory reverse(int fromIndex, int toIndex)
	    throws IllegalArgumentException {
	if (fromIndex == toIndex) {
	    return this;
	} else if (fromIndex > toIndex) {
	    throw new IllegalArgumentException("fromIndex > toIndex");
	} else if (toIndex >= _count) {
	    throw new IllegalArgumentException(
		    "argument 'toIndex' out of range (" + toIndex + ')');
	} else if (fromIndex < 0) {
	    throw new IllegalArgumentException(
		    "argument 'fromIndex' out of range(" + fromIndex + ')');
	}
	while (fromIndex < toIndex) {
	    char z = _data[toIndex];
	    _data[toIndex] = _data[fromIndex];
	    _data[fromIndex] = z;
	    fromIndex++;
	    toIndex--;
	}
	return this;
    }

    /**
         * Reverses the complete buffer.
         */
    public StringFactory reverse() {
	return reverse(0, _count - 1);
    }

    /**
         * Determines the first occurrence of <code>text</code> in the buffer,
         * and starts at index <code>firstIndex</code>.
         * 
         * @param text
         *                The string to search for.
         * @param firstIndex
         *                The index of the character to start search.
         * @return The index of first occurrence. If not found, then -1.
         * @throws IllegalArgumentException
         *                 if <code>text</code> is <code>null</code>,
         *                 <code>text</code> is empty or
         *                 <code>firstIndex &gt; size()</code>.
         */
    public int indexOf(String text, int firstIndex) {
	// Argumenten-Check
	if (text == null) {
	    throw new IllegalArgumentException("argument 'text' is null");
	} else if (text.length() < 1) {
	    throw new IllegalArgumentException("argument 'text' is empty");
	} else if (firstIndex < 0) {
	    throw new IllegalArgumentException("argument 'firstIndex' ["
		    + firstIndex + "] out of range [0-" + _count + ']');
	} else if (firstIndex > _count) {
	    // firstIndex == count ist erlaubt! (liefert -1)
	    throw new IllegalArgumentException("argument 'firstIndex' ["
		    + firstIndex + "] out of range [0-" + _count + ']');
	}
	// shortcut : Suchstring l�nger als Zeichen im Buffer?!?
	if (_count - firstIndex < text.length())
	    return -1;
	char[] textChars = text.toCharArray();
	bufLoop: for (int bufIndex = firstIndex; bufIndex < _count
		- text.length() + 1; bufIndex++) {
	    for (int tcIndex = 0; tcIndex < textChars.length; tcIndex++) {
		if (textChars[tcIndex] != _data[bufIndex + tcIndex]) {
		    continue bufLoop;
		}
	    }
	    // uh, durch und kein continue ?!? na dann ...
	    return bufIndex;
	}
	// ja das kann zwar nicht gehen, aber trotzdem ...
	return -1;
    }

    /**
         * Resolves variables of the form <code>${variable.name}</code> by
         * values of <<code>props</code>, where <code>value.name</code> is
         * used as key.
         * 
         * <strong>Caution ! </strong></br>
         * 
         * TODO Circular references not yet detected and resolved!
         * 
         * TODO Integrate in non static field
         * 
         * @param props
         *                key-value-pairs to detect and resolve.
         * @return this.
         * @throws ParseException
         *                 If at least one closing bracelet is missing.
         */
    public StringFactory resolveVars(Properties props) throws ParseException {

	String resolvedString = resolveVars(toString(), props);
	_count = 0;
	append(resolvedString);
	return this;
    }

    /**
         * Converts the buffer into a String. Different to StringBuffer, this
         * method does decouple the buffer array from the string representation
         * returned by this method. After this, you can modify the buffer again,
         * and later call <code>toString()</code> again.
         * 
         * @see java.lang.Object#toString()
         * @see StringBuffer#toString()
         */
    public String toString() {
	return new String(_data, 0, _count);
    }

    /**
         * Replaces part of the characters by the characters of a replacement
         * string. The replaced area will be as long as the replacement string..
         * 
         * @param fromIndex
         *                Index from which chars should be replaced..
         * @param replacement
         *                The string the replacement characters are taken from..
         * @throws IndexOutOfBoundsException
         *                 If index is higher than the buffer has characters, or
         *                 is %lt; 0.
         * @return
         */
    public StringFactory replace(int fromIndex, String replacement) {
	if (fromIndex < 0) {
	    throw new IllegalArgumentException("argument 'fromIndex' < 0");
	} else if (fromIndex > _count) {
	    throw new IllegalArgumentException(
		    "index beyond number of chars in buffer (fromIndex >"
			    + _count + ")");
	}

	if (replacement.length() == 0) {
	    return this;
	}

	int newSize = fromIndex + replacement.length();
	ensureCapacity(newSize);

	char[] repChars = replacement.toCharArray();
	System.arraycopy(repChars, 0, _data, fromIndex, repChars.length);

	_count = newSize > _count ? newSize : _count;

	return this;
    }

    /**
         * Inserts a string multiple times at a specific position.
         * 
         * @param what
         *                string to insert.
         * @param fromIndex
         *                position to insert.
         * @param howOften
         *                how often <code>what</code> shoult be inserted.
         * @return this (for chaining)
         */
    public StringFactory insert(String what, int fromIndex, int howOften) {
	// parameter-checks
	if (what == null) {
	    throw new IllegalArgumentException("argument 'what' is null");
	} else if (what.length() < 1) {
	    // short cut : doNothing
	    return this;
	} else if ((fromIndex < 0) || (fromIndex > _count)) {
	    throw new IndexOutOfBoundsException("argument 'fromIndex' ["
		    + fromIndex + "] is out of range");
	} else if (howOften < 0) {
	    throw new IllegalArgumentException("argument 'howOften' ["
		    + howOften + "] is below 0");
	} else if (howOften == 0) {
	    // shortcut : doNothing
	    return this;
	}
	// Platz schaffen durch "einf�gen" eines Platzhalters
	int insertedCharsCount = howOften * what.length();
	if (fromIndex >= _count) {
	    appendMulti('_', insertedCharsCount);
	} else {
	    insert('_', fromIndex, insertedCharsCount);
	}
	// Mehrfaches einkopieren der Characters von what
	char[] whatChars = what.toCharArray();
	int targetIndex = fromIndex;
	for (int i = 0; i < howOften; i++) {
	    System
		    .arraycopy(whatChars, 0, _data, targetIndex,
			    whatChars.length);
	    targetIndex += whatChars.length;
	}
	return this;
    }

    /**
         * Appends <code>c</code> multiple times to the buffer.
         * 
         * @param c
         *                Char to append
         * @param howMany
         *                How often the character should be appended.
         * 
         */
    public StringFactory appendMulti(char c, int howMany) {
	// parameter-checks
	if (howMany < 0) {
	    throw new IllegalArgumentException("argument 'howMany' [" + howMany
		    + "] is below 0");
	} else if (howMany == 0) {
	    // shortcut : doNothing
	    return this;
	}
	ensureCapacity(_count + howMany);
	for (int i = 0; i < howMany; i++) {
	    _data[_count + i] = c;
	}
	_count += howMany;
	return this;
    }

    /**
         * Insert <code>what</code> <code>howOften</code> times into the
         * buffer at position <code>fromIndex</code>.
         * 
         * @param what
         *                Character to insert.
         * @param fromIndex
         *                start index to insert to.
         * @param howOften
         *                Number of times the character should be inserted..
         * @return this
         */
    public StringFactory insert(char what, int fromIndex, int howOften) {
	if ((fromIndex < 0) || (fromIndex > _count)) {
	    throw new IndexOutOfBoundsException("argument 'fromIndex' ["
		    + fromIndex + "] is out of bounds");
	}
	if (howOften < 1) {
	    throw new IllegalArgumentException("argument 'howOften' ["
		    + howOften + "] is out of range");
	}
	ensureCapacity(_count + howOften);
	// hintere verschieben
	System.arraycopy(_data, fromIndex, _data, fromIndex + howOften, _count
		- fromIndex);
	// Buchstaben einf�gen
	for (int i = 0; i < howOften; i++) {
	    _data[fromIndex + i] = what;
	}
	_count += howOften;
	return this;
    }

    /**
         * removes a certain number of characters from the buffer..
         * 
         * @param fromIndex
         *                start index to remove from.
         * @param howMany
         *                Number of characters to remove.
         * @return
         */
    public StringFactory remove(int fromIndex, int howMany) {
	if ((fromIndex < 0) || (fromIndex >= _count)) {
	    throw new IndexOutOfBoundsException("argument 'fromIndex' ["
		    + fromIndex + "] is out of bounds");
	} else if (howMany < 0) {
	    throw new IllegalArgumentException("argument 'howMany' [" + howMany
		    + "] is out of range");
	}
	if (fromIndex + howMany > _count) {
	    howMany = _count - fromIndex + 1;
	}
	if (howMany == 0) {
	    // shortcut : nothing to do
	    return this;
	}
	if (fromIndex + howMany < _count) {
	    System.arraycopy(_data, fromIndex + howMany, _data, fromIndex,
		    _count - howMany - fromIndex);
	}
	_count = Math.max(0, _count - howMany);
	return this;
    }

    /**
         * Appends the elements of the array into the buffer. Between elements
         * there will be put <code>separator</code>.
         * 
         * @param array
         *                The array to append. If <code>null</code>, then
         *                "null" will be appended.
         * @param separator
         * @return
         */
    StringFactory append(Object[] array, String separator) {
	if (separator == null) {
	    throw new IllegalArgumentException("argument 'separator' is null");
	}
	// Calculate required buffer length
	int length = separator.length() * (array != null ? array.length : 0);
	if (array != null) {
	    for (int i = 0; i < array.length; i++) {
		length += (array[i] != null ? array[i].toString().length() : 0);
	    }
	}
	if (array != null) {
	    for (int i = 0; i < array.length; i++) {
		if (i > 0) {
		    append(separator);
		}
		append(array[i] != null ? array[i].toString() : "null");
	    }
	} else {
	    append("null");
	}
	return this;
    }

    /**
         * Convenience method for StringFactory.append(Object[] , ",").
         * 
         * @see StringFactory#append(Object[], String)
         * @param array
         * @return
         */
    StringFactory append(Object[] array) {
	return append(array, ",");
    }

    /**
         * Appends all lines of the given Reader to the buffer.
         * 
         * @param r
         * @param lineSeparator
         *                This string used to separate the lines read from this
         *                reader.
         */
    public void appendAll(Reader r, String lineSeparator) throws IOException {
	BufferedReader br = new BufferedReader(r);

	while (br.ready()) {
	    append(br.readLine());
	    if (lineSeparator != null)
		append(lineSeparator);
	}

    }

    /**
         * Returns the number or characters actally in the buffer.
         */
    public int length() {
	return _count;

    }

    /**
         * Converts an array of Strings to an array of chars by taking the nth
         * character of these strings.
         * 
         * @param strings
         * @param n
         *                The character index to take.
         * @param lenient
         *                if
         *                <code>true, then the nth or the last character is taken.</code>
         * @return
         * @throws IllegalArgumentException
         *                 If the index does exceed or undergo one of the
         *                 strings length.
         */
    public static char[] toCharArray(String[] strings, int n, boolean lenient)
	    throws IllegalArgumentException {

	if (n < 0) {
	    throw new IllegalArgumentException("argument 'n' is " + n);
	} else if (strings == null) {
	    return null;
	} else if (strings.length == 0) {
	    return new char[0];
	}
	if (!lenient) {
	    // check length of Strings
	    for (int i = 0; i < strings.length; i++) {
		if (strings[i] == null) {
		    throw new IllegalArgumentException("position " + i
			    + " of argument 'strings' is null");
		} else if (strings[i].length() <= n) {
		    throw new IllegalArgumentException("position " + i
			    + " of argument 'strings' (" + strings[i]
			    + ") is too short - must be at least " + n
			    + " chars long");
		}
	    }
	}

	char[] result = new char[strings.length];

	for (int i = 0; i < strings.length; i++) {
	    int index = Math.min (n,strings[i].length() -1) ;
	    result[i] = strings[i].charAt(index) ;
	}
	return result;
    }
}