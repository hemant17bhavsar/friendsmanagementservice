package com.friends.mangement.exception;

import org.springframework.http.HttpStatus;

public class CustomGenericException extends Exception {

    private static final long serialVersionUID = 1L;

    private String errCode;

    private String errMsg;

    protected HttpStatus httpErrorCode;

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }


    public HttpStatus getHttpErrorCode() {
        return httpErrorCode;
    }

    public void setHttpErrorCode(HttpStatus httpErrorCode) {
        this.httpErrorCode = httpErrorCode;
    }

    public CustomGenericException(String errCode, String errMsg,
            HttpStatus httpErrorCode) {
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.httpErrorCode = httpErrorCode;
    }

}
