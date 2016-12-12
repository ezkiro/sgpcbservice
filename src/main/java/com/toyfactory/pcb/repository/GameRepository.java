package com.toyfactory.pcb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toyfactory.pcb.domain.Game;

public interface GameRepository extends JpaRepository<Game, String> {

}
