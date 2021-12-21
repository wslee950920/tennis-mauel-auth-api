package com.tennismauel.auth.repository;

import com.tennismauel.auth.entity.Role;
import com.tennismauel.auth.entity.User;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
//@ExtendWith(SpringExtension.class)가 @DataJpaTest에 포함 됨
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository UserRepository;

    @Test
    public void saveUserSuccess() {
        String email = "foo@bar";
        String nick = null;
        Character gender = null;
        String provider = "kakao";
        Integer age = null;
        String phone = null;
        String profile = null;
        Role role = Role.GUEST;

        User savedUser = UserRepository.save(User.builder()
                .email(email)
                .nick(nick)
                .gender(gender)
                .age(age)
                .phone(phone)
                .role(role)
                .profile(profile)
                .provider(provider).build());

        User selectedUser = UserRepository.getById(savedUser.getId());
        assertEquals(email, selectedUser.getEmail());
        assertEquals(nick, selectedUser.getNick());
        assertEquals(gender, selectedUser.getGender());
        assertEquals(provider, selectedUser.getProvider());
        assertEquals(age, selectedUser.getAge());
        assertEquals(phone, selectedUser.getPhone());
        assertEquals(profile, selectedUser.getProfile());
        assertEquals(role, selectedUser.getRole());
    }

    @Test
    public void saveUserFail_UniqueEmail(){
        String email = "foo@bar";
        String nick = null;
        Character gender = null;
        String provider = "kakao";
        Integer age = null;
        String phone = null;
        String profile = null;
        Role role = Role.GUEST;

        UserRepository.save(User.builder()
                .email(email)
                .nick(nick)
                .gender(gender)
                .age(age)
                .phone(phone)
                .role(role)
                .profile(profile)
                .provider(provider).build());

        User user=User.builder()
                .email(email)
                .nick(nick)
                .age(age)
                .phone(phone)
                .profile(profile)
                .gender(gender)
                .role(role)
                .provider(provider).build();
        assertThrows(DataIntegrityViolationException.class, () -> {
            UserRepository.save(user);
        });
    }

    @Test
    public void saveUserFail_UniqueNick(){
        String email = "foo@bar";
        String nick = "foo";
        Character gender = null;
        String provider = "kakao";
        Integer age = null;
        String phone = null;
        String profile = null;
        Role role = Role.GUEST;

        UserRepository.save(User.builder()
                .email(email)
                .nick(nick)
                .gender(gender)
                .age(age)
                .phone(phone)
                .role(role)
                .profile(profile)
                .provider(provider).build());

        email="bar@foo";
        User user=User.builder()
                .email(email)
                .nick(nick)
                .age(age)
                .phone(phone)
                .profile(profile)
                .gender(gender)
                .role(role)
                .provider(provider).build();
        assertThrows(DataIntegrityViolationException.class, () -> {
            UserRepository.save(user);
        });
    }

    @Test
    public void saveUserFail_Null() {
        String email = null;
        String nick = "foo";
        Character gender = 'M';
        String provider = null;
        Integer age = 20;
        String phone = "01012345678";
        String profile = "profile.jpg";
        Role role = null;

        User user=User.builder()
                .email(email)
                .nick(nick)
                .age(age)
                .phone(phone)
                .profile(profile)
                .gender(gender)
                .role(role)
                .provider(provider).build();
        assertThrows(DataIntegrityViolationException.class, () -> {
            UserRepository.save(user);
        });
    }

    @Test
    public void saveUserFail_Length() {
        String email = "foo@bar";
        String nick = "foo".repeat(50);
        Character gender = 'M';
        String provider = "kakao";
        Integer age = 20;
        String profile = "profile.jpg";
        String phone = "01012345678";
        Role role = Role.MEMBER;

        User user=User.builder()
                .email(email)
                .nick(nick)
                .age(age)
                .phone(phone)
                .profile(profile)
                .gender(gender)
                .role(role)
                .provider(provider).build();
        assertThrows(DataIntegrityViolationException.class, () -> {
            UserRepository.save(user);
        });
    }
}
