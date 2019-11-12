package com.sjd.demo.vault.dto.refresh;

import lombok.Data;

@Data
public class CredentialsDto {

    private boolean lastSuccessful = false;

    private String presentlyWorkingUserName = null;

    private String presentlyWorkingPassword = null;

}
