package com.friends.mangement.dto;

public class RespSuccess {


    private String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("success=");
        builder.append(success);
        return builder.toString();

    }


}
