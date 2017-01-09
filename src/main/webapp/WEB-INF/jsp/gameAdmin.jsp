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
		  <li role="presentation"><a href="/admin/agent">업체관리</a></li>
		  <li role="presentation"><a href="/admin/gamepatch">설치/패치관리</a></li>
		  <li role="presentation"><a href="/admin/pcbang">가맹점관리</a></li>
		  <li role="presentation" class="active"><a href="#">Game관리</a></li>
		  <li role="presentation"><a href="/logout">로그아웃</a></li>		  	  
		</ul>
  	</div>
  	<hr>  		
	
	<table class="table table-bordered table-hover">
		<tr class="info">
			<td>GSN</td>
			<td>게임명</td>
			<td>major버전</td>
			<td>minor버전</td>
			<td>변경날짜</td>
			<td>생성날짜</td>
			<td>관리</td>
		</tr>
	<c:forEach var="game" items="${gameList}">
		<tr>
   			<td><input type="checkbox" value="${game.getGsn()}">${game.getGsn()}</td>
   			<td>${game.getName()}</td>
   			<td>${game.getMajor()}</td>    			    			    			
   			<td>${game.getMinor()}</td>
   			<td>${game.getUptDt().toString()}</td>   			
   			<td>${game.getCrtDt().toString()}</td>
   			<td>
				<a class="btn btn-default" href="/admin/game/update?gsn=${game.getGsn()}" role="button">수정</a>
   			</td>
   		</tr>
	</c:forEach>
	</table>
	<br>
	<div class="form-group">
		<div class="col-md-2"></div>
		<div class="col-md-2"></div>		    
		<div class="col-md-2"><a class="btn btn-default btn-block" href="/admin/game/add" role="button">등록</a></div>
		<div class="col-md-2"><a class="btn btn-default btn-block" href="/admin/game/remove" role="button">삭제</a></div>	
	</div>
	
	</div><!-- container -->	
  </body>
</html>