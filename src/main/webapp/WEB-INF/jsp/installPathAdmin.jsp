<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
	<jsp:include page="common.jsp" flush="true"/>
	
  <script>
	$(document).ready(function(){
		$("#removeItem").click(function(){
			
			var targetItems = new Array();
			
			$(":checked").each(function(){
				targetItems.push($(this).val());
			});
			
			if (targetItems.length == 0) {
				return;
			}
			
			if (!confirm(targetItems + ' path 정보를 삭제 하시겠습니까?')) {
				return;
			}
			
		    $.post("/admin/api/installpath/remove",
		    		{
		    			id_list: targetItems,
		    			access_token: getCookie("access_token")
		    		},
		    		function(data, status){
		    			if(data) {
		    				alert('install path 정보 삭제에 성공했습니다.');		    				
		    				location.href = '/admin/installpath';
		    			} else {
		    				alert('install path 삭제에 실패했습니다. 다시 시도 하세요.');
		    			}
		    		}
		    );			
		});
		
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
		  <li role="presentation"><a href="/admin/game">Game관리</a></li>
		  <li role="presentation" class="active"><a href="#">InstallPath관리</a></li>
		  <li role="presentation"><a href="/logout">로그아웃</a></li>  	  
		</ul>
  	</div>
  	<hr>  		
	
	<table class="table table-bordered table-hover">
		<tr class="warning">
			<td>id</td>
			<td>Game</td>			
			<td>category</td>
			<td>path</td>
			<td>변경날짜</td>
			<td>관리</td>
		</tr>
	<c:forEach var="installPath" items="${installPathList}">
		<tr>		
   			<td><input type="checkbox" value="${installPath.getId()}">${installPath.getId()}</td>
   			<td>${gameMap.get(installPath.getGsn()).getName()}</td>
   			<td>${installPath.getType()}</td>
   			<td>${installPath.getPath()}</td>   			   			   			   			
   			<td>${installPath.getUptDt().toString()}</td>   			
   			<td>
				<a class="btn btn-default" href="/admin/installpath/update?id=${installPath.getId()}" role="button">수정</a>
   			</td>
   		</tr>
	</c:forEach>
	</table>
	<br>
	<div class="form-group">
		<div class="col-md-2"></div>
		<div class="col-md-2"></div>		    
		<div class="col-md-2"><a class="btn btn-default btn-block" href="/admin/installpath/add" role="button">등록</a></div>
		<div class="col-md-2"><button type="button" class="btn btn-default btn-block" id="removeItem">삭제</button></div>
	</div>
	
	</div><!-- container -->	
  </body>
</html>