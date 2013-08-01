package client;

import Models.Restaurants;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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
        HttpURLConnection myConnection = null;
        HttpURLConnection myOutConnection = null;
        URL myURL = null;
        URL myOutURL = null;
        String nextLine = null;
        String xml = "";
        try {
            myURL = new URL("http://localhost:8080/rest/PlaceOrder");
            myOutURL = new URL("http://localhost:8080/rest/PlaceOrder");


            myConnection = (HttpURLConnection) myURL.openConnection();
            myConnection.setRequestMethod("GET");
            myConnection.setReadTimeout(100000);
            myConnection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
//            while((nextLine = in.readLine()) != null) {
//                 xml += "\n" + nextLine;
//            }

//            System.out.println(xml);


            try {
                JAXBContext context = JAXBContext.newInstance("Models");
                Unmarshaller un = context.createUnmarshaller();
//                File newXMLfile = new File("buttface.xml");
//                PrintWriter pw = new PrintWriter(newXMLfile);
//                pw.write(xml);
//                pw.flush();
//                pw.close();
//                Scanner s = new Scanner(newXMLfile);
//                while(s.hasNextLine()) {
//                    System.out.println(s.nextLine());
//                }
                StringReader myXML = new StringReader(xml);
                rests = (Restaurants) un.unmarshal(in);
                in.close();
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
            myConnection.disconnect();
            myConnection = null;

            myConnection = (HttpURLConnection) myOutURL.openConnection();
            myConnection.setRequestMethod("POST");
            myConnection.setDoOutput(true);
            myConnection.setReadTimeout(100000);
            myConnection.connect();
            String results = "<Restaurants>\n" + "<Restaurant name = \"" + rests.getRestaurant().get(rIndex).getName() + "\">\n" +
                    "<Menu>\n" + "<MenuItem name=\"" + rests.getRestaurant().get(rIndex).getMenu().getMenuItem().get(iIndex).getName() + "\" price=\"3.65\" discription=\"A big slice of pepperoni pizza\"/>\n" +
                    "</Menu>\n" + "</Restaurant>\n" +"</Restaurants>";
            PrintWriter pw = new PrintWriter(myOutConnection.getOutputStream());
            pw.println(results);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            System.out.println("HttpURLConnection could not connect! - " + e.getMessage());
        }

    }
}
