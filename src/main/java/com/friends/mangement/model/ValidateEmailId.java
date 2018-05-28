package com.friends.mangement.model;

import java.util.HashMap;
import java.util.Map;

public class ValidateEmailId {

    boolean status;

    String emailId;

    Map<String, Integer> map = new HashMap<String, Integer>();

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }



}
