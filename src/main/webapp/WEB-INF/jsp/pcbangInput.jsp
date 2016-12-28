<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
	<jsp:include page="common.jsp" flush="true"/>

  <script>
	$(document).ready(function(){
		
		var updateMode = false;
		
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
		
		var setUpForUpdate = function() {
			
			var pcbId = "${pcbang.getPcbId()}";
		
			if(pcbId !== "") {
				updateMode = true;
				$("#inputPcbId").val("${pcbang.getPcbId()}");
				$("#inputCompanyCode").val("${pcbang.getCompanyCode()}");
				$("#inputCompany").val("${pcbang.getCompanyName()}");
				$("#inputAddress").val("${pcbang.getAddress()}");
				$("#inputIPStart").val("${pcbang.getIpStart()}");
				$("#inputIPEnd").val("${pcbang.getIpEnd()}");
				$("#inputSubmask").val("${pcbang.getSubmask()}");
				$("#inputProgram").val("${pcbang.getProgram()}");
				$("#inputAgent").val("${pcbang.getAgent().getAgentId()}");
				$("#inputStatus").val("${pcbang.getStatus()}");
			}
		}
		
		//submit
		$("#submitPcbang").click(function(){
			
			if(!checkValidInput()) return;
						
			//add mode
			if(!updateMode) {
				
			    $.post("/member/pcbang/add",
			    		{
			    			company_code: $("#inputCompanyCode").val(),
			    			company_name:$("#inputCompany").val(),
			    			address: $("#inputAddress").val(),
			    			start_ip: $("#inputIPStart").val(),
			    			end_ip: $("#inputIPEnd").val(),
			    			submask: $("#inputSubmask").val(),
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
			//update mode    
			} else {
			    $.post("/member/pcbang",
			    		{
							pcb_id:$("#inputPcbId").val(),	    	
			    			company_code: $("#inputCompanyCode").val(),
			    			company_name:$("#inputCompany").val(),
			    			address: $("#inputAddress").val(),
			    			start_ip: $("#inputIPStart").val(),
			    			end_ip: $("#inputIPEnd").val(),
			    			submask: $("#inputSubmask").val(),
			    			program: $("#inputProgram").val(),
			    			agent_id: $("#inputAgent option:selected").val(),
			    			status: $("#inputStatus option:selected").val()
			    		},
			    		function(data, status){
			    			if(data) {
			    				alert("PC방 수정에 성공하였습니다.");
			    				location.href = '/admin/pcbang';
			    			}
			    		}
			    );				
			}
			
		});
		
		setUpForUpdate();
	});  
  </script> 
	
		
  </head>
  <body>
  
<div class="container bg-info">  
	<div class="row text-center">
		<h3>PC Bang 정보 입력/수정</h3>
	</div>
	
<form class="form-horizontal">
<c:set var="aPcbang" value="${pcbang}" />
<c:if test="${!empty aPcbang}">
	<div class="row">
		<div class="form-group">
			<div class="col-md-2 text-right"><label for="lbPcbId">pcbid</label></div>
			<div class="col-md-2"><input type="text" class="form-control" id="inputPcbId" placeholder="" disabled></div>
			<div class="col-md-2"></div>
			<div class="col-md-4"></div>
		</div>
   </div>
</c:if>
	<div class="row">
		<div class="form-group">
			<div class="col-md-2 text-right"><label for="lbCompanyCode">사업자번호</label></div>
			<div class="col-md-4"><input type="text" class="form-control" id="inputCompanyCode" placeholder="111-2222-33333"></div>
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
			<div class="col-md-2 text-right"><label for="lbSubmask">subnet mask</label></div>
			<div class="col-md-3"><input type="text" class="form-control" id="inputSubmask" placeholder="255.255.255.0"></div>
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

<c:if test="${!empty aPcbang}">
	<div class="row">
		<div class="form-group">
			<div class="col-md-2 text-right"><label for="lbStatus">status</label></div>
			<div class="col-md-4">
				<select class="form-control" id ="inputStatus">
					<option value="WAIT">WAIT</option>
					<option value="OK">OK</option>
				</select>						
			</div>
			<div class="col-md-2"></div>
			<div class="col-md-4"></div>
		</div>
   </div>
</c:if>

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