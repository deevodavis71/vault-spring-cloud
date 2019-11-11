package com.sjd.demo.vault.dto;

import lombok.Data;

@Data
public class CredentialsBeanDto {

    private boolean lastSuccessful = false;

    private String presentlyWorkingUserName = null;

    private String presentlyWorkingPassword = null;

}
