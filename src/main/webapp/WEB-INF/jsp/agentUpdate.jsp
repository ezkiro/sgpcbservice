<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ko">
  <head>
	<jsp:include page="common.jsp" flush="true"/>
	 
  <script>
	$(document).ready(function(){
	
		//setting existing data		
		$("#inputID").val("${agent.getAgentId()}");
		$("#inputCompanyCode").val("${agent.getCompanyCode()}");
		$("#inputCompany").val("${agent.getCompanyName()}");
		$("#inputAddress").val("${agent.getAddress()}");
		$("#inputCEO").val("${agent.getCeo()}");
		$("#inputContact").val("${agent.getContactNum()}");
		$("#inputEmail").val("${agent.getEmail()}");
		$("#inputBankAccount").val("${agent.getBankAccount()}");
		$("#inputPermission").val("${agent.getAccount().getPermission()}");
		$("#inputStatus").val("${agent.getStatus()}");
		
		var checkValidInput = function() {								
			//id 체크
			if($("#inputID").val().length === 0 ) {
				alert("ID를 입력해 주세요.");
				return false;				
			}
			
			return true;
		}
				
		//submit
		$("#saveAgentInfo").click(function(){
			
			if(!checkValidInput()) return;
						
			var bankAccount;
			//make bank account
			if($("#inputBank option:selected").val() !== '직접입력'){
				bankAccount = $("#inputBank option:selected").val() + ' ' + $("#inputBankAccount").val();
			} else {
				bankAccount = $("#inputBankAccount").val();
			}
			
		    $.post("/member/agent",
		    		{
		    			agent_id: $("#inputID").val(),
		    			status: $("#inputStatus option:selected").val(),
		    			permission: $("#inputPermission option:selected").val(),
		    			company_code: $("#inputCompanyCode").val(),
		    			company_name:$("#inputCompany").val(),
		    			ceo: $("#inputCEO").val(),
		    			contact_num: $("#inputContact").val(),
		    			address: $("#inputAddress").val(),
		    			email: $("#inputEmail").val(),
		    			bank_account: bankAccount,
		    		},
		    		function(data, status){
		    			if(data) {
		    				location.href = '/admin/agent';
		    			} else {
		    				alert('업체 정보 수정에 실패했습니다. 다시 시도 하세요.');
		    			}
		    		}
		    );			
		});
		
	});  
  </script> 
    
  </head>  
  <body>	
        
   <div class="container bg-info">   
    <div class="row text-center">
 		<h3>업체 정보 수정</h3>
    </div>

	<form class="form-horizontal">     
    <div class="row">
		<div class="form-group">    
			<div class="col-md-2 text-right"><label for="lbID">Agent ID</label></div>
	 		<div class="col-md-4"><input type="text" class="form-control" id="inputID" placeholder="" disabled></div>
 		</div>
    </div>
    
	<div class="row">
		<div class="form-group">
			<div class="col-md-2 text-right"><label for="lbStatus">status</label></div>
			<div class="col-md-4">
				<select class="form-control" id ="inputStatus">
					<option value="WAIT">WAIT</option>
					<option value="OK">OK</option>
				</select>						
			</div>
		</div>
   </div>
   
	<div class="row">
		<div class="form-group">
			<div class="col-md-2 text-right"><label for="lbPermission">Permission</label></div>
			<div class="col-md-4">
				<select class="form-control" id ="inputPermission">
					<option value="ADMIN">ADMIN</option>
					<option value="PARTNER">PARTNER</option>
					<option value="AGENT">AGENT</option>					
				</select>						
			</div>
		</div>
   </div>       
        
    <div class="row">
 		<div class="form-group">
			<div class="col-md-2 text-right"><label for="InputCompanyNumber">사업자번호</label></div>
	 		<div class="col-md-4"><input type="text" class="form-control" id="inputCompanyCode" placeholder=""></div>
 		</div>
    </div>
    
    <div class="row">
		<div class="form-group">    
			<div class="col-md-2 text-right"><label for="lbCompany">상호</label></div>
	 		<div class="col-md-4"><input type="text" class="form-control" id="inputCompany" placeholder=""></div>
 		</div>
    </div>

    <div class="row">
		<div class="form-group">    
			<div class="col-md-2 text-right"><label for="lbCEO">대표자</label></div>
	 		<div class="col-md-4"><input type="text" class="form-control" id="inputCEO" placeholder=""></div>
 		</div>
    </div>
    
   <div class="row">
		<div class="form-group">    
			<div class="col-md-2 text-right"><label for="lbContact">연락처</label></div>
	 		<div class="col-md-4"><input type="text" class="form-control" id="inputContact" placeholder=""></div>
 		</div>
    </div>

    <div class="row">
		<div class="form-group">    
			<div class="col-md-2 text-right"><label for="lbAddress">주소</label></div>
	 		<div class="col-md-6"><input type="text" class="form-control" id="inputAddress" placeholder=""></div>
 		</div>
    </div>

   <div class="row">
		<div class="form-group">    
			<div class="col-md-2 text-right"><label for="lbEmail">EMail</label></div>
	 		<div class="col-md-4"><input type="email" class="form-control" id="inputEmail" placeholder=""></div>
 		</div>
    </div>

   <div class="row">
		<div class="form-group">    
			<div class="col-md-2 text-right"><label for="lbBankAccount">입금계좌</label></div>
	 		<div class="col-md-2">
				<select class="form-control" id ="inputBank">
				  <option>직접입력</option>				
				  <option>국민은행</option>
				  <option>기업은행</option>			  
				  <option>농협</option>			  
				  <option>신한은행</option>
				  <option>우체국</option>			  
				  <option>우리은행</option>
				  <option>KEB하나은행</option>
				  <option>한국씨티은행</option>			  
				  <option>새마을금고</option>		  
				</select>
	 		</div>
	 		<div class="col-md-4"><input type="text" class="form-control" id="inputBankAccount" placeholder=""></div>
 		</div>
    </div>
    
    <div class="row">
		<div class="form-group">
			<div class="col-md-2"></div>
	 		<div class="col-md-2"><button type="button" class="btn btn-lg btn-primary btn-block" id="saveAgentInfo">수정</button></div>
	 		<div class="col-md-2"><a class="btn btn-lg btn-default btn-block" href="/admin/agent" role="button">취소</a></div>
 		</div>
    </div>
    
	</form>
                 
   </div> <!-- /container -->    
  </body>
</html>