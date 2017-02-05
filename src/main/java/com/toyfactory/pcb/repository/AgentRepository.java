package com.toyfactory.pcb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toyfactory.pcb.domain.Agent;
import com.toyfactory.pcb.model.StatusCd;

public interface AgentRepository extends JpaRepository<Agent, Long> {
	Agent findByCompanyCode(String companyCode);
	Agent findByContactNum(String contact);
	Agent findByEmail(String email);
	List<Agent> findByStatus(StatusCd status);
	List<Agent> findAllByOrderByCrtDtDesc();
	
	@Query("select a from Agent a where a.account.id = :userid")
	List<Agent> findByAccount(@Param(value = "userid") String accountId);
	
	List<Agent> findByCeo(String ceo);
	
	List<Agent> findByCompanyNameContaining(String companyName);
	
}
