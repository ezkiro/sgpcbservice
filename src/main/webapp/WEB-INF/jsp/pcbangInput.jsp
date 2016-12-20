<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
	<jsp:include page="common.jsp" flush="true"/>		
  </head>
  <body>
  
<div class="container bg-info">  
	<div class="row text-center">
		<h3>PC Bang 정보 입력/수정</h3>
	</div>
	
<form class="form-horizontal">	      
	<div class="row">
		<div class="form-group">
			<div class="col-md-2 text-right"><label for="lbCompanyNumber">사업자번호</label></div>
			<div class="col-md-4"><input type="text" class="form-control" id="inputCompanyNumber" placeholder="111-2222-33333"></div>
			<div class="col-md-2"></div>
			<div class="col-md-4"></div>
		</div>
   </div>
   
	<div class="row">
		<div class="form-group">    
			<div class="col-md-2 text-right"><label for="lbCompany">상호</label></div>
			<div class="col-md-4"><input type="text" class="form-control" id="inputCompany" placeholder=""></div>
			<div class="col-md-6"></div>
		</div>
	</div>

	<div class="row">
		<div class="form-group">    
			<div class="col-md-2 text-right"><label for="lbAddress">주소</label></div>
			<div class="col-md-6"><input type="text" class="form-control" id="inputAddress" placeholder=""></div>
			<div class="col-md-4"></div>
		</div>
	</div>
   
	<div class="row">
		<div class="form-group">
			<div class="col-md-2 text-right"><label for="lbIPScope">IP대역</label></div>
			<div class="col-md-3"><input type="text" class="form-control" id="inputIPStart" placeholder=""></div>
			<div class="col-md-3"><input type="text" class="form-control" id="inputIPEnd" placeholder=""></div>
		</div>
	</div>		    

	<div class="row">
		<div class="form-group">
			<div class="col-md-2 text-right"><label for="lbMasterIP">서버IP</label></div>
			<div class="col-md-3"><input type="text" class="form-control" id="inputMasterIP" placeholder=""></div>
		</div>
	</div>

	<div class="row">
		<div class="form-group">
			<div class="col-md-2 text-right"><label for="lbAgent">관리업체</label></div>
			<div class="col-md-3">
				<select class="form-control" id ="inputAgent">
				<c:forEach var="agent" items="${agentList}">
					<option value="${agent.getAgentId()}">${agent.getCompanyName()}</option>
				</c:forEach>	
				</select>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="form-group">
			<div class="col-md-2 text-right"><label for="lbProgram">프로그램</label></div>
			<div class="col-md-3"><input type="text" class="form-control" id="inputProgram" placeholder=""></div>
		</div>
	</div>

    <div class="row">
		<div class="form-group">
		<div class="col-md-2"></div>
 		<div class="col-md-4"></div>
 		<div class="col-md-2"><button type="button" class="btn btn-lg btn-primary btn-block" id="submitPcbang">저장</button></div>
 		<div class="col-md-2"><a class="btn btn-lg btn-default btn-block" href="/admin/pcbang" role="button">취소</a></div>
 		</div>
    </div>
	
</form>
</div>

  </body>
</html>