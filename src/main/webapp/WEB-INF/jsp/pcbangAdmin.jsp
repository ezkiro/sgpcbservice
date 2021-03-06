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
		
		//select all checkboxes
		$("#select_all").change(function(){  //"select all" change 
		    $("input:checkbox").prop('checked', $(this).prop("checked")); //change all ".checkbox" checked status
		});

		//".checkbox" change 
		$("input:checkbox").change(function(){
		    //uncheck "select all", if one of the listed checkbox item is unchecked
		    if(false == $(this).prop("checked")){ //if this item is unchecked
		        $("#select_all").prop('checked', false); //change "select all" checked status to false
		    }
		    //check "select all" if all checkbox items are checked
		    if ($("input:checkbox:checked").length == $("input:checkbox").length ){
		        $("#select_all").prop('checked', true);
		    }
		});		
		
		$("#removePcbang").click(function(){
			
			var targetPcbangs = new Array();
			
			$(":checkbox:checked").not("#select_all").each(function(){
			    targetPcbangs.push($(this).val());
			});
			
			if (targetPcbangs.length == 0) {
				return;
			}
			
			if (!confirm(targetPcbangs + ' Pcbang 정보를 삭제 하시겠습니까?')) {
				return;
			}
			
		    $.post("/api/member/pcbang/remove",
		    		{
		    			pcbid_list: targetPcbangs,
		    			access_token: getCookie("access_token")
		    		},
		    		function(data, status){
		    			if(data) {
		    				alert('pcbang 정보 삭제에 성공했습니다.');		    				
		    				location.href = '/admin/pcbang';
		    			} else {
		    				alert('pcbang 삭제에 실패했습니다. 다시 시도 하세요.');
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
		  <li role="presentation" class="active"><a href="#">가맹점관리</a></li>
		  <li role="presentation"><a href="/admin/game">Game관리</a></li>
		  <li role="presentation"><a href="/admin/installpath">InstallPath관리</a></li>		  
		  <li role="presentation"><a href="/logout">로그아웃</a></li>		    
		</ul>
  	</div>
  	<hr>
  	
	<form class="form-horizontal" action="/admin/pcbang" method="post">
		<input type="hidden" name="search_key" value="" />
		<input type="hidden" name="search_value" value="" />
		
  	<div class="form-group bg-info">
		<div class="col-md-1 text-right">
			<label for="lbID">조회조건</label>
		</div>
		<div class="col-md-2">		
			<select class="form-control" id ="inputSerachKey">
			  <option value="all">전체</option>	
			  <option value="agentName">관리업체1</option>
			  <option value="companyCode">관리업체2</option>			
			  <option value="companyName">상호</option>			  
			  <option value="ipRange">IP</option>
			  <option value="program">프로그램명</option>
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
	
  	<div class="form-group bg-info">
		<div class="col-md-2">
			<label for="lbTotalAgent">총 가맹점 수 :${pcbangCnt}개 </label>
		</div>
	</div>
	</div><!-- container -->	
	
	<table class="table table-bordered table-hover">
		<tr class="info">
			<td><input type="checkbox" id="select_all"/>All</td>
			<td>PSN</td>
			<td>상호</td>
			<td>주소</td>
			<td>IP대역</td>
			<td>subnet mask</td>
			<td>관리업체1</td>
			<td>관리업체2</td>			
			<td>프로그램명</td>
			<td>상태</td>			
			<td>등록일</td>
			<td>관리</td>
		</tr>
	<c:forEach var="pcbang" items="${pcbangList}">
		<tr>
   			<td><input type="checkbox" value="${pcbang.getPcbId()}">${pcbang.getPcbId()}</td>
   			<td>${pcbang.getCeo()}</td>   			
   			<td>${pcbang.getCompanyName()}</td>    			    			    			
   			<td>${pcbang.getAddress()}</td>
   			<td>${pcbang.getIpStart()} - ${pcbang.getIpEnd()}</td>    			    			    			
   			<td>${pcbang.getSubmask()}</td>
   			<td>${pcbang.getAgent().getCompanyName()}</td>
   			<td>${pcbang.getCompanyCode()}</td>   			    			    			    			
   			<td>${pcbang.getProgram()}</td>
   			<td>${pcbang.getStatus()}</td>   			  			
   			<td>${pcbang.getCrtDt().toString().substring(0,10)}</td>
   			<td>
				<a class="btn btn-default" href="/admin/pcbang/update?pcb_id=${pcbang.getPcbId()}" role="button">수정</a>
   			</td>
   		</tr>
	</c:forEach>
	</table>
	<br>
	<div class="form-group">    
		<div class="col-md-2"></div>
		<div class="col-md-2"><a class="btn btn-default btn-block" href="/admin/pcbang/upload" role="button">일괄등록</a></div>		
		<div class="col-md-2"><a class="btn btn-default btn-block" href="/admin/pcbang/add" role="button">등록</a></div>
		<div class="col-md-2"><button type="button" class="btn btn-default btn-block" id="removePcbang">삭제</button></div>		
	</div>
	<br>	
  </body>
</html>