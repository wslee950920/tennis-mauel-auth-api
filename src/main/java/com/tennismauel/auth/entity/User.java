package com.tennismauel.auth.entity;

import com.tennismauel.auth.config.security.dto.OAuthAttributes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
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
}
