package com.friends.mangement.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonAutoDetect(fieldVisibility=Visibility.ANY)
public class ResponseError {

    private String code;
	private String failureMessage;
	
	/**
	 * 
	 */
	public ResponseError() {
		// Default Constructor
	}

    public ResponseError(String code, String failureMessage) {
        this.code = code;
		this.failureMessage = failureMessage;
	}
	
	@JsonProperty("code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@JsonProperty("failureMessage")
	public String getFailureMessage() {
		return failureMessage;
	}

	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}


}
