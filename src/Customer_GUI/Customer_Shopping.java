package Customer_GUI;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import CLI.Clothing;
import CLI.Electronics;
import CLI.Product;



public class Customer_Shopping extends JFrame {

    private JTable productTable;
    private final String[] columnNames = {"ID", "Name", "Category", "Price", "Info"};
    private HashMap<String, List<Product>> productCategories;
    private static final String fileName = "products.txt";
    private JPanel southPanel;
    private JPanel south1;
    private JPanel south2;
    private Shoping_Cart shoppingCart;

    public Customer_Shopping(HashMap<String, List<Product>> productCategories) {
        this.productCategories = productCategories;
        productCategories.put("Electronics", new ArrayList<>());
        productCategories.put("Clothing", new ArrayList<>());
        loadProductsFromFile();
        shoppingCart = new Shoping_Cart();
        initUI();
        setTitle("Shopping UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // North Panel
        JPanel northPanel = new JPanel(new BorderLayout());
        JPanel north1 = new JPanel(new BorderLayout());
        JPanel north2 = new JPanel(new BorderLayout());
        JPanel leftN = new JPanel(new BorderLayout());
        JPanel centerN = new JPanel(new BorderLayout());
        JPanel rightN = new JPanel(new BorderLayout());

        northPanel.add(north1, BorderLayout.NORTH);
        northPanel.add(north2, BorderLayout.SOUTH);
        north1.add(leftN, BorderLayout.WEST);
        north1.add(centerN, BorderLayout.CENTER);
        north1.add(rightN, BorderLayout.EAST);

        JButton viewCartButton = new JButton("View Cart");
        rightN.add(viewCartButton, BorderLayout.CENTER);
        rightN.setPreferredSize(new Dimension(100, 50));


        viewCartButton.addActionListener(e -> {
            List<String> cartDetails = shoppingCart.getCartDetails();
            StringBuilder cartDetailsText = new StringBuilder("Shopping Cart:\n");

            for (String detail : cartDetails) {
                cartDetailsText.append(detail).append("\n");
            }

            cartDetailsText.append("Total Cost: $").append(shoppingCart.getTotalCost());

            JOptionPane.showMessageDialog(null, cartDetailsText.toString(), "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
        });

        JLabel categoryLabel = new JLabel("Select the category:");
        leftN.add(categoryLabel, BorderLayout.CENTER);
        leftN.setPreferredSize(new Dimension(150, 50));

        String[] categories = {"All", "Electronics", "Clothing"};
        JComboBox<String> categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.addActionListener(e -> {
            String selectedCategory = (String) categoryComboBox.getSelectedItem();
            List<Product> filteredProducts = filterProductsByCategory(selectedCategory);
            updateTableData(filteredProducts);

        });


        centerN.add(categoryComboBox, BorderLayout.CENTER);

        productTable = new JTable(new DefaultTableModel(new Object[][]{}, columnNames));
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        north2.setPreferredSize(new Dimension(600, 300));

        north2.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.setPreferredSize(new Dimension(600, 300));

        // South Panel
        southPanel = new JPanel(new BorderLayout());
        south1 = new JPanel(new BorderLayout());
        south2 = new JPanel(new BorderLayout());
        southPanel.add(south1, BorderLayout.NORTH);
        southPanel.add(south2, BorderLayout.SOUTH);

        JButton addToCartButton = new JButton("Add to Cart");
        // Add this line to set a selection listener on the table
        productTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow >= 0) {
                Product selectedProduct = getSelectedProduct(selectedRow);
                displayProductDetails(selectedProduct);
            }
        });
        addToCartButton.addActionListener(e -> {
            // Check if south1 panel has components
            if (south1.getComponentCount() > 0) {
                // Get the JScrollPane
                JScrollPane scrollPane = (JScrollPane) south1.getComponent(0);

                // Check if the JScrollPane has a viewport
                if (scrollPane.getViewport().getView() instanceof JTextArea) {
                    // Get the JTextArea from the JScrollPane's viewport
                    JTextArea detailsTextArea = (JTextArea) scrollPane.getViewport().getView();

                    // Rest of the code...
                    String productDetails = detailsTextArea.getText();

                    if (!productDetails.isEmpty()) {
                        // Get the selected product
                        Product selectedProduct = getSelectedProduct(productTable.getSelectedRow());

                        // Decrease available items when adding to cart
                        if (selectedProduct != null) {
                            selectedProduct.decreaseAvailableItems();
                        }

                        // Add the selected product to the shopping cart
                        shoppingCart.addItem(selectedProduct);
                        shoppingCart.applyCategoryDiscount(3, 20);

                        // Update the displayed details and available items
                        displayProductDetails(selectedProduct);
                        updateTableData(filterProductsByCategory((String) categoryComboBox.getSelectedItem()));

                        System.out.println("Added to Cart: " + productDetails);
                    } else {
                        System.out.println("No product selected.");
                    }
                } else {
                    System.out.println("No JTextArea found in the JScrollPane's viewport.");
                }
            } else {
                System.out.println("No components found in the south1 panel.");
            }
        });

        // Add this line to set a selection listener on the table
        productTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow >= 0) {
                Product selectedProduct = getSelectedProduct(selectedRow);
                displayProductDetails(selectedProduct);
            }
        });

        south2.add(addToCartButton, BorderLayout.CENTER);

        mainPanel.add(southPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);

        // Set selection mode to single selection
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add a selection listener to handle row selection
        productTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = productTable.getSelectedRow();
                    if (selectedRow != -1) {
                        // Row is selected, get the entire row data
                        Product selectedProduct = getSelectedProduct(selectedRow);
                        // Do something with the selected product
                        displayProductDetails(selectedProduct);
                    }
                }
            }
        });
    }


    private Product getSelectedProduct(int selectedRow) {
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();

        // Ensure the selectedRow is within bounds
        if (selectedRow >= 0 && selectedRow < model.getRowCount()) {
            String productId = (String) model.getValueAt(selectedRow, 0); // Assuming product ID is in the first column
            String category = (String) model.getValueAt(selectedRow, 2); // Assuming category is in the third column

            // Use the category to get the correct list of products
            List<Product> productList = productCategories.getOrDefault(category, List.of());

            // Find the selected product in the list
            return productList.stream()
                    .filter(product -> product.getProduct_id().equals(productId))
                    .findFirst()
                    .orElse(null);
        } else {
            // Return null if selectedRow is out of bounds
            return null;
        }
    }
    private List<Product> filterProductsByCategory(String category) {
        if (productCategories != null) {  // Ensure productCategories is not null
            if ("All".equals(category)) {
                // Combine all products from different categories
                return productCategories.values().stream().flatMap(List::stream).toList();
            } else {
                return productCategories.getOrDefault(category, List.of());
            }
        } else {
            // Handle the case where productCategories is null
            return List.of();
        }
    }

    private void updateTableData(List<Product> products) {
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        model.setRowCount(0);

        for (Product product : products) {
            Object[] rowData = {
                    product.getProduct_id(),
                    product.getProduct_name(),
                    getCategory(product),
                    product.getPrice(),
                    getProductInfo(product)
            };

            // Check if available items <= 3, and set row background color to red
            if (product.getNo_of_available_item() <= 3) {
                model.addRow(rowData);
                int row = model.getRowCount() - 1;
                productTable.setDefaultRenderer(Object.class, new CustomCellRenderer(row, Color.RED));
            } else {
                model.addRow(rowData);
            }
        }
    }

    private String getProductInfo(Product product) {
        if (product instanceof Electronics) {
            return "Brand: " + ((Electronics) product).getBrand() + ", Warranty: " + ((Electronics) product).getWarranty_period();
        } else if (product instanceof Clothing) {
            return "Size: " + ((Clothing) product).getSize() + ", Color: " + ((Clothing) product).getColour();
        }
        return "";
    }

    private String getCategory(Product product) {
        if (product instanceof Electronics) {
            return "Electronics";
        } else if (product instanceof Clothing) {
            return "Clothing";
        } else {
            return "Other";
        }
    }

    // Method to display product details in the South panel
    private void displayProductDetails(Product product) {
        JTextArea detailsTextArea = new JTextArea();
        detailsTextArea.setEditable(false);
        if (product != null) {
            detailsTextArea.append("Product ID: " + product.getProduct_id() + "\n");
            detailsTextArea.append("Product Category: " + getCategory(product) + "\n");
            detailsTextArea.append("Product Name: " + product.getProduct_name() + "\n");

            // Customize this part based on your product class structure
            if (product instanceof Electronics) {
                detailsTextArea.append("Product Type: Electronics\n");
                detailsTextArea.append("Brand: " + ((Electronics) product).getBrand() + "\n");
                detailsTextArea.append("Warranty: " + ((Electronics) product).getWarranty_period() + "\n");
            } else if (product instanceof Clothing) {
                detailsTextArea.append("Product Type: Clothing\n");
                detailsTextArea.append("Size: " + ((Clothing) product).getSize() + "\n");
                detailsTextArea.append("Color: " + ((Clothing) product).getColour() + "\n");
            }
            detailsTextArea.append("Available Items: " + product.getNo_of_available_item());
        }

        // Add any other details you want to display
        // ...

        // Remove the existing detailsTextArea from southPanel
        south1.removeAll();

        // Add the new detailsTextArea to southPanel
        south1.add(new JScrollPane(detailsTextArea), BorderLayout.CENTER);

        // Revalidate the southPanel to update the UI
        south1.revalidate();
        south1.repaint();

    }

    // Function to load products from the file
    public void loadProductsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            String currentCategory = null;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    currentCategory = null; // Reset current category at empty lines
                } else if (productCategories.containsKey(line)) {
                    currentCategory = line;
                } else {
                    addProductFromLine(line, currentCategory);
                }
            }

            System.out.println("Products loaded from file successfully.");
        } catch (IOException e) {
            System.out.println("Error loading products from file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addProductFromLine(String line, String category) {
        String[] details = line.split(",");
        String id = details[0].trim();
        String name = details[1].trim();
        int quantity = Integer.parseInt(details[2].trim());
        double price = Double.parseDouble(details[3].trim());

        if ("Electronics".equals(category)) {
            String brand = details[4].trim();
            String warranty = details[5].trim();
            Electronics electronics = new Electronics(id, name, quantity, price, brand, warranty);
            productCategories.get(category).add(electronics);
        } else if ("Clothing".equals(category)) {
            String size = details[4].trim();
            String color = details[5].trim();
            Clothing clothing = new Clothing(id, name, quantity, price, size, color);
            productCategories.get(category).add(clothing);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HashMap<String, List<Product>> productCategories = new HashMap<>();
            // Load or initialize your productCategories data here

            new Customer_Shopping(productCategories);
        });
    }
}

