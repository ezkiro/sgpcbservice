<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
	<jsp:include page="common.jsp" flush="true"/>
  </head>
  <body>
	<div class="container"> 
  	<div class="row">
		<ul class="nav nav-pills">
		  <li role="presentation" class="active"><a href="#">업체관리</a></li>
		  <li role="presentation"><a href="/admin/gamepatch">설치/패치관리</a></li>
		  <li role="presentation"><a href="/admin/pcbang">가맹점관리</a></li>
		  <li role="presentation"><a href="/admin/game">Game관리</a></li>
		  <li role="presentation"><a href="/logout">로그아웃</a></li>		  		  
		</ul>
  	</div>
  	<hr>
  	
	<form class="form-horizontal">
  	<div class="form-group bg-info">
		<div class="col-md-1 text-right">
			<label for="lbID">조회조건</label>
		</div>
		<div class="col-md-2">		
			<select class="form-control" id ="inputKeyword">
			  <option>사업자번호</option>
			  <option>아이디</option>			  
			  <option>상호</option>			  
			  <option>대표자</option>
			  <option>주소</option>			  
			  <option>상태</option>
			</select>
		</div>
		<div class="col-md-4">	  			
			<input type="text" class="form-control" id="serachKeyword" placeholder="">
		</div>
		<div class="col-md-2">		
			<button type="submit" class="btn btn-primary btn-block">조회</button>
		</div>
	</div>	  
	</form>
	
  	<div class="form-group">
		<div class="col-md-2 bg-warning">
			<label for="lbTotalAgent">총 가맹점 수 :${agentList.size()} 개 </label>
		</div>
		<div class="col-md-2 bg-warning">
			<label for="lbOK">정상 :${agentOKCnt} 개 </label>
		</div>
		<div class="col-md-2 bg-warning">
			<label for="lbWait">미승인 :${agentWaitCnt} 개 </label>
		</div>		
	</div>
	</div><!-- container -->	
	
		<table class="table table-bordered table-hover">
			<tr class="info">
				<td>구분</td>
				<td>사업자번호</td>
				<td>아이디</td>
				<td>상호</td>
				<td>대표자</td>
				<td>연락처</td>
				<td>주소</td>
				<td>email</td>
				<td>계좌번호</td>
				<td>관리매장수</td>
				<td>등록일</td>
				<td>상태</td>
				<td>권한</td>
				<td>관리</td>
				<td>삭제</td>																																																	
			</tr>
		<c:forEach var="agent" items="${agentList}">
			<tr>
    			<td>${agent.getAgentId()}</td>
    			<td>${agent.getCompanyCode()}</td>
    			<td>${agent.getAccount().getId()}</td>
    			<td>${agent.getCompanyName()}</td>    			    			    			
    			<td>${agent.getCeo()}</td>
    			<td>${agent.getContactNum()}</td>
    			<td>${agent.getAddress()}</td>
    			<td>${agent.getEmail()}</td>    			    			    			
    			<td>${agent.getBankAccount()}</td>
    			<td>${agent.getPcbangs().size()}</td>
    			<td>${agent.getCrtDt().toString().substring(0,10)}</td>
    			<td>${agent.getStatus()}</td>    			    			    			
    			<td>${agent.getAccount().getPermission()}</td>
    			<td><a class="btn btn-default" href="/admin/agent/update?agent_id=${agent.getAgentId()}" role="button">수정</a></td>
    			<td><button type="button" class="btn btn-danger btn-block" id="removeAgent">삭제</button></td>
    		</tr>
		</c:forEach>
		</table>
		
  </body>
</html>