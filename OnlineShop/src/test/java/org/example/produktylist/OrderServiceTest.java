package org.example.produktylist;

import org.example.produktylist.Entity.*;
import org.example.produktylist.Repository.OrderRepository;
import org.example.produktylist.Repository.MessageRepository;
import org.example.produktylist.Service.MessageService;
import org.example.produktylist.Service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MessageRepository messageRepository;

    private OrderService orderService;
    private MessageService messageService;

    private Order testOrder;
    private DataForm testDataForm;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository);
        messageService = new MessageService(messageRepository);

        testDataForm = DataForm.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan@example.com")
                .phone("123456789")
                .address("ul. Testowa 1")
                .city("Warszawa")
                .postalCode("00-001")
                .build();

        testOrder = Order.builder()
                .id(1L)
                .status("nowe")
                .dataForm(testDataForm)
                .deliveryDate("2025-02-01")
                .statusDate("2025-01-28")
                .build();
    }

    @Test
    void getOrderById_ExistingOrder_ReturnsOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        Order result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("nowe", result.getStatus());
        assertEquals("Jan", result.getDataForm().getFirstName());
        verify(orderRepository).findById(1L);
    }

    @Test
    void getOrderById_NonExistingOrder_ThrowsException() {
        // Arrange
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.getOrderById(999L));
        assertEquals("Nie znaleziono zamówienia o ID: 999", exception.getMessage());
    }

    @Test
    void updateOrderStatus_Success() {
        String newStatus = "w realizacji";
        testOrder.setStatus(newStatus);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        orderService.saveOrder(testOrder);

        verify(orderRepository).save(testOrder);
        assertEquals(newStatus, testOrder.getStatus());
    }

    @Test
    void updateOrderWithDataForm_Success() {
        DataForm updatedDataForm = DataForm.builder()
                .firstName("Jan")
                .lastName("Nowak")
                .email("jan.nowak@example.com")
                .phone("987654321")
                .address("ul. Nowa 2")
                .city("Kraków")
                .postalCode("30-001")
                .build();

        testOrder.setDataForm(updatedDataForm);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        orderService.saveOrder(testOrder);

        verify(orderRepository).save(testOrder);
        assertEquals("Nowak", testOrder.getDataForm().getLastName());
        assertEquals("Kraków", testOrder.getDataForm().getCity());
    }
}