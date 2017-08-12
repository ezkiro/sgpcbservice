package com.toyfactory.pcb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toyfactory.pcb.domain.History;

public interface HistoryRepository extends JpaRepository<History, String> {
	List<History> findByDateKeyBetween(String startKey, String endKey);
}
