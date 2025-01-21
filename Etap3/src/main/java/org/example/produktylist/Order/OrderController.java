package org.example.produktylist.Order;

import com.itextpdf.text.DocumentException;
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

import java.io.IOException;
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
        try {
            String status = payload.get("status");

            Order order = orderService.getOrderById(orderId);

            order.setStatus(status);
            order.setStatusDate(java.time.LocalDate.now().toString());

            orderService.saveOrder(order);

            String messageContent = messageService.getMessageForStatus(status, order);
            Message message = messageService.createMessage(order, messageContent);

            // Jeśli status to "wysłane", generujemy list przewozowy
            if ("wysłane".equals(status)) {
                try {
                    shippingLabelService.generateShippingLabel(order);
                } catch (IOException | DocumentException e) {
                    // Logujemy błąd, ale pozwalamy kontynuować aktualizację statusu
                    e.printStackTrace();
                    return ResponseEntity.ok(Map.of(
                            "message", "Status zaktualizowany, ale wystąpił błąd podczas generowania listu przewozowego",
                            "status", status,
                            "newMessage", message.getContent(),
                            "error", "Nie udało się wygenerować listu przewozowego: " + e.getMessage()
                    ));
                }
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Status zaktualizowany pomyślnie",
                    "status", status,
                    "newMessage", message.getContent()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Wystąpił błąd podczas aktualizacji statusu: " + e.getMessage()));
        }
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
