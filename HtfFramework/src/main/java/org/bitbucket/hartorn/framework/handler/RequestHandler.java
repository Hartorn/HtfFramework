package org.bitbucket.hartorn.framework.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bitbucket.hartorn.framework.annotations.HttpVerb.HttpVerbs;

/**
 * Main class for handling the request, directing to right method and controller.
 *
 * @author Hartorn
 *
 */
public final class RequestHandler extends HttpServlet {
    /**
     * Serial ID.
     */
    private static final long serialVersionUID = -6533912768946164886L;
    private static final String SERVLET_INFO = "RequestHandler, part of Htf Framework (author Hartorn)";

    /**
     * Constructor.
     */
    public RequestHandler() {
        super();
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.GenericServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return RequestHandler.SERVLET_INFO;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.GenericServlet#init()
     */
    @Override
    public void init() throws ServletException {
        // Initialise the servlet resources
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.doHandleRequest(HttpVerbs.DELETE, req, resp);
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.doHandleRequest(HttpVerbs.GET, req, resp);
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doHead(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doHead(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.doHandleRequest(HttpVerbs.HEAD, req, resp);
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.doHandleRequest(HttpVerbs.POST, req, resp);
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.doHandleRequest(HttpVerbs.PUT, req, resp);
    }

    private void doHandleRequest(final HttpVerbs verb, final HttpServletRequest req, final HttpServletResponse resp) {
        // 1 - Wrap parameters

        // 2 - Identify controller and method

        // 3 - Get answer, and write response
    }
}
