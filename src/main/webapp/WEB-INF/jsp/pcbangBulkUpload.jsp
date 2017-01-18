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
  
<div class="container bg-info">  
	<div class="row text-center">
		<h3>PC Bang 정보 대량 등록</h3>
	</div>
	
<form class="form-horizontal" method="POST" enctype="multipart/form-data" action="/admin/pcbang/upload">

	<div class="row">
		<div class="form-group">
			<div class="col-sm-2 text-right"><label for="lbFileFormat">파일포맷</label></div>
			<div class="col-sm-8"><label for="lbExample">agent id(관리업체1),대표자,상호,주소,start ip,end ip,submask,관리업체2,프로그램</label></div>
		</div>
	</div>

	<div class="row">
		<div class="form-group">    
			<div class="col-sm-2 text-right"><label for="lbCeo">업로드파일</label></div>
			<div class="col-sm-4"><input type="file" class="form-control" name="file"></div>
			<div class="col-sm-6"></div>
		</div>
	</div>
   
    <div class="row">
		<div class="form-group">
		<div class="col-sm-2"></div>
 		<div class="col-sm-2"><button type="submit" class="btn btn-lg btn-primary btn-block" id="submitPcbang">Upload</button></div>
 		<div class="col-sm-2"><a class="btn btn-lg btn-default btn-block" href="/admin/pcbang" role="button">취소</a></div>
 		</div>
    </div>
	
</form>
</div>

  </body>
</html>