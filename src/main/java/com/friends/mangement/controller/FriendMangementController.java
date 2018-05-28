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
import com.friends.mangement.dto.ReqSubcribeFriend;
import com.friends.mangement.dto.RespSuccess;
import com.friends.mangement.dto.ResponseBlockfriend;
import com.friends.mangement.exception.CustomGenericException;
import com.friends.mangement.model.RespData;
import com.friends.mangement.model.ResponseError;
import com.friends.mangement.model.ValidateEmailId;
import com.friends.mangement.service.FriendMangementService;

@RestController
@RequestMapping("friendmanagmentservice")
public class FriendMangementController {

    private static Logger log = LogManager.getLogger();


    @Autowired
    FriendMangementService friendMangementService;
    
    /**
     * API to create a friend connection between two email addresses.
     * 
     * @param ReqCreateFriend
     * @return sucessResponse
     */

    @RequestMapping(path = "/createFriendConnection",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<RespData> createFriendConnection(
            @RequestBody ReqCreateFriend friends) {
        log.info(
                "friendMangementController :: createFriendConnection :: creating friend connection ");
        ResponseEntity<RespData> responseEntity = null;
        RespData respData = new RespData();
        try {
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
                    "friendMangementController :: createFriendConnection :: creating friend connection for betweeen {} and {}",
                    friends.getFriends().get(0), friends.getFriends().get(1));
            ValidateEmailId validateEmailId =
                    friendMangementService.validateEmailIds(friends);
            
            if(validateEmailId.isStatus())
            {
                friendMangementService.createFriendConnection(validateEmailId,
                        friends);
                log.info(
                        "friendMangementController :: createFriendConnection :: created friend connection for betweeen {} and {}",
                        friends.getFriends().get(0),
                        friends.getFriends().get(1));

            } else {
                throw new CustomGenericException(Constants.ERRO_CODE_404,
                        Constants.ERRO_CODE_404_MESSAGE,
                        HttpStatus.NOT_FOUND);
            }
            RespSuccess sucessResponse = new RespSuccess();
            sucessResponse.setSuccess(Constants.TRUE);
            respData.setResponseData(sucessResponse);
            responseEntity = new ResponseEntity<>(respData, HttpStatus.OK);
        } catch (CustomGenericException ex) {
            log.error(
                    "friendMangementController :: createFriendConnection :: custom generic exception occured for {},{}",
                    friends.getFriends().get(0), ex.getErrMsg());
            respData = new RespData();
            setError(respData, ex);
            responseEntity =
                    new ResponseEntity<>(respData, ex.getHttpErrorCode());
        } catch (Exception e) {
            log.error(
                    "friendMangementController :: createFriendConnection ::  exception occured for {},{}",
                    friends.getFriends().get(0));
            respData.setResponseData(Constants.FAIL);
            responseEntity = new ResponseEntity<>(respData,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;

    }

    /**
     * API to retrieve the friends list for an email address.
     * 
     * @param ReqSubcribeFriend
     * @return sucessResponse
     */


    @RequestMapping(path = "/subcribeFriend", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<RespData> subcribeFriend(
            @RequestBody ReqSubcribeFriend reqSubcribeFriend) {
        log.info(
                "friendMangementController :: subcribeFriend :: subscribing friends for updates requestor={},target={} ",
                reqSubcribeFriend.getRequestor(),
                reqSubcribeFriend.getTarget());
        ResponseEntity<RespData> responseEntity = null;
        RespData respData = new RespData();
        try {
            ReqCreateFriend friends = getRequest(reqSubcribeFriend);
            ValidateEmailId validateEmailId =
                    friendMangementService.validateEmailIds(friends);

            if (validateEmailId.isStatus()) {
                friendMangementService.subcribeFriend(validateEmailId,
                        friends);
                log.info(
                        "friendMangementController :: subcribeFriend :: subscribed friends for updates requestor={},target={} ",
                        reqSubcribeFriend.getRequestor(),
                        reqSubcribeFriend.getTarget());

            } else {
                throw new CustomGenericException(Constants.ERRO_CODE_404,
                        Constants.ERRO_CODE_404_MESSAGE,
                        HttpStatus.NOT_FOUND);
            }
            RespSuccess sucessResponse = new RespSuccess();
            sucessResponse.setSuccess(Constants.TRUE);
            respData.setResponseData(sucessResponse);
            responseEntity = new ResponseEntity<>(respData, HttpStatus.OK);
        } catch (CustomGenericException ex) {
            log.error(
                    "friendMangementController :: subcribeFriend :: custom generic exception occured for {},{}",
                    reqSubcribeFriend.getRequestor(), ex.getErrMsg());
            respData = new RespData();
            setError(respData, ex);
            responseEntity =
                    new ResponseEntity<>(respData, ex.getHttpErrorCode());
        } catch (Exception e) {
            respData.setResponseData(Constants.FAIL);
            responseEntity = new ResponseEntity<>(respData,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;

    }

    /**
     * API to block updates from an email address.
     * 
     * @param ReqSubcribeFriend
     * @return responseBlockfriend
     */
    @RequestMapping(path = "/blockingFriend", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<RespData> blockingFriend(
            @RequestBody ReqSubcribeFriend reqSubcribeFriend) {
        log.info(
                "friendMangementController :: blockingFriend :: blocking friends  requestor={},target={} ",
                reqSubcribeFriend.getRequestor(),
                reqSubcribeFriend.getTarget());
        ResponseEntity<RespData> responseEntity = null;
        RespData respData = new RespData();
        ResponseBlockfriend responseBlockfriend = new ResponseBlockfriend();
        ValidateEmailId validateEmailId;
        try {
            validateEmailId =
                    friendMangementService.validateEmailIds(getRequest(reqSubcribeFriend));

            if (null != validateEmailId && validateEmailId.isStatus()) {
                responseBlockfriend =
                        friendMangementService
                                .blockFriend(
                        validateEmailId.getMap()
                                .get(reqSubcribeFriend.getRequestor()),
                        validateEmailId.getMap()
                                .get(reqSubcribeFriend.getTarget()));

            } else {
                throw new CustomGenericException(Constants.ERRO_CODE_404,
                        Constants.ERRO_CODE_404_MESSAGE,
                        HttpStatus.NOT_FOUND);
            }
            log.info(
                    "friendMangementController :: blockingFriend :: blocked friends  requestor={},target={} ",
                    reqSubcribeFriend.getRequestor(),
                    reqSubcribeFriend.getTarget());
            respData.setResponseData(responseBlockfriend);
            responseEntity = new ResponseEntity<>(respData, HttpStatus.OK);
        } catch (CustomGenericException ex) {
            log.error(
                    "friendMangementController :: blockingFriend :: custom generic exception occured for {},{}",
                    reqSubcribeFriend.getRequestor(),
                    reqSubcribeFriend.getTarget(), ex.getErrMsg());
            respData = new RespData();
            setError(respData, ex);
            responseEntity =
                    new ResponseEntity<>(respData, ex.getHttpErrorCode());
        } catch (Exception e) {
            respData.setResponseData(Constants.FAIL);
            responseEntity = new ResponseEntity<>(respData,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;

    }



    private ReqCreateFriend getRequest(ReqSubcribeFriend reqSubcribeFriend) {
        ReqCreateFriend friends = new ReqCreateFriend();
        List<String> emailList = new ArrayList<>();
        emailList.add(reqSubcribeFriend.getRequestor());
        emailList.add(reqSubcribeFriend.getTarget());
        friends.setFriends(emailList);
        return friends;
    }



    private void setError(RespData respData, CustomGenericException e) {

        ResponseError responseError = new ResponseError();

        responseError.setCode(e.getErrCode());
        responseError.setFailureMessage(e.getErrMsg());
        respData.setResponseError(responseError);

    }


}
