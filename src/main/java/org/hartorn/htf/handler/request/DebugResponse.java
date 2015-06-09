package org.hartorn.htf.handler.request;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hartorn.htf.exception.ImplementationException;

/**
 * DebugResponse is used for test, and show the content of the http request received.
 *
 * @author Hartorn
 *
 */
public class DebugResponse implements HtfResponse {

    /**
     * Constructor.
     */
    public DebugResponse() {
        // Nothing to do
    }

    @Override
    public void doWriteResponse(final HttpServletRequest request, final HttpServletResponse response) throws ImplementationException {
        PrintWriter out;
        try {
            out = response.getWriter();
        } catch (final IOException e1) {
            throw new ImplementationException(e1);
        }
        response.setContentType("text/plain");

        out.println("Snoop Servlet");
        out.println();
        Enumeration e;
        out.println("Request attributes:");
        e = request.getAttributeNames();
        while (e.hasMoreElements()) {
            final String key = (String) e.nextElement();
            final Object value = request.getAttribute(key);
            out.println("   " + key + " = " + value);
        }
        out.println();
        out.println("Protocol: " + request.getProtocol());
        out.println("Scheme: " + request.getScheme());
        out.println("Server Name: " + request.getServerName());
        out.println("Server Port: " + request.getServerPort());
        out.println("Remote Addr: " + request.getRemoteAddr());
        out.println("Remote Host: " + request.getRemoteHost());
        out.println("Character Encoding: " + request.getCharacterEncoding());
        out.println("Content Length: " + request.getContentLength());
        out.println("Content Type: " + request.getContentType());
        out.println("Locale: " + request.getLocale().toString());
        out.println("Default Response Buffer: " + response.getBufferSize());
        out.println();
        out.println("Parameter names in this request:");
        e = request.getParameterNames();
        while (e.hasMoreElements()) {
            final String key = (String) e.nextElement();
            final String[] values = request.getParameterValues(key);
            out.print("   " + key + " = ");
            for (final String value : values) {
                out.print(value + " ");
            }
            out.println();
        }
        out.println();
        out.println("Headers in this request:");
        e = request.getHeaderNames();
        while (e.hasMoreElements()) {
            final String key = (String) e.nextElement();
            final String value = request.getHeader(key);
            out.println("   " + key + ": " + value);
        }
        out.println();
        out.println("Cookies in this request:");
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (final Cookie cookie : cookies) {
                out.println("   " + cookie.getName() + " = " + cookie.getValue());
            }
        }
        out.println();

        out.println("Request Is Secure: " + request.isSecure());
        out.println("Auth Type: " + request.getAuthType());
        out.println("HTTP Method: " + request.getMethod());
        out.println("Remote User: " + request.getRemoteUser());
        out.println("Request URI: " + request.getRequestURI());
        out.println("Context Path: " + request.getContextPath());
        out.println("Servlet Path: " + request.getServletPath());
        out.println("Path Info: " + request.getPathInfo());
        out.println("Path Trans: " + request.getPathTranslated());
        out.println("Query String: " + request.getQueryString());

        out.println();
        final HttpSession session = request.getSession();
        out.println("Requested Session Id: " + request.getRequestedSessionId());
        out.println("Current Session Id: " + session.getId());
        out.println("Session Created Time: " + session.getCreationTime());
        out.println("Session Last Accessed Time: " + session.getLastAccessedTime());
        out.println("Session Max Inactive Interval Seconds: " + session.getMaxInactiveInterval());
        out.println();
        out.println("Session values: ");
        final Enumeration names = session.getAttributeNames();
        while (names.hasMoreElements()) {
            final String name = (String) names.nextElement();
            out.println("   " + name + " = " + session.getAttribute(name));
        }
    }

}
