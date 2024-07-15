package com.learning.shoppingportal.modal.mapper;


import com.learning.shoppingportal.modal.entity.UserDetails;
import com.learning.shoppingportal.modal.io.SignUpRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserDetailsMapper {

    UserDetailsMapper INSTANCE= Mappers.getMapper(UserDetailsMapper.class);

    UserDetails mapToEntity(SignUpRequest signUpRequest);
}
