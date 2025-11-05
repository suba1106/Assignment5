package org.example.Amazon;

import org.example.Amazon.Cost.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AmazonIntegrationTest {
    private Database database;
    private ShoppingCartAdaptor cart;
    private Amazon amazon;

    @BeforeEach
    void Setup(){
        database = new Database();
        database.resetDatabase();
        cart = new ShoppingCartAdaptor(database);

        List<PriceRule> rules = List.of(new RegularCost(), new DeliveryPrice(), new ExtraCostForElectronics());

        amazon = new Amazon(cart, rules);
    }

    @AfterEach
    void tearDown(){
        database.close();
    }

    @Test
    @DisplayName("Specification-based")
    void testCalculate(){
        Item item1 = new Item(ItemType.ELECTRONIC, "Laptop", 12, 14.0);
        Item item2 = new Item(ItemType.OTHER, "Book", 2, 15.0);
        amazon.addToCart(item1);
        amazon.addToCart(item2);

        assertEquals(210.50, amazon.calculate());
    }

    @Test
    @DisplayName("Specification-based")
    void testExtraCostForElectronics(){
        Item item1 = new Item(ItemType.ELECTRONIC, "Laptop", 1, 14.0);
        amazon.addToCart(item1);

        assertEquals(26.50, amazon.calculate());
    }

    @Test
    @DisplayName("Specification-based")
    void testOtherCost(){
        Item item2 = new Item(ItemType.OTHER, "Book", 2, 15.0);
        amazon.addToCart(item2);

        assertEquals(35.0, amazon.calculate());
    }

    @Test
    @DisplayName("Specification-based")
    void testNoItems(){
        assertEquals(0, amazon.calculate());
    }

    @Test
    @DisplayName("Structural-based")
    void testDeliveryOne(){
        Item item2 = new Item(ItemType.OTHER, "Book", 3, 15.0);
        amazon.addToCart(item2);
        assertEquals(50.0, amazon.calculate());
    }

    @Test
    @DisplayName("Structural-based")
    void testDeliveryThree(){
        amazon.addToCart(new Item(ItemType.OTHER, "Book1", 1, 10.0));
        amazon.addToCart(new Item(ItemType.OTHER, "Book2", 1, 10.0));
        amazon.addToCart(new Item(ItemType.OTHER, "Book3", 1, 10.0));
        double total3 = amazon.calculate();
        assertEquals(35.0, total3);
    }


    @Test
    @DisplayName("Structural-based")
    void testDeliveryFour(){
        for (int i = 0; i < 4; i++) {
            amazon.addToCart(new Item(ItemType.OTHER, "Item" + i, 1, 10.0));
        }
        assertEquals(52.5, amazon.calculate());
    }

    @Test
    @DisplayName("Structural-based")
    void testDeliveryTen(){
        for (int i = 0; i < 10; i++) {
            amazon.addToCart(new Item(ItemType.OTHER, "Item" + i, 1, 10.0));
        }
        assertEquals(112.5, amazon.calculate());
    }

    @Test
    @DisplayName("Structural-based")
    void testDeliveryMoreThanTen(){
        for (int i = 0; i < 11; i++) {
            amazon.addToCart(new Item(ItemType.OTHER, "Item" + i, 1, 10.0));
        }
        assertEquals(130.0, amazon.calculate());
    }


}