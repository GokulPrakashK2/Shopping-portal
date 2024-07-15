package com.learning.shoppingportal.service;


import com.learning.shoppingportal.modal.entity.UserDetails;
import com.learning.shoppingportal.modal.io.LoginResponse;
import com.learning.shoppingportal.modal.io.SignUpRequest;
import com.learning.shoppingportal.modal.mapper.UserDetailsMapper;
import com.learning.shoppingportal.repository.UserDetailRepository;
import com.learning.shoppingportal.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SignupService {

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private LoginService loginService;

    public Mono<LoginResponse> createUserAccount(SignUpRequest signUpRequest) {
        return Mono.just(signUpRequest)
                .map(UserDetailsMapper.INSTANCE::mapToEntity)
                .flatMap(this::hashPassword)
                .map(userDetailRepository::save)
                .flatMap(loginService::createAccessToken)
                .doOnError(throwable -> Mono.error(() -> throwable));
    }

    private Mono<UserDetails> hashPassword(UserDetails userDetails) {
        return Mono.just(userDetails.getPassword())
                .map(password -> new BCryptPasswordEncoder().encode(password))
                .doOnNext(userDetails::setPassword)
                .thenReturn(userDetails);
    }
}
