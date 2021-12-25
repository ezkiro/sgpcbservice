package com.toyfactory.pcb.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toyfactory.pcb.domain.GamePatchHistory;

public interface GamePatchHistoryRepository extends JpaRepository<GamePatchHistory, Long> {
	List<GamePatchHistory> findByDateKeyBetween(String startKey, String endKey);
	
	GamePatchHistory findByDateKeyAndGsn(String dateKey, String gsn);

	void deleteByCrtDtBefore(Date crtDt);
}
