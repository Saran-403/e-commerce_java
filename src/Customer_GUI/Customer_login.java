package Customer_GUI;

import CLI.Product;
import CLI.WestminsterShoppingManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

public class Customer_login extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private List<User> userList;
    private HashMap<String, List<Product>> productCategories;


    public Customer_login(List<User> userList) {
        this.userList = userList;
        setTitle("Login Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        initUI();

        setVisible(true);

    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Signup");

        // Action listener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HashMap<String, List<Product>> productCategories = new HashMap<>();
                // Handle login logic here
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                // Check if the username and password match any user in the list
                if (isValidLogin(username, password)) {
                    JOptionPane.showMessageDialog(null, "Login Successful");
                    dispose();  // Close the current login form
                    new Customer_Shopping(productCategories);
                    // You can add your logic to open the main application window or perform other actions
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.");
                }
            }
        });

        // Action listener for the signup button
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Close the current login form
                new Customer_signup();  // Open the signup form
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());  // Empty label for spacing
        panel.add(loginButton);
        panel.add(new JLabel());  // Empty label for spacing
        panel.add(signupButton);

        add(panel);
    }

    private boolean isValidLogin(String username, String password) {
        for (User user : userList) {
            if (isValidCredentials(user, username, password)) {
                return true;  // Valid login
            }
        }
        return false;  // Invalid login
    }

    private boolean isValidCredentials(User user, String inputUsername, String inputPassword) {
        return user.getUsername().equalsIgnoreCase(inputUsername) && user.getPassword().equals(inputPassword);
    }
}