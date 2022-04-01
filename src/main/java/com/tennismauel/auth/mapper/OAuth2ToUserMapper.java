package com.tennismauel.auth.mapper;

import com.tennismauel.auth.config.security.dto.OAuthAttributes;
import com.tennismauel.auth.entity.Role;
import com.tennismauel.auth.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OAuth2ToUserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    User oAuth2ToUser(OAuthAttributes oAuthAttributes);

    @AfterMapping
    default void setRole(@MappingTarget User.UserBuilder user, OAuthAttributes oAuthAttributes){
        if(oAuthAttributes.getAge()==null||oAuthAttributes.getGender()==null||oAuthAttributes.getNick()==null){
            user.role(Role.GUEST);
        } else{
            user.role(Role.MEMBER);
        }
    }
}
