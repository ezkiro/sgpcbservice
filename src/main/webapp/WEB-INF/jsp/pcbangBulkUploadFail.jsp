<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
  <head>
	<jsp:include page="common.jsp" flush="true"/>
	
  <script>
	$(document).ready(function(){
		//alert(document.cookie);
	});
	
</script>
	
	
  </head>
  <body>
	<div class="jumbotron text-center">
	  <h2>가맹점 일괄 등록에 실패하였습니다. 다음 데이터를 확인해 주세요.</h2>
	</div>
	
	<div class="container">	
	  <table class="table table-bordered table-hover">
	  	<tr>
	  		<td>순번</td>
	  		<td>invalid Data(입력 포맷(agent id(관리업체1),대표자,상호,start ip,end ip,submask,관리업체2,프로그램,주소)을 확인하세요.)</td>
	  	</tr>
		<c:forEach var="data" items="${invalidDatas}" varStatus="status">
		<tr>
			<td>${status.count}</td>		
   			<td>${data}</td>
   		</tr>
		</c:forEach>	
	  </table>
	  
	  <p><a class="btn btn-primary btn-lg" href="/v2/admin/pcbang" role="button">가맹점관리</a></p>
	</div>
	
	
  </body>
</html>