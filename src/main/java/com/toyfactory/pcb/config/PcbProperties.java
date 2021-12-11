package com.toyfactory.pcb.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix="pcbservice.config")
public class PcbProperties {
    private Long agentExpireDays = 1L;
    private Long installCnt = 1L;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExpireDays:");
        sb.append(agentExpireDays);
        sb.append(",installCnt:");
        sb.append(installCnt);
        return sb.toString();
    }
}
