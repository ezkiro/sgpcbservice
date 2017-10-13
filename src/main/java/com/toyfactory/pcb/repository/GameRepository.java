package com.toyfactory.pcb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toyfactory.pcb.domain.Game;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, String> {

    List<Game> findByEnable(String enable);
}
