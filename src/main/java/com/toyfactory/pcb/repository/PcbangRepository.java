package com.toyfactory.pcb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toyfactory.pcb.domain.Pcbang;
import com.toyfactory.pcb.model.StatusCd;

public interface PcbangRepository extends JpaRepository<Pcbang, Long> {
	List<Pcbang> findByStatus(StatusCd status);
	List<Pcbang> findByIpStartAndIpEnd(String ipStart, String ipEnd);
	List<Pcbang> findByProgram(String program);
	
	List<Pcbang> findAllByOrderByUptDtDesc();
	
	@Query("select p from Pcbang p where p.agent.agentId = :agentId and p.status = :status")
	List<Pcbang> findByAgentAndStatus(@Param(value = "agentId")Long agentId, @Param(value = "status") StatusCd status);

	@Query("select p from Pcbang p where p.agent.companyName = :companyName and p.status = :status")
	List<Pcbang> findByAgentNameAndStatus(@Param(value = "companyName")String agentName, @Param(value = "status") StatusCd status);
	
	List<Pcbang> findByCompanyCodeAndStatus(String companyCode, StatusCd status);
	
	//ipStart% search
	List<Pcbang> findByIpStartStartingWithAndStatus(String ipStart, StatusCd status);
	
	//%companyName% search
	List<Pcbang> findByCompanyNameContainingAndStatus(String companyName, StatusCd status);
	
	@Query("select p from Pcbang p where p.agent.agentId = :agentId")
	List<Pcbang> findByAgent(@Param(value = "agentId")Long agentId);

	@Query("select p from Pcbang p where p.agent.companyName = :companyName")
	List<Pcbang> findByAgentName(@Param(value = "companyName")String agentName);
	
	List<Pcbang> findByCompanyCode(String companyCode);
	
	//ipStart% search
	List<Pcbang> findByIpStartStartingWith(String ipStart);
	
	//%companyName% search
	List<Pcbang> findByCompanyNameContaining(String companyName);
}
