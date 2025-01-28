package org.example.produktylist;

import org.example.produktylist.Entity.*;
import org.example.produktylist.Repository.MessageRepository;
import org.example.produktylist.Service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    private MessageService messageService;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        messageService = new MessageService(messageRepository);

        DataForm testDataForm = DataForm.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .build();

        testOrder = Order.builder()
                .id(1L)
                .dataForm(testDataForm)
                .build();
    }

    @Test
    void getMessagesByOrderId_ReturnsMessages() {
        // Arrange
        Message message1 = new Message();
        message1.setOrder(testOrder);
        message1.setContent("Test message 1");

        Message message2 = new Message();
        message2.setOrder(testOrder);
        message2.setContent("Test message 2");

        List<Message> expectedMessages = Arrays.asList(message1, message2);
        when(messageRepository.findByOrderId(1L)).thenReturn(expectedMessages);

        // Act
        List<Message> result = messageService.getMessagesByOrderId(1L);

        // Assert
        assertEquals(2, result.size());
        verify(messageRepository).findByOrderId(1L);
    }

    @Test
    void createMessage_Success() {
        // Arrange
        String content = "Test message";
        Message message = new Message();
        message.setOrder(testOrder);
        message.setContent(content);
        message.setDate(new Date());

        when(messageRepository.save(any(Message.class))).thenReturn(message);

        // Act
        Message result = messageService.createMessage(testOrder, content);

        // Assert
        assertNotNull(result);
        assertEquals(content, result.getContent());
        assertEquals(testOrder, result.getOrder());
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void getMessageForStatus_DifferentStatuses() {
        // Arrange
        String customerName = testOrder.getDataForm().getFirstName() + " " + testOrder.getDataForm().getLastName();

        // Act & Assert
        String realizationMessage = messageService.getMessageForStatus("w realizacji", testOrder);
        assertTrue(realizationMessage.contains(customerName));
        assertTrue(realizationMessage.contains("jest obecnie w realizacji"));

        String sentMessage = messageService.getMessageForStatus("wysłane", testOrder);
        assertTrue(sentMessage.contains(customerName));
        assertTrue(sentMessage.contains("zostało właśnie wysłane"));

        String cancelledMessage = messageService.getMessageForStatus("anulowane", testOrder);
        assertTrue(cancelledMessage.contains(customerName));
        assertTrue(cancelledMessage.contains("zostało anulowane"));

        String deliveredMessage = messageService.getMessageForStatus("dostarczone", testOrder);
        assertTrue(deliveredMessage.contains(customerName));
        assertTrue(deliveredMessage.contains("zostało dostarczone"));

        String defaultMessage = messageService.getMessageForStatus("inny status", testOrder);
        assertTrue(defaultMessage.contains("Status zamówienia został zaktualizowany"));
    }
}
