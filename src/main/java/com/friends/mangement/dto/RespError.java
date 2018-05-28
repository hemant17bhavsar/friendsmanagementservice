package com.friends.mangement.dto;

public class RespError {
    
    private String error;
    
    private String code;
    
    private String failureMessage;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("error=");
        builder.append(error);
        builder.append(",code=");
        builder.append(code);
        builder.append(",failureMessage=");
        builder.append(failureMessage);
        return builder.toString();
        

    }

}
