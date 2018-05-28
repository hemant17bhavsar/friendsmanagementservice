package com.friends.mangement.service;

import com.friends.mangement.dto.ReqCreateFriend;
import com.friends.mangement.dto.RespFriendList;
import com.friends.mangement.dto.ResponseBlockfriend;
import com.friends.mangement.dto.ReqRetrivedFriendList;
import com.friends.mangement.exception.CustomGenericException;
import com.friends.mangement.model.ValidateEmailId;


public interface FriendMangementService {

    ValidateEmailId validateEmailIds(ReqCreateFriend validateEmailIds)
            throws CustomGenericException;

    void createFriendConnection(ValidateEmailId validateEmailId,
            ReqCreateFriend validateEmailIds)
            throws CustomGenericException, Exception;

    RespFriendList getFriendList(ReqRetrivedFriendList friends)
            throws CustomGenericException;

    RespFriendList getCommonFriendList(ValidateEmailId validateEmailId,
            ReqCreateFriend friends)
            throws CustomGenericException;

    void subcribeFriend(ValidateEmailId validateEmailId, ReqCreateFriend friends)
            throws CustomGenericException, Exception;

    ResponseBlockfriend blockFriend(int requestId, int targetId)
            throws CustomGenericException;
    
    RespFriendList getListEmailForNotification(int userId)
            throws CustomGenericException;



}
