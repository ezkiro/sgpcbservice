package com.toyfactory.pcb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toyfactory.pcb.domain.InstallPath;

public interface InstallPathRepository extends JpaRepository<InstallPath, Long> {
	List<InstallPath> findByGsn(String gsn);
	List<InstallPath> findAllByOrderByGsn();
}
