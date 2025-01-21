package org.example.produktylist.Order;

import org.example.produktylist.DataForm.DataForm;
import org.example.produktylist.Message.Message;
import org.example.produktylist.ShippingLabel.ShippingLabelService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.produktylist.Message.MessageService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final MessageService messageService; // Dodajemy usługę obsługującą wiadomości
    private final ShippingLabelService shippingLabelService;

    public OrderController(OrderService orderService, MessageService messageService, ShippingLabelService shippingLabelService) {
        this.orderService = orderService;
        this.messageService = messageService;
        this.shippingLabelService = shippingLabelService;
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

        List<Message> messages = messageService.getMessagesByOrderId(orderId);
        model.addAttribute("messages", messages);

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
        order.setStatusDate(java.time.LocalDate.now().toString());

        // Zapisz zmiany w zamówieniu
        orderService.saveOrder(order);

        // Wygeneruj i zapisz odpowiednią wiadomość
        String messageContent = messageService.getMessageForStatus(status, order);
        Message message = messageService.createMessage(order, messageContent);

        // Zwróć odpowiedź JSON z komunikatem
        return ResponseEntity.ok(Map.of(
                "message", "Status zaktualizowany pomyślnie",
                "status", status,
                "newMessage", message.getContent()
        ));
    }

    @GetMapping("/{orderId}/shipping-label")
    public ResponseEntity<byte[]> generateShippingLabel(@PathVariable Long orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            byte[] pdfContent = shippingLabelService.generateShippingLabel(order);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "list-przewozowy-" + orderId + ".pdf");

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
