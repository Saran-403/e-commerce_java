package Customer_GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Customer_signup extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private List<User> userList;
    private static final String file_name = "user_details.txt";

    public Customer_signup() {
        setTitle("Signup Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(250, 200);
        setLocationRelativeTo(null);

        userList = new ArrayList<>();
        initUI();

        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton signupButton = new JButton("Signup");

        // Action listener for the signup button
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle signup logic here
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                // Check if username or password is null or empty
                if (username.trim().isEmpty() || password.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username and password cannot be empty.");
                    return;
                }

                // Check if username already exists
                if (isUsernameExists(username)) {
                    JOptionPane.showMessageDialog(null, "Username already exists. Please choose a different one.");
                    return;
                }

                // Save the new user
                User newUser = new User(username, password);
                userList.add(newUser);

                // Display a message
                JOptionPane.showMessageDialog(null, "Account Created Successfully");

                // Direct to login page (you can replace this with your desired navigation logic)
                dispose();  // Close the current signup form
                new Customer_login(userList);  // Open the login form with the user list
                saveUserDetailsToFile(file_name);
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());  // Empty label for spacing
        panel.add(signupButton);

        add(panel);
    }

    private boolean isUsernameExists(String username) {
        for (Object user : userList) {
            if (((User) user).getUsername().equalsIgnoreCase(username)) {
                return true;  // Username already exists
            }
        }
        return false;  // Username does not exist
    }

    // Add this method to save userList to a file
    private void saveUserDetailsToFile(String file_name) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file_name, true))) {
            for (User user : userList) {
                writer.println(user.toFileString()); // Assuming toFileString method in User class
            }
            System.out.println("User details saved to file successfully.");
        } catch (IOException e) {
            System.out.println("Error saving user details to file: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for more information
        }
    }




}