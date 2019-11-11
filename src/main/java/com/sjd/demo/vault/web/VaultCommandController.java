package com.sjd.demo.vault.web;

import static org.springframework.http.ResponseEntity.created;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sjd.demo.vault.business.VaultQueryService;
import com.sjd.demo.vault.domain.Actor;
import com.sjd.demo.vault.dto.ActorDtoV1;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class VaultCommandController {

    private final VaultQueryService vaultQueryService;

    @PostMapping("/actors")
    public ResponseEntity<ActorDtoV1> create() {

        Actor actor = vaultQueryService.create();

        return created(null).body(new ActorDtoV1(actor.getActorId(), actor.getActorName()));

    }

}
