package com.toyfactory.pcb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toyfactory.pcb.domain.Agent;
import com.toyfactory.pcb.model.StatusCd;

public interface AgentRepository extends JpaRepository<Agent, Long> {
	Agent findByCompanyCode(String companyCode);
	Agent findByContactNum(String contact);
	Agent findByEmail(String email);
	List<Agent> findByStatus(StatusCd status);
}
