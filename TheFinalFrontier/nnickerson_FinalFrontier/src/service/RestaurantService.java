package service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nnickerson
 * Date: 8/11/13
 * Time: 8:05 PM
 * To change this template use File | Settings | File Templates.
 */

@WebService(name = "RestaurantService", targetNamespace = "http://localhost/RestaurantService")
public interface RestaurantService {

        @WebMethod(operationName = "getRestaurant")
        public @WebResult(name = "restaurant") List<Restaurant> getRestaurant();

        @WebMethod(operationName = "getMenuItem")
        public @WebResult(name = "menuItem") List<MenuItem> getMenu(@WebParam(name = "restaurantName") String restaurantName);
}
