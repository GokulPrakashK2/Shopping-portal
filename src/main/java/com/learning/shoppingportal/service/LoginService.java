package com.learning.shoppingportal.service;


import com.learning.shoppingportal.modal.entity.UserDetails;
import com.learning.shoppingportal.modal.io.LoginRequest;
import com.learning.shoppingportal.modal.io.LoginResponse;
import com.learning.shoppingportal.modal.io.ResponseWrapper;
import com.learning.shoppingportal.modal.io.SignUpRequest;
import com.learning.shoppingportal.repository.UserDetailRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class LoginService {

    @Autowired
    private UserDetailRepository userDetailRepo;

    public Mono<LoginResponse> authenticateUser(LoginRequest loginRequest) {
        return Mono.fromCallable(() -> userDetailRepo.findByEmail(loginRequest.getUserName()).get())
                .flatMap(userDetails -> new BCryptPasswordEncoder().matches(loginRequest.getPassword(), userDetails.getPassword()) ?
                        createAccessToken(userDetails) : Mono.error(new ValidationException("Please use valid credentials")));
    }

    public Mono<LoginResponse> createAccessToken(UserDetails userDetails) {
        return Mono.just(Keys.secretKeyFor(SignatureAlgorithm.HS256))
                .map(key -> Jwts.builder()
                        .setSubject(userDetails.getEmail())
                        .claim("firstName", userDetails.getFirstName())
                        .claim("lastName", userDetails.getLastName())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(new Date().getTime() + 240000))
                        .signWith(key)
                        .compact())
                .map(accessToken -> LoginResponse.builder()
                        .accessToken(accessToken)
                        .userName(userDetails.getFirstName() + " " + userDetails.getLastName())
                        .build());
    }

//    public Mono<ResponseWrapper> registerUser(SignUpRequest loginRequest) {
//        UserDetails userDetails=new UserDetails();
//        userDetails.setAddress(loginRequest.getAddress());
//        userDetails.setFirstName(loginRequest.getFirstName());
//        userDetails.setLastName(loginRequest.getLastName());
//        userDetails.setPassword(loginRequest.getPassword());
//        userDetailRepo.save(userDetails);
//        ResponseWrapper<String> responseWrapper=ResponseWrapper.<String>builder().data("").success(true).build();
//        return Mono.just(responseWrapper);
//    }
}
