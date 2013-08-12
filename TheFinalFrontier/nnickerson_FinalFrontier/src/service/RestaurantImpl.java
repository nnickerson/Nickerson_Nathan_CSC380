package service;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nnickerson
 * Date: 8/11/13
 * Time: 8:20 PM
 * To change this template use File | Settings | File Templates.
 */
@WebService(endpointInterface = "service.RestaurantService", serviceName = "RestaurantService")
public class RestaurantImpl implements RestaurantService {

    private static final Restaurants RESTAURANTS = new Restaurants();

    static {
        RESTAURANTS.restaurant.add(new Restaurant("Cluck and Chuck"));
        RESTAURANTS.restaurant.add(new Restaurant("Steak and Bake"));
        RESTAURANTS.restaurant.add(new Restaurant("Waffle Days"));
        RESTAURANTS.restaurant.add(new Restaurant("ChamHam"));
    }

    @Override
    public List<Restaurant> getRestaurant() {
        return RESTAURANTS.getRestaurant();
    }

    @Override
    public List<MenuItem> getMenu(@WebParam(name = "restaurantName") String restaurantName) {
        Restaurant re = null;
        for(Restaurant r : RESTAURANTS.getRestaurant()) {
             if(r.getName().equalsIgnoreCase(restaurantName)) {
                 re = r;
             }
        }
        return re.getMenu().getMenuItem();  //To change body of implemented methods use File | Settings | File Templates.
    }
}
