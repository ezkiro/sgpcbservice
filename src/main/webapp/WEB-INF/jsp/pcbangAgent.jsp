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
	});  
  </script> 
	
		
  </head>
  <body>
	<div class="container"> 
  	<div class="row">
		<ul class="nav nav-pills">
		  <li role="presentation"><a href="/member/gamepatch">설치/패치관리</a></li>
		  <li role="presentation" class="active"><a href="#">가맹점관리</a></li>
		  <li role="presentation"><a href="/logout">로그아웃</a></li>		  
		</ul>
  	</div>
  	<hr>

	<form class="form-horizontal" action="/member/pcbang" method="post">
		<input type="hidden" name="search_key" value="" />
		<input type="hidden" name="search_value" value="" />

  	<div class="form-group bg-info">
		<div class="col-md-1 text-right">
			<label for="lbID">조회조건</label>
		</div>
		<div class="col-md-2">
			<select class="form-control" id ="inputSerachKey">
			  <option value="all">전체</option>
			  <option value="companyCode">관리업체</option>
			  <option value="companyName">상호</option>
			  <option value="ipRange">IP</option>
			  <option value="program">프로그램명</option>
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
			<td>구분</td>
			<td>PSN</td>
			<td>상호</td>
			<td>주소</td>
			<td>IP대역</td>
			<td>subnet mask</td>
			<td>관리업체</td>
			<td>프로그램</td>						
			<td>상태</td>			
			<td>등록일</td>
		</tr>
	<c:forEach var="pcbang" items="${pcbangList}">
		<tr>
   			<td><input type="checkbox" value="${pcbang.getPcbId()}">${pcbang.getPcbId()}</td>
   			<td>${pcbang.getCeo()}</td>
   			<td>${pcbang.getCompanyName()}</td>    			    			    			
   			<td>${pcbang.getAddress()}</td>
   			<td>${pcbang.getIpStart()} - ${pcbang.getIpEnd()}</td>    			    			    			
   			<td>${pcbang.getSubmask()}</td>
   			<td>${pcbang.getCompanyCode()}</td>
   			<td>${pcbang.getProgram()}</td>   			   			
   			<td>${pcbang.getStatus()}</td>   			  			
   			<td>${pcbang.getCrtDt().toString().substring(0,10)}</td>
   		</tr>
	</c:forEach>
	</table>
	<br>
	<div class="form-group">    
		<div class="col-md-4"></div>
		<div class="col-md-2"><a class="btn btn-default btn-block" href="/member/pcbang/add" role="button">등록</a></div>
		<div class="col-md-2"><button type="button" class="btn btn-default btn-block" id="removePcbang">삭제</button></div>
	</div>
	<br>
  </body>
</html>