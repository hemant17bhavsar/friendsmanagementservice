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
@Table(name = "relationShipType")
@XmlRootElement
public class RelationShipType implements Serializable {


    private static final long serialVersionUID = 2535090460811888936L;

    /**
     * An id which is unique for each customer in the db
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "typeid", nullable = false, length = 10)
    private Integer typeId;

    @Column(name = "type", length = 20, nullable = false)
    private String type;

    /**
     * @return the typeId
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * @param typeId the typeId to set
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }



}
