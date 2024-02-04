package Customer_GUI;

import CLI.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shoping_Cart extends JFrame {
    private final Map<String, Integer> itemQuantity; // Map to store quantity of each product
    private final Map<String, Double> itemPrices; // Map to store the price of each product
    private double totalCost;
    private boolean firstPurchaseDiscount;

    private JTable cartTable;
    private final String[] cartColumnNames = {"Product", "Quantity", "Price"};

    public Shoping_Cart() {
        itemQuantity = new HashMap<>();
        itemPrices = new HashMap<>();
        totalCost = 0;
        firstPurchaseDiscount = true;
        setTitle("Shopping Cart");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        initUI();

        setVisible(true);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // North Panel
        JPanel northPanel = new JPanel(new BorderLayout());

        cartTable = new JTable(new Object[][]{}, cartColumnNames);
        JScrollPane tableScrollPane = new JScrollPane(cartTable);
        northPanel.add(tableScrollPane, BorderLayout.CENTER);

        mainPanel.add(northPanel, BorderLayout.NORTH);

        // South Panel
        JPanel southPanel = new JPanel(new BorderLayout());

        JLabel totalPriceLabel = new JLabel("Total Price: $" + getTotalCost());
        southPanel.add(totalPriceLabel, BorderLayout.CENTER);

        mainPanel.add(southPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);
    }

    public void addItem(Product product) {
        String productId = product.getProduct_id();

        // Update quantity
        itemQuantity.put(productId, itemQuantity.getOrDefault(productId, 0) + 1);

        // Update price
        double price = calculateItemPrice(product);
        itemPrices.put(productId, price);

        // Update total cost
        totalCost += price;
    }

    private double calculateItemPrice(Product product) {
        double price = product.getPrice();

        // Apply discount for the first purchase
        if (firstPurchaseDiscount) {
            price *= 0.9; // 10% discount for the very first purchase
            firstPurchaseDiscount = false;
        }

        return price;
    }

    public void populateCartDetails(List<String> cartDetails) {
        DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
        for (String detail : cartDetails) {
            String[] data = detail.split(", ");
            model.addRow(data);
        }
    }

    public void displayTotalCost(double totalCost) {
        JLabel totalPriceLabel = new JLabel("Total Price: $" + totalCost);
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(totalPriceLabel, BorderLayout.CENTER);

        getContentPane().add(southPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    public double getTotalCost() {
        return totalCost;
    }

    public List<String> getCartDetails() {
        List<String> cartDetails = new ArrayList<>();

        for (String productId : itemQuantity.keySet()) {
            int quantity = itemQuantity.get(productId);
            double price = itemPrices.get(productId) ;

            cartDetails.add("Product ID: " + productId + ", Quantity: " + quantity + ", Price: $" + price);
        }

        return cartDetails;
    }

    public void applyCategoryDiscount(int threshold, double discountPercentage) {
        // Create a map to store the quantity of each category
        Map<String, Integer> categoryQuantity = new HashMap<>();

        // Iterate through items to calculate the quantity of each category
        for (String productId : itemQuantity.keySet()) {
            String category = getCategoryFromProductId(productId);
            int quantity = itemQuantity.get(productId);
            categoryQuantity.put(category, categoryQuantity.getOrDefault(category, 0) + quantity);
        }

        // Apply discount for categories with quantity greater than or equal to the threshold
        for (String productId : itemQuantity.keySet()) {
            String category = getCategoryFromProductId(productId);
            int quantity = itemQuantity.get(productId);

            if (categoryQuantity.get(category) >= threshold) {
                double originalPrice = itemPrices.get(productId);
                double discountedPrice;

                // Apply a 20% discount only if the user buys at least three products of the same category
                if (quantity >= 3) {
                    discountedPrice = originalPrice * (1 - discountPercentage / 100.0); // 20% discount
                } else {
                    discountedPrice = originalPrice; // No discount for less than three items
                }

                double totalDiscount = originalPrice - discountedPrice;

                totalCost -= totalDiscount * quantity;
                itemPrices.put(productId, discountedPrice);
            }
        }
    }

    private String getCategoryFromProductId(String productId) {
        // Modify this method based on your product ID format
        // For example, if the format is "E001", "C001", you can extract the first character
        return productId.substring(0, 1);
    }
}
