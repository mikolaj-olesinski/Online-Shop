package org.example.produktylist.Order;

import org.example.produktylist.DataForm.DataForm;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Lista wszystkich zamówień
    @GetMapping("/")
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "orders/all";
    }

    // Szczegóły pojedynczego zamówienia
    @GetMapping("/{orderId}")
    public String orderDetails(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "orders/details";
    }

    // Formularz edycji zamówienia
    @GetMapping("/edit/{orderId}")
    public String editOrderForm(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "orders/edit"; // Widok dla edycji zamówienia
    }

    // Obsługa edycji zamówienia (aktualizacja danych)
    @PostMapping("/{orderId}/edit")
    public String editOrder(@PathVariable Long orderId, @ModelAttribute Order updatedOrder, Model model) {
        Order existingOrder = orderService.getOrderById(orderId);

        existingOrder.setStatus(updatedOrder.getStatus());
        existingOrder.setDeliveryDate(updatedOrder.getDeliveryDate());
        existingOrder.setStatusDate(updatedOrder.getStatusDate());

        DataForm updatedDataForm = existingOrder.getDataForm();
        updatedDataForm.setFirstName(updatedOrder.getDataForm().getFirstName());
        updatedDataForm.setLastName(updatedOrder.getDataForm().getLastName());
        updatedDataForm.setCompany(updatedOrder.getDataForm().getCompany());
        updatedDataForm.setAddress(updatedOrder.getDataForm().getAddress());
        updatedDataForm.setPostalCode(updatedOrder.getDataForm().getPostalCode());
        updatedDataForm.setCity(updatedOrder.getDataForm().getCity());
        updatedDataForm.setState(updatedOrder.getDataForm().getState());
        updatedDataForm.setCountry(updatedOrder.getDataForm().getCountry());
        updatedDataForm.setDeliveryType(updatedOrder.getDataForm().getDeliveryType());

        orderService.saveOrder(existingOrder);

        return "redirect:/orders/" + orderId;
    }

    @PostMapping("/{orderId}/update-status")
    @ResponseBody
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, String> payload) {
        String status = payload.get("status");

        // Znajdź zamówienie
        Order order = orderService.getOrderById(orderId);

        // Zaktualizuj status
        order.setStatus(status);

        // Zapisz zmiany
        orderService.saveOrder(order);

        return ResponseEntity.ok(Map.of("message", "Status zaktualizowany pomyślnie", "status", status));
    }
}
