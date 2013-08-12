package service;


import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nnickerson
 * Date: 8/11/13
 * Time: 8:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class Menu {
    protected List<MenuItem> menuItem;

    public List<MenuItem> getMenuItem() {
        if (menuItem == null) {
            menuItem = new ArrayList<MenuItem>();
        }
        return this.menuItem;
    }
}
