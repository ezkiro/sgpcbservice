package com.toyfactory.pcb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toyfactory.pcb.domain.Pcbang;
import com.toyfactory.pcb.model.StatusCd;

public interface PcbangRepository extends JpaRepository<Pcbang, Long> {
	List<Pcbang> findByStatus(StatusCd status);
	
	@Query("select p from Pcbang p where p.agent.agentId = :agentId and p.status = :status")
	List<Pcbang> findByAgentAndStatus(@Param(value = "agentId")Long agentId, @Param(value = "status") StatusCd status);
}
