package com.sjd.demo.vault.dto.refresh;

import lombok.Data;

@Data
public class DatabasePropertiesDto {

    private static final int MIN_IDLE = 3;

    private static final int MAX_POOL_SIZE = 10;

    private String driverClassName;
    private String url;
    private String username;
    private String password;

    private String hibernateDialect;
    private String hibernateDdl;

    private int minIdle = MIN_IDLE;
    private int maxPoolSize = MAX_POOL_SIZE;

    @Override
    public String toString() {

        return "DatabaseProperties{" + "url='" + url + '\'' + ", username='" + username + '\'' + ", password='"
                + password + '\'' + '}';

    }
}
