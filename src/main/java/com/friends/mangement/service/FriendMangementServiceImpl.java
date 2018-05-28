package com.friends.mangement.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.friends.mangement.constants.Constants;
import com.friends.mangement.dao.FriendMangementDao;
import com.friends.mangement.dto.ReqCreateFriend;
import com.friends.mangement.dto.ReqRetrivedFriendList;
import com.friends.mangement.dto.RespFriendList;
import com.friends.mangement.dto.ResponseBlockfriend;
import com.friends.mangement.exception.CustomGenericException;
import com.friends.mangement.model.UserRelationShip;
import com.friends.mangement.model.ValidateEmailId;

@Service("friendMangementService")
public class FriendMangementServiceImpl implements FriendMangementService {
    private static Logger log = LogManager.getLogger();
    @Autowired
    FriendMangementDao friendMangementDao;

    /**
     * Validating emailIds.
     * 
     * @param ReqCreateFriend
     * @return ValidateEmailId
     * @throws CustomGenericException
     */
    @Override
    public ValidateEmailId validateEmailIds(ReqCreateFriend validateEmailIds)
            throws CustomGenericException {
        log.info(
                "friendMangementServiceImpl::validateEmailIds::validating email ids for requestid={}",
                validateEmailIds.getFriends().get(0));
        return friendMangementDao.validateEmailIds(validateEmailIds);
    }

    /**
     * created friend connection between two friends
     * 
     * @param ValidateEmailId,ReqCreateFriend
     * @return
     * @throws Exception
     */

    @Override
    public void createFriendConnection(ValidateEmailId validateEmailId,
            ReqCreateFriend requestFrdship) throws Exception {

        insertRelationShipData(validateEmailId, requestFrdship,
                Constants.FRIEND, true);
    }

    @Override
    public void subcribeFriend(ValidateEmailId validateEmailId,
            ReqCreateFriend subcribeFriend)
            throws CustomGenericException, Exception {
        insertRelationShipData(validateEmailId, subcribeFriend,
                Constants.SUBCRIBE, true);

    }
    private void insertRelationShipData(ValidateEmailId validateEmailId,
            ReqCreateFriend validateEmailIds, String relationType, boolean flag)
            throws Exception, CustomGenericException {
        log.info(
                "friendMangementServiceImpl::insertRelationShipData::creating friend connection betwwen two friends",
                validateEmailIds.getFriends().get(0));

        UserRelationShip userRelationShip = new UserRelationShip();

        userRelationShip.setRequestId((validateEmailId.getMap()
                .get(validateEmailIds.getFriends().get(0))));
        userRelationShip.setTargetId(validateEmailId.getMap()
                .get(validateEmailIds.getFriends().get(1)));

        userRelationShip.setRelationType(relationType);
        List<UserRelationShip> userRelationShipList = friendMangementDao.getExistRelationShip(
                userRelationShip.getRequestId(), userRelationShip.getTargetId(),
                        relationType, flag);
        for (UserRelationShip userRelaShip : userRelationShipList) {
            if (Constants.BLOCK.equals(userRelaShip.getRelationType())) {
                log.info("relationship is block");
                throw new CustomGenericException(Constants.ERRO_CODE_401,
                        Constants.ERRO_CODE_401_MESSAGE,
                        HttpStatus.UNAUTHORIZED);
            } else if (Constants.FRIEND
                    .equals(userRelaShip.getRelationType())) {
                log.info("friend connection already exist");
                throw new CustomGenericException("ERRO_CODE_409",
                        "data already exist for relationship :" + relationType,
                        HttpStatus.CONFLICT);

            } else if (Constants.SUBCRIBE
                    .equals(userRelaShip.getRelationType())
                    && userRelaShip.getRequestId() == validateEmailId.getMap()
                            .get(validateEmailIds.getFriends().get(0))
                    && userRelaShip.getTargetId() == validateEmailId.getMap()
                            .get(validateEmailIds.getFriends().get(1))) {
                log.info("friend connection already exist");
                throw new CustomGenericException("ERRO_CODE_409",
                        "data already exist for relationship :" + relationType,
                        HttpStatus.CONFLICT);
            }
        }
        friendMangementDao.insertRelationShipData(userRelationShip);
        log.info(
                "friendMangementServiceImpl::insertRelationShipData::created friend connection betwwen two friends",
                validateEmailIds.getFriends().get(0));

    }

    /**
     * retrieving friend list.
     * 
     * @param ReqRetrivedFriendList
     * @return
     * @throws CustomGenericException
     */

    @Override
    public RespFriendList getFriendList(ReqRetrivedFriendList friends)
            throws CustomGenericException {
        log.info(
                "friendMangementServiceImpl::getFriendList::retrieving friend list for {}",
                        friends.getEmail());
        return friendMangementDao.getFriendList(friends);
    }

    /**
     * retrieving common friend list between two friends.
     * 
     * @param ValidateEmailId , ReqCreateFriend
     * @return RespFriendList
     * @throws CustomGenericException
     */
    @Override
    public RespFriendList getCommonFriendList(
            ValidateEmailId validateEmailId, ReqCreateFriend friends)
            throws CustomGenericException {
        RespFriendList friendListResponse = new RespFriendList();
        List<String> frdList = new ArrayList<String>();
        ReqRetrivedFriendList firstFriend = new ReqRetrivedFriendList();
        firstFriend.setUserId(
                (validateEmailId.getMap().get(friends.getFriends().get(0))));
        ReqRetrivedFriendList secondFriend = new ReqRetrivedFriendList();
        secondFriend.setUserId(
                (validateEmailId.getMap().get(friends.getFriends().get(1))));
        RespFriendList firstfrdList =
                friendMangementDao.getFriendList(firstFriend);
        RespFriendList secondfrdList =
                friendMangementDao.getFriendList(secondFriend);

        for (String emailId : secondfrdList.getFriendList()) {
            if (firstfrdList.getMap().containsKey(emailId)) {
                frdList.add(emailId);
            }
        }
        friendListResponse.setFriendList(frdList);
        friendListResponse.setCount(frdList.size());
        friendListResponse.setSuccess(Constants.TRUE);

        return friendListResponse;
    }

    /**
     * blocking friend .
     * 
     * @param requestId , targetId
     * @return ResponseBlockfriend
     * @throws CustomGenericException
     */

    @Override
    public ResponseBlockfriend blockFriend(int requestId,
            int targetId)
            throws CustomGenericException {

        List<UserRelationShip> userRelationShipList =
                friendMangementDao.getUserRelationData(requestId, targetId);
        ResponseBlockfriend responseBlockfriend = new ResponseBlockfriend();
        int count=0;
        boolean flag = false;
        if (null != userRelationShipList && userRelationShipList.size() > 1) {
            for (UserRelationShip userRelationShip : userRelationShipList) {
                insertOrUpdateUserRelationShip(userRelationShip, count, flag);
            }
            responseBlockfriend.setSuccess(Constants.TRUE);
        } else if (null != userRelationShipList
                && userRelationShipList.size() == 1) {
            for (UserRelationShip userRelationShip : userRelationShipList) {
                insertUpdateUserRelationShip(userRelationShip);
            }

        } else {
            insertRelationShipData(requestId, targetId, Constants.BLOCK);
            insertRelationShipData(requestId, targetId, Constants.UNSUBCRIBE);

        }
        responseBlockfriend.setSuccess(Constants.TRUE);
        return responseBlockfriend;
    }
    private void insertUpdateUserRelationShip(UserRelationShip userRelationShip)
            throws CustomGenericException {
        if (Constants.FRIEND.equals(userRelationShip.getRelationType())) {
            friendMangementDao.updateUserRelationData(
                    userRelationShip.getRequestId(),
                    userRelationShip.getTargetId(),
                    Constants.BLOCK, Constants.FRIEND);
            insertRelationShipData(userRelationShip.getRequestId(),
                    userRelationShip.getTargetId(), Constants.UNSUBCRIBE);

        }
        if (Constants.SUBCRIBE.equals(userRelationShip.getRelationType())) {
            ;
            friendMangementDao.updateUserRelationData(
                    userRelationShip.getRequestId(),
                    userRelationShip.getTargetId(),
                    Constants.UNSUBCRIBE, Constants.SUBCRIBE);
            insertRelationShipData(userRelationShip.getRequestId(),
                    userRelationShip.getTargetId(), Constants.BLOCK);

        }

    }
    private void insertOrUpdateUserRelationShip(
            UserRelationShip userRelationShip, int count,
            boolean flag) throws CustomGenericException {
        if (Constants.FRIEND
                .equals(userRelationShip.getRelationType())) {
            friendMangementDao.updateUserRelationData(
                    userRelationShip.getRequestId(),
                    userRelationShip.getTargetId(), Constants.BLOCK,
                    Constants.FRIEND);
            flag = true;
        }
        else if (Constants.SUBCRIBE
                .equals(userRelationShip.getRelationType())) {

            friendMangementDao.updateUserRelationData(
                    userRelationShip.getRequestId(),
                    userRelationShip.getTargetId(),
                    Constants.UNSUBCRIBE, Constants.SUBCRIBE);
            count++;
        }
        if (count == 2 && !flag)
        {
            insertRelationShipData(userRelationShip.getRequestId(),
                    userRelationShip.getTargetId(), Constants.BLOCK);
        }

    }

    private void insertRelationShipData(int requestId, int targetId,
            String relationType) throws CustomGenericException {
        UserRelationShip userRelaShip = new UserRelationShip();
        userRelaShip.setRequestId(requestId);
        userRelaShip.setTargetId(targetId);
        userRelaShip.setRelationType(relationType);
        friendMangementDao.insertRelationShipData(userRelaShip);

    }
    @Override
    public RespFriendList getListEmailForNotification(int userId)
            throws CustomGenericException {
        return friendMangementDao.getListEmailForNotification(userId);
    }



}
