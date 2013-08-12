package service;


/**
 * Created with IntelliJ IDEA.
 * User: nnickerson
 * Date: 8/11/13
 * Time: 8:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class Restaurant {

    protected Menu menu;

    protected String name;

    public Restaurant(String restaurantName) {
           name = restaurantName;
    }

    public Menu getMenu() {
        return menu;
    }


    public void setMenu(Menu value) {
        this.menu = value;
    }


    public String getName() {
        return name;
    }


    public void setName(String value) {
        this.name = value;
    }

}

