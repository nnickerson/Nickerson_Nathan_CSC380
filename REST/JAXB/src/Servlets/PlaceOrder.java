package Servlets;

import Models.Restaurant;
import Models.Restaurants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bwatson
 * Date: 7/25/13
 * Time: 5:56 PM
 * To change this template use File | Settings | File Templates.
 */



@WebServlet("/PlaceOrder")
public class PlaceOrder extends HttpServlet {

    static {
        File file = new File(Class.class.getClassLoader().getResource("resources/FoodPlaces.xml"));
    }

    private Restaurants restaurants = new Restaurants();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Yeah they ordered");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Scanner scan = new Scanner(file);
//        String xml = "";
//
//        while (scan.hasNext()) {
//            xml += scan.nextLine();
//        }
//        System.out.println(xml);
//
//
//        response.setContentType("text/xml");
//        response.setContentLength(xml.length());
//        response.getWriter().print(xml);

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance("Models");
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Object object = unmarshaller.unmarshal(file);
            System.out.println("UNMARSHALLED SUCCESSFULLY!");
        } catch (JAXBException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        System.out.println("CATALINA BASE: " + System.getProperty("catalina.base"));

        List<Restaurant> rest = new ArrayList<Restaurant>();
        Restaurant resta = new Restaurant();
        resta.setName("Boby");
        rest.add(resta);
        resta.setName("Moby");
        rest.add(resta);
        resta.setName("Moby");
        rest.add(resta);

        restaurants.setRestaurants(rest);
        System.out.println("Yeah they ordered");
        request.setAttribute("restaurants", restaurants.getRestaurants());
        getServletContext().getRequestDispatcher("/PlaceOrder.jsp")
                .forward(request, response);
    }
}
