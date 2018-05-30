package com.friends.mangement.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.friends.mangement.constants.Constants;
import com.friends.mangement.dto.ReqCreateFriend;
import com.friends.mangement.dto.ReqRetrivedFriendList;
import com.friends.mangement.dto.RespFriendList;
import com.friends.mangement.exception.CustomGenericException;
import com.friends.mangement.model.UserDetails;
import com.friends.mangement.model.UserRelationShip;
import com.friends.mangement.model.ValidateEmailId;

@Repository
public class FriendMangementDaoImpl extends BaseDao<Object>
        implements FriendMangementDao {

    private static Logger log = LogManager.getLogger();

    /**
     * Retrieving email id and validating emailIds.
     * 
     * @param ReqCreateFriend
     * @return ValidateEmailId
     * @throws CustomGenericException
     */
    @SuppressWarnings("unchecked")
    @Override
    public ValidateEmailId validateEmailIds(ReqCreateFriend validateEmailIds)
            throws CustomGenericException {

        log.info(
                "friendMangementDaoImpl::validateEmailIds::validating email ids for requestid={}",
                validateEmailIds.getFriends().get(0));
        ValidateEmailId validateEmailId = null;
        try {
            Map<String, Integer> map = new HashMap<String, Integer>();
            TypedQuery<UserDetails> query =
                    (TypedQuery<UserDetails>) getEntityManager().createQuery(
                            "SELECT ud  FROM UserDetails ud WHERE ud.emailId in :emailIds");
            query.setParameter(Constants.EMAILIDS,
                    validateEmailIds.getFriends());
            List<UserDetails> userDetailList = query.getResultList();

            validateEmailId =
                    getValidateEmailids(validateEmailIds, userDetailList);
            log.info(
                    "friendMangementDaoImpl::validateEmailIds::validated email ids for requestid={},status : {}",
                    validateEmailIds.getFriends().get(0),
                    validateEmailId.isStatus());
        }catch(Exception e){
            log.error(
                    "friendMangementDaoImpl::validateEmailIds::exception Occured for validating emailids  {}, {},exception={}",
                    validateEmailIds.getFriends().get(0), e);
            throw new CustomGenericException(Constants.ERROR_CODE_500,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return validateEmailId;
    }

    private ValidateEmailId getValidateEmailids(ReqCreateFriend validateEmailIds,
            List<UserDetails> userDetailList) {
        log.info(
                "friendMangementDaoImpl::getValidateEmailids:: retriving validated email data for {}, {}",
                validateEmailIds.getFriends().get(0));
        Map<String, Integer> map = new HashMap<String, Integer>();
        ValidateEmailId validateEmailId = new ValidateEmailId();
        StringBuilder emailString = new StringBuilder();

        if (null != userDetailList) {
            for (UserDetails userDetails : userDetailList) {
                map.put(userDetails.getEmailId(), userDetails.getUserId());
            }
            validateEmailId.setMap(map);
            if (userDetailList.size() == 2) {
                validateEmailId.setStatus(true);
                return validateEmailId;
            }
            
            validateEmailId.setStatus(false);
        }


        if (!map.containsKey(validateEmailIds.getFriends().get(0))) {
            emailString.append(validateEmailIds.getFriends().get(0) + " ");

        }
        if (validateEmailIds.getFriends().size() > 1
                && !map.containsKey(validateEmailIds.getFriends().get(1))) {
            emailString.append(validateEmailIds.getFriends().get(1));;
        } else if (validateEmailIds.getFriends().size() < 2) {
            validateEmailId.setStatus(true);
        }
        validateEmailId.setEmailId(emailString.toString());
        log.info(
                "friendMangementDaoImpl::getValidateEmailids:: retrived validated email data for {}, {}",
                validateEmailIds.getFriends().get(0));
        return validateEmailId;
    }

    /**
     * creating friend relation between two friends
     * 
     * @param UserRelationShip
     * @return
     * @throws CustomGenericException
     */
    @Override
    public void insertRelationShipData(UserRelationShip userRelationShip)
            throws CustomGenericException {
        log.info(
                "friendMangementDaoImpl::insertRelationShipData::creating relation for email ids request={}, target={}",
                userRelationShip.getRequestId(),
                userRelationShip.getTargetId());
        try {
            getEntityManager().persist(userRelationShip);
            log.info(
                    "friendMangementDaoImpl::insertRelationShipData::created relation for request={}, target={}",
                    userRelationShip.getRequestId(),
                    userRelationShip.getTargetId());

        } catch (Exception e) {
            log.error(
                    "friendMangementDaoImpl::insertRelationShipData::exception occured for request={}, target={}, exception={}",
                    userRelationShip.getRequestId(),
                    userRelationShip.getTargetId(), e);
            throw new CustomGenericException(Constants.ERROR_CODE_500,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Retrieving friend relation between lists
     * 
     * @param requestId,targetId,relationType,flag
     * @return List<UserRelationShip>
     * @throws CustomGenericException
     */

    @SuppressWarnings("unchecked")
    @Override
    public List<UserRelationShip> getExistRelationShip(int requestId,
            int targetId, String relationType, boolean flag) throws Exception {
        log.info(
                "friendMangementDaoImpl::getExistRelationShip::retriving exist relationship for request={}, target={}, relationType={}",
                requestId, targetId, relationType);
        List<UserRelationShip> resultList = new ArrayList<UserRelationShip>();
        try {
            StringBuilder qry = new StringBuilder();

            qry.append(
                    "SELECT urs from UserRelationShip urs  WHERE ");
            if (flag) {
                qry.append(
                        "(( urs.requestId =:requestId and urs.targetId=:targetId)");

                qry.append(" OR ");
                qry.append(
                        "( urs.requestId =:targetId and urs.targetId=:requestId))");
            } else {
                qry.append(
                        " urs.requestId =:requestId and urs.targetId=:targetId ");
            }

            qry.append(
                    "and (urs.relationType=:relationType  or urs.relationType=:blockRelationType )");
            Query query = getEntityManager().createQuery(qry.toString());
            query.setParameter(Constants.REQUEST_ID, requestId);
            query.setParameter(Constants.TARGET_ID, targetId);
            query.setParameter(Constants.RELATION_TYPE, relationType);
            query.setParameter(Constants.BLOCK_RELATION_TYPE, Constants.BLOCK);
            resultList = query.getResultList();

            log.info(
                    "friendMangementDaoImpl::getExistRelationShip::retrived exist relationship for request={}, target={}, relationType={}",
                    requestId, targetId, relationType);
            return resultList;

        } catch (Exception e) {
            log.error(
                    "friendMangementDaoImpl::getExistRelationShip::exception Occured request={}, target={}, relationType={} exception={}",
                    requestId, targetId, relationType, e);
            throw new CustomGenericException(Constants.ERROR_CODE_500,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    /**
     * Retrieving friend relation between lists
     * 
     * @param ReqRetrivedFriendList
     * @return RespFriendList
     * @throws CustomGenericException
     */

    @Override
    public RespFriendList getFriendList(ReqRetrivedFriendList friends)
            throws CustomGenericException {
        log.info(
                "friendMangementDaoImpl::getFriendList::retriving friend list for request={}",
                friends.getUserId());
        RespFriendList friendListResponse = new RespFriendList();
        try {
            //StringBuilder qry = new StringBuilder();
            String qry = "SELECT ud FROM UserDetails ud where ud.userId in ( "
                    + "(SELECT urs.targetId FROM UserRelationShip urs WHERE urs.requestId =:userId) ) "
                    + " OR ud.userId in"
                    + "(Select urs.requestId FROM UserRelationShip urs WHERE urs.targetId =:userId)";

            Query query = getEntityManager().createQuery(qry);

            query.setParameter(Constants.USERID, friends.getUserId());


            @SuppressWarnings("unchecked")
            List<UserDetails> resultList = query.getResultList();
            List<String> friendList = new ArrayList<String>();
            if (null != resultList) {
                friendListResponse.setCount(resultList.size());
                Map<String, Integer> map = new HashMap<String, Integer>();
                for (UserDetails userDetails : resultList) {
                    friendList.add(userDetails.getEmailId());
                    map.put(userDetails.getEmailId(), userDetails.getUserId());
                }
                friendListResponse.setFriendList(friendList);
                friendListResponse.setMap(map);
            } else {
                friendListResponse.setCount(0);

            }
            friendListResponse.setSuccess(Constants.TRUE);
            log.info(
                    "friendMangementDaoImpl::getFriendList::retrived friend list for request={}",
                    friends.getUserId());
        } catch (Exception e) {
            log.error(
                    "friendMangementDaoImpl::getFriendList::exception Occured request={}",
                    friends.getUserId(), e);
            throw new CustomGenericException(Constants.ERROR_CODE_500,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return friendListResponse;
    }

    /**
     * Retrieving friend relation between lists
     * 
     * @param requestId,targetId
     * @return RespFriendList
     * @throws CustomGenericException
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<UserRelationShip> getUserRelationData(int requestId,
            int targetId) throws CustomGenericException {
        log.info(
                "friendMangementDaoImpl::getExistRelationShip::getting user relationship data list for request={}, target={}",
                requestId, targetId);
        List<UserRelationShip> userDetailList = null;
        try
        {

            TypedQuery<UserRelationShip> query =
                    (TypedQuery<UserRelationShip>) getEntityManager().createQuery(
                                    "SELECT usr  FROM UserRelationShip usr WHERE (usr.requestId =:requestId and usr.targetId=:targetId) or (usr.requestId =:targetId and usr.targetId=:requestId)");
            query.setParameter(Constants.REQUEST_ID, requestId);
            query.setParameter(Constants.TARGET_ID, targetId);
            userDetailList = query.getResultList();
            log.info(
                    "friendMangementDaoImpl::getExistRelationShip::get user relationship data list for request={}, target={}",
                    requestId, targetId);
        } catch (NoResultException e) {
            log.info(
                    "friendMangementDaoImpl::getExistRelationShip::No result exception for request={}, target={}",
                    requestId, targetId);
            return null;
        } catch (Exception e) {
            log.error(
                    "friendMangementDaoImpl::getUserRelationData::exception occured for request={},target={},exception={}",
                    requestId, targetId, e);
            throw new CustomGenericException(Constants.ERROR_CODE_500,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return userDetailList;
    }

    /**
     * update UserRelationData
     * 
     * @param requestId,targetId
     * @return
     * @throws CustomGenericException
     */
    @SuppressWarnings("unchecked")
    @Override
    public void updateUserRelationData(int requestId, int targetId,
            String newRelationShip, String oldRelationShip)
            throws CustomGenericException {
        log.info(
                "friendMangementDaoImpl::getExistRelationShip::updating exist relationship for request={}, target={}, relationType={}",
                requestId, targetId, newRelationShip, oldRelationShip);
        try {

            TypedQuery<UserRelationShip> query =
                    (TypedQuery<UserRelationShip>) getEntityManager()
                            .createQuery(
                                    "UPDATE UserRelationShip SET relationType=:newRelationShip WHERE requestId=:requestId and targetId=:targetId and relationType=:oldRelationShip");
            query.setParameter(Constants.REQUEST_ID, requestId);
            query.setParameter(Constants.TARGET_ID, targetId);
            query.setParameter(Constants.NEW_RELATIONSHIP, newRelationShip);
            query.setParameter(Constants.OLD_RELATIONSHIP, oldRelationShip);
            query.executeUpdate();
            log.info(
                    "friendMangementDaoImpl::getExistRelationShip::updated exist relationship for request={}, target={}, relationType={}",
                    requestId, targetId, newRelationShip, oldRelationShip);
        } catch (Exception e) {
            log.error(
                    "friendMangementDaoImpl::updateUserRelationData::exception Occured request={}, target={}, exception={}",
                    requestId, targetId, e);
            throw new CustomGenericException(Constants.ERROR_CODE_500,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Retrieving 'from subscribed' email id list
     * 
     * @param userid
     * @return RespFriendList
     * @throws CustomGenericException
     */

    @SuppressWarnings("unchecked")
    @Override
    public RespFriendList getListEmailForNotification(int userId)
            throws CustomGenericException {
        log.info(
                "friendMangementDaoImpl::getListEmailForNotification::retrieving email ids getting notification to userid={}",
                userId);
        RespFriendList friendListResponse = new RespFriendList();
        try {
            String qry = "SELECT ud FROM UserDetails ud where ud.userId in ( "
                    + "(SELECT urs.targetId FROM UserRelationShip urs WHERE (urs.relationType=:subcribeRelation or urs.relationType=:friendRelation"
                    + " ) and urs.requestId =:userId) ) " + " OR ud.userId in"
                    + "(Select urs.requestId FROM UserRelationShip urs WHERE (urs.relationType=:subcribeRelation or urs.relationType=:friendRelation"
                    + " ) and  urs.targetId =:userId)";

            Query query = getEntityManager().createQuery(qry);

            query.setParameter(Constants.USERID, userId);
            query.setParameter(Constants.SUBCRIBE_RELATION_TYPE,
                    Constants.SUBCRIBE);
            query.setParameter(Constants.FRIEND_RELATION_TYPE,
                    Constants.FRIEND);

            List<UserDetails> resultList = query.getResultList();
            log.info(
                    "friendMangementDaoImpl::getListEmailForNotification::retrieved email ids getting notification to userid={}",
                    userId);
            List<String> friendList = new ArrayList<String>();
            if (null != resultList) {
                friendListResponse.setCount(resultList.size());
                Map<String, Integer> map = new HashMap<String, Integer>();
                for (UserDetails userDetails : resultList) {
                    friendList.add(userDetails.getEmailId());
                    map.put(userDetails.getEmailId(), userDetails.getUserId());
                }
                friendListResponse.setFriendList(friendList);
                friendListResponse.setMap(map);
            } else {
                friendListResponse.setCount(0);

            }
            friendListResponse.setSuccess(Constants.TRUE);
        } catch (Exception e) {
            log.error(
                    "friendMangementDaoImpl::getListEmailForNotification::exception Occured userid={}, exception={}",
                    userId, e);
            throw new CustomGenericException(Constants.ERROR_CODE_500,
                    e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return friendListResponse;

    }


}
