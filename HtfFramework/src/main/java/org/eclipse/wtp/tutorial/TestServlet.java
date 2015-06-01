package org.eclipse.wtp.tutorial;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hartorn.htf.annotation.HtfController;
import org.hartorn.htf.annotation.HtfMethod;
import org.hartorn.htf.annotation.HtfMethod.HttpVerbs;
import org.hartorn.htf.handler.request.DebugResponse;
import org.hartorn.htf.handler.request.JsonResponse;

/**
 * TestServlet : Servlet used for testing.
 *
 * @author Hartorn
 *
 */
@HtfController(address = "/test/bla")
public final class TestServlet extends HttpServlet {
    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 5033982325853799184L;

    /**
     * Constructor.
     */
    public TestServlet() {
        super();
    }

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String userAgent = req.getHeader("user-agent");
        String clientBrowser = "Not known!";
        if (userAgent != null) {
            clientBrowser = userAgent;
        }
        req.setAttribute("client.browser", clientBrowser);
        req.getRequestDispatcher("/showBrowser.jsp").forward(req, resp);
    }

    @HtfMethod(adress = "/hahah")
    public DebugResponse test() {

        return new DebugResponse();
    }

    @HtfMethod(adress = "/test2", httpVerbs = HttpVerbs.GET)
    public DebugResponse test2() {

        return new DebugResponse();
    }

    @HtfMethod(adress = "/test3/", httpVerbs = { HttpVerbs.GET, HttpVerbs.POST })
    public JsonResponse test3() {
        return null;

    }
}
