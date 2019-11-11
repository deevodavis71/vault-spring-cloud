package com.sjd.demo.vault.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Actor implements Serializable {

    private static final long serialVersionUID = -1458664305153063790L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int actorId;

    private String actorName;

}
