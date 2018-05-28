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
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.friends.mangement.dto.ReqCreateFriend;
import com.friends.mangement.dto.ReqRetrivedFriendList;
import com.friends.mangement.dto.ReqSubcribeFriend;
import com.friends.mangement.exception.CustomGenericException;
import com.friends.mangement.model.RequestReceivingUpdatesEmails;
import com.friends.mangement.model.ValidateEmailId;
import com.friends.mangement.service.FriendMangementService;

@SuppressWarnings("deprecation")
@RunWith(MockitoJUnitRunner.class)
public class ViewMangementControllerTest {


    @InjectMocks
    ViewMangementController viewMangementController =
            new ViewMangementController();

    @Mock
    FriendMangementService friendMangementService;
    
    static RequestReceivingUpdatesEmails friendEx;

    static ReqRetrivedFriendList reqRetrivedFriendList, reqRetrivedFriendListEx;
    static ReqCreateFriend reqCreateFriend, reqCreateFriendException;

    static ValidateEmailId validateEmailId;

    static ReqSubcribeFriend reqSubcribeFriend,reqSubcribeFriendException;

    @BeforeClass
    public static void init() {

        reqRetrivedFriendList = new ReqRetrivedFriendList();
        reqCreateFriendException = new ReqCreateFriend();
        reqCreateFriend = new ReqCreateFriend();
        validateEmailId = new ValidateEmailId();
        reqRetrivedFriendListEx = new ReqRetrivedFriendList();
        List<String> friendList = new ArrayList<String>();
        List<String> friendListException = new ArrayList<String>();
        friendList.add("abc@example.com");
        friendList.add("xyz@example.com");
        friendListException.add("abc@example.com");
        reqCreateFriendException.setFriends(friendListException);
        reqCreateFriend.setFriends(friendList);
        reqRetrivedFriendListEx.setEmail("xyz@example.com");
        validateEmailId.setStatus(true);
        reqRetrivedFriendList.setEmail("xyz@example.com");
        
        Map<String,Integer> map = new HashMap<String, Integer>();
        map.put("abc@example.com", 1);
        map.put("xyz@example.com", 2);
        validateEmailId.setMap(map);
        validateEmailId.setMap(map);

        friendEx = new RequestReceivingUpdatesEmails();
        friendEx.setSender("abc@example.com");

        reqSubcribeFriend = new ReqSubcribeFriend();
        reqSubcribeFriend.setRequestor("abc@example.com");
        reqSubcribeFriend.setTarget("xyz@example.com");


    }


    @Test
    public void testretrivedFriendListExecption()
            throws CustomGenericException {

        Assert.assertEquals(HttpStatus.NOT_FOUND, viewMangementController
                .retrivedFriendList(reqRetrivedFriendList).getStatusCode());


    }

    @Test
    public void testretrivedCommonFriendsListExecption()
            throws CustomGenericException {

        Assert.assertEquals(HttpStatus.BAD_REQUEST,
                viewMangementController
                .retrivedCommonFriendsList(reqCreateFriendException)
                .getStatusCode());


    }

    @Test
    public void testgetListEmailForNotificationExecption()
            throws CustomGenericException {

        Assert.assertEquals(HttpStatus.NOT_FOUND,
                viewMangementController
                        .getListEmailForNotification(friendEx)
                        .getStatusCode());


    }

}
