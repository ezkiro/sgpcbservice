<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
	<jsp:include page="common.jsp" flush="true"/>
	
  <script>
	$(document).ready(function(){
		
		var checkValidInput = function() {
			
			if($("#inputCompanyCode").val().length === 0 ) {
				alert("사업자 번호를 입력해 주세요.");
				return false;				
			}			

			if($("#inputCompany").val().length === 0 ) {
				alert("상호 를 입력해 주세요.");
				return false;				
			}			
			
			return true;
		}		
		
		//submit
		$("#submitPcbang").click(function(){
			
			if(!checkValidInput()) return;
									
		    $.post("/member/pcbang",
		    		{
		    			company_code: $("#inputCompanyCode").val(),
		    			company_name:$("#inputCompany").val(),
		    			address: $("#inputAddress").val(),
		    			start_ip: $("#inputIPStart").val(),
		    			end_ip: $("#inputIPEnd").val(),
		    			master_ip: $("#inputMasterIP").val(),
		    			program: $("#inputProgram").val(),
		    			agent_id: $("#inputAgent option:selected").val(),		    			
		    		},
		    		function(data, status){
		    			if(data) {
		    				alert("PC방 등록에 성공하였습니다.");
		    				location.href = '/admin/pcbang';
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
		</ul>
  	</div>
  	<hr>
  	
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
			  <option>프로그램명</option>
			  <option>상태</option>		  			  			  			  
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
	
  	<div class="form-group bg-info">
		<div class="col-md-2">
			<label for="lbTotalAgent">총 가명점 수 :${pcbangCnt}개 </label>
		</div>
	</div>
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
			<td>프로그램명</td>
			<td>상태</td>			
			<td>등록일</td>
			<td>관리</td>
		</tr>
	<c:forEach var="pcbang" items="${pcbangList}">
		<tr>
   			<td><input type="checkbox" value="${pcbang.getPcbId()}">${pcbang.getPcbId()}</td>
   			<td>${pcbang.getCompanyCode()}</td>
   			<td>${pcbang.getCompanyName()}</td>    			    			    			
   			<td>${pcbang.getAddress()}</td>
   			<td>${pcbang.getIpStart()} - ${pcbang.getIpEnd()}</td>    			    			    			
   			<td>${pcbang.getMasterIp()}</td>
   			<td>${pcbang.getAgent().getCompanyName()}</td>    			    			    			
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
		<div class="col-md-2"><a class="btn btn-default btn-block" href="#" role="button">일괄등록</a></div>		
		<div class="col-md-2"><a class="btn btn-default btn-block" href="/admin/pcbang/add" role="button">등록</a></div>
		<div class="col-md-2"><a class="btn btn-default btn-block" href="/admin/pcbang/remove" role="button">삭제</a></div>		
	</div>

	<div class="modal fade" id="myModal">
	  <div class="modal-dialog modal-lg">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title">Modal title</h4>
	      </div>
	      <div class="modal-body">

	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary">Save changes</button>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
		
  </body>
</html>