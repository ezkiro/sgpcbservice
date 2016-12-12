package com.toyfactory.pcb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toyfactory.pcb.domain.GamePatchLog;

public interface GamePatchLogRepository extends JpaRepository<GamePatchLog, Long> {

}
