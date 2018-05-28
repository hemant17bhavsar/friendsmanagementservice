package com.friends.mangement.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.friends.mangement.constants.Constants;
import com.friends.mangement.dto.ReqCreateFriend;
import com.friends.mangement.dto.ReqRetrivedFriendList;
import com.friends.mangement.dto.RespFriendList;
import com.friends.mangement.exception.CustomGenericException;
import com.friends.mangement.model.RequestReceivingUpdatesEmails;
import com.friends.mangement.model.RespData;
import com.friends.mangement.model.ResponseError;
import com.friends.mangement.model.ValidateEmailId;
import com.friends.mangement.service.FriendMangementService;

@RestController
@RequestMapping("friendmanagmentservice")
public class ViewMangementController {
    private static Logger log = LogManager.getLogger();

    @Autowired
    FriendMangementService friendMangementService;

    /**
     * API to retrieve the friends list for an email address.
     * 
     * @param ReqRetrivedFriendList
     * @return respFriendList: list of friends emailid.
     */
    @RequestMapping(path = "/retrivedFriendList", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<RespData> retrivedFriendList(
            @RequestBody ReqRetrivedFriendList friends) {
        log.info(
                "viewFriendManagment :: retrivedFriendList :: retrieving friends list for  requestor={} ",
                friends.getEmail());
        ResponseEntity<RespData> responseEntity = null;
        RespData respData = new RespData();
        try {
            RespFriendList sucessResponse = new RespFriendList();
            List<String> friendList = new ArrayList<>();
            friendList.add(friends.getEmail());
            ReqCreateFriend createFriend = new ReqCreateFriend();
            createFriend.setFriends(friendList);
            ValidateEmailId validateEmailId =
                    friendMangementService.validateEmailIds(createFriend);
            if (null != validateEmailId && validateEmailId.isStatus()
                    && !validateEmailId.getMap().isEmpty()) {
                friends.setUserId(
                        validateEmailId.getMap().get(friendList.get(0)));
                sucessResponse =
                        friendMangementService.getFriendList(friends);
                log.info(
                        "viewFriendManagment :: retrivedFriendList :: retrived friends list for  requestor={} ",
                        friends.getEmail());
            } else {
                throw new CustomGenericException(Constants.ERRO_CODE_404,
                        Constants.ERRO_CODE_404_MESSAGE,
                        HttpStatus.NOT_FOUND);
            }

            respData.setResponseData(sucessResponse);
            responseEntity = new ResponseEntity<>(respData, HttpStatus.OK);
        } catch (CustomGenericException ex) {
            log.error(
                    "viewFriendManagment :: retrivedFriendList :: custom generic exception occured for requestor={},{},exception={}",
                    friends.getEmail(), ex.getErrMsg());
            respData = new RespData();
            setError(respData, ex);
            responseEntity =
                    new ResponseEntity<>(respData, ex.getHttpErrorCode());
        } catch (Exception e) {
            log.error(
                    "viewFriendManagment :: retrivedFriendList ::  exception occured for requestor={},{},exception={}",
                    friends.getEmail(), e);
            respData.setResponseData(Constants.FAIL);
            responseEntity = new ResponseEntity<>(respData,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;

    }

    /**
     * API to retrieving common friend list between two friends.
     * 
     * @param ReqCreateFriend
     * @return respFriendList : list of common friend list
     */
    @RequestMapping(path = "/retrivedCommonFriendList",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<RespData> retrivedCommonFriendsList(
            @RequestBody ReqCreateFriend friends) {
        log.info(
                "viewFriendManagment :: retrivedCommonFriendsList :: retrieving common friends list  ");
        ResponseEntity<RespData> responseEntity = null;
        RespData respData = new RespData();
        try {
            RespFriendList sucessResponse = new RespFriendList();
            if (friends.getFriends() == null) {
                throw new CustomGenericException(Constants.ERRO_CODE_400,
                        Constants.ERRO_CODE_400_MESSAGE,
                        HttpStatus.BAD_REQUEST);
            }
            if (friends.getFriends().size() != 2) {
                throw new CustomGenericException(Constants.ERRO_CODE_402,
                        Constants.ERRO_CODE_402_MESSAGE,
                        HttpStatus.BAD_REQUEST);
            }
            log.info(
                    "viewFriendManagment :: retrivedCommonFriendsList :: retrieving common friends list betweeen {} and {}",
                    friends.getFriends().get(0), friends.getFriends().get(1));
            ValidateEmailId validateEmailId =
                    friendMangementService.validateEmailIds(friends);

            if (null != validateEmailId && validateEmailId.isStatus()) {

                sucessResponse =
                        friendMangementService
                                .getCommonFriendList(validateEmailId, friends);
                log.info(
                        "viewFriendManagment :: retrivedCommonFriendsList :: retrived common friends list betweeen {} and {}",
                        friends.getFriends().get(0),
                        friends.getFriends().get(1));
            } else {
                throw new CustomGenericException(Constants.ERRO_CODE_404,
                        Constants.ERRO_CODE_404_MESSAGE,
                        HttpStatus.NOT_FOUND);
            }

            respData.setResponseData(sucessResponse);
            responseEntity = new ResponseEntity<>(respData, HttpStatus.OK);
        } catch (CustomGenericException ex) {
            log.error(
                    "viewFriendManagment :: retrivedCommonFriendsList :: custom generic exception occured for {},{},exception={}",
                    friends.getFriends().get(0), ex.getErrCode());
            respData = new RespData();
            setError(respData, ex);
            responseEntity =
                    new ResponseEntity<>(respData, ex.getHttpErrorCode());
        } catch (Exception e) {
            log.error(
                    "viewFriendManagment :: retrivedCommonFriendsList :: exception occured for {},{},exception={}",
                    friends.getFriends().get(0), friends.getFriends().get(1),
                    e);
            respData.setResponseData(Constants.FAIL);
            responseEntity = new ResponseEntity<>(respData,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;

    }

    /**
     * API to retrieve all email addresses that can receive updates from an
     * email address.
     * 
     * @param RequestReceivingUpdatesEmails
     * @return RespFriendList
     */

    @RequestMapping(path = "/getListEmailForNotification",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<RespData> getListEmailForNotification(
            @RequestBody RequestReceivingUpdatesEmails friends) {
        log.info(
                "viewFriendManagment :: getListEmailForNotification :: retrieving list of email for notificaitons ,sender={} ",
                friends.getSender());

        ResponseEntity<RespData> responseEntity = null;
        RespData respData = new RespData();
        try {
            RespFriendList sucessResponse = new RespFriendList();
            List<String> friendList = new ArrayList<>();
            friendList.add(friends.getSender());
            ReqCreateFriend createFriend = new ReqCreateFriend();
            createFriend.setFriends(friendList);
            ValidateEmailId validateEmailId =
                    friendMangementService.validateEmailIds(createFriend);
            if (null != validateEmailId && validateEmailId.isStatus()) {
                sucessResponse = friendMangementService
                        .getListEmailForNotification(validateEmailId.getMap()
                                .get(friendList.get(0)));
            } else {
                throw new CustomGenericException(Constants.ERRO_CODE_404,
                        Constants.ERRO_CODE_404_MESSAGE,
                        HttpStatus.NOT_FOUND);
            }
            log.info(
                    "viewFriendManagment :: getListEmailForNotification :: retrived list of email for notificaitons ,sender={} ",
                    friends.getSender());
            respData.setResponseData(sucessResponse);
            responseEntity = new ResponseEntity<>(respData, HttpStatus.OK);
        } catch (CustomGenericException ex) {
            log.error(
                    "viewFriendManagment :: getListEmailForNotification :: custom generic exception occured for sender={},exception={}",
                    friends.getSender(), ex.getErrMsg());
            respData = new RespData();
            setError(respData, ex);
            responseEntity =
                    new ResponseEntity<>(respData, ex.getHttpErrorCode());
        } catch (Exception e) {
            log.error(
                    "viewFriendManagment :: getListEmailForNotification :: exception occured for sender={},exception={}",
                    friends.getSender(), e);
            respData.setResponseData(Constants.FAIL);
            responseEntity = new ResponseEntity<>(respData,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;

    }

    private void setError(RespData respData, CustomGenericException e) {

        ResponseError responseError = new ResponseError();

        responseError.setCode(e.getErrCode());
        responseError.setFailureMessage(e.getErrMsg());
        respData.setResponseError(responseError);

    }

}
