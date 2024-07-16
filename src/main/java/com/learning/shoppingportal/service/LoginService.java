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

    /**
     * Creates an access token for the given user details by generating a JWT token with the user's email as the subject,
     * first name and last name as claims, current date as the issued date, and an expiration time of 240 seconds.
     * The token is signed using a secret key generated with the HS256 algorithm.
     * The access token and the user's full name are encapsulated in a LoginResponse object and returned.
     */
    public Mono<LoginResponse> authenticateUser(LoginRequest loginRequest) {
        return Mono.fromCallable(() -> userDetailRepo.findByEmail(loginRequest.getUserName()).get())
                .flatMap(userDetails -> new BCryptPasswordEncoder().matches(loginRequest.getPassword(), userDetails.getPassword()) ?
                        createAccessToken(userDetails) : Mono.error(new ValidationException("Please use valid credentials")));
    }

    /**
     * Creates an access token for the given user details by generating a JWT token with the user's email as the subject,
     * first name and last name as claims, current date as the issued date, and an expiration time of 240 seconds.
     * The token is signed using a secret key generated with the HS256 algorithm.
     * The access token and the user's full name are encapsulated in a LoginResponse object and returned.
     */
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

    public Mono<ResponseWrapper> registerUser(SignUpRequest loginRequest) {
        return Mono.defer(() -> {
            if(userDetailRepo.findByEmail(loginRequest.getEmail()).isPresent()) {
                return Mono.error(new ValidationException("User already exists"));
            }
        
            UserDetails userDetails = new UserDetails();
            userDetails.setAddress(loginRequest.getAddress());
            userDetails.setFirstName(loginRequest.getFirstName());
            userDetails.setLastName(loginRequest.getLastName());
            userDetails.setPassword(new BCryptPasswordEncoder().encode(loginRequest.getPassword()));
            userDetails.setEmail(loginRequest.getEmail());
            userDetails.setPhoneNumber(loginRequest.getPhoneNumber());
        
            try {
                userDetailRepo.save(userDetails);
            } catch (Exception e) {
                return Mono.error(e);
            }
        
            ResponseWrapper<String> responseWrapper = ResponseWrapper.<String>builder().data("").success(true).build();
            return Mono.just(responseWrapper);
        });
    }
}
