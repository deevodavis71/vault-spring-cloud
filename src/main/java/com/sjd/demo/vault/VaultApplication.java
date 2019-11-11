package com.sjd.demo.vault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.sjd" }, excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.sjd.demo.vault.refresh.*"))
@EnableJpaRepositories(basePackages = { "com.sjd.demo" })
@EnableScheduling
public class VaultApplication {

    public static void main(String[] args) {
        SpringApplication.run(VaultApplication.class, args);
    }

}
