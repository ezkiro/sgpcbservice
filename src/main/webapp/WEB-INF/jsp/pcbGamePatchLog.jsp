<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
	<p class="bg-info">
		<table class="table table-bordered">
			<tr class="info">
				<td>Pcbang raw data</td>
				<td>${pcbang.getCompanyName()}</td>
				<td>전체IP수:${totalIPs}</td>
				<td>수집된IP수:${pcbGamePatchList.size()}</td>
				<td><a class="btn btn-default btn-block" href="/v2/admin/gamepatch" role="button">back</a></td>
			</tr>		
		</table>
			
	</div><!-- container -->	
	<hr>
	<table class="table table-bordered table-hover">
		<tr class="info">
			<td>IP</td>
			<td>version</td>			
			<td>수집날짜</td>
			<td>Data</td>
		</tr>
	<c:forEach var="pcbGamePatch" items="${pcbGamePatchList}">
		<tr>
   			<td>${pcbGamePatch.getClientIp()}</td>   			
   			<td>${pcbGamePatch.getVersion()}</td>    			    			    			
   			<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${pcbGamePatch.getCrtDt()}" /></td>
   			<td>
   		<c:forEach var="pcbGame" items="${pcbGamePatch.getPcbGames()}">
   			${pcbGame.toString()}
		</c:forEach>
			</td>   		
   		</tr>
	</c:forEach>
	</table>			
  </body>
</html>