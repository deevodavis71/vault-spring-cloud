package com.sjd.demo.vault.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.sjd.demo.vault.dto.refresh.CredentialsDto;
import com.sjd.demo.vault.dto.refresh.DatabasePropertiesDto;
import com.sjd.demo.vault.refresh.VaultDbRefreshService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class VaultPersistenceConfig {

    private static final String PROJECTION_NAME = "vault";

    private final CredentialsDto credentialsDto;

    private final VaultDbRefreshService vaultDbRefreshService;

    private final DataSource dataSource;

    private String databaseUrl = "jdbc:mysql://localhost:3306/db1";

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        //        factoryBean.setDataSource(dataSource());
        factoryBean.setDataSource(dataSource);
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setJpaProperties(hibernateProperties());
        factoryBean.setPackagesToScan("com.sjd.demo");
        factoryBean.setPersistenceUnitName("persistence-unit-proj-" + PROJECTION_NAME);

        return factoryBean;

    }

    @Bean
    public PlatformTransactionManager transactionManager() {

        return new JpaTransactionManager(entityManagerFactory().getObject());

    }

    private Properties hibernateProperties() {

        DatabasePropertiesDto dbProps = databaseProperties();

        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", dbProps.getHibernateDialect());
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", dbProps.getHibernateDdl());

        return hibernateProperties;

    }

    @Primary
    @RefreshScope
    @Bean(name = "datasource")
    public HikariDataSource dataSource() {

        log.debug("Called datasource...");

        if (!credentialsDto.isLastSuccessful()) {
            vaultDbRefreshService.getAndStoreCredentials();
        }

        HikariConfig jdbcConfig = new HikariConfig();

        jdbcConfig.setUsername(credentialsDto.getPresentlyWorkingUserName());
        jdbcConfig.setPassword(credentialsDto.getPresentlyWorkingPassword());
        jdbcConfig.setJdbcUrl(databaseUrl);

        log.debug("jdbcConfig : {}, {}, {}", jdbcConfig.getUsername(), jdbcConfig.getPassword(),
                jdbcConfig.getJdbcUrl());

        //jdbcConfig.setMaximumPoolSize(10);
        //jdbcConfig.setMinimumIdle(2);

        return new HikariDataSource(jdbcConfig);

    }
    
    @Bean
    @ConfigurationProperties(prefix = "swift.projection." + PROJECTION_NAME + ".database")
    public DatabasePropertiesDto databaseProperties() {

        return new DatabasePropertiesDto();

    }

}
