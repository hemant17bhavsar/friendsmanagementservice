package com.friends.mangement.model;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;



@Entity
@Table(name = "userRelationShip")
@XmlRootElement
public class UserRelationShip implements Serializable {


    private static final long serialVersionUID = 2535090460811888936L;

    /**
     * An id which is unique for each customer in the db
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "relationshipid", nullable = false, length = 10)
    private Integer relationshipId;


    @Column(name = "requestid", nullable = false)
    private int requestId;

    @Column(name = "targetid", nullable = false)
    private int targetId;

    @Column(name = "relationtype", nullable = false, length = 10)
    private String relationType;

    /**
     * @return the relationshipId
     */
    public Integer getRelationshipId() {
        return relationshipId;
    }

    /**
     * @param relationshipId the relationshipId to set
     */
    public void setRelationshipId(Integer relationshipId) {
        this.relationshipId = relationshipId;
    }



    /**
     * @return the requestId
     */
    public int getRequestId() {
        return requestId;
    }

    /**
     * @param requestId the requestId to set
     */
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    /**
     * @return the targetId
     */
    public int getTargetId() {
        return targetId;
    }

    /**
     * @param targetId the targetId to set
     */
    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    /**
     * @return the relationType
     */
    public String getRelationType() {
        return relationType;
    }

    /**
     * @param relationType the relationType to set
     */
    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }



}
