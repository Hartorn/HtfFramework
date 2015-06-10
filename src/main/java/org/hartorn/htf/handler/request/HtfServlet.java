package org.hartorn.htf.handler.request;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hartorn.htf.annotation.AnnotationHelper;
import org.hartorn.htf.annotation.HtfController;
import org.hartorn.htf.annotation.HttpVerbs;
import org.hartorn.htf.exception.ImplementationException;
import org.hartorn.htf.handler.path.UrlResolver;
import org.hartorn.htf.util.Pair;

/**
 * Main class for handling the request, directing to right method and controller.
 *
 * @author Hartorn
 *
 */
@WebServlet(urlPatterns = { "/*" }, loadOnStartup = 1)
public final class HtfServlet extends HttpServlet {
    /**
     * Serial ID.
     */
    private static final long serialVersionUID = -6533912768946164886L;
    private static final Logger LOG = LogManager.getLogger();

    private static final String SERVLET_INFO = "HtfRequestHandler, part of Htf Framework (author Hartorn)";

    private UrlResolver pathResolver;

    /**
     * Constructor.
     */
    public HtfServlet() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return HtfServlet.SERVLET_INFO;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#init()
     */
    @Override
    public void init() throws ServletException {
        HtfServlet.LOG.info("HTF - Initialising ...");
        // Initialise the servlet resources
        final Set<Class<?>> controllers = AnnotationHelper.getAnnotatedClasses(HtfController.class);
        try {
            this.pathResolver = new UrlResolver(controllers);
        } catch (final ImplementationException e) {
            throw new ServletException(e);
        }
        HtfServlet.LOG.info("HTF - Initialisation finished");
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
            HtfServlet.LOG.error("Exception happened while handling request from [{}] with HttpVerb [{}]", req.getRequestURL(), verb.name());
            HtfServlet.LOG.error(e);
            throw new ServletException(e);
        }
    }

    private void handleRequest(final HttpVerbs verb, final HttpServletRequest req, final HttpServletResponse resp) throws ImplementationException {
        HtfServlet.LOG.debug("HTF - Handling request from [{}] with HttpVerb [{}]", req.getRequestURL(), verb.name());

        // 1 - Identify controller and method
        final Pair<Object, Method> ctrlMethod = this.pathResolver.resolveRequest(req, verb);

        final Method method = ctrlMethod.right();
        final Object ctrl = ctrlMethod.left();

        // 2 - Build arguments for invocation
        final Object[] argToCall = HtfRequestHandler.getMethodParametersFromRequest(method, req);

        // 3 - Invoke, and get the answer
        final HtfResponse htfResponse = HtfRequestHandler.getHtfResponse(ctrl, method, argToCall);

        // 4 - Write the answer
        HtfServlet.LOG.debug("HTF - Writing response for request from [{}] with HttpVerb [{}]", req.getRequestURL(), verb.name());
        htfResponse.doWriteResponse(req, resp);
        HtfServlet.LOG.debug("HTF - Finished handling request from [{}] with HttpVerb [{}]", req.getRequestURL(), verb.name());
    }

}
