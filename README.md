
### Friend Management Application : 
This is an application with a need to build its own social network, "Friends Management" is a common requirement which usually starts off simple but can grow in complexity depending on the application's use case. 

Usually, applications would start with features like "Friend", "Unfriend", "Block", "Receive Updates" etc. 

### Technologies : 
               Java, Spring boot, rest web services, JPA

### Database : 
          MySQL

Created two tables as below : userDetail,userRelationShip. 

create table userDetail(
   userid INT NOT NULL AUTO_INCREMENT,
   emailid VARCHAR(100) NOT NULL,
   PRIMARY KEY ( userid )
);

CREATE TABLE userRelationShip (
    relationshipid int NOT NULL,
    requestid int NOT NULL,
    targetid int NOT NULL,
    relationtype VARCHAR(20) NOT NULL,
    PRIMARY KEY (relationshipid),
    FOREIGN KEY (requestid) REFERENCES userDetail(userid),
	`FOREIGN KEY (targetid) REFERENCES userDetail(userid)
);

As per requirement shared, assuming user data already created.

***

### List of Rest End Points :

1. …./friendmanagmentservice/createFriendConnection

Request:

{
	"friends": [
		"amol@example.com",
		"hemant@example.com"
	]
     }	

Positive Response: 


{
    "responseData": {
        "RespSuccess": {
            "success": "true"
        }
    },
    "responseError": null
}

Negative Response:

{
    "responseData": null,
    "responseError": {
        "code": "ERRO_CODE_409",
        "failureMessage": "data already exist for relationship :FRIEND"
    }
}

***

2)	.. ./friendmanagmentservice/retrivedFriendList

Request :

{
 "email": "amol@example.com"
}


Positive Response :

{
    "responseData": {
        "RespFriendList": {
            "success": "true",
            "friendList": [
                "hemant@example.com",
                "ketan@example.com",
                "sally@example.com"
            ],
            "count": 3
        }
    },
    "responseError": null
}

Negative Response:

{
    "responseData": null,
    "responseError": {
        "code": "ERRO_CODE_404",
        "failureMessage": "data not found for given request "
    }
}

***

3)	.. ./friendmanagmentservice/retrivedCommonFriendList

Request :

{
	"friends": [
		"amol@example.com",
		"hemant@example.com"
	]
}


Positive Response :

{
    "responseData": {
        "RespFriendList": {
            "success": "true",
            "friendList": [
                "ketan@example.com",
                "sally@example.com"
            ],
            "count": 2
        }
    },
    "responseError": null
}

Negative Response:

{
    "responseData": null,
    "responseError": {
        "code": "ERRO_CODE_404",
        "failureMessage": "data not found for given request "
    }
}

***

4)	…./friendmanagmentservice/subcribeFriend

Request:
{  	"requestor": "hemant@example.com", 
	"target": "amol@example.com" 
}

Positive Response :

{
    "responseData": {
        "RespSuccess": {
            "success": "true"
        }
    },
    "responseError": null
}

Negative Response:

{
    "responseData": null,
    "responseError": {
        "code": "ERRO_CODE_401",
        "failureMessage": "relationship is block"
    }
}


***

5)	.. ./friendmanagmentservice/blockingFriend

Request :
{ 	"requestor": "hemant@example.com", 
	"target": "sally@example.com" 
}


Positive Response:

{
    "responseData": {
        "ResponseBlockfriend": {
            "success": "true"
        }
    },
    "responseError": null
}

Negative Response:

{
    "responseData": null,
    "responseError": {
        "code": "ERRO_CODE_404",
        "failureMessage": "data not found for given request "
    }
}


***


6).. ./friendmanagmentservice/getListEmailForNotification

Request : 
{  	"sender": amol@example.com
}

Positive Response::

{
    "responseData": {
        "RespFriendList": {
            "success": "true",
            "friendList": [
                "hemant@example.com",
                "ketan@example.com",
                "sally@example.com"
            ],
            "count": 3
        }
    },
    "responseError": null
}

Negative Response:

{
    "responseData": null,
    "responseError": {
        "code": "ERRO_CODE_404",
        "failureMessage": "data not found for given request "
    }
}

***
### Deployment to PCF 

The Application is deployed on PCF. It can be accessed by below url

[https://friendsmanagementservice-demo.cfapps.io/](https://friendsmanagementservice-demo.cfapps.io/)

e.g if you access rest end point ../friendmanagmentservice/retrivedFriendList  URL should be as below with request (suggest in above).

[https://friendsmanagementservice-demo.cfapps.io/friendmanagmentservice/retrivedFriendList](https://friendsmanagementservice-demo.cfapps.io/friendmanagmentservice/retrivedFriendList)
