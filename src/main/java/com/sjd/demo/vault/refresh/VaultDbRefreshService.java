package com.sjd.demo.vault.refresh;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;
import static org.springframework.util.StringUtils.isEmpty;

import org.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sjd.demo.vault.dto.refresh.CredentialsDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VaultDbRefreshService {

    private final CredentialsDto credentialsDto;

    private final Environment env;

    private String vaultRootToken = "my-token";

    private String vaultScheme = "http";

    private String vaultHost = "127.0.0.1";

    private int vaultPort = 8200;

    private String vaultPath = "database/creds/read-write";

    //@Scheduled(cron = "${my.vault.token.renew.cron.expression}")
    //@Scheduled(initialDelay = 1000000, fixedDelay = 1000000)
    public void renewVaultToken() throws Exception {

        log.debug("Renewing root token ...");

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Vault-Token", vaultRootToken);
        headers.add("Cache-Control", "no-cache");

        new RestTemplate().exchange(vaultScheme + "://" + vaultHost + ":" + vaultPort + "/v1/auth/token/renew-self", //
                HttpMethod.POST, new HttpEntity<>(headers), String.class);

    }

    //@Scheduled(cron = "${swift.credentials.renew-every}")
    @Scheduled(initialDelay = 120_000, fixedDelay = 120_000)
    public void attemptCredentialsRefresh() {

        log.debug("Renewing db creds ...");

        getAndStoreCredentials();

        if (!credentialsDto.isLastSuccessful()) {
            return;
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);

        log.debug("Refreshing ...");

        new RestTemplate().exchange("http://localhost:" + env.getProperty("server.port") + "/actuator/refresh", //
                HttpMethod.POST, httpEntity, String.class);

    }

    public void getAndStoreCredentials() {

        boolean vaultCallSuccess = false;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Vault-Token", vaultRootToken);
        headers.add("Cache-Control", "no-cache");

        String urlToVault = vaultScheme + "://" + vaultHost + ":" + vaultPort + "/v1/" + vaultPath;

        ResponseEntity<String> response = new RestTemplate()
                .exchange(urlToVault, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        String latestDbUserName = null;
        String latestDbPassword = null;

        if (OK == response.getStatusCode()) {

            JSONObject vaultJsonObject = new JSONObject(response.getBody());

            latestDbUserName = vaultJsonObject.getJSONObject("data").getString("username");
            latestDbPassword = vaultJsonObject.getJSONObject("data").getString("password");

            vaultCallSuccess = !isEmpty(latestDbUserName) && !isEmpty(latestDbPassword);

        }

        credentialsDto.setLastSuccessful(vaultCallSuccess);

        if (vaultCallSuccess) {

            credentialsDto.setWorkingUserName(latestDbUserName);
            credentialsDto.setWorkingPassword(latestDbPassword);

        }

        log.debug("vaultCallSuccess : {}", vaultCallSuccess);

    }

}
