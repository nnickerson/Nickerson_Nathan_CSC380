package servlet;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import service.RestaurantImpl;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.xml.ws.Endpoint;

/**
 * Created with IntelliJ IDEA.
 * User: nnickerson
 * Date: 8/12/13
 * Time: 4:34 AM
 * To change this template use File | Settings | File Templates.
 */

@WebServlet("/RestaurantService/*")
public class RestaurantServlet extends CXFNonSpringServlet {
    @Override
    protected void loadBus(ServletConfig sc) {
        super.loadBus(sc);

        Bus bus = getBus();
        BusFactory.setDefaultBus(bus);
        Endpoint.publish("/RestaurantService", new RestaurantImpl());
    }
}
