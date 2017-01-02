<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ko">
  <head>
	<jsp:include page="common.jsp" flush="true"/>
	
  <script>
	$(document).ready(function(){
		alert(document.cookie);
	});
	
</script>
	
	
  </head>
  <body>
	<div class="jumbotron text-center">
	  <h1>Hello, world!</h1>
	  <p><a class="btn btn-primary btn-lg" href="#" role="button">Learn more</a></p>
	</div>
  </body>
</html>