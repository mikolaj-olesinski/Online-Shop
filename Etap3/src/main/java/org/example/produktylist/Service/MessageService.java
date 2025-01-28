package org.example.produktylist.Service;

import org.example.produktylist.Entity.Message;
import org.example.produktylist.Entity.Order;
import org.example.produktylist.Repository.MessageRepository;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getMessagesByOrderId(Long orderId) {
        return messageRepository.findByOrderId(orderId);
    }

    public Message createMessage(Order order, String content) {
        Message message = new Message();
        message.setOrder(order);
        message.setDate(new Date());
        message.setContent(content);
        return messageRepository.save(message);
    }

    public String getMessageForStatus(String status, Order order) {
        String customerName = order.getDataForm().getFirstName() + " " + order.getDataForm().getLastName();

        switch (status) {
            case "w realizacji":
                return "Dzień dobry " + customerName + ", Twoje zamówienie jest obecnie w realizacji. " +
                        "Pracujemy nad jego przygotowaniem. O wysyłce poinformujemy w osobnej wiadomości.";

            case "wysłane":
                return "Dzień dobry " + customerName + ", Twoje zamówienie zostało właśnie wysłane! " +
                        "Spodziewaj się dostawy w ciągu najbliższych dni. W załączniku znajdziesz list przewozowy.";

            case "anulowane":
                return "Dzień dobry " + customerName + ", informujemy, że zamówienie zostało anulowane. " +
                        "W razie pytań prosimy o kontakt z obsługą klienta.";

            case "dostarczone":
                return "Dzień dobry " + customerName + ", Twoje zamówienie zostało dostarczone. " +
                        "Dziękujemy za zakupy i zapraszamy ponownie!";

            default:
                return "Status zamówienia został zaktualizowany na: " + status;
        }
    }
}
