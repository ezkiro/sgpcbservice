<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
	<jsp:include page="common.jsp" flush="true"/>
  <script>
	$(document).ready(function(){
		
		$("#inputSerachKey").click(function(){
			if ($(this).val() == "all") {
				$("#inputSerachValue").val("");			
			}			
		});
		
		$("form").submit(function(event){
			$("input[name=search_key]").val($("#inputSerachKey option:selected").val());
			
			$("input[name=search_value]").val($("#inputSerachValue").val());
		});
		
		var searchKey = "${search_key}";
		var searchValue = "${search_value}";
		
		if (searchKey.length > 0) {
			$("#inputSerachKey").val(searchKey);
		}
		
		if (searchValue.length > 0) {
			$("#inputSerachValue").val(searchValue);
		}
		
		$("#excelDownload").click(function(){
			if (searchKey.length > 0 && searchValue.length > 0) {
				location.href='/v2/admin/agent/excel?search_key=' + searchKey + '&search_value=' + searchValue;
			} else {
				location.href='/v2/admin/agent/excel';
			}
		});
		
		$(":button").click(function(){
			
			if ($(this).attr("name") != "removeAgent") {
				return;
			}
			
			if (!confirm($(this).val() + '번 Agent 정보를 삭제 하시겠습니까?\n 로그인 계정 정보도 같이 삭제가 됩니다.')) {
				return;
			}
			
		    $.post("/v2/api/member/agent/unregister",
		    		{
		    			agent_id:$(this).val(),
		    			access_token: getCookie("access_token")
		    		},
		    		function(data, status){
		    			if(data) {
		    				alert('업체 정보 삭제에 성공했습니다.');		    				
		    				location.href = '/v2/admin/agent';
		    			} else {
		    				alert('업체 정보 삭제에 실패했습니다. 다시 시도 하세요.');
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
		  <li role="presentation" class="active"><a href="#">업체관리</a></li>
		  <li role="presentation"><a href="/v2/admin/gamepatch">설치/패치관리</a></li>
		  <li role="presentation"><a href="/v2/admin/pcbang">가맹점관리</a></li>
		  <li role="presentation"><a href="/v2/admin/game">Game관리</a></li>
		  <li role="presentation"><a href="/v2/admin/installpath">InstallPath관리</a></li>
		  <li role="presentation"><a href="/v2/logout">로그아웃</a></li>
		</ul>
  	</div>
  	<hr>
  	
	<form class="form-horizontal" action="admin/agent" method="post">
		<input type="hidden" name="search_key" value="" />
		<input type="hidden" name="search_value" value="" />

  	<div class="form-group bg-info">
		<div class="col-md-1 text-right">
			<label for="lbID">조회조건</label>
		</div>
		<div class="col-md-2">		
			<select class="form-control" id ="inputSerachKey">
			  <option value="all">전체</option>
			  <option value="account">아이디</option>			  
			  <option value="ceo">대표자</option>
			  <option value="companyName">상호</option>			  		  
			  <option value="status">상태</option>			  
			</select>
		</div>
		<div class="col-md-4">	  			
			<input type="text" class="form-control" id="inputSerachValue" placeholder="">
		</div>
		<div class="col-md-2">		
			<button type="submit" class="btn btn-primary btn-block">조회</button>
		</div>
	</div>	  
	</form>
	
  	<div class="form-group">
		<div class="col-md-2 bg-warning">
			<label for="lbTotalAgent">총 가맹점 수 :${agentList.size()} 개 </label>
		</div>
		<div class="col-md-2 bg-warning">
			<label for="lbOK">정상 :${agentOKCnt} 개 </label>
		</div>
		<div class="col-md-2 bg-warning">
			<label for="lbWait">미승인 :${agentWaitCnt} 개 </label>
		</div>		
	</div>
	</div><!-- container -->	
	
		<table class="table table-bordered table-hover">
			<tr class="info">
				<td>구분</td>
				<td>사업자번호</td>
				<td>아이디</td>
				<td>상호</td>
				<td>대표자</td>
				<td>연락처</td>
				<td>주소</td>
				<td>email</td>
				<td>계좌번호</td>
				<td>관리매장수</td>
				<td>등록일</td>
				<td>상태</td>
				<td>권한</td>
				<td>관리</td>
				<td>삭제</td>																																																	
			</tr>
		<c:forEach var="agent" items="${agentList}">
			<tr>
    			<td>${agent.getAgentId()}</td>
    			<td>${agent.getCompanyCode()}</td>
    			<td>${agent.getAccount().getId()}</td>
    			<td>${agent.getCompanyName()}</td>    			    			    			
    			<td>${agent.getCeo()}</td>
    			<td>${agent.getContactNum()}</td>
    			<td>${agent.getAddress()}</td>
    			<td>${agent.getEmail()}</td>    			    			    			
    			<td>${agent.getBankAccount()}</td>
    			<td>${agent.getPcbangs().size()}</td>
    			<td>${agent.getCrtDt().toString().substring(0,10)}</td>
    			<td>${agent.getStatus()}</td>    			    			    			
    			<td>${agent.getAccount().getPermission()}</td>
    			<td><a class="btn btn-default" href="/v2/admin/agent/update?agent_id=${agent.getAgentId()}" role="button">수정</a></td>
    			<td><button type="button" class="btn btn-danger btn-block" name="removeAgent" value="${agent.getAgentId()}">삭제</button></td>
    		</tr>
		</c:forEach>
		</table>
	<br>
	
	<div class="form-group">
		<div class="col-md-2 col-md-offset-6"><button type="button" class="btn btn-default btn-block" id="excelDownload">Excel download</button></div>
	</div>
		
  </body>
</html>