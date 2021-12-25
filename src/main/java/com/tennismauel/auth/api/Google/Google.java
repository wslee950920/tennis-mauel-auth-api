package com.tennismauel.auth.api.Google;

import com.tennismauel.auth.api.ApiBinding;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@NoArgsConstructor  //내부적으로는 부모 객체 생성 후, 자식 객체가 생성된다.
@RequestScope   //빈이 주입되는 시점을 의미한다.
@Component
public class Google extends ApiBinding {
    private static final String PEOPLE_API_BASE_URL="https://people.googleapis.com/v1/people/me";

    public String getPerson(){
        return restTemplate.getForObject(PEOPLE_API_BASE_URL+"?personFields=genders,birthdays,phoneNumbers,nicknames", String.class);
    }
}
