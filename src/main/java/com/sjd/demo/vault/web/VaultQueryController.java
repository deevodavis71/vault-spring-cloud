package com.sjd.demo.vault.web;

import static org.springframework.http.ResponseEntity.ok;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sjd.demo.vault.business.VaultConfigService;
import com.sjd.demo.vault.business.VaultQueryService;
import com.sjd.demo.vault.domain.Actor;
import com.sjd.demo.vault.dto.ActorDtoV1;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class VaultQueryController {

    private final VaultConfigService vaultConfigService;

    private final VaultQueryService vaultQueryService;

    @GetMapping("/config")
    public void getConfig() {

        vaultConfigService.dump();

    }

    @GetMapping("/actors")
    public ResponseEntity<List<ActorDtoV1>> get() {

        List<ActorDtoV1> actorDtos = new ArrayList<>();

        List<Actor> actors = vaultQueryService.get();
        for (Actor actor : actors) {
            actorDtos.add(new ActorDtoV1(actor.getActorId(), actor.getActorName()));
        }

        return ok(actorDtos);

    }

}
