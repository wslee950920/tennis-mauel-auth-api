package com.tennismauel.auth.entity;

import com.tennismauel.auth.config.security.dto.OAuthAttributes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        uniqueConstraints={
                @UniqueConstraint(
                        columnNames={"email"}
                ),
                @UniqueConstraint(
                        columnNames={"nick"}
                )
        }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 50, nullable = true)
    private String nick;

    @Column(length = 300, nullable = true)
    private String profile;

    @Column(nullable = true)
    private Character gender;

    @Column(nullable = true)
    private Integer age;

    @Column(length = 20, nullable = true)
    private String phone;

    @Column(length = 10, nullable = false)
    private String provider;

    @Enumerated(EnumType.STRING)
    @Column(length=20, nullable = false)
    private Role role;

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public User update(OAuthAttributes oAuthAttributes) {
        if(this.nick==null||!this.nick.equals(oAuthAttributes.getNick())){
            this.nick=oAuthAttributes.getNick();
        }

        if(this.profile==null||!this.profile.equals(oAuthAttributes.getProfile())){
            this.profile= oAuthAttributes.getProfile();
        }

        if(this.gender==null||!this.gender.equals(oAuthAttributes.getGender())){
            this.gender=oAuthAttributes.getGender();
        }

        if(this.age==null||!this.age.equals(oAuthAttributes.getAge())){
            this.age=oAuthAttributes.getAge();
        }

        if(this.phone==null||!this.phone.equals(oAuthAttributes.getPhone())){
            this.phone=oAuthAttributes.getPhone();
        }

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public static class UserBuilder {
        private Long id;
        private String email;
        private String nick;
        private String profile;
        private Character gender;
        private Integer age;
        private String phone;
        private String provider;
        private Role role;

        UserBuilder() {
        }

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder nick(String nick) {
            this.nick = nick;
            return this;
        }

        public UserBuilder profile(String profile) {
            this.profile = profile;
            return this;
        }

        public UserBuilder gender(Character gender) {
            this.gender = gender;
            return this;
        }

        public UserBuilder age(Integer age) {
            this.age = age;
            return this;
        }

        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder provider(String provider) {
            this.provider = provider;
            return this;
        }

        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public User build() {
            return new User(id, email, nick, profile, gender, age, phone, provider, role);
        }

        public String toString() {
            return "User.UserBuilder(id=" + this.id + ", email=" + this.email + ", nick=" + this.nick + ", profile=" + this.profile + ", gender=" + this.gender + ", age=" + this.age + ", phone=" + this.phone + ", provider=" + this.provider + ", role=" + this.role + ")";
        }
    }
}
