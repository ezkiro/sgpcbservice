<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
	<jsp:include page="common.jsp" flush="true"/>
	
  <script>
	$(document).ready(function(){
		$("#removeGame").click(function(){
			
			var targetGames = new Array();
			
			$(":checked").each(function(){
				targetGames.push($(this).val());
			});
			
			if (targetGames.length == 0) {
				return;
			}
			
			if (!confirm(targetGames + ' Game 정보를 삭제 하시겠습니까?')) {
				return;
			}
			
		    $.post("/v2/admin/api/game/remove",
		    		{
		    			gsn_list:targetGames,
		    			access_token: getCookie("access_token")
		    		},
		    		function(data, status){
		    			if(data) {
		    				alert('game 정보 삭제에 성공했습니다.');		    				
		    				location.href = '/v2/admin/game';
		    			} else {
		    				alert('game 삭제에 실패했습니다. 다시 시도 하세요.');
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
		  <li role="presentation"><a href="/v2/admin/agent">업체관리</a></li>
		  <li role="presentation"><a href="/v2/admin/gamepatch">설치/패치관리</a></li>
		  <li role="presentation"><a href="/v2/admin/pcbang">가맹점관리</a></li>
		  <li role="presentation" class="active"><a href="#">Game관리</a></li>
		  <li role="presentation"><a href="/v2/admin/installpath">InstallPath관리</a></li>
		  <li role="presentation"><a href="/v2/logout">로그아웃</a></li>
		</ul>
  	</div>
  	<hr>  		
	
	<table class="table table-bordered table-hover">
		<tr class="info">
			<td>GSN</td>
		    <td>사용여부</td>
			<td>게임명</td>
			<td>확인방법</td>
			<td>major버전</td>
			<td>minor버전</td>
			<td>실행파일명</td>
			<td>상위dir</td>
			<td>버전파일명</td>
			<td>버전파일포맷</td>
			<td>변경날짜</td>
			<td>생성날짜</td>
			<td>관리</td>
		</tr>
	<c:forEach var="game" items="${gameList}">
		<tr>
   			<td><input type="checkbox" value="${game.getGsn()}">${game.getGsn()}</td>
   			<td>${game.getEnable()}</td>
   			<td>${game.getName()}</td>
   			<td>${game.getVerifyType()}</td>   			
   			<td>${game.getMajor()}</td>    			    			    			
   			<td>${game.getMinor()}</td>
   			<td>${game.getExeFile()}</td>
   			<td>${game.getDirName()}</td>
   			<td>${game.getVerFile()}</td>
   			<td>${game.getVerFileFmt()}</td>   			   			   			   			
   			<td>${game.getUptDt().toString()}</td>   			
   			<td>${game.getCrtDt().toString()}</td>
   			<td>
				<a class="btn btn-default" href="/v2/admin/game/update?gsn=${game.getGsn()}" role="button">수정</a>
   			</td>
   		</tr>
	</c:forEach>
	</table>
	<br>
	<div class="form-group">
		<div class="col-md-2"></div>
		<div class="col-md-2"></div>		    
		<div class="col-md-2"><a class="btn btn-default btn-block" href="/v2/admin/game/add" role="button">등록</a></div>
		<div class="col-md-2"><button type="button" class="btn btn-default btn-block" id="removeGame">삭제</button></div>
	</div>
	
	</div><!-- container -->	
  </body>
</html>