package com.friends.mangement.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.friends.mangement.dto.ReqCreateFriend;
import com.friends.mangement.dto.RespFriendList;
import com.friends.mangement.dto.ReqRetrivedFriendList;
import com.friends.mangement.exception.CustomGenericException;
import com.friends.mangement.model.UserRelationShip;
import com.friends.mangement.model.ValidateEmailId;

@Transactional
public interface FriendMangementDao {

    ValidateEmailId validateEmailIds(ReqCreateFriend validateEmailIds)
            throws CustomGenericException;

    void insertRelationShipData(UserRelationShip userRelationShip)
            throws CustomGenericException;

    RespFriendList getFriendList(ReqRetrivedFriendList friends)
            throws CustomGenericException;

    List<UserRelationShip>  getExistRelationShip(int requestId, int targetId,
            String relationType, boolean flag)
            throws Exception;

    List<UserRelationShip> getUserRelationData(int requestId, int targetId)
            throws CustomGenericException;

    void updateUserRelationData(int requestId, int targetId,
            String newRelationShip, String oldRelationShip)
            throws CustomGenericException;

    RespFriendList getListEmailForNotification(int userId)
            throws CustomGenericException;
}
