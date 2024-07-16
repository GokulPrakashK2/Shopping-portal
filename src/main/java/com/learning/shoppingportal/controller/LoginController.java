package com.learning.shoppingportal.controller;

import com.learning.shoppingportal.modal.io.LoginRequest;
import com.learning.shoppingportal.modal.io.ResponseWrapper;
import com.learning.shoppingportal.modal.io.SignUpRequest;
import com.learning.shoppingportal.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/auth/login")
    public Mono<ResponseWrapper> loginUser(@RequestBody LoginRequest loginRequest){
        return loginService.authenticateUser(loginRequest)
                .map(response->new ResponseWrapper(true,response,null));
    }

    /**
     * Registers a new user by calling the 'registerUser' method of the 'LoginService' class.
     *
     * @param sign The SignUpRequest object containing the new user's details.
     * @return A Mono emitting a ResponseWrapper object representing the registration response.
     */
    @PostMapping("/sign-up")
    public Mono<ResponseWrapper> signUpUser(@RequestBody SignUpRequest sign){
        return loginService.registerUser(sign);
    }

}
