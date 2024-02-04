package CLI;

public class Clothing extends Product{
    private String size;
    private String colour;

    public Clothing(String product_id, String product_name, int no_of_available_item, double price, String size, String colour) {
        super(product_id, product_name, no_of_available_item, price);
        this.size = size;
        this.colour = colour;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String toString() {
        return super.toString() + "," + size + "," + colour;
    }
}
