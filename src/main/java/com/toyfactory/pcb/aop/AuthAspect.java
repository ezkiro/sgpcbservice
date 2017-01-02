package com.toyfactory.pcb.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.toyfactory.pcb.exception.InvalidTokenException;

import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class AuthAspect{
	
	private static final Logger logger = LoggerFactory.getLogger(AuthAspect.class);	
	
	@Around("@annotation(pcbAuth)")
	public Object verifyAuthrization(ProceedingJoinPoint joinPoint,  PcbAuthorization pcbAuth) throws Throwable {
		
		HttpServletRequest request = null;

		//get HttpServletRequest
		for(Object obj : joinPoint.getArgs()){
			
			if(logger.isDebugEnabled()) logger.debug("joinPoint obj:" + obj);
			
			if(obj instanceof HttpServletRequest){
				request = (HttpServletRequest) obj;
				break;
			}
		}
		
		//before
		if(pcbAuth.requireAccessToken() && request != null) {				
			try {
				Cookie cookie = WebUtils.getCookie(request, "access_token");
				if(cookie == null) {
					if(logger.isDebugEnabled()) logger.debug("cookie isn't exist...");
					throw new InvalidTokenException();
				}
				
				String token = URLDecoder.decode(cookie.getValue(), "UTF-8");
				if(false) //check
					throw new InvalidTokenException();
								
				
			} catch(InvalidTokenException e) {
				if(logger.isDebugEnabled()) logger.debug("invaild Token exception!");
				
				return new ModelAndView("redirect:/login");
			}		
		}

		Object result =  joinPoint.proceed();
		
		return result;

	}
}
