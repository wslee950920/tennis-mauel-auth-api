package com.tennismauel.auth.api.Google;

import com.tennismauel.auth.api.ApiBinding;

public class Google extends ApiBinding {
    private static final String PEOPLE_API_BASE_URL="https://people.googleapis.com/v1/people/me";

    public Google(String accessToken){
        super(accessToken);
    }

    public String getPerson(){
        return restTemplate.getForObject(PEOPLE_API_BASE_URL+"?personFields=genders,birthdays,phoneNumbers,nicknames", String.class);
    }
}
