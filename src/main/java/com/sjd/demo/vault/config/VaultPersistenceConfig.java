package com.sjd.demo.vault.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.sjd.demo.vault.dto.DatabasePropertiesDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class VaultPersistenceConfig {

    public static final String PROJECTION_NAME = "vault";

    @Autowired
    private DataSource dataSource;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        //        factoryBean.setDataSource(dataSource());
        factoryBean.setDataSource(dataSource);
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setJpaProperties(hibernateProperties());
        factoryBean.setPackagesToScan("com.sjd");
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

    /*
    @RefreshScope
    @Bean
    public DataSource dataSource() {

        DatabaseProperties dbProps = databaseProperties();

        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setPoolName("HikariPool-" + PROJECTION_NAME);
        //dataSourceConfig.setDriverClassName(dbProps.getDriverClassName());
        dataSourceConfig.setJdbcUrl(dbProps.getUrl());
        dataSourceConfig.setUsername(dbProps.getUsername());
        dataSourceConfig.setPassword(dbProps.getPassword());
        dataSourceConfig.setMinimumIdle(dbProps.getMinIdle());
        dataSourceConfig.setMaximumPoolSize(dbProps.getMaxPoolSize());

        log.info("{}", dbProps);

        return new HikariDataSource(dataSourceConfig);

    }
     */

    @Bean
    @ConfigurationProperties(prefix = "swift.projection." + PROJECTION_NAME + ".database")
    public DatabasePropertiesDto databaseProperties() {

        return new DatabasePropertiesDto();

    }

}
