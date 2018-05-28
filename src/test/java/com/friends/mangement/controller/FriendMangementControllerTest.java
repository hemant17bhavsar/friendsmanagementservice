package com.friends.mangement.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.friends.mangement.dto.ReqCreateFriend;
import com.friends.mangement.dto.ReqSubcribeFriend;
import com.friends.mangement.exception.CustomGenericException;
import com.friends.mangement.model.ValidateEmailId;
import com.friends.mangement.service.FriendMangementService;
import com.friends.mangement.service.FriendMangementServiceImpl;

@SuppressWarnings("deprecation")
@RunWith(MockitoJUnitRunner.class)
public class FriendMangementControllerTest {


    @InjectMocks
    FriendMangementController friendMangementController =
            new FriendMangementController();

    @Mock
    FriendMangementService friendMangementService;
    
    @Mock
    FriendMangementServiceImpl friendMangementServiceImpl =
            new FriendMangementServiceImpl();

    static ReqCreateFriend reqCreateFriend, reqCreateFriendException;

    static ValidateEmailId validateEmailId;

    static ReqSubcribeFriend reqSubcribeFriend,reqSubcribeFriendException;

    @BeforeClass
    public static void init() {

        reqCreateFriendException = new ReqCreateFriend();
        validateEmailId = new ValidateEmailId();
        reqCreateFriend = new ReqCreateFriend();
        List<String> friendList = new ArrayList<String>();
        List<String> friendListException = new ArrayList<String>();
        friendList.add("abc@example.com");
        friendList.add("xyz@example.com");
        friendListException.add("abc@example.com");
        reqCreateFriendException.setFriends(friendListException);
        reqCreateFriend.setFriends(friendList);
        validateEmailId.setStatus(true);
        
        Map<String,Integer> map = new HashMap<String, Integer>();
        map.put("abc@example.com", 1);
        map.put("xyz@example.com", 2);
        validateEmailId.setMap(map);
        validateEmailId.setMap(map);

        reqSubcribeFriend = new ReqSubcribeFriend();
        reqSubcribeFriend.setRequestor("abc@example.com");
        reqSubcribeFriend.setTarget("xyz@example.com");


    }


    @Test
    public void testcreateFriendConnection() throws CustomGenericException {

        Mockito.when(friendMangementService.validateEmailIds(reqCreateFriend))
                .thenReturn(validateEmailId);
        Assert.assertEquals(
                friendMangementController
                        .createFriendConnection(reqCreateFriend)
                        .getStatusCode(),
                HttpStatus.OK);

    }

    @Test
    public void testcreateFriendConnectionException()
            throws CustomGenericException {

        Assert.assertEquals(friendMangementController
                .createFriendConnection(reqCreateFriendException)
                .getStatusCode(),
                HttpStatus.BAD_REQUEST);
    }


    @Test
    public void testblockingFriendException() throws CustomGenericException {

        Assert.assertEquals(friendMangementController
                .blockingFriend(reqSubcribeFriend).getStatusCode(),
                HttpStatus.NOT_FOUND);
    }

}
