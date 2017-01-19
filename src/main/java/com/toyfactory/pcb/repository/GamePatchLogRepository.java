package com.toyfactory.pcb.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toyfactory.pcb.domain.GamePatchLog;
import com.toyfactory.pcb.domain.GamePatchLogPK;

public interface GamePatchLogRepository extends JpaRepository<GamePatchLog, GamePatchLogPK> {

	List<GamePatchLog> findByPcbId(Long pcbId);
	List<GamePatchLog> findByPcbIdAndUptDt(Long pcbId, Date uptDt);
	void deleteByPcbId(Long pcbId);
}
