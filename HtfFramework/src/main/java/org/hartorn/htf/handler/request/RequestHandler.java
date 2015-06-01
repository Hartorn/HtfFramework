package org.hartorn.htf.handler.request;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hartorn.htf.annotation.AnnotationHelper;
import org.hartorn.htf.annotation.HtfController;
import org.hartorn.htf.annotation.HtfMethod.HttpVerbs;
import org.hartorn.htf.exception.ImplementationException;
import org.hartorn.htf.handler.path.UrlResolver;
import org.hartorn.htf.util.Pair;

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
    private static final Logger LOG = LogManager.getLogger();

    private static final String SERVLET_INFO = "RequestHandler, part of Htf Framework (author Hartorn)";

    private UrlResolver pathResolver;

    /**
     * Constructor.
     */
    public RequestHandler() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.GenericServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return RequestHandler.SERVLET_INFO;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.GenericServlet#init()
     */
    @Override
    public void init() throws ServletException {
        // Initialise the servlet resources
        final Set<Class<?>> controllers = AnnotationHelper.getAnnotatedClasses(HtfController.class);
        try {
            this.pathResolver = new UrlResolver(controllers);
        } catch (final ImplementationException e) {
            throw new ServletException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.doHandleRequest(HttpVerbs.DELETE, req, resp);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.doHandleRequest(HttpVerbs.GET, req, resp);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServlet#doHead(javax.servlet.http.HttpServletRequest , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doHead(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.doHandleRequest(HttpVerbs.HEAD, req, resp);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.doHandleRequest(HttpVerbs.POST, req, resp);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.doHandleRequest(HttpVerbs.PUT, req, resp);
    }

    private void doHandleRequest(final HttpVerbs verb, final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {
        try {
            this.handleRequest(verb, req, resp);
        } catch (final ImplementationException e) {
            RequestHandler.LOG.error("Exception happened while handling request from {0} with HttpVerb {1}", req.getRequestURL(), verb.name());
            RequestHandler.LOG.error(e);
            throw new ServletException(e);
        }
    }

    private HtfResponse getHtfResponse(final Object controller, final Method method, final Object... methodParams) throws ImplementationException {
        RequestHandler.LOG.debug("Invoking controller {0} with method {1}", controller.getClass().getCanonicalName(), method.getName());

        try {
            return (HtfResponse) method.invoke(controller, methodParams);
        } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            RequestHandler.LOG.error(e);
            throw new ImplementationException("Error while invocating controller", e);
        }
    }

    private void handleRequest(final HttpVerbs verb, final HttpServletRequest req, final HttpServletResponse resp) throws ImplementationException {
        RequestHandler.LOG.debug("Handling request from {0} with HttpVerb {1}", req.getRequestURL(), verb.name());

        // 1 - Identify controller and method
        final Pair<Object, Method> ctrlMethod = this.pathResolver.resolveRequest(req, verb);

        // 3 - Get the answer
        final HtfResponse htfResponse = this.getHtfResponse(ctrlMethod.left(), ctrlMethod.right());

        // 4 - Write the answer
        RequestHandler.LOG.debug("Writing response for request from {0} with HttpVerb {1}", req.getRequestURL(), verb.name());
        htfResponse.doWriteResponse(req, resp);
        RequestHandler.LOG.debug("Finished handling request from {0} with HttpVerb {1}", req.getRequestURL(), verb.name());
    }
}
