package com.toyfactory.pcb;

import com.toyfactory.pcb.config.PcbProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableCaching
@Configuration
@EnableConfigurationProperties(PcbProperties.class)
public class SgpcbserviceApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SgpcbserviceApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(SgpcbserviceApplication.class, args);
	}
}
