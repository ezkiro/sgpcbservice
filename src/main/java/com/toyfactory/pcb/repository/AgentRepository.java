package com.toyfactory.pcb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toyfactory.pcb.domain.Agent;

public interface AgentRepository extends JpaRepository<Agent, Long> {
	Agent findByCompanyCode(String companyCode);
	Agent findByContactNum(String contact);
	Agent findByEmail(String email);	
}
