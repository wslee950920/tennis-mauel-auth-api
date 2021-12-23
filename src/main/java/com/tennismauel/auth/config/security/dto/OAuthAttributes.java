package com.tennismauel.auth.config.security.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tennismauel.auth.api.Google.Google;
import com.tennismauel.auth.api.Google.Person;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.Map;

@Slf4j
@Getter
@Builder
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;

    private String email;
    private String nick;
    private String profile;
    private Character gender;
    private Integer age;
    private String phone;
    private String provider;

    public void setNickNull(){
        this.nick=null;
    }

    public static OAuthAttributes of(OAuth2UserRequest request,  Map<String, Object> attributes) {
        String registrationId=request.getClientRegistration().getRegistrationId();
        String userNameAttributeName=request.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        if("naver".equals(registrationId)) {
            return ofNaver(registrationId, "id", attributes);
        } else if("kakao".equals(registrationId)){
            return ofKakao(registrationId, "id", attributes);
        }

        String accessToken=request.getAccessToken().getTokenValue();
        log.debug("access token : "+accessToken);
        return ofGoogle(accessToken, registrationId, userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String accessToken, String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        log.debug(attributes.toString());

        Google google = new Google(accessToken);
        String raw = google.getPerson();
        log.debug(raw);

        ObjectMapper objectMapper = new ObjectMapper();
        Person person = null;
        try {
            person = objectMapper.readValue(raw, Person.class);
            log.debug(String.format("%c, %d, %s, %s", person.getGender(), person.getAge(), person.getNick(), person.getPhone()));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

        return OAuthAttributes.builder()
                .email((String) attributes.get("email"))
                .profile((String) attributes.get("picture"))
                .nick(person.getNick())
                .gender(person.getGender())
                .age(person.getAge())
                .phone(person.getPhone())
                .provider(registrationId)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        log.debug(response.toString());

        String mobile=(String) response.get("mobile");
        String phone=null;
        if(mobile!=null){
            phone=mobile.replace("-", "");
        }

        String agerange=(String) response.get("age");
        Integer age=null;
        if(agerange!=null){
            age=Integer.valueOf(agerange.split("-")[0]);
        }

        String gender=(String) response.get("gender");
        Character g=null;
        if(gender!=null) {
            g=gender.charAt(0);
        }

        return OAuthAttributes.builder()
                .email((String) response.get("email"))
                .profile((String) response.get("profile_image"))
                .nick((String) response.get("nickname"))
                .age(age)
                .gender(g)
                .phone(phone)
                .provider(registrationId)
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        log.debug(attributes.toString());

        // kakao는 kakao_account에 유저정보가 있다. (email)
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");

        String gender=(String) kakaoAccount.get("gender");
        log.debug(gender);
        Character g=null;
        if(gender!=null){
            if(gender.equals("male")){
                g='M';
            } else{
                log.debug(gender);
                g='F';
            }
        }

        String agerange=(String) kakaoAccount.get("age_range");
        Integer age=null;
        if(agerange!=null){
            age=Integer.valueOf(agerange.split("~")[0]);
        }

        // kakao_account안에 또 profile이라는 JSON객체가 있다. (nickname, profile_image)
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

        String nick=null;
        String profile=null;
        if(kakaoProfile!=null){
            nick=(String) kakaoProfile.get("nickname");
            profile=(String) kakaoProfile.get("profile_image_url");
        }

        return OAuthAttributes.builder()
                .email((String) kakaoAccount.get("email"))
                .nick(nick)
                .profile(profile)
                .gender(g)
                .age(age)
                .provider(registrationId)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
}
