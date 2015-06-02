package org.hartorn.htf.handler.request;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
import org.hartorn.htf.util.JsonHelper;
import org.hartorn.htf.util.Pair;

/**
 * Main class for handling the request, directing to right method and controller.
 *
 * @author Hartorn
 *
 */
public final class HtfRequestHandler extends HttpServlet {
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
    public HtfRequestHandler() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.GenericServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return HtfRequestHandler.SERVLET_INFO;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.GenericServlet#init()
     */
    @Override
    public void init() throws ServletException {
        HtfRequestHandler.LOG.info("HTF - Initialising ...");
        // Initialise the servlet resources
        final Set<Class<?>> controllers = AnnotationHelper.getAnnotatedClasses(HtfController.class);
        try {
            this.pathResolver = new UrlResolver(controllers);
        } catch (final ImplementationException e) {
            throw new ServletException(e);
        }
        HtfRequestHandler.LOG.info("HTF - Initialisation finished");
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
            HtfRequestHandler.LOG.error("Exception happened while handling request from [{}] with HttpVerb [{}]", req.getRequestURL(), verb.name());
            HtfRequestHandler.LOG.error(e);
            throw new ServletException(e);
        }
    }

    private HtfResponse getHtfResponse(final Object controller, final Method method, final Object... methodParams) throws ImplementationException {
        HtfRequestHandler.LOG.debug("Invoking controller [{}] with method [{}]", controller.getClass().getCanonicalName(), method.getName());

        try {
            return (HtfResponse) method.invoke(controller, methodParams);
        } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            HtfRequestHandler.LOG.error(e);
            throw new ImplementationException("Error while invocating controller", e);
        }
    }

    private void handleRequest(final HttpVerbs verb, final HttpServletRequest req, final HttpServletResponse resp) throws ImplementationException,
    IOException {
        HtfRequestHandler.LOG.debug("Handling request from [{}] with HttpVerb [{}]", req.getRequestURL(), verb.name());

        // 1 - Identify controller and method
        final Pair<Object, Method> ctrlMethod = this.pathResolver.resolveRequest(req, verb);

        // 1 - Identify Content type, and build basics arguments
        if ("application/json".equals(req.getContentType())) {
            req.getParameterMap();
        }

        final Type[] args = ctrlMethod.right().getGenericParameterTypes();
        Object argToCall = null;
        for (final Type arg : args) {
            HtfRequestHandler.LOG.debug("Type :{}", arg.getTypeName());
            if (arg instanceof ParameterizedType) {
                final ParameterizedType type = (ParameterizedType) arg;
                HtfRequestHandler.LOG.debug("Parametized type :{}", type.getTypeName());
                argToCall = JsonHelper.getObjectFromJson(req, type);
            }

            // ParameterizedTypeImpl typeImpl = new ParameterizedTypeImpl();
            // typeImpl.
            // final Type fooType = new TypeToken<Foo<Bar>>() {
            // }.getType();
            //
            // }
            // for (final Type t : argTypes) {
            // HtfRequestHandler.LOG.debug("ArgType :{}", t.getTypeName());
            // }

        }
        // 3 - Get the answer
        final HtfResponse htfResponse = this.getHtfResponse(ctrlMethod.left(), ctrlMethod.right(), argToCall);

        // 4 - Write the answer
        HtfRequestHandler.LOG.debug("Writing response for request from [{}] with HttpVerb [{}]", req.getRequestURL(), verb.name());
        htfResponse.doWriteResponse(req, resp);
        HtfRequestHandler.LOG.debug("Finished handling request from [{}] with HttpVerb [{}]", req.getRequestURL(), verb.name());
    }
}
