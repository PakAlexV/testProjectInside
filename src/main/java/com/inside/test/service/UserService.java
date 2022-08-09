package com.inside.test.service;

import com.inside.test.model.Message;
import com.inside.test.model.User;
import com.inside.test.model.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * get user by name
     * @param name search username
     * @return found user
     */
    public User getByName(
            @NonNull String name
    ){
        if (name == null){
            throw new NullPointerException("name cant be null");
        }
        return userRepository.findByName(name);
    }

    /**
     * creating a new user
     * @param name user name (string)
     * @param password user password (string)
     * @return will return the newly created user
     */
    public User createUser(
            @NonNull String name,
            @NonNull String password
    ){
        if (name == null){
            throw new NullPointerException("name cant be null");
        }
        if (password == null){
            throw new NullPointerException("password cant be null");
        }

        User user = new User();
        user.setName(name);
        user.setPassword(password);
        userRepository.save(user);
        return user;
    }

    /**
     * get user id by name
     * @param name searched username (string)
     * @return id of the found user (int)
     */
    public int getIdByUserName(
            @NonNull String name
    ){
        if (name == null){
            throw new NullPointerException("name cant be null");
        }
        User user = userRepository.findByName(name);
        return user.getId();
    }

    /**
     * get message history for user
     * @param userName username for messages (string)
     * @param limit number of returned messages (int)
     * @return List with user messages sorted by creation time in descending order
     */
    public List<Message> getHistory(
            @NonNull String userName,
            @NonNull int limit){
        if (userName == null){
            throw new NullPointerException("user name cant be null");
        }
        if (limit < 1){
            throw new IllegalArgumentException("limit need more zero");
        }
        User user = userRepository.findByName(userName);
        List<Message> messageList = user.getMessagesOrderedDesc();
        return messageList.subList(0, limit);
    }
}
