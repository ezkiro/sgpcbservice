package com.toyfactory.pcb.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.util.SubnetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.toyfactory.pcb.domain.Agent;
import com.toyfactory.pcb.domain.Pcbang;
import com.toyfactory.pcb.model.StatusCd;
import com.toyfactory.pcb.repository.PcbangRepository;

@Service("pcbangService")
public class PcbangService {

	private static final Logger logger = LoggerFactory.getLogger(PcbangService.class);		
	
	@Autowired	
	private PcbangRepository pcbangDao;
	
	@Autowired
	private MemberService memberService;	
	
	public boolean isPcbangIP(String ipAddress){
		String cidrNotation = "192.168.0.3/31";
		SubnetUtils utils = new SubnetUtils(cidrNotation);
		return utils.getInfo().isInRange(ipAddress);
	}
	
	/**
	 * PC방에 포함된 IP리스트를 생성한다.
	 * 실제 subnetmask보다  range가 작을수 있기 때문에 ipStart 와 ipEnd로 필터링이 필요할 수 도 있다.
	 * @param ipStart
	 * @param ipEnd
	 * @param subnetMask
	 * @return
	 */
	public List<String> buildPcbangIPs(String ipStart, String ipEnd, String subnetMask) {
		
		SubnetUtils utils = new SubnetUtils(ipStart,subnetMask);
		
		//sort 되어서 나온다고 가정
		String[] allIPs = utils.getInfo().getAllAddresses();
		
		List<String> pcbangIPs = new ArrayList<String>();
		
		//ipStart부터 - ipEnd까지만 넣기
		boolean isRange = false;
		for (String ip : allIPs) {			
			if (ipStart.equals(ip)) {
				isRange = true;
			}
			
			if (ipEnd.equals(ip)) {
				pcbangIPs.add(ip);
				break;
			}
			
			if(isRange){
				pcbangIPs.add(ip);				
			}
		}
		
		return pcbangIPs;
	}
	
	public List<Pcbang> findPcbangs(String key, String keyworkd) {		
		
		if ("agentName".equals(key)) {
			return pcbangDao.findByAgentNameAndStatus(keyworkd, StatusCd.OK);
		}
		
		if ("companyCode".equals(key)) {
			return pcbangDao.findByCompanyCodeAndStatus(keyworkd, StatusCd.OK);
		}

		if ("all".equals(key)) {
			return pcbangDao.findByStatus(StatusCd.OK);
		}		
		
		//key:status
		if ("status".equals(key)) {
			return pcbangDao.findByStatus(StatusCd.valueOf(keyworkd));
		}
		
		//all pcbangs
		return pcbangDao.findAll();
	}
	
	public Pcbang findPcbang(Long pcbId){
		return pcbangDao.findOne(pcbId);
	}
	
	public List<Pcbang> findPcbangs(Long agentId, StatusCd status){
		return pcbangDao.findByAgentAndStatus(agentId, status);
	}
	
	public boolean insertBulkData(MultipartFile multipart) {

		try {
			
			List<Pcbang> pcbangs = readFromMultipartFile(multipart);
			
			for (Pcbang pcbang : pcbangs) {				
				memberService.addPcbang(pcbang, pcbang.getAgent().getAgentId());
			}
			
		} catch (Exception e) {
			logger.error("[readFromFile] exception:" + e.getMessage());
			
			return false;
		}
		return true;
	}
		
	private List<Pcbang> readFromMultipartFile(MultipartFile multipart) throws IOException {
		List<Pcbang> outputList = new ArrayList<Pcbang>();
		
		BufferedReader bufferedReader = null;
				
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(multipart.getInputStream(), "MS949"));			
			String pcbData = null;
			
			while ((pcbData = bufferedReader.readLine()) != null) {				
				if (logger.isDebugEnabled()) logger.debug("[readFromMultipartFile] pcbData:" + pcbData);				
				
				Pcbang pcbang = parseCsv(pcbData);
				
				if (pcbang == null) continue;
				
				outputList.add(pcbang);
			}
						
		} catch (Exception e) {
			logger.error("[readFromFile] exception:" + e.getMessage());
		} finally {
			bufferedReader.close();			
		}
		
		return outputList;
	}
	
	private Pcbang parseCsv(String csvLine) {
		
		//0 agent id(관리업체1), 1 대표자,2 상호,3 주소,4 start ip, 5 end ip, 6 submask, 7 관리업체2, 8 프로그램
		String[] pcbData = csvLine.split(",");

		if (pcbData.length < 8) return null;
				
    	Pcbang aPcbang = new Pcbang(new Date());
    	
    	Agent aAgent = new Agent(new Date());
    	aAgent.setAgentId(Long.valueOf(pcbData[0].trim()));
    	
    	aPcbang.setAgent(aAgent);    	
    	aPcbang.setCeo(pcbData[1].trim());
    	aPcbang.setCompanyName(pcbData[2].trim());    	
    	aPcbang.setAddress(pcbData[3].trim());
    	aPcbang.setIpStart(pcbData[4].trim());
    	aPcbang.setIpEnd(pcbData[5].trim());
    	aPcbang.setSubmask(pcbData[6].trim());
    	aPcbang.setCompanyCode(pcbData[7].trim());    	
    	aPcbang.setProgram(pcbData[8].trim());
    	aPcbang.setStatus(StatusCd.OK);
				
		return aPcbang;
	}
}
