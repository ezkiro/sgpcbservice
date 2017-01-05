package com.toyfactory.pcb.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import com.toyfactory.pcb.exception.InvalidPermissionException;
import com.toyfactory.pcb.exception.InvalidTokenException;
import com.toyfactory.pcb.model.Permission;
import com.toyfactory.pcb.service.MemberService;

import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class AuthAspect{
	
	private static final Logger logger = LoggerFactory.getLogger(AuthAspect.class);	

	@Autowired
	private MemberService memberService;	
	
	@Around("@annotation(pcbAuth)")
	public Object verifyAuthrization(ProceedingJoinPoint joinPoint,  PcbAuthorization pcbAuth) throws Throwable {
		
		//get HttpServletRequest
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		//before
		if(pcbAuth.requireAccessToken()) {				
			try {
				Cookie cookie = WebUtils.getCookie(request, "access_token");
				if(cookie == null) {
					if(logger.isDebugEnabled()) logger.debug("cookie isn't exist...");
					throw new InvalidTokenException();
				}
				
				String token = URLDecoder.decode(cookie.getValue(), "UTF-8");
				
				Permission userPerm = memberService.verifyAccessToken(token);
				if(userPerm == Permission.NOBODY){ //check
					throw new InvalidTokenException();
				}
				
				//check permission
				if(userPerm != Permission.valueOf(pcbAuth.permission())){
					throw new InvalidPermissionException();
				}
				
			} catch(InvalidTokenException e) {
				if(logger.isDebugEnabled()) logger.debug("invalid Token exception!");
				
				//return new ModelAndView("redirect:/login");
				return "redirect:/login";
			} catch(InvalidPermissionException e) {
				if(logger.isDebugEnabled()) logger.debug("invalid permission exception!");
				
				return "redirect:/errormsg";
			}
		}

		Object result =  joinPoint.proceed();
		
		return result;

	}
}
