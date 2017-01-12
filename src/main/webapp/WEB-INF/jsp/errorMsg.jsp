<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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
	  <h2>${message}</h2>
	  <p><a class="btn btn-primary btn-lg" href="/login" role="button">goto login</a></p>
	</div>
  </body>
</html>