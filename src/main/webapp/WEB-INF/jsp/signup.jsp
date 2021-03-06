<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ko">
  <head>
	<jsp:include page="common.jsp" flush="true"/>
	 
  <script>
	$(document).ready(function(){
		
		var checkValidInput = function() {								
			//id 체크
			if ($("#inputID").val().length === 0 ) {
				alert("ID를 입력해 주세요.");
				return false;				
			}
			
			//email형식 체크
			var regEmail = /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
			
			if (!regEmail.test($("#inputID").val())) {
				alert("ID가  email 형식이 아닙니다. email 형식으로 입력해 주세요.");
				return false;				
			}
			
			//password 체크
			if ($("#inputPassword1").val().length < 8 ) {
				alert("패스워드는 최소 8자 이상이어야 합니다. 다시 입력해주세요.");
				return false;				
			}			
			
			if ($("#inputPassword1").val() !== $("#inputPassword2").val()) {
				alert("패스워드와 패스워드 확인 이 일치하지 않습니다. 다시 입력해주세요.");
				return false;
			}

			return true;
		}
		
		var duplicateCheck = function(item1, value1, title, controlObj) {
			var isExist = false;
		    $.get("/api/member/isExist",
		    		{
		    			item:item1,
		    			value:value1
		    		},
		    		function(data, status){
		    			isExist = data;
		    			if (isExist) {
		    				alert('중복된 ' + title + ' 입니다. 다시 입력해 주세요.');
		    				controlObj.focus();
		    			} else {
		    				alert('사용가능한 ' + title + ' 입니다.');
		    			}
		    		}
		    );
		    
		    return isExist;
		}
		
		//check double
		$("#checkDoubleId").click(function(){
			if ($("#inputID").val() === "") return ;
						
			duplicateCheck("id", $("#inputID").val(), 'ID', $("#inputID"));
			
		});
		
		$("#checkDoubleCompanyCode").click(function(){
			if ($("#inputPart1").val() === "") return ;
			
			var companyCode = $("#inputPart1").val() + '-' + $("#inputPart2").val() + '-' + $("#inputPart3").val();
						
			duplicateCheck("company_code", companyCode, '사업자번호', $("#inputPart1"));
			
		});

		$("#checkDoubleContact").click(function(){
			if ($("#inputContact").val() === "") return ;

			duplicateCheck("contact", $("#inputContact").val(), '연락처', $("#inputContact"));
			
		});

		$("#checkDoubleEmail").click(function(){
			if ($("#inputEmail").val() === "") return ;

			duplicateCheck("email", $("#inputEmail").val(), 'email', $("#inputEmail"));			
			
		});
		
		//bank
		$("#inputBank").change(function(){			
//			alert($("#inputBank option:selected").val());
		});

		//bank
		$(":radio").click(function(){			
			//alert($(':radio[name="eulaAgree"]:checked').val());
		});		
		
		//submit
		$("#submitSignUp").click(function(){
			
			if (!checkValidInput()) return;
						
			var bankAccount;
			//make bank account
			
			if ($("#inputBankAccount").val().length > 0) {
				if ($("#inputBank option:selected").val() !== '직접입력') {
					bankAccount = $("#inputBank option:selected").val() + ' ' + $("#inputBankAccount").val();
				} else {
					bankAccount = $("#inputBankAccount").val();
				}				
			}
			
			var companyCode;
			
			if ($("#inputPart1").val().length > 0) {
				
				companyCode = $("#inputPart1").val() + '-' + $("#inputPart2").val() + '-' + $("#inputPart3").val();
			}

			var signUpUrl = "/api/member/signUp";
						
		    $.post(signUpUrl,
		    		{
		    			id: $("#inputID").val(),
		    			password: $("#inputPassword1").val(),
		    			company_code: companyCode,
		    			company_name:$("#inputCompany").val(),
		    			ceo: $("#inputCEO").val(),
		    			contact_num: $("#inputContact").val(),
		    			address: $("#inputAddress").val(),
		    			email: $("#inputEmail").val(),
		    			bank_account: bankAccount,
		    		},
		    		function(data, status){
		    			if(data) {
		    				location.href = '/hello';
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
 		<h2>SignUp for E-GPMS Game Patch Monitoring System</h2>
    </div>

	<form class="form-horizontal">     
    <div class="row">
		<div class="form-group">    
		<div class="col-md-2 text-right">
			<label for="lbID">ID</label>
		</div>
 		<div class="col-md-4">  		
			<input type="email" class="form-control" id="inputID" placeholder="Email을 입력하세요">
 		</div>
 		<div class="col-md-6"><button type="button" class="btn btn-warning" id="checkDoubleId">중복확인</button></div>		            
 		</div>
    </div>
    <div class="row">
		<div class="form-group">    
		<div class="col-md-2 text-right">
			<label for="lbPassword">패스워드</label>
		</div>
 		<div class="col-md-4">
			<input type="password" class="form-control" id="inputPassword1" placeholder="암호">
 		</div>
 		<div class="col-md-6"></div>
 		</div>
    </div>
    <div class="row">
		<div class="form-group">    
		<div class="col-md-2 text-right">
			<label for="lbPassword">패스워드 확인</label>
		</div>
 		<div class="col-md-4">
			<input type="password" class="form-control" id="inputPassword2" placeholder="암호확인">
 		</div>
 		<div class="col-md-6"></div>
 		</div>
    </div>
    <div class="row">
 		<div class="form-group">
		<div class="col-md-2 text-right">
			<label for="InputCompanyNumber">사업자번호</label>
		</div>
		
 		<div class="col-md-2"><input type="text" class="form-control" id="inputPart1" placeholder=""></div>
 		<div class="col-md-2"><input type="text" class="form-control" id="inputPart2" placeholder=""></div>
 		<div class="col-md-3"><input type="text" class="form-control" id="inputPart3" placeholder=""></div>
 		<div class="col-md-3"><button type="button" class="btn btn-warning" id="checkDoubleCompanyCode">중복확인</button></div>
 		</div>
    </div>
    
    <div class="row">
		<div class="form-group">    
		<div class="col-md-2 text-right"><label for="lbCompany">상호</label></div>
 		<div class="col-md-4">
			<input type="text" class="form-control" id="inputCompany" placeholder="">
 		</div>
 		<div class="col-md-6"></div>
 		</div>
    </div>

    <div class="row">
		<div class="form-group">    
		<div class="col-md-2 text-right"><label for="lbCEO">대표자</label></div>
 		<div class="col-md-4">
			<input type="text" class="form-control" id="inputCEO" placeholder="">
 		</div>
 		<div class="col-md-6"></div>
 		</div>
    </div>
    
   <div class="row">
		<div class="form-group">    
		<div class="col-md-2 text-right"><label for="lbContact">연락처</label></div>
 		<div class="col-md-4">
			<input type="text" class="form-control" id="inputContact" placeholder="">
 		</div>
 		<div class="col-md-6"><button type="button" class="btn btn-warning" id="checkDoubleContact">중복확인</button></div>
 		</div>
    </div>

    <div class="row">
		<div class="form-group">    
		<div class="col-md-2 text-right"><label for="lbAddress">주소</label></div>
 		<div class="col-md-6">
			<input type="text" class="form-control" id="inputAddress" placeholder="">
 		</div>
 		<div class="col-md-4"></div>
 		</div>
    </div>

   <div class="row">
		<div class="form-group">    
		<div class="col-md-2 text-right"><label for="lbEmail">EMail</label></div>
 		<div class="col-md-4">
			<input type="text" class="form-control" id="inputEmail" placeholder="">
 		</div>
 		<div class="col-md-6"><button type="button" class="btn btn-warning" id="checkDoubleEmail">중복확인</button></div>
 		</div>
    </div>

   <div class="row">
		<div class="form-group">    
		<div class="col-md-2 text-right"><label for="lbBankAccount">입금계좌</label></div>
 		<div class="col-md-2">
			<select class="form-control" id ="inputBank">
			  <option>국민은행</option>
			  <option>기업은행</option>			  
			  <option>농협</option>			  
			  <option>신한은행</option>
			  <option>우체국</option>			  
			  <option>우리은행</option>
			  <option>KEB하나은행</option>
			  <option>한국씨티은행</option>			  
			  <option>새마을금고</option>
			  <option>직접입력</option>		  
			</select>
 		</div>
 		<div class="col-md-6"><input type="text" class="form-control" id="inputBankAccount" placeholder=""></div>
 		</div>
    </div>
        
    <div class="row">
		<div class="form-group">
		<div class="col-md-2"></div>
 		<div class="col-md-6"></div>
 		<div class="col-md-2"><button type="button" class="btn btn-lg btn-primary btn-block" id="submitSignUp">가입</button></div>
 		<div class="col-md-2"></div>
 		</div>
    </div>
	</form>
                 
   </div> <!-- /container -->    
  </body>
</html>