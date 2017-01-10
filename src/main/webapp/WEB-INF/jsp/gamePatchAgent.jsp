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
		  <li role="presentation" class="active"><a href="#">설치/패치관리</a></li>
		  <li role="presentation"><a href="/member/pcbang">가맹점관리</a></li>
		  <li role="presentation"><a href="/logout">로그아웃</a></li>		  
		</ul>
  	</div>
  	<hr>

	<div class="form-group bg-info">
		<div class="col-md-1 text-right">
			<label for="lbGame">게임선택</label>
		</div>	
		<label class="checkbox-inline">
		  <input type="checkbox" id="allGameOpt" value="all"> 전체선택
		</label>
	<c:forEach var="game" items="${gameList}">
		<label class="checkbox-inline">
		  <input type="checkbox" id="gameOpt" value="${game.getGsn()}"> ${game.getName()}
		</label>
	</c:forEach>																																																										
	</div>
  	
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
			  <option>관리업체</option>
			  <option>패치유무</option>
			  <option>지급대상</option>			  			  			  
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
	</div><!-- container -->	
	
		<table class="table table-bordered table-hover">
			<tr class="info">
				<td>구분</td>
				<td>사업자번호</td>
				<td>상호</td>
				<td>주소</td>				
				<td>IP대역</td>
				<td>submask</td>
				<td>관리업체</td>
				<td>관리IP수</td>
				<td>지급대상(Y/N)</td>
			<c:forEach var="game" items="${gameList}">
				<td>${game.getName()}</td>
			</c:forEach>																																																						
			</tr>
		<c:forEach var="pcbGamePatchResult" items="${pcbGamePatchResultList}">
			<tr>
    			<td>${pcbGamePatchResult.getPcbang().getPcbId()}</td>
    			<td>${pcbGamePatchResult.getPcbang().getCompanyCode()}</td>
    			<td>${pcbGamePatchResult.getPcbang().getCompanyName()}</td>
    			<td>${pcbGamePatchResult.getPcbang().getAddress()}</td>    			    			    			
    			<td>${pcbGamePatchResult.getPcbang().getIpStart()} - ${pcbang.getIpEnd()}</td>
    			<td>${pcbGamePatchResult.getPcbang().getSubmask()}</td>
    			<td>${pcbGamePatchResult.getPcbang().getAgent().getCompanyName()}</td>
    			<td>${pcbGamePatchResult.getPcbang().getIpTotal()}</td>    			    			    			
    			<td>${pcbGamePatchResult.getIsPaymentPcbang().toString()}</td>
			<c:forEach var="game" items="${gameList}">
				<td>${pcbGamePatchResult.getGamePatchMap().get(game.getGsn())}</td>
			</c:forEach>																																																						
    		</tr>
		</c:forEach>
		</table>
		
  </body>
</html>