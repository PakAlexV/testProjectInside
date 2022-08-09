package com.inside.test.service;

import com.inside.test.model.Message;
import com.inside.test.model.MessageRepository;
import com.inside.test.model.UserRepository;
import com.inside.test.model.User;
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
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    private final String name = "test";
    private final String password = "123";

    @Test
    @DisplayName("Should create user")
    public void shouldCreateUser() {
        assertThrows(NullPointerException.class, ()->userService.createUser(null, password));
        assertThrows(NullPointerException.class, ()->userService.createUser(name, null));

        User user = userService.createUser(name, password);
        assertNotNull(user);
        assertNotNull(user.getName());
        assertNotNull(user.getPassword());

        assertEquals(name, user.getName());
        assertEquals(password, user.getPassword());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should get user by name")
    public void shouldGetByName() {
        assertThrows(NullPointerException.class, ()->userService.getByName(null));

        User mockUser = userService.createUser(name, password);
        when(userRepository.findByName(name)).thenReturn(mockUser);

        User user = userService.getByName(name);

        assertEquals(mockUser, user);
        assertEquals(name, user.getName());
        verify(userRepository, times(1)).findByName(name);
    }

    @Test
    @DisplayName("Should get id for user by name")
    void shouldGetIdByUserName() {
        assertThrows(NullPointerException.class, ()->userService.getIdByUserName(null));

        User mockUser = userService.createUser(name, password);
        mockUser.setId(1);
        when(userRepository.findByName(name)).thenReturn(mockUser);

        int userId = userService.getIdByUserName(name);

        assertEquals(mockUser.getId(), userId);
        assertEquals(1, userId);
        verify(userRepository, times(1)).findByName(name);
    }

    @Test
    @DisplayName("Should get history")
    void shouldGetHistory() {
        assertThrows(IllegalArgumentException.class, () -> userService.getHistory(name, -1));
        assertThrows(IllegalArgumentException.class, () -> userService.getHistory(name, 0));
        assertThrows(NullPointerException.class, ()-> userService.getHistory(null, 1));

        User mockUser = userService.createUser(name, password);
        mockUser.setId(1);
        Message mockMessage = messageService.createMessage(mockUser.getId(), "test-text");
        List<Message> messages = new ArrayList<>();
        messages.add(mockMessage);
        messages.add(mockMessage);
        mockUser.setMessages(messages);

        when(userRepository.findByName(name)).thenReturn(mockUser);

        Iterable<Message> messageIterable = userService.getHistory(name, 1);
        assertEquals(1, IterableUtil.sizeOf(messageIterable));
    }
}