package com.inside.test.controller;

import com.inside.test.model.*;
import com.inside.test.service.AuthService;
import com.inside.test.service.JwtProvider;
import com.inside.test.service.MessageService;
import com.inside.test.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class MainController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final MessageService messageService;

    /**
     * endpoint '/api/login' for user authorization. with luck, the answer will come
     * @param authRequest object with username and password
     * @return returns an object with a access token
     */
    @PostMapping(path="login")
    public ResponseEntity<JwtResponse> login(
            @RequestBody JwtRequest authRequest
    ) {
        final JwtResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    /**
     * endpoint '/api/add' to create a new user
     * @param authRequest object with username and password
     * @return will return the newly created user
     */
    @PostMapping(path="add")
    public ResponseEntity createUser(
            @RequestBody JwtRequest authRequest
    ){
        if (userService.getByName(authRequest.getName()) != null){
            return ResponseEntity.badRequest().body("user exist");
        } else {
            User user = userService.createUser(authRequest.getName(), authRequest.getPassword());
            return ResponseEntity.ok(user);
        }
    }

    /**
     * endpoint '/api/message'
     to create a new user message or get the history of user messages if the message starts with a history, you need to specify the number of returned messages separated by a space
     * @param token header Authorization requires token
     * @param messageRequest object with username and message text
     * @return an object with messages will be returned if it is a history request or a server response 200 if the message was successfully created and 401 if the access token is invalid
     */
    @PostMapping(path="message")
    public ResponseEntity sendMessage(
            @RequestHeader("Authorization") String token,
            @RequestBody MessageRequest messageRequest
    ) {
        if (jwtProvider.validateAccessToken(token)){
            final int userId = userService.getIdByUserName(messageRequest.getName());
            if (messageService.isHistory(messageRequest.getMessage())) {
                final int limit = Integer.parseInt(messageRequest.getMessage().replace("history ", ""));
                return ResponseEntity.ok(userService.getHistory(messageRequest.getName(), limit));
            } else {
                messageService.createMessage(
                        userId,
                        messageRequest.getMessage()
                );
                return ResponseEntity.ok().body("message create");
            }
        } else {
            return ResponseEntity.status(401).body("wrong token");
        }
    }
}
