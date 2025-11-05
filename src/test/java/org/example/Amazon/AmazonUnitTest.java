package org.example.Amazon;

import org.example.Amazon.Cost.ItemType;
import org.example.Amazon.Cost.PriceRule;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AmazonUnitTest {
    @Mock
    private ShoppingCart cart;
    @Mock
    private PriceRule rule1;
    @Mock
    private PriceRule rule2;
    @Mock
    private PriceRule rule3;

    private Amazon amazon;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Specification-based")
    void testCalculateReg() {
        List<PriceRule> rules = List.of(rule1, rule2, rule3);
        when(cart.getItems()).thenReturn(List.of());
        when(rule1.priceToAggregate(any())).thenReturn(10.0);
        when(rule2.priceToAggregate(any())).thenReturn(5.0);
        when(rule3.priceToAggregate(any())).thenReturn(6.0);

        amazon = new Amazon(cart, rules);
        assertEquals(21.0, amazon.calculate());
    }

    @Test
    @DisplayName("Specification-based")
    void testNoRules() {
        List<PriceRule> rules = new ArrayList<>();

        amazon = new Amazon(cart, rules);
        assertEquals(0, amazon.calculate());
    }

    @Test
    @DisplayName("Specification-based")
    void testOneRule() {
        List<PriceRule> rules = List.of(rule1);
        when(cart.getItems()).thenReturn(List.of());
        when(rule1.priceToAggregate(any())).thenReturn(21.0);

        amazon = new Amazon(cart, rules);
        assertEquals(21.0, amazon.calculate());
    }

    @Test
    @DisplayName("Structural-based")
    void addToCartTest() {
        List<PriceRule> rules = List.of(rule1, rule2, rule3);
        Item item1 = new Item(ItemType.ELECTRONIC, "headphones", 9, 15.0);

        amazon = new Amazon(cart, rules);
        amazon.addToCart(item1);
        verify(cart).add(item1);
    }

    @Test
    @DisplayName("Structural-based")
    void testGetItems() {
        List<PriceRule> rules = List.of(rule1);
        amazon = new Amazon(cart, rules);

        List<Item> items = List.of(new Item(ItemType.OTHER, "Book", 15, 10.0));
        when(cart.getItems()).thenReturn(items);
        when(rule1.priceToAggregate(items)).thenReturn(10.0);

        amazon.calculate();
        verify(cart).getItems();
    }

    @Test
    @DisplayName("Specification-based")
    void testPriceToAggregate() {
        List<PriceRule> rules = List.of(rule1, rule2, rule3);
        amazon = new Amazon(cart, rules);

        Item item1 = new Item(ItemType.ELECTRONIC, "Laptop", 12, 14.0);
        Item item2 = new Item(ItemType.OTHER, "Book", 2, 15.50);
        List<Item> mockItems = List.of(item1, item2);

        when(cart.getItems()).thenReturn(mockItems);
        when(rule1.priceToAggregate(mockItems)).thenReturn(50.0);
        when(rule2.priceToAggregate(mockItems)).thenReturn(25.0);
        when(rule3.priceToAggregate(mockItems)).thenReturn(10.0);

        assertEquals(85.0, amazon.calculate());
        verify(rule1).priceToAggregate(mockItems);
        verify(rule2).priceToAggregate(mockItems);
        verify(rule3).priceToAggregate(mockItems);
    }
}
