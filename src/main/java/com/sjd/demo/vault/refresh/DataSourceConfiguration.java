package com.sjd.demo.vault.refresh;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.sjd.demo.vault.dto.CredentialsDto;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@EnableTransactionManagement
@Component
public class DataSourceConfiguration {

    private final CredentialsDto credentialsDto;

    private final VaultDbRefreshService vaultDbRefreshService;

    private String databaseUrl = "jdbc:mysql://localhost:3306/db1";

    @Primary
    @RefreshScope
    @Bean(name = "datasource")
    public HikariDataSource dataSource() {

        log.debug("Called datasource...");

        // call Vault if the credentials have not already been refreshed
        if (!credentialsDto.isLastSuccessful()) {
            vaultDbRefreshService.getAndStoreCredentials();
        }

        HikariConfig jdbcConfig = new HikariConfig();

        jdbcConfig.setUsername(credentialsDto.getPresentlyWorkingUserName());
        jdbcConfig.setPassword(credentialsDto.getPresentlyWorkingPassword());
        jdbcConfig.setJdbcUrl(databaseUrl); // database URL from application properties

        log.debug("jdbcConfig : {}, {}, {}", jdbcConfig.getUsername(), jdbcConfig.getPassword(),
                jdbcConfig.getJdbcUrl());

        jdbcConfig.setMaximumPoolSize(10);
        jdbcConfig.setMinimumIdle(2);

        return new HikariDataSource(jdbcConfig);

    }

}
