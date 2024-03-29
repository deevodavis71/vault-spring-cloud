package com.sjd.demo.vault.business;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.sjd.demo.vault.config.TestConfiguration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RefreshScope
@Slf4j
@Service
@RequiredArgsConstructor
public class VaultConfigService {

    private final TestConfiguration testConfiguration;

    public void dump() {

        log.info("----------------------------------------");
        log.info("Configuration properties");
        log.info("        example.username is {}", testConfiguration.getUsername());
        log.info("        example.password is {}", testConfiguration.getPassword());
        log.info("----------------------------------------");

    }

}
