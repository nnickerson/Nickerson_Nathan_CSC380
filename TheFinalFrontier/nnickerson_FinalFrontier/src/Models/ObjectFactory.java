
package Models;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the Models package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetMenuItem_QNAME = new QName("http://localhost/RestaurantService", "getMenuItem");
    private final static QName _GetMenuItemResponse_QNAME = new QName("http://localhost/RestaurantService", "getMenuItemResponse");
    private final static QName _GetRestaurant_QNAME = new QName("http://localhost/RestaurantService", "getRestaurant");
    private final static QName _GetRestaurantResponse_QNAME = new QName("http://localhost/RestaurantService", "getRestaurantResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: Models
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetMenuItem }
     * 
     */
    public GetMenuItem createGetMenuItem() {
        return new GetMenuItem();
    }

    /**
     * Create an instance of {@link GetRestaurant }
     * 
     */
    public GetRestaurant createGetRestaurant() {
        return new GetRestaurant();
    }

    /**
     * Create an instance of {@link GetMenuItemResponse }
     * 
     */
    public GetMenuItemResponse createGetMenuItemResponse() {
        return new GetMenuItemResponse();
    }

    /**
     * Create an instance of {@link GetRestaurantResponse }
     * 
     */
    public GetRestaurantResponse createGetRestaurantResponse() {
        return new GetRestaurantResponse();
    }

    /**
     * Create an instance of {@link MenuItem }
     * 
     */
    public MenuItem createMenuItem() {
        return new MenuItem();
    }

    /**
     * Create an instance of {@link Menu }
     * 
     */
    public Menu createMenu() {
        return new Menu();
    }

    /**
     * Create an instance of {@link Restaurant }
     * 
     */
    public Restaurant createRestaurant() {
        return new Restaurant();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMenuItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost/RestaurantService", name = "getMenuItem")
    public JAXBElement<GetMenuItem> createGetMenuItem(GetMenuItem value) {
        return new JAXBElement<GetMenuItem>(_GetMenuItem_QNAME, GetMenuItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMenuItemResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost/RestaurantService", name = "getMenuItemResponse")
    public JAXBElement<GetMenuItemResponse> createGetMenuItemResponse(GetMenuItemResponse value) {
        return new JAXBElement<GetMenuItemResponse>(_GetMenuItemResponse_QNAME, GetMenuItemResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRestaurant }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost/RestaurantService", name = "getRestaurant")
    public JAXBElement<GetRestaurant> createGetRestaurant(GetRestaurant value) {
        return new JAXBElement<GetRestaurant>(_GetRestaurant_QNAME, GetRestaurant.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRestaurantResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost/RestaurantService", name = "getRestaurantResponse")
    public JAXBElement<GetRestaurantResponse> createGetRestaurantResponse(GetRestaurantResponse value) {
        return new JAXBElement<GetRestaurantResponse>(_GetRestaurantResponse_QNAME, GetRestaurantResponse.class, null, value);
    }

}
