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

	@Query("select p from Pcbang p where p.agent.agentId = :agentId and p.program = :program")
	List<Pcbang> findByAgentAndProgram(@Param(value = "agentId")Long agentId, @Param(value = "program")String program);

	List<Pcbang> findAllByOrderByUptDtDesc();
	
	@Query("select p from Pcbang p where p.agent.agentId = :agentId and p.status = :status")
	List<Pcbang> findByAgentAndStatus(@Param(value = "agentId")Long agentId, @Param(value = "status") StatusCd status);

	@Query("select p from Pcbang p where p.agent.companyName = :companyName and p.status = :status")
	List<Pcbang> findByAgentNameAndStatus(@Param(value = "companyName")String agentName, @Param(value = "status") StatusCd status);
	
	List<Pcbang> findByCompanyCodeAndStatus(String companyCode, StatusCd status);

	@Query("select p from Pcbang p where p.agent.agentId = :agentId and p.companyCode =:companyCode and p.status = :status")
	List<Pcbang> findByAgentAndCompanyCodeAndStatus(@Param(value = "agentId")Long agentId, @Param(value = "companyCode")String companyCode, @Param(value = "status")StatusCd status);

	@Query("select p from Pcbang p where p.agent.agentId = :agentId and p.companyCode =:companyCode")
	List<Pcbang> findByAgentAndCompanyCode(@Param(value = "agentId")Long agentId, @Param(value = "companyCode")String companyCode);

	//ipStart% search
	List<Pcbang> findByIpStartStartingWithAndStatus(String ipStart, StatusCd status);

	//ipStart% search
	@Query("select p from Pcbang p where p.agent.agentId = :agentId and p.ipStart like :ipStart% and p.status = :status")
	List<Pcbang> findByAgentAndIpStartStartingWithAndStatus(@Param(value = "agentId")Long agentId, @Param(value = "ipStart")String ipStart, @Param(value = "status")StatusCd status);

	//ipStart% search
	@Query("select p from Pcbang p where p.agent.agentId = :agentId and p.ipStart like :ipStart%")
	List<Pcbang> findByAgentAndIpStartStartingWith(@Param(value = "agentId")Long agentId, @Param(value = "ipStart")String ipStart);

	//%companyName% search
	List<Pcbang> findByCompanyNameContainingAndStatus(String companyName, StatusCd status);

	//%companyName% search
	@Query("select p from Pcbang p where p.agent.agentId = :agentId and p.companyName like %:companyName% and p.status = :status")
	List<Pcbang> findByAgentAndCompanyNameContainingAndStatus(@Param(value = "agentId")Long agentId, @Param(value = "companyName")String companyName, @Param(value = "status")StatusCd status);

	//%companyName% search
	@Query("select p from Pcbang p where p.agent.agentId = :agentId and p.companyName like %:companyName%")
	List<Pcbang> findByAgentAndCompanyNameContaining(@Param(value = "agentId")Long agentId, @Param(value = "companyName")String companyName);

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
