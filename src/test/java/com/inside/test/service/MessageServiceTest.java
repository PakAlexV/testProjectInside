package com.inside.test.service;

import com.inside.test.model.Message;
import com.inside.test.model.MessageRepository;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class MessageServiceTest {

    @MockBean
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;
    private final int userId = 1;
    private final String textMessage = "test message";

    @Test
    @DisplayName("Should create message")
    void shouldCreateMessage() {
        assertThrows(NullPointerException.class, ()-> messageService.createMessage(1, null));
        assertThrows(IllegalArgumentException.class, () -> messageService.createMessage(0, textMessage));
        assertThrows(IllegalArgumentException.class, () -> messageService.createMessage(-1, textMessage));

        Message message = messageService.createMessage(userId, textMessage);
        assertNotNull(message);
        assertNotNull(message.getMessage());
        assertNotNull(message.getUserId());
        assertNotNull(message.getCreateDate());

        assertEquals(userId, message.getUserId());
        assertEquals(textMessage, message.getMessage());

        verify(messageRepository, times(1)).save(message);
    }

    @Test
    @DisplayName("Should is history")
    void shouldIsHistory() {
        assertThrows(NullPointerException.class, ()->messageService.isHistory(null));

        assertTrue(messageService.isHistory("history 1"));
        assertFalse(messageService.isHistory("hist"));
        assertFalse(messageService.isHistory("1"));
    }

    @Test
    @DisplayName("Should get history")
    void shouldGetHistory() {
        int[] userIds = {0, -1};
        int[] limits = {0, -1};
        for (int userId : userIds) {
            for (int limit : limits) {
                assertThrows(IllegalArgumentException.class,
                        () -> messageService.getHistory(userId, limit));
            }
        }

        assertThrows(NullPointerException.class, ()-> messageService.getHistory(1, 1));

        Message mockMessage = messageService.createMessage(userId, textMessage);
        List<Message> messages = new ArrayList<>();
        messages.add(mockMessage);
        Page<Message> messagePage = new PageImpl<>(messages);

        Pageable pageable = PageRequest.of(0, 1);
        when(messageRepository.findAllByUserIdOrderByCreateDateDesc(userId, pageable))
                .thenReturn(messagePage);

        Iterable<Message> messageIterable = messageService.getHistory(userId, 1);
        assertEquals(1, IterableUtil.sizeOf(messageIterable));
    }
}