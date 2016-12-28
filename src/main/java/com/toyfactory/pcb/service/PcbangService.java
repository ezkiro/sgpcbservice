package com.toyfactory.pcb.service;

import org.apache.commons.net.util.SubnetUtils;
import org.springframework.stereotype.Service;

@Service("pcbangService")
public class PcbangService {

	public boolean isPcbangIP(String ipAddress){
		String subnet = "192.168.0.3/31";
		SubnetUtils utils = new SubnetUtils(subnet);
		return utils.getInfo().isInRange(ipAddress);
	}
	
}
