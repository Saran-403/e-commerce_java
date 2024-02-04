package CLI;

import Customer_GUI.Customer_login;
import Customer_GUI.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.io.*;
public class WestminsterShoppingManager implements ShopingManager {
    private HashMap<String, List<Product>> productCategories;
    private Scanner scanner;
    private static final String fileName = "products.txt";
    private static final String file_name = "user_details.txt";
    private List<User> userList;

    public WestminsterShoppingManager() {
        this.productCategories = new HashMap<>();
        productCategories.put("Electronics", new ArrayList<>());
        productCategories.put("Clothing", new ArrayList<>());
        this.scanner = new Scanner(System.in);
        userList = new ArrayList<>();
    }

    public void showLoginOptions() {
        boolean isValidChoice = false;

        do {
            System.out.println("Choose an option:");
            System.out.println("1. Login As Manager");
            System.out.println("2. Login As Customer");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        loadProductsFromFile();
                        loginAsManager();
                        isValidChoice = true;
                        break;
                    case 2:
                        loadUserDetailsFromFile(file_name);
                        new Customer_login(userList);
                        isValidChoice = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1 or 2.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
            }

        } while (!isValidChoice);
    }

    private void loginAsManager() {
        boolean isValidCredentials = false;

        do {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            // Validate the credentials for Manager
            if (username.equals("Admin") && password.equals("admin123")) {
                // Credentials are correct, run the manager menu
                runConsoleMenu();
                isValidCredentials = true;
            } else {
                System.out.println("Invalid credentials for Manager. Please try again.");
                System.out.println("Do you want to try again? (Y/N): ");
                String retryChoice = scanner.nextLine().trim();

                if (!retryChoice.equalsIgnoreCase("Y")) {
                    break;  // Exit the loop if the user chooses not to retry
                }
            }
        } while (!isValidCredentials);
    }
    public void runConsoleMenu() {
        int choice;
        //loadProductsFromFile();

        do {
            System.out.println("\n*** Shopping Manager Menu ***");
            System.out.println("1. Add a new product");
            System.out.println("2. Delete a product");
            System.out.println("3. Print all products");
            System.out.println("4. Print Electronics");
            System.out.println("5. Print Clothing");
            System.out.println("6. Save to file");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine();  // Consume the newline character
                processMenuChoice(choice);
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();  // Consume the invalid input
                choice = -1;  // Set to -1 to continue the loop
            }

        } while (choice != 0);

        scanner.close();
    }

    private void processMenuChoice(int choice) {
        switch (choice) {
            case 1:
                // Add a new product
                addNewProduct();
                break;
            case 2:
                // Delete a product
                deleteProduct();
                break;
            case 3:
                // Print all products
                printAllProducts();
                break;
            case 4:
                // Print Electronics
                printElectronicProducts();
                break;
            case 5:
                // Print Clothing
                printClothingProducts();
                break;
            case 6:
                // Save to file
                saveProductsToFile();
                break;
            case 0:
                System.out.println("Exiting the application. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice. Please enter a valid option.");
        }
    }

    public void addNewProduct() {
        if (productCategories.get("Electronics").size() + productCategories.get("Clothing").size() < 50) {
            System.out.println("Choose the product type:");
            System.out.println("1. Electronics");
            System.out.println("2. Clothing");
            System.out.print("Enter your choice: ");

            int productTypeChoice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            if (productTypeChoice == 1) {
                addElectronics();
            } else if (productTypeChoice == 2) {
                addClothing();
            } else {
                System.out.println("Invalid choice. Please enter 1 for Electronics or 2 for Clothing.");
            }
        } else {
            System.out.println("Maximum limit of products reached. Cannot add more products.");
        }
    }
    private void addElectronics() {
        String productID;
        do {
            System.out.print("Enter product ID: ");
            productID = scanner.nextLine();
            if (productID.trim().isEmpty()) {
                System.out.println("Product ID cannot be empty.");
            } else if (isProductIDExists(productID)) {
                System.out.println("Product ID already exists. Please enter a different one.");
            }
        } while (productID.trim().isEmpty() || isProductIDExists(productID));

        String productName;
        do {
            System.out.print("Enter product name: ");
            productName = scanner.nextLine();
            if (productName.trim().isEmpty()) {
                System.out.println("Product Name cannot be empty.");
            }
        } while (productName.trim().isEmpty());

        int availableItems;
        do {
            System.out.print("Enter available items: ");
            availableItems = scanner.nextInt();
            if (!isValidateAvailableItems(availableItems)) {
                System.out.println("Available Items cannot be less than '0'.");
            }
        } while (!isValidateAvailableItems(availableItems));

        double price;
        do {
            System.out.print("Enter price: ");
            price = scanner.nextDouble();
            scanner.nextLine();  // Consume the newline character
            if (price < 0) {
                System.out.println("Price cannot be less than '0'.");
            }
        } while (price < 0);

        String brand;
        do {
            System.out.print("Enter brand: ");
            brand = scanner.nextLine();
            if (brand.trim().isEmpty()) {
                System.out.println("Brand cannot be empty.");
            }
        } while (brand.trim().isEmpty());

        String warranty_period;
        do {
            System.out.print("Enter warranty period (months): ");
            warranty_period = scanner.nextLine();
            if (warranty_period.trim().isEmpty()) {
                System.out.println("Warranty period cannot be empty.");
            }
        } while (warranty_period.trim().isEmpty());

        Electronics electronics = new Electronics(productID, productName, availableItems, price, brand, warranty_period);
        productCategories.get("Electronics").add(electronics);

        System.out.println("Electronics product added successfully.");
    }

    private void addClothing() {
        String size = null;
        System.out.print("Enter product ID: ");
        String productID = scanner.nextLine();
        if (productID.trim().isEmpty())
        {
            System.out.println("Product ID can not be NULL");
            return;
        } else if(isProductIDExists(productID)) {
            System.out.println("Product ID already Exists");
            return;
        }

        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        if (productName.trim().isEmpty()) {
            System.out.println("Product Name can not be NULL");
            return;
        }

        System.out.print("Enter available items: ");
        int availableItems = scanner.nextInt();
        if (!isValidateAvailableItems(availableItems)) {
            System.out.println("Available Items Cannot be less than '0'");
            return;
        }

        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();  // Consume the newline character

        getSizeInput();

        System.out.print("Enter color: ");
        String color = scanner.nextLine();

        Clothing clothing = new Clothing(productID, productName, availableItems, price, size, color);
        productCategories.get("Clothing").add(clothing);

        System.out.println("Clothing product added successfully.");
    }

    private String getSizeInput() {
        String size;
        List<String> validSizes = List.of("XS", "S", "M", "L", "XL", "XXL", "XXXL");

        do {
            System.out.print("Enter size: ");
            size = scanner.nextLine().toUpperCase(); // Convert to uppercase for case-insensitive comparison

            if (!validSizes.contains(size)) {
                System.out.println("Invalid size. Please enter one of: XS, S, M, L, XL, XXL, XXXL");
            }
        } while (!validSizes.contains(size));

        return size;
    }


    private boolean isProductIDExists(String productID)
    {
        for (List<Product> productList : productCategories.values()) {
            for (Product product : productList) {
                if (product.getProduct_id().equalsIgnoreCase(productID)) {
                    return true;  // Product ID already exists
                }
            }
        }
        return false;  // Product ID does not exist
    }
    private static boolean isValidateAvailableItems(int availableItems)
    {
        return availableItems > 0;
    }

    public void printAllProducts() {
        System.out.println("All Products:");

        // Iterate over each category (Electronics, Clothing)
        for (List<Product> productList : productCategories.values()) {
            // Iterate over each product in the category
            for (Product product : productList) {
                // Print product information
                System.out.println("Product ID: " + product.getProduct_id());
                System.out.println("Product Name: " + product.getProduct_name());
                System.out.println("Available Items: " + product.getNo_of_available_item());
                System.out.println("Price: Rs " + product.getPrice());

                // Check the category and print specific information
                if (product instanceof Electronics) {
                    Electronics electronicsProduct = (Electronics) product;
                    System.out.println("Brand: " + electronicsProduct.getBrand());
                    System.out.println("Warranty Period: " + electronicsProduct.getWarranty_period() + " months");
                } else if (product instanceof Clothing) {
                    Clothing clothingProduct = (Clothing) product;
                    System.out.println("Size: " + clothingProduct.getSize());
                    System.out.println("Color: " + clothingProduct.getColour());
                }

                System.out.println("-------------"); // Separator between products
            }
        }
    }
    // Function to print only Electronic products
    public void printElectronicProducts() {
        System.out.println("Electronic Products:");

        // Iterate over Electronic products
        for (Product product : productCategories.get("Electronics")) {
            System.out.println("Product ID: " + product.getProduct_id());
            System.out.println("Product Name: " + product.getProduct_name());
            System.out.println("Available Items: " + product.getNo_of_available_item());
            System.out.println("Price: Rs " + product.getPrice());

            // Print Electronic-specific information
            Electronics electronicsProduct = (Electronics) product;
            System.out.println("Brand: " + electronicsProduct.getBrand());
            System.out.println("Warranty Period: " + electronicsProduct.getWarranty_period() + " months");

            System.out.println("-------------"); // Separator between products
        }
    }

    // Function to print only CLI.Clothing products
    public void printClothingProducts() {
        System.out.println("Clothing Products:");

        // Iterate over CLI.Clothing products
        for (Product product : productCategories.get("Clothing")) {
            System.out.println("Product ID: " + product.getProduct_id());
            System.out.println("Product Name: " + product.getProduct_name());
            System.out.println("Available Items: " + product.getNo_of_available_item());
            System.out.println("Price: Rs" + product.getPrice());

            // Print CLI.Clothing-specific information
            Clothing clothingProduct = (Clothing) product;
            System.out.println("Size: " + clothingProduct.getSize());
            System.out.println("Color: " + clothingProduct.getColour());

            System.out.println("-------------"); // Separator between products
        }
    }

    public void deleteProduct() {
        String userChoice = null;

        do {
            System.out.print("Enter the product ID to delete: ");
            String productID = scanner.nextLine();

            // Validate product ID
            if (productID.trim().isEmpty()) {
                System.out.println("Product ID can not be NULL");
                continue;
            }

            boolean productFound = false;

            // Iterate over each category (CLI.Electronics, CLI.Clothing)
            for (List<Product> productList : productCategories.values()) {
                // Iterate over each product in the category
                for (Product product : productList) {
                    if (product.getProduct_id().equalsIgnoreCase(productID)) {
                        // CLI.Product found, display information and delete
                        System.out.println("Product deleted:");
                        System.out.println("Product ID: " + product.getProduct_id());
                        System.out.println("Product Name: " + product.getProduct_name());

                        // Check the category and print specific information
                        if (product instanceof Electronics) {
                            Electronics electronicsProduct = (Electronics) product;
                            System.out.println("Category: Electronics");
                            System.out.println("Brand: " + electronicsProduct.getBrand());
                            System.out.println("Warranty Period: " + electronicsProduct.getWarranty_period() + " months");
                        } else if (product instanceof Clothing) {
                            Clothing clothingProduct = (Clothing) product;
                            System.out.println("Category: Clothing");
                            System.out.println("Size: " + clothingProduct.getSize());
                            System.out.println("Color: " + clothingProduct.getColour());
                        }

                        // Delete the product
                        productList.remove(product);
                        System.out.println("Product deleted successfully.");

                        // Display the total number of products left in the system
                        System.out.println("Total number of products left: " + getTotalProducts());
                        productFound = true;
                        break;  // Exit the inner loop after deleting the product
                    }
                }
                if (productFound) {
                    break;  // Exit the outer loop after deleting the product
                }
            }

            // CLI.Product not found with the given ID
            if (!productFound) {
                System.out.println("Product with ID '" + productID + "' not found.");
            }

            // Ask the user if they want to delete another product
            System.out.print("Do you want to delete another product? (y/n): ");
            userChoice = scanner.nextLine();

        } while (userChoice.equalsIgnoreCase("y"));
    }

    // Function to get the total number of products in the system
    private int getTotalProducts() {
        int totalProducts = 0;
        for (List<Product> productList : productCategories.values()) {
            totalProducts += productList.size();
        }
        return totalProducts;
    }

    // Function to save products to a text file
    public void saveProductsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (HashMap.Entry<String, List<Product>> entry : productCategories.entrySet()) {
                String category = entry.getKey();
                List<Product> productList = entry.getValue();

                writer.println(category);
                for (Product product : productList) {
                    writer.println(product.toString()); // Assuming toFileString method in CLI.Product class
                }
                writer.println(); // Add an empty line between categories
            }
            System.out.println("Products saved to file successfully.");
        } catch (IOException e) {
            System.out.println("Error saving products to file: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for more information
        }
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
    public void loadUserDetailsFromFile(String file_name) {
        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
            String line;

            while ((line = br.readLine()) != null) {
                addUserFromLine(line);
            }

            System.out.println("User details loaded from file successfully.");
        } catch (IOException e) {
            System.out.println("Error loading user details from file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addUserFromLine(String line) {
        String[] details = line.split(",");
        if (details.length >= 2) {
            String username = details[0].trim();
            String password = details[1].trim();

            User user = new User(username, password);
            userList.add(user);
        } else {
            System.out.println("Invalid format for line: " + line);
            // Handle the invalid format as per your requirement
        }

    }


}