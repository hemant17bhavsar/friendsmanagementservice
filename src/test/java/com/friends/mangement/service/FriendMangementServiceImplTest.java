package com.friends.mangement.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.friends.mangement.dao.FriendMangementDao;
import com.friends.mangement.dto.ReqCreateFriend;
import com.friends.mangement.dto.ReqRetrivedFriendList;
import com.friends.mangement.dto.RespFriendList;
import com.friends.mangement.dto.ResponseBlockfriend;
import com.friends.mangement.exception.CustomGenericException;
import com.friends.mangement.model.UserRelationShip;
import com.friends.mangement.model.ValidateEmailId;

@SuppressWarnings("deprecation")
@RunWith(MockitoJUnitRunner.class)
public class FriendMangementServiceImplTest {


    static ReqCreateFriend reqCreateFriend;
    static ValidateEmailId validateEmailId;
    static ReqRetrivedFriendList friends;
    static RespFriendList respFriendList;
    static ReqRetrivedFriendList reqRetrivedFriendList;
    static ResponseBlockfriend responseBlockfriend;
    static List<UserRelationShip> userRelationShipList;


    @Mock
    FriendMangementService friendMangementService;
    @Autowired
    FriendMangementServiceImpl friendMangementServiceImpl;



    @Mock
    FriendMangementDao friendMangementDao;

    @BeforeClass
    public static void init() {
        reqCreateFriend = new ReqCreateFriend();
        List<String> friendList = new ArrayList<String>();
        List<String> friendListException = new ArrayList<String>();
        friendList.add("abc@example.com");
        friendList.add("xyz@example.com");
        friendListException.add("abc@example.com");
        reqCreateFriend.setFriends(friendList);



        reqRetrivedFriendList = new ReqRetrivedFriendList();
        reqRetrivedFriendList.setEmail("abc@example.com");
        reqRetrivedFriendList.setUserId(2);

        UserRelationShip userRelationShip = new UserRelationShip();
        userRelationShip.setRelationshipId(2);
        userRelationShip.setRequestId(1);
        userRelationShip.setTargetId(2);
        userRelationShip.setRelationType("FRIEND");
        
        userRelationShipList =
                new ArrayList<UserRelationShip>();
        userRelationShipList.add(userRelationShip);



    }
    

    @Test
    public void testvalidateEmailIds() throws CustomGenericException {

        ValidateEmailId validateEmailIdResp =
                friendMangementService.validateEmailIds(reqCreateFriend);
        assertEquals(validateEmailIdResp, validateEmailId);

    }


    @Test
    public void testgetFriendList() throws CustomGenericException {

        List<String> friendList = new ArrayList<String>();
        friendList.add("abc@example.com");
        friendList.add("xyz@example.com");
        RespFriendList respFrdList =
                friendMangementService.getFriendList(friends);
        assertEquals(respFrdList, validateEmailId);

    }


    @Test
    public void testgetcommonFriendList() throws CustomGenericException {

        List<String> friendList = new ArrayList<String>();
        friendList.add("abc@example.com");
        friendList.add("xyz@example.com");
        RespFriendList respFrdList =
                friendMangementService.getCommonFriendList(validateEmailId,
                        reqCreateFriend);
        assertEquals(respFrdList, validateEmailId);

    }

    @Test
    public void testblockFriend() throws CustomGenericException {

        ResponseBlockfriend respblockfriend =
                friendMangementService.blockFriend(1, 2);
        assertEquals(responseBlockfriend, respblockfriend);

    }

    @Test
    public void testgetListEmailForNotification()
            throws CustomGenericException {

        RespFriendList resfrdList =
                friendMangementService.getListEmailForNotification(1);
        assertEquals(respFriendList, resfrdList);

    }


}
