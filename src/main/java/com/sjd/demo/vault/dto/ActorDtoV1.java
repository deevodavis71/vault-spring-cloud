package com.sjd.demo.vault.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActorDtoV1 implements Serializable {

    private static final long serialVersionUID = -3228033639980865488L;

    private int actorId;

    private String actorName;

}
