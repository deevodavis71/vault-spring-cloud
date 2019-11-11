package com.sjd.demo.vault.refresh;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sjd.demo.vault.dto.CredentialsBeanDto;

@Component
public class CredentialsBeanService {

    @Bean
    @Scope(value = "singleton")
    public CredentialsBeanDto createMyDbCredentialsBean() {
        return new CredentialsBeanDto();
    }

}
