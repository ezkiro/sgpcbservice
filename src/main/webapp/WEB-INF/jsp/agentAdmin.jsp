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
		  <li role="presentation"><a href="#">설치/패치관리</a></li>
		  <li role="presentation"><a href="#">가맹점관리</a></li>
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
		<div class="col-md-3">	  			
			<input type="text" class="form-control" id="serachKeyword" placeholder="">
		</div>
		<div class="col-md-2">		
			<button type="submit" class="btn btn-primary btn-block">조회</button>
		</div>
	</div>	  
	</form>
	
  	<div class="form-group bg-info">
		<div class="col-md-2">
			<label for="lbTotalAgent">총 가명점 수 : 개 </label>
		</div>
		<div class="col-md-2">
			<label for="lbOK">정상 : 개 </label>
		</div>
		<div class="col-md-2">
			<label for="lbWait">미승인 : 개 </label>
		</div>		
	</div>
	<div class="row">
		<table class="table table-bordered">
			<tr>
				<td>구분</td>
				<td>사업자번호</td>
				<td>아이디</td>
				<td>상호</td>
				<td>대표자</td>
				<td>연락처</td>
				<td>주소</td>
				<td>email</td>
				<td>계좌번호</td>
				<td>관리매장</td>
				<td>등록일</td>
				<td>상태</td>
				<td>권한</td>
				<td>관리</td>																																																		
			</tr>
		<c:forEach var="agent" items="${agentList}">
			<tr>
    			<td>${agent.id}</td>
    			<td>${agent.companyCode}</td>
    			<td>${agent.accountId}</td>
    			<td>${agent.companyName}</td>    			    			    			
    			<td>${agent.ceo}</td>
    			<td>${agent.contactNum}</td>
    			<td>${agent.address}</td>
    			<td>${agent.email}</td>    			    			    			
    			<td>${agent.bankAccount}</td>
    			<td>${agent.pcbangCnt}</td>
    			<td>${agent.crtDt}</td>
    			<td>${agent.status}</td>    			    			    			
    			<td>${agent.permission}</td>
    			<td>수정</td>
    		</tr>
		</c:forEach>
		</table>
	</div>	
	
  </body>
</html>