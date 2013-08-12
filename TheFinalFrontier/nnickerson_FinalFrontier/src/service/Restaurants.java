package service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nnickerson
 * Date: 8/12/13
 * Time: 6:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class Restaurants {

    protected List<Restaurant> restaurant = new ArrayList<Restaurant>();

    public List<Restaurant> getRestaurant() {
        if (restaurant == null) {
            restaurant = new ArrayList<Restaurant>();
        }
        return this.restaurant;
    }
}
