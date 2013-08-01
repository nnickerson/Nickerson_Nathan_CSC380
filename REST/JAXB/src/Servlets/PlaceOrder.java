package Servlets;

import Models.MenuItem;
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
import java.io.*;
import java.util.Scanner;


/**
 * Created with IntelliJ IDEA.
 * User: bwatson
 * Date: 7/25/13
 * Time: 5:56 PM
 */
@WebServlet("/PlaceOrder")
public class PlaceOrder extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                                 System.out.println("We've received an order! Get busy!");

        BufferedReader in = request.getReader();
        Restaurants rests = null;

        try {
            JAXBContext context = JAXBContext.newInstance("Models");
            Unmarshaller un = context.createUnmarshaller();
            rests = (Restaurants) un.unmarshal(in);
            in.close();
            System.out.println("Unmarshalled the xml successfully");

            Restaurant r = rests.getRestaurant().get(0);
            MenuItem mi = r.getMenu().getMenuItem().get(0);
            System.out.println("Get this guy a " + mi.getName() + " from the " + r.getName() + " restaurant!");
        } catch (JAXBException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            System.out.println("You've called my GET!");
            JAXBContext context = JAXBContext.newInstance("Models");
            Unmarshaller un = context.createUnmarshaller();
            Restaurants rests = (Restaurants) un.unmarshal(this.getClass().getClassLoader().getResource("Models/RestaurantXML.xml"));

        } catch (JAXBException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        File file = new File(this.getClass().getClassLoader().getResource("Models/RestaurantXML.xml").getPath());
        Scanner scan = new Scanner(file);
        String filexml = "";
        while (scan.hasNext()) {
            filexml += scan.nextLine();
        }


        response.setContentType("text/xml");
        response.setContentLength(filexml.length());
        response.getWriter().write(filexml);

    }
}
