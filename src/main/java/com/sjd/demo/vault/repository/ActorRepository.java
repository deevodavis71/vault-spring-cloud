package com.sjd.demo.vault.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sjd.demo.vault.domain.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {
}
