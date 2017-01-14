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
		
		if(logger.isDebugEnabled()) logger.debug("verifyAuthrization called! request:" + request.getRequestURL());
		
		//before
		if(pcbAuth.requireAccessToken()) {
			try {
				//parameter에 값이 있는지 확인한다.
				String accessToken = request.getParameter("access_token");
				
				if (accessToken == null) {
					//쿠키에서 찾아본다.
					Cookie cookie = WebUtils.getCookie(request, "access_token");
					if(cookie == null) {
						if(logger.isDebugEnabled()) logger.debug("cookie isn't exist...");
						throw new InvalidTokenException();
					}
					
					accessToken = URLDecoder.decode(cookie.getValue(), "UTF-8");
				}
				
				Permission userPerm = memberService.verifyAccessToken(accessToken);
				if(userPerm == Permission.NOBODY){ //check
					return "redirect:/hello";
				}
				
				//check permission
				if(userPerm.getLevel() < Permission.valueOf(pcbAuth.permission()).getLevel()){
					throw new InvalidPermissionException();
				}
				
			} catch(InvalidTokenException e) {
				if(logger.isDebugEnabled()) logger.debug("invalid Token exception!");
				
				//return new ModelAndView("redirect:/login");
				return "redirect:/login";
			} catch(InvalidPermissionException e) {
				if(logger.isDebugEnabled()) logger.debug("invalid permission exception!");
				
				return "redirect:/errormsg?message=NO Permission!";
			} catch(Exception e) {
				logger.error("error in verifyAuthrization exception:" + e.getMessage());
				
				return "redirect:/errormsg?message=Unknown Error!";
			}
		}

		Object result =  joinPoint.proceed();
		
		return result;

	}
}
