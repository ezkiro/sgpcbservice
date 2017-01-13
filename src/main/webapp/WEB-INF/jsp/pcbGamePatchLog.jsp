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
   			<td>${pcbGamePatch.getCrtDt()}</td>
   		<c:forEach var="pcbGame" items="${pcbGamePatch.getPcbGames()}">
   			<td>${pcbGame.toString()}</td>
		</c:forEach>   		
   		</tr>
	</c:forEach>
	</table>
	</div><!-- container -->			
  </body>
</html>