package com.sjd.demo.vault.refresh;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sjd.demo.vault.dto.CredentialsDto;

@Component
public class CredentialsService {

    @Bean
    @Scope(value = "singleton")
    public CredentialsDto createMyDbCredentialsBean() {
        return new CredentialsDto();
    }

}
