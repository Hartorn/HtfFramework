package org.eclipse.wtp.tutorial;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServlet;

import org.hartorn.htf.annotation.FromUrl;
import org.hartorn.htf.annotation.HtfController;
import org.hartorn.htf.annotation.HtfMethod;
import org.hartorn.htf.annotation.HttpVerbs;
import org.hartorn.htf.handler.request.DebugResponse;
import org.hartorn.htf.handler.request.HtfResponse;
import org.hartorn.htf.handler.request.JsonResponse;

/**
 * TestServlet : Servlet used for testing.
 *
 * @author Hartorn
 *
 */
@HtfController(address = "/test/bla")
public final class TestServlet extends HttpServlet {
    public class Bidule {
        BigInteger d;
        BigDecimal e;
        List<String> f;
    }

    public class Machin {
        Integer a;
        BigDecimal b;
        String c;
    }

    public class Truc {
        Integer num;
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

    @HtfMethod(address = "/test7", httpVerbs = { HttpVerbs.GET, HttpVerbs.POST })
    public HtfResponse test7(@FromUrl final int id, final Collection<Truc> bla, final Truc trucMuche) {
        for (final Truc truc : bla) {
            System.out.println("num:" + String.valueOf(truc.num));
            System.out.println("bla:" + String.valueOf(truc.bla));
            System.out.println("sum:" + String.valueOf(truc.sum));
            System.out.println();
        }
        System.out.println("Id :" + id);
        final List<Truc> trucList = new ArrayList<Truc>();
        trucList.addAll(bla);
        trucList.add(trucMuche);

        return new JsonResponse<List<Truc>>(trucList);
    }

    @HtfMethod(address = "/test8", httpVerbs = { HttpVerbs.GET, HttpVerbs.POST })
    public HtfResponse test8(final Bidule bidule, final Collection<Truc> bla, final Machin machin) {
        for (final Truc truc : bla) {
            System.out.println("num:" + String.valueOf(truc.num));
            System.out.println("bla:" + String.valueOf(truc.bla));
            System.out.println("sum:" + String.valueOf(truc.sum));
            System.out.println();
        }
        final List<Truc> trucList = new ArrayList<Truc>();
        trucList.addAll(bla);
        bidule.f = new ArrayList<String>();
        bidule.f.add("Un");
        bidule.f.add("deux");
        bidule.f.add("trois");
        bidule.e = new BigDecimal(1000002534.65);
        bidule.d = new BigInteger("1500000000000000000000");
        return new JsonResponse<Bidule>(bidule);
    }
}
