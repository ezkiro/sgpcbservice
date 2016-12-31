package com.toyfactory.pcb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toyfactory.pcb.domain.Pcbang;
import com.toyfactory.pcb.model.StatusCd;

public interface PcbangRepository extends JpaRepository<Pcbang, Long> {
	List<Pcbang> findByStatus(StatusCd status);
}
