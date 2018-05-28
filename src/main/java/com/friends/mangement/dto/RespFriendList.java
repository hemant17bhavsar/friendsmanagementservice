package com.friends.mangement.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RespFriendList {
    private String success;
    private List<String> friendList;
    private int count;
    private Map<String, Integer> map;

    /**
     * @return the success
     */
    public String getSuccess() {
        return success;
    }

    /**
     * @param success the success to set
     */
    public void setSuccess(String success) {
        this.success = success;
    }

    /**
     * @return the friendList
     */
    public List<String> getFriendList() {
        return friendList;
    }

    /**
     * @param friendList the friendList to set
     */
    public void setFriendList(List<String> friendList) {
        this.friendList = friendList;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the map
     */
    @JsonIgnore
    public Map<String, Integer> getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }
    



}
