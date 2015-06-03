package org.eclipse.wtp.tutorial;

import java.math.BigDecimal;
import java.util.Collection;

import javax.servlet.http.HttpServlet;

import org.hartorn.htf.annotation.FromUrl;
import org.hartorn.htf.annotation.HtfController;
import org.hartorn.htf.annotation.HtfMethod;
import org.hartorn.htf.annotation.HtfMethod.HttpVerbs;
import org.hartorn.htf.handler.request.DebugResponse;
import org.hartorn.htf.handler.request.HtfResponse;

/**
 * TestServlet : Servlet used for testing.
 *
 * @author Hartorn
 *
 */
@HtfController(address = "/test/bla")
public final class TestServlet extends HttpServlet {
    public class Truc {
        int num;
        BigDecimal sum;
        String bla;
    }

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

    @HtfMethod(address = "/hahah")
    public HtfResponse test() {
        return new DebugResponse();
    }

    @HtfMethod(address = "/test2", httpVerbs = HttpVerbs.GET)
    public HtfResponse test2() {
        return new DebugResponse();
    }

    @HtfMethod(address = "/test3/", httpVerbs = { HttpVerbs.GET, HttpVerbs.POST })
    public HtfResponse test3() {
        return new DebugResponse();
    }

    @HtfMethod(httpVerbs = { HttpVerbs.GET, HttpVerbs.POST })
    public HtfResponse test4(final Collection<Truc> bla) {
        for (final Truc truc : bla) {

            System.out.println("num:" + String.valueOf(truc.num));
            System.out.println("bla:" + String.valueOf(truc.bla));
            System.out.println("sum:" + String.valueOf(truc.sum));
            System.out.println();
        }
        return new DebugResponse();
    }

    @HtfMethod(address = "/test5", httpVerbs = { HttpVerbs.GET, HttpVerbs.POST })
    public HtfResponse test5(@FromUrl final int id, final Collection<Truc> bla) {
        for (final Truc truc : bla) {

            System.out.println("num:" + String.valueOf(truc.num));
            System.out.println("bla:" + String.valueOf(truc.bla));
            System.out.println("sum:" + String.valueOf(truc.sum));
            System.out.println();
        }
        return new DebugResponse();
    }

    @HtfMethod(address = "/test6", httpVerbs = { HttpVerbs.GET, HttpVerbs.POST })
    public HtfResponse test6(@FromUrl final int id1, @FromUrl final int id2, final Collection<Truc> bla) {
        for (final Truc truc : bla) {

            System.out.println("num:" + String.valueOf(truc.num));
            System.out.println("bla:" + String.valueOf(truc.bla));
            System.out.println("sum:" + String.valueOf(truc.sum));
            System.out.println();
        }
        return new DebugResponse();
    }
}
