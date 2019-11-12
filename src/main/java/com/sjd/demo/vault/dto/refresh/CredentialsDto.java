package com.sjd.demo.vault.dto.refresh;

import lombok.Data;

@Data
public class CredentialsDto {

    private boolean initialised;

    private boolean lastSuccessful;

    private String workingUserName;

    private String workingPassword;

}
