package com.inside.test.service;

import com.inside.test.model.Message;
import com.inside.test.model.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    /**
     * create a message for the user
     * @param userId user id (int)
     * @param message message text (string)
     * @return object of message type with created message
     */
    public Message createMessage(
            @NonNull int userId,
            @NonNull String message
    ){
        if (message == null){
            throw new NullPointerException("message cant be null");
        }
        if (userId < 1){
            throw new IllegalArgumentException("user id need more zero");
        }
        Message _message = new Message();
        _message.setMessage(message);
        _message.setUserId(userId);
        _message.setCreateDate(new Timestamp(new Date().getTime()));
        messageRepository.save(_message);
        return _message;
    }

    /**
     * checks if the message is system history. message must start with the keyword history
     * @param message message text (string)
     * @return true if the message asks for a history and false for a normal message
     */
    public boolean isHistory(
            @NonNull String message
    ){
        if (message == null){
            throw new NullPointerException("message cant be null");
        }
        return message.startsWith("history ");
    }


    /**
     * get a given number of recent messages for a user
     * @param userId user id (int)
     * @param limit latest message limit (int)
     * @return iterable object with found messages
     */
    public Iterable<Message> getHistory(
            @NonNull int userId,
            @NonNull int limit
    ){
        if (userId < 1){
            throw new IllegalArgumentException("user id need more zero");
        }
        if (limit < 1){
            throw new IllegalArgumentException("limit need more zero");
        }
        Pageable pageable = PageRequest.of(0, limit);
        return messageRepository.findAllByUserIdOrderByCreateDateDesc(userId, pageable).getContent();
    }
}
