package com.toyfactory.pcb.resolver;

import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.WebUtils;

import com.toyfactory.pcb.domain.Agent;
import com.toyfactory.pcb.exception.InvalidTokenException;
import com.toyfactory.pcb.exception.NotExistAgentException;
import com.toyfactory.pcb.service.MemberService;

public class AgentArgumentResolver implements HandlerMethodArgumentResolver {

	private static final Logger logger = LoggerFactory.getLogger(AgentArgumentResolver.class);		
	
	@Autowired
	private MemberService memberService;
	
	public static Long getAgentId(NativeWebRequest webRequest){
		
		HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
	
		try {		
		
			Cookie cookie = WebUtils.getCookie(httpServletRequest, "access_token");
			if(cookie == null) {
				if(logger.isDebugEnabled()) logger.debug("cookie isn't exist...");
				throw new InvalidTokenException();
			}
			
			String token = URLDecoder.decode(cookie.getValue(), "UTF-8");
			
			String[] tmp = token.split("\\|");
			
			return Long.valueOf(tmp[0]);

		} catch(Exception e) {
			logger.error("error in getAgentId exception:" + e.getMessage());
			throw new InvalidTokenException();
		}
	}
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (parameter.getParameterAnnotation(AgentArg.class) != null && Agent.class == parameter.getParameterType()) return true;
		else return false;
	}

	@Override
	public Agent resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		try {
			Long agentId = getAgentId(webRequest);
			
			if(logger.isDebugEnabled()) logger.debug("getAgentId agent_id:" + agentId);
			
			Agent agent = memberService.findAgent(agentId);
						
			if(agent == null) {
				logger.error("no agent! agent id:" + agentId + " , request:" + webRequest.getDescription(true));
				AgentArg agentArg = parameter.getParameterAnnotation(AgentArg.class);
				if (agentArg.required()) {
					throw new NotExistAgentException();
				} else {
					return null;					
				}
			}
			
			if(logger.isDebugEnabled()) logger.debug("agent resolved! agent_id:" + agent.getAgentId());
			
			return agent;
			
		} catch(Exception e) {
			logger.error("error in resolveArgument exception:" + e.getMessage());			
			AgentArg agentArg = parameter.getParameterAnnotation(AgentArg.class);
			if (agentArg.required()) {
				throw new NotExistAgentException();
			} else {
				return null;					
			}
		}
	}
}
