package com.toyfactory.pcb.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.util.SubnetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toyfactory.pcb.domain.Pcbang;
import com.toyfactory.pcb.model.StatusCd;
import com.toyfactory.pcb.repository.PcbangRepository;

@Service("pcbangService")
public class PcbangService {

	@Autowired	
	private PcbangRepository pcbangDao;	
	
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
	public List<String> buildPcbangIPs(String ipStart, String ipEnd, String subnetMask){
		
		SubnetUtils utils = new SubnetUtils(ipStart,subnetMask);
		
		//sort 되어서 나온다고 가정
		String[] allIPs = utils.getInfo().getAllAddresses();
		
		List<String> pcbangIPs = new ArrayList<String>();
		
		//ipStart부터 - ipEnd까지만 넣기
		boolean isRange = false;
		for(String ip : allIPs){			
			if(ipStart.equals(ip)){
				isRange = true;
			}
			
			if(ipEnd.equals(ip)){
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
		//key:status 
		if("status".equals(key)){
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
}
