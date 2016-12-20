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
		  <li role="presentation"><a href="/admin/agent">업체관리</a></li>
		  <li role="presentation" class="active"><a href="#">설치/패치관리</a></li>
		  <li role="presentation"><a href="/admin/pcbang">가맹점관리</a></li>
		</ul>
  	</div>
  	<hr>

	<div class="form-group bg-info">
		<div class="col-md-1 text-right">
			<label for="lbGame">게임선택</label>
		</div>	
		<label class="checkbox-inline">
		  <input type="checkbox" id="totalGame" value="option1"> 전체선택
		</label>
		<label class="checkbox-inline">
		  <input type="checkbox" id="game1" value="option2"> 프리스타일
		</label>
		<label class="checkbox-inline">
		  <input type="checkbox" id="game2" value="option3"> 프리스타일2
		</label>
		<label class="checkbox-inline">
		  <input type="checkbox" id="game3" value="option3"> 프리스타일풋볼Z
		</label>
		<label class="checkbox-inline">
		  <input type="checkbox" id="game4" value="option3"> 크로스파이어
		</label>
		<label class="checkbox-inline">
		  <input type="checkbox" id="game4" value="option3"> 소울워커
		</label>
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
			  <option>서버IP</option>
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
				<td>서버IP</td>
				<td>관리업체</td>
				<td>관리IP수</td>
				<td>지급대상(O/X)</td>
				<td>게임1</td>
				<td>게임2</td>
				<td>게임3</td>
				<td>게임4</td>
				<td>게임5</td>																																																		
			</tr>
		<c:forEach var="agent" items="${gamePatchList}">
			<tr>
    			<td><input type="checkbox" value=""></td>
    			<td></td>
    			<td></td>
    			<td></td>    			    			    			
    			<td></td>
    			<td></td>
    			<td></td>
    			<td></td>    			    			    			
    			<td></td>
    			<td></td>
    			<td></td>
    			<td></td>    			    			    			
    			<td></td>
    			<td></td>
    		</tr>
		</c:forEach>
		</table>
		
  </body>
</html>