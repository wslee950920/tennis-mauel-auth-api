package com.tennismauel.auth.api.Google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Person {
    private ArrayList<JSONObject> genders;
    private ArrayList<JSONObject> birthdays;
    private ArrayList<JSONObject> phoneNumbers;
    private ArrayList<JSONObject> nicknames;

    public Character getGender() {
        if(this.genders==null){
            return null;
        }

        String gender=(String) this.genders.get(0).get("value");
        log.debug(gender);

        if(gender.equals("male")){
            return 'M';
        } else {
            return 'F';
        }
    }

    public Integer getAge(){
        if(this.birthdays==null){
            return null;
        }

        HashMap date=(LinkedHashMap) this.birthdays.get(0).get("date");
        int year=(int) date.get("year");
        int now = Calendar.getInstance().get(Calendar.YEAR);
        log.debug(Integer.toString(now)+' '+Integer.toString(year));

        int age=(int) Math.floor((now-year)/10)*10;

        return age;
    }

    public String getPhone(){
        if(this.phoneNumbers==null){
            return null;
        }

        String phone=(String) this.phoneNumbers.get(0).get("value");
        String ret=phone.replace("-", "");

        return ret;
    }

    public String getNick(){
        if(this.nicknames==null){
            return null;
        }

        String nick=(String) this.nicknames.get(0).get("value");

        return nick;
    }
}
