package com.tennismauel.auth.mapper;

import com.tennismauel.auth.config.security.dto.OAuthAttributes;
import com.tennismauel.auth.entity.Role;
import com.tennismauel.auth.entity.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OAuth2ToUserMapperTest {
    private OAuth2ToUserMapper oAuth2ToUserMapper= Mappers.getMapper(OAuth2ToUserMapper.class);

    @Test
    public void setRole_Member(){
        String email="foo@bar";
        String nick="foo";
        Character gender='M';
        Integer age=20;
        String provider="naver";

        OAuthAttributes oAuthAttributes=OAuthAttributes.builder()
                .email(email)
                .nick(nick)
                .gender(gender)
                .age(age)
                .provider(provider).build();

        User user=oAuth2ToUserMapper.oAuth2ToUser(oAuthAttributes);
        assertEquals(user.getEmail(), email);
        assertEquals(user.getNick(), nick);
        assertEquals(user.getGender(), gender);
        assertEquals(user.getAge(), age);
        assertEquals(user.getProvider(), provider);
        assertEquals(user.getRole(), Role.MEMBER);
    }

    @Test
    public void setRole_Guest(){
        String email="foo@bar";
        String nick="foo";
        String profile="profile.jpg";
        Character gender=null;
        Integer age=20;
        String phone="01012345678";
        String provider="naver";

        OAuthAttributes oAuthAttributes=OAuthAttributes.builder()
                .email(email)
                .nick(nick)
                .profile(profile)
                .gender(gender)
                .age(age)
                .phone(phone)
                .provider(provider).build();

        User user=oAuth2ToUserMapper.oAuth2ToUser(oAuthAttributes);
        assertEquals(user.getEmail(), email);
        assertEquals(user.getNick(), nick);
        assertEquals(user.getProfile(), profile);
        assertEquals(user.getGender(), gender);
        assertEquals(user.getAge(), age);
        assertEquals(user.getPhone(), phone);
        assertEquals(user.getProvider(), provider);
        assertEquals(user.getRole(), Role.GUEST);
    }
}
