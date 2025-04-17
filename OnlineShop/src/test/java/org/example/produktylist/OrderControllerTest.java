package org.example.produktylist;

import org.example.produktylist.Controller.OrderController;
import org.example.produktylist.Entity.DataForm;
import org.example.produktylist.Entity.Order;
import org.example.produktylist.Service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private Model model;

    private OrderController orderController;

    private Order testOrder;
    private DataForm testDataForm;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderController = new OrderController(orderService, null, null);

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
    void listOrders_ShouldAddOrdersToModel() {
        List<Order> orders = new ArrayList<>();
        orders.add(testOrder);
        when(orderService.getAllOrders()).thenReturn(orders);

        String viewName = orderController.listOrders(model);

        verify(model).addAttribute("orders", orders);
        assertEquals("orders/all", viewName);
    }

    @Test
    void editOrderForm_ShouldAddOrderToModel() {
        when(orderService.getOrderById(1L)).thenReturn(testOrder);

        String viewName = orderController.editOrderForm(1L, model);

        verify(model).addAttribute("order", testOrder);
        assertEquals("orders/edit", viewName);
    }

    @Test
    void editOrder_ShouldUpdateOrderAndRedirect() {
        Order updatedOrder = Order.builder()
                .status("zrealizowane")
                .deliveryDate("2025-02-15")
                .statusDate("2025-01-29")
                .dataForm(testDataForm)
                .build();

        when(orderService.getOrderById(1L)).thenReturn(testOrder);

        String redirectUrl = orderController.editOrder(1L, updatedOrder, model);

        verify(orderService).saveOrder(testOrder);
        assertEquals("zrealizowane", testOrder.getStatus());
        assertEquals("2025-02-15", testOrder.getDeliveryDate());
        assertEquals("redirect:/orders/1", redirectUrl);
    }

    @Test
    void updateOrderStatus_ShouldUpdateStatus() {
        Order updatedOrder = Order.builder()
                .status("wysłane")
                .statusDate("2025-01-29")
                .build();

        when(orderService.getOrderById(1L)).thenReturn(testOrder);

        testOrder.setStatus("wysłane");
        testOrder.setStatusDate("2025-01-29");
        orderService.saveOrder(testOrder);

        verify(orderService).saveOrder(testOrder);
        assertEquals("wysłane", testOrder.getStatus());
    }

}
