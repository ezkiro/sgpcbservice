package com.toyfactory.pcb;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.toyfactory.pcb.resolver.AgentArgumentResolver;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SgpcbserviceApplication extends WebMvcConfigurerAdapter{

	//참고 https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-creating-a-custom-handlermethodargumentresolver
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(activateAgentArgumentResolver());
    }
	
    // AgentArgumentResolver 내에서  DI를 사용하려면 Bean으로 등록해야 한다.
    @Bean
    public AgentArgumentResolver activateAgentArgumentResolver() {
        return new AgentArgumentResolver();
    }    
    
	public static void main(String[] args) {
		SpringApplication.run(SgpcbserviceApplication.class, args);
	}
}
