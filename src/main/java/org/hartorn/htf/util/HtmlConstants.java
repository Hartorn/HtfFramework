package org.hartorn.htf.util;

/**
 * Class registering all the useful constants for HTML.
 *
 * @author Hartorn
 *
 */
public enum HtmlConstants {
    ;
    /**
     * Enumeration of the HTTP ContentType.
     *
     * @author Hartorn
     *
     */
    public enum ContentType {
        /**
         * "multipart/form-data".
         */
        FILE("multipart/form-data"),
        /**
         * "application/json".
         */
        JSON("application/json");

        private final String contentType;

        private ContentType(final String contentTypeArg) {
            this.contentType = contentTypeArg;
        }

        /**
         * Tell if the given content type is the enum one.
         *
         * @param content
         *            the strign to test
         * @return true if it is the given content, else false
         */
        public final boolean isContentType(final String content) {
            return this.contentType.equalsIgnoreCase(content);
        }

        @Override
        public final String toString() {
            return this.contentType;
        }
    }

    /**
     * Enumeration of HTTP headers.
     *
     * @author Hartorn
     *
     */
    public enum Header {
        /**
         * "content-disposition".
         */
        CONTENT_DISPOSITION("content-disposition");
        private final String disposition;

        private Header(final String dispositionArg) {
            this.disposition = dispositionArg;
        }

        @Override
        public final String toString() {
            return this.disposition;
        }

    }

}
