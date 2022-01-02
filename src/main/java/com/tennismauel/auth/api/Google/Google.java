package com.tennismauel.auth.api.Google;

import com.tennismauel.auth.api.ApiBinding;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

@Slf4j
@RequestScope   //빈이 주입되는 시점을 의미한다.
@Component
public class Google extends ApiBinding {
    private static final String PEOPLE_API_BASE_URL="https://people.googleapis.com/v1/people/me";

    public Google(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Retry(name = "person")
    @CircuitBreaker(name = "person", fallbackMethod = "emptyPerson")
    public Person getPerson(){
        return restTemplate.getForObject(PEOPLE_API_BASE_URL+"?personFields=genders,birthdays,phoneNumbers,nicknames", Person.class);
    }

    private Person emptyPerson(RuntimeException e){
        log.error(e.getLocalizedMessage());

        return new Person();
    }
}