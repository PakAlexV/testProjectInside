package com.inside.test.service;

import com.inside.test.model.JwtRequest;
import com.inside.test.model.JwtResponse;
import com.inside.test.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final UserService userService;

    /**
     * checks for the presence of a user and his password. returns a token for an existing one
     * @param autRequest object with username and password
     * @return returns an object with a access token or an exception
     */
    public JwtResponse login(
            @NonNull JwtRequest autRequest
    ) {
        if (autRequest.getName() == null){
            throw new NullPointerException("name cant be null");
        }
        if (autRequest.getPassword() == null){
            throw new NullPointerException("password cant be null");
        }

        final User user = userService.getByName(autRequest.getName());
        if (user == null){
            throw new NullPointerException("user not found");
        }

        if (user.getPassword().equals(autRequest.getPassword()) == false){
            throw new SecurityException("wrong password");
        }
        else {
            final String accessToken = jwtProvider.generateAccessToken(user);
            refreshStorage.put(user.getName(), accessToken);
            return new JwtResponse(accessToken);
        }
    }

}
