package org.hartorn.htf.annotation;

/**
 * Enumeration of the HttpVerbs (DELETE, GET, HEAD, OPTIONS, POST, PUT, TRACE).
 *
 * @author Hartorn
 * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html">W3C Method Definitions</a>
 *
 */
public enum HttpVerbs {
    /**
     * DELETE : Verb used in REST to delete object.
     */
    DELETE,
    /**
     * GET : Verb used in REST to read object (id in URL).
     */
    GET,
    /**
     * HEAD : Verb, identical as GET, but no message-body in the response (same HTTP Headers as for GET). Used for getting meta-information, or for
     * testing hypertext links for validity, accessibility, and recent modification.
     */
    HEAD,
    /**
     * POST : Verb used in REST to create an object (data in BODY).
     */
    POST,
    /**
     * PUT : Verb used in REST to update an object (id in URL, data in BODY).
     */
    PUT;
}
