package com.sjd.demo.vault.business;

import static java.util.UUID.randomUUID;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sjd.demo.vault.domain.Actor;
import com.sjd.demo.vault.repository.ActorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VaultQueryService {

    private final ActorRepository actorRepository;

    public List<Actor> get() {

        return actorRepository.findAll();

    }

    public Actor create() {

        Actor actor = new Actor();
        actor.setActorName(randomUUID().toString());

        return actorRepository.saveAndFlush(actor);

    }
}
