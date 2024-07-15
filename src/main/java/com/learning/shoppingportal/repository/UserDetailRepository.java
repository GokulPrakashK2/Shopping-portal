package com.learning.shoppingportal.repository;


import com.learning.shoppingportal.modal.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetails, BigInteger> {


    Optional<UserDetails> findByEmailAndPassword(String userName, String encodedPassword);

    Optional<UserDetails> findByEmail(String userName);

}
