package service;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: nnickerson
 * Date: 8/11/13
 * Time: 8:18 PM
 * To change this template use File | Settings | File Templates.
 */

public class MenuItem {

    protected String discription;

    protected String name;

    protected BigDecimal price;


    public String getDiscription() {
        return discription;
    }


    public void setDiscription(String value) {
        this.discription = value;
    }


    public String getName() {
        return name;
    }


    public void setName(String value) {
        this.name = value;
    }


    public BigDecimal getPrice() {
        return price;
    }


    public void setPrice(BigDecimal value) {
        this.price = value;
    }
}
