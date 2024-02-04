package CLI;

import java.io.Serializable;

import java.io.Serializable;

public abstract class Product implements Serializable {
    private String product_id;
    private String product_name;
    private int no_of_available_item;
    private double price;

    public Product(String product_id, String product_name, int no_of_available_item, double price) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.no_of_available_item = no_of_available_item;
        this.price = price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getNo_of_available_item() {
        return no_of_available_item;
    }

    public void setNo_of_available_item(int no_of_available_item) {
        this.no_of_available_item = no_of_available_item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public void decreaseAvailableItems() {
        if (no_of_available_item > 0) {
            no_of_available_item--;

        }
    }

    @Override
    public String toString() {
        return product_id + "," + product_name + "," + no_of_available_item + "," + price;
    }

}
