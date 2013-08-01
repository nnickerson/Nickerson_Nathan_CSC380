package client;

import Models.Restaurants;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: nnickerson
 * Date: 7/30/13
 * Time: 6:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class Client {

    private Restaurants rests;

    public Client() {
        createClient();
    }

    public void createClient() {
        URLConnection myConnection = null;
        URLConnection myOutConnection = null;
        URL myURL = null;
        URL myOutURL = null;
        String nextLine = null;
        String xml = "";
        try {
            myURL = new URL("http://localhost:8080/rest/PlaceOrder");
            myOutURL = new URL("http://localhost:8080/rest/PlaceOrder");


            myConnection = (HttpURLConnection) myURL.openConnection();
            myConnection.setReadTimeout(100000);
            myConnection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));


            try {
                JAXBContext context = JAXBContext.newInstance("Models");
                Unmarshaller un = context.createUnmarshaller();
                StringReader myXML = new StringReader(xml);
                rests = (Restaurants) un.unmarshal(in);
                in.close();
                myConnection.getInputStream().close();
                System.out.println("Unmarshalled the xml successfully");
            } catch (JAXBException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                System.out.println("Could not unmarshal the xml!!!!!");
            }


            Scanner scan = new Scanner(System.in);
            System.out.println("Please enter the resturant you want:");
            for (int i = 0; i < rests.getRestaurant().size(); i++) {
                System.out.println(i + ": " + rests.getRestaurant().get(i).getName());
            }
            String userInput = scan.nextLine();

            System.out.println("Which item would you like?");

            int rIndex = Integer.parseInt(userInput);

            for (int i = 0; i < rests.getRestaurant().get(rIndex).getMenu().getMenuItem().size(); i++) {
                System.out.println(i + ": " + rests.getRestaurant().get(rIndex).getMenu().getMenuItem().get(i).getName());
            }

            userInput = scan.nextLine();

            int iIndex = Integer.parseInt(userInput);
            System.out.println("Got your selection");

            myURL = null;
            myOutURL = null;
            myConnection = null;
            myOutConnection = null;
            in = null;

            myURL = new URL("http://localhost:8080/rest/PlaceOrder");

            HttpURLConnection postConnection = (HttpURLConnection)myURL.openConnection();
            postConnection.setRequestMethod("POST");
            postConnection.setDoOutput(true);
            postConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

            String results = "<Restaurants>\n" + "<Restaurant name = \"" + rests.getRestaurant().get(rIndex).getName() + "\">\n" +
                    "<Menu>\n" + "<MenuItem name=\"" + rests.getRestaurant().get(rIndex).getMenu().getMenuItem().get(iIndex).getName() + "\" price=\"3.65\" discription=\"A big slice of pepperoni pizza\"/>\n" +
                    "</Menu>\n" + "</Restaurant>\n" +"</Restaurants>";
            postConnection.setRequestProperty( "Content-Length", "" + results.length());
            postConnection.connect();

            PrintWriter pw = new PrintWriter(postConnection.getOutputStream());
            pw.write(results);
            pw.flush();
            pw.close();
            postConnection.getResponseMessage();

            System.out.println("Order sent!");
        } catch (Exception e) {
            System.out.println("HttpURLConnection could not connect! - " + e.getMessage());
        }

    }
}
