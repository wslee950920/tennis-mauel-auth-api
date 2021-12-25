package com.tennismauel.auth.config.security.service;

import com.tennismauel.auth.config.security.dto.OAuthAttributes;
import com.tennismauel.auth.config.security.exception.EmailAlreadyExistException;
import com.tennismauel.auth.entity.User;
import com.tennismauel.auth.mapper.OAuth2ToUserMapper;
import com.tennismauel.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService {
    private final UserRepository userRepository;
    private final OAuth2ToUserMapper oAuth2ToUserMapper;
    private final OAuth2AttributeService oAuth2AttributeService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException{
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate=new DefaultOAuth2UserService();
        OAuth2User oAuth2User=delegate.loadUser(request);

        OAuthAttributes attributes= oAuth2AttributeService.getOAuthAttributes(request, oAuth2User.getAttributes());
        User user=saveOrUpdate(attributes);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes){
        User user=userRepository.findByEmail(attributes.getEmail())
                .map(entity->{
                    if(!entity.getProvider().equals(attributes.getProvider())){
                        throw new EmailAlreadyExistException("Please use your "+entity.getProvider()+" account to login.");
                    }

                    return entity.update(attributes);
                })
                .orElseGet(()->{
                    userRepository.findByNick(attributes.getNick()).ifPresent(entity->{
                        attributes.setNickNull();
                    });

                    return oAuth2ToUserMapper.oAuth2ToUser(attributes);
                });

        return userRepository.save(user);
    }
}
