/*
package CLI;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WestminsterShoppingManagerTest {

    private WestminsterShoppingManager shoppingManager;

    @Before
    public void setUp() {
        shoppingManager = new WestminsterShoppingManager();
    }

    @Test
    public void testAddElectronics() {
        // Simulate user input for adding Electronics
        setInput("1\nABC123\nProduct1\n10\n20.0\nBrandX\n12\n");
        shoppingManager.addNewProduct();

        // Retrieve the list of Electronics products
        //int electronicsCount = shoppingManager.getProductCategories().get("Electronics").size();

        // Assert that the Electronics product has been added
        assertEquals(1, electronicsCount);
    }

    @Test
    public void testAddClothing() {
        // Simulate user input for adding Clothing
        setInput("2\nXYZ456\nProduct2\n15\n25.0\nM\nBlue\n");
        shoppingManager.addNewProduct();

        // Retrieve the list of Clothing products
        int clothingCount = shoppingManager.getProductCategories().get("Clothing").size();

        // Assert that the Clothing product has been added
        assertEquals(1, clothingCount);
    }

    @Test
    public void testDeleteProduct() {
        // Add a sample product
        shoppingManager.getProductCategories().get("Electronics").add(
                new Electronics("ABC123", "Product1", 10, 20.0, "BrandX", "12")
        );

        // Simulate user input for deleting a product
        setInput("ABC123\n");
        shoppingManager.deleteProduct();

        // Retrieve the list of Electronics products after deletion
        int electronicsCount = shoppingManager.getProductCategories().get("Electronics").size();

        // Assert that the Electronics product has been deleted
        assertEquals(0, electronicsCount);
    }

    // Add more test methods for other use cases

    // Helper method to simulate user input
    private void setInput(String input) {
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
    }
}
*/
