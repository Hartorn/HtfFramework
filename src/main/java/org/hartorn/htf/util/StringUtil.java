package org.hartorn.htf.util;

/**
 * Utility class to for string.
 *
 * @author Hartorn
 *
 */
public enum StringUtil {
    ;
    /**
     * The Empty String (same as "").
     */
    public static final String EMPTY = "";

    private static final String NULL = "null";

    private StringUtil() {
        // helper, private constructor
    }

    /**
     * Return an empty string if null, else the string itself.
     *
     * @param toTest
     *            the string to test (can be null)
     * @return the string, or the empty string if null
     */
    public static String emptyIfNull(final String toTest) {
        if (toTest == null) {
            return StringUtil.EMPTY;
        }
        return toTest;
    }

    /**
     * Method testing if a string is null, is empty, or contains only whitespace.
     *
     * @param toTest
     *            the string to test (can be null)
     * @return true is the string is null or empty, false else
     */
    public static boolean isEmpty(final String toTest) {
        return (toTest == null) || toTest.trim().isEmpty();
    }

    /**
     * Method converting any object to a string, returning 'null' for the null pointer, else calling the toString method.
     *
     * @param <O>
     *            type of the object
     *
     * @param object
     *            the object to convert to string
     * @return the string of the object
     */
    public static <O extends Object> String stringify(final O object) {
        if (object == null) {
            return StringUtil.NULL;
        }
        return object.toString();
    }

    /**
     * Function stripping the given character, at the beginning and at the end of the string.
     *
     * @param toStrip
     *            the string to strip
     *
     * @param character
     *            the character to strip
     *
     * @return the stripped string
     */
    public static String strip(final String toStrip, final char character) {
        final String stringChar = String.valueOf(character);
        final String url = StringUtil.emptyIfNull(toStrip).trim();
        if (url.isEmpty()) {
            return url;
        }
        int beginIndex = 0;
        int endIndex = url.length();
        // Remove the slash, if begin with slash
        if (url.startsWith(stringChar)) {
            beginIndex = Math.min(beginIndex + 1, url.length() - 1);
        }
        // Remove the slash at the end, if end with slash
        if (url.endsWith(stringChar)) {
            endIndex = Math.max(1, endIndex - 1);
        }

        return url.substring(beginIndex, endIndex);
    }

    /**
     * Function stripping the given character, at the beginning and at the end of the string.
     *
     * @param toStrip
     *            the string to strip
     *
     * @param character
     *            the character to strip
     *
     * @return the stripped string
     */
    public static String stripAndToLower(final String toStrip, final char character) {

        return StringUtil.strip(toStrip, character).toLowerCase();
    }
}
