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
    private static final String SLASH = "/";

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
     * Function stripping "/", at the beginning and at the end of the string.
     *
     * @param urlPart
     *            the string to strip
     * @return the stripped string
     */
    public static String stripSlash(final String urlPart) {
        final String url = StringUtil.emptyIfNull(urlPart).trim().toLowerCase();
        if (url.isEmpty()) {
            return null;
        }
        int beginIndex = 0;
        int endIndex = urlPart.length();
        // Remove the slash, if begin with slash
        if (url.startsWith(StringUtil.SLASH)) {
            beginIndex = Math.min(beginIndex + 1, url.length() - 1);
        }
        // Remove the slash at the end, if end with slash
        if (url.endsWith(StringUtil.SLASH)) {
            endIndex = Math.max(1, endIndex - 1);
        }

        return url.substring(beginIndex, endIndex);
    }
}
