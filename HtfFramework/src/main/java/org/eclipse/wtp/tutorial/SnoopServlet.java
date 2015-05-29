package org.eclipse.wtp.tutorial;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SnoopServlet extends HttpServlet {
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
}
