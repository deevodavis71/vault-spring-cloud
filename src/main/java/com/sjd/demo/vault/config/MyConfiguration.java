package com.sjd.demo.vault.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties("example")
@Data
public class MyConfiguration {

    private String username;

    private String password;

}
