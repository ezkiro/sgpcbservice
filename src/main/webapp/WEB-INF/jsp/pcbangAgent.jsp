<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
	<jsp:include page="common.jsp" flush="true"/>
	
  <script>
	$(document).ready(function(){
				
	});  
  </script> 
	
		
  </head>
  <body>
	<div class="container"> 
  	<div class="row">
		<ul class="nav nav-pills">
		  <li role="presentation"><a href="/member/gamepatch">설치/패치관리</a></li>
		  <li role="presentation" class="active"><a href="#">가맹점관리</a></li>
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
			  <option>상호</option>			  
			  <option>주소</option>			  
			  <option>IP대역</option>
			  <option>프로그램명</option>
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
	
  	<div class="form-group bg-info">
		<div class="col-md-2">
			<label for="lbTotalAgent">총 가맹점 수 :${pcbangCnt}개 </label>
		</div>
	</div>
	
	<table class="table table-bordered table-hover">
		<tr class="info">
			<td>구분</td>
			<td>사업자번호</td>
			<td>상호</td>
			<td>주소</td>
			<td>IP대역</td>
			<td>subnet mask</td>
			<td>상태</td>			
			<td>등록일</td>
		</tr>
	<c:forEach var="pcbang" items="${pcbangList}">
		<tr>
   			<td><input type="checkbox" value="${pcbang.getPcbId()}">${pcbang.getPcbId()}</td>
   			<td>${pcbang.getCompanyCode()}</td>
   			<td>${pcbang.getCompanyName()}</td>    			    			    			
   			<td>${pcbang.getAddress()}</td>
   			<td>${pcbang.getIpStart()} - ${pcbang.getIpEnd()}</td>    			    			    			
   			<td>${pcbang.getSubmask()}</td>
   			<td>${pcbang.getStatus()}</td>   			  			
   			<td>${pcbang.getCrtDt().toString().substring(0,10)}</td>
   		</tr>
	</c:forEach>
	</table>
	<br>
	<div class="form-group">    
		<div class="col-md-4"></div>
		<div class="col-md-2"><a class="btn btn-default btn-block" href="/member/pcbang/add" role="button">등록</a></div>
		<div class="col-md-2"><a class="btn btn-default btn-block" href="#" role="button">삭제</a></div>		
	</div>
	
	</div><!-- container -->	
		
  </body>
</html>