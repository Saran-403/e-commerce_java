package CLI;

public class Electronics extends Product {
    private String brand;
    private String warranty_period;

    public Electronics(String product_id, String product_name, int no_of_available_item, double price, String brand, String warrant_period) {
        super(product_id, product_name, no_of_available_item, price);
        this.brand = brand;
        this.warranty_period = warrant_period;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getWarranty_period() {
        return warranty_period;
    }

    public void setWarranty_period(String warranty_period) {
        this.warranty_period = warranty_period;
    }

    public String toString()
    {
        return super.toString() + "," + brand + "," + warranty_period;
    }
}