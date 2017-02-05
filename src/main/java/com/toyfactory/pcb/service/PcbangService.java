package com.toyfactory.pcb.service;

import java.io.BufferedReader;
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
	
	public Long getPcbId(String ipAddress) {
		
		
		
		return 0L;
	}
	
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
	
	public List<Pcbang> findPcbangs(String key, String keyword) {		
		
		if ("agentName".equals(key)) {
			return pcbangDao.findByAgentNameAndStatus(keyword, StatusCd.OK);
		}
		
		if ("companyCode".equals(key)) {
			return pcbangDao.findByCompanyCodeAndStatus(keyword, StatusCd.OK);
		}
		
		if ("companyName".equals(key)) {
			return pcbangDao.findByCompanyNameContainingAndStatus(keyword, StatusCd.OK);
		}

		if ("ipRange".equals(key)) {
			// 100.1.1.1 -> 100.1.1% search
			String ipKey = keyword.substring(0,keyword.lastIndexOf("."));
			if (logger.isDebugEnabled()) logger.debug("[findPcbangs] ipKey:" + ipKey);
			return pcbangDao.findByIpStartStartingWithAndStatus(ipKey, StatusCd.OK);
		}
		
		if ("all".equals(key) || "patchYN".equals(key)) {
			return pcbangDao.findByStatus(StatusCd.OK);
		}		
		
		//key:status
		if ("status".equals(key)) {
			return pcbangDao.findByStatus(StatusCd.valueOf(keyword));
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
	
	/**
	 * PC방 가맹점 일괄 입력 처리, 실패 목록을 반환한다.
	 * @param multipart
	 * @return
	 */
	public List<String> insertBulkData(MultipartFile multipart) {

		List<String> invalidDatas = new ArrayList<String>();
		
		try {

			List<String> rawDatas = readFromMultipartFile(multipart);
			
			List<Pcbang> pcbangs = new ArrayList<Pcbang>();
			
			//검증작업
			for (String rawData : rawDatas) {
				Pcbang pcbang = parseCsv(rawData);
				if (pcbang == null) {
					invalidDatas.add(rawData);
					continue;
				}
				
				pcbangs.add(pcbang);
			}			
			
			if (!invalidDatas.isEmpty()) {
				return invalidDatas;
			}
			
			for (Pcbang pcbang : pcbangs) {				
				memberService.addPcbang(pcbang, pcbang.getAgent().getAgentId());
			}
			
			return invalidDatas;
			
		} catch (Exception e) {
			logger.error("[insertBulkData] exception:" + e.getMessage());
			invalidDatas.add("fail to addPcbang:" + e.getMessage());
			return invalidDatas;
		}		
	}
		
	private List<String> readFromMultipartFile(MultipartFile multipart) throws IOException {
		List<String> outputList = new ArrayList<String>();
		
		BufferedReader bufferedReader = null;
				
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(multipart.getInputStream(), "MS949"));			
			String pcbData = null;
			
			while ((pcbData = bufferedReader.readLine()) != null) {				
				if (logger.isDebugEnabled()) logger.debug("[readFromMultipartFile] pcbData:" + pcbData);
								
				outputList.add(pcbData);
			}
						
		} catch (Exception e) {
			logger.error("[readFromFile] exception:" + e.getMessage());
		} finally {
			bufferedReader.close();			
		}
		
		return outputList;
	}
	
	private Pcbang parseCsv(String csvLine) {

		try {
			//0 agent id(관리업체1), 1 대표자, 2 상호, 3 start ip, 4 end ip, 5 submask, 6 관리업체2, 7 프로그램, 8 주소
			String[] pcbData = csvLine.split(",");

			if (pcbData.length < 9) return null;
					
	    	Pcbang aPcbang = new Pcbang(new Date());
	    	
	    	Agent aAgent = new Agent(new Date());
	    	aAgent.setAgentId(Long.valueOf(pcbData[0].trim()));
	    	
	    	aPcbang.setAgent(aAgent);    	
	    	aPcbang.setCeo(pcbData[1].trim());
	    	aPcbang.setCompanyName(pcbData[2].trim());    	
	    	aPcbang.setIpStart(pcbData[3].trim());
	    	aPcbang.setIpEnd(pcbData[4].trim());
	    	aPcbang.setSubmask(pcbData[5].trim());
	    	aPcbang.setCompanyCode(pcbData[6].trim());    	
	    	aPcbang.setProgram(pcbData[7].trim());
	    	aPcbang.setStatus(StatusCd.OK);	    	
	    	
	    	//주소에 구분자 , 가 포함되는 경우에 대비해서 아래와 같이 처리
    		StringBuilder sb = new StringBuilder();
    		for (int i = 8; i < pcbData.length; i++ ) {
    			sb.append(pcbData[i]);
    		}
    		
	    	aPcbang.setAddress(sb.toString());
			return aPcbang;
			
		} catch (Exception e) {
			logger.error("[parseCsv] exception:" + e.getMessage());
			return null;
		}
	}
}
