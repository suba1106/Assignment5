package org.example.Barnes;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BarnesAndNobleTest {

    @Mock
    private BookDatabase mockDatabase;
    @Mock
    private BuyBookProcess mockBuy;
    private BarnesAndNoble barnes;

    @BeforeEach
    void setUp() {
        Book book1 = new Book("321856482", 12, 5);
        Book book2 = new Book("987654321", 14, 6);
        Book book3 = new Book("519816819", 10, 3);
        MockitoAnnotations.openMocks(this);
        when(mockDatabase.findByISBN("321856482")).thenReturn(book1);
        when(mockDatabase.findByISBN("987654321")).thenReturn(book2);
        when(mockDatabase.findByISBN("519816819")).thenReturn(book3);
        barnes = new BarnesAndNoble(mockDatabase, mockBuy);
    }

    @Test
    @DisplayName("specification-based")
    void OrderCorrect() {
        Map<String, Integer> cart = Map.of("321856482", 2, "987654321", 4);
        PurchaseSummary summary = barnes.getPriceForCart(cart);
        assertEquals(80.0, summary.getTotalPrice());
        verify(mockBuy).buyBook(new Book("321856482", 12, 5),2);
        verify(mockBuy).buyBook(new Book("987654321", 14, 6), 4);
    }

    @Test
    @DisplayName("specification-based")
    void OrderUnavailableQuantity() {
        Map<String, Integer> cart = Map.of("321856482", 6, "987654321", 4);
        PurchaseSummary summary = barnes.getPriceForCart(cart);
        assertEquals(116.0, summary.getTotalPrice());
        verify(mockBuy).buyBook(new Book("321856482", 12, 5),5);
        verify(mockBuy).buyBook(new Book("987654321", 14, 6), 4);
    }

    @Test
    @DisplayName("specification-based")
    void OrderNull() {
        PurchaseSummary summary = barnes.getPriceForCart(null);
        assertNull(summary);
    }
}