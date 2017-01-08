<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ko">
  <head>
	<jsp:include page="common.jsp" flush="true"/>

  <script>
	$(document).ready(function(){
		
		var errorMsg = '${error}';
		if(errorMsg.length > 0 ) {
			alert('Error! ' + errorMsg);
		}
		
		var checkValidInput = function() {								
			//id 체크
			if($("#inputEmail").val().length === 0 ) {
				alert("ID를 입력해 주세요.");
				return false;				
			}			
			
			//password 체크
			if($("#inputPassword").val().length === 0 ) {
				alert("패스워드를 입력해주세요.");
				return false;				
			}
			
			return true;
		}
		
		//submit
		$("form").submit(function(event){			
			if(!checkValidInput()) {
				event.preventDefault();
			}
		});		
	});
	
</script>
	      
  </head>
  <body>	
        
   <div class="container">
	<div class="jumbotron">
    <div class = "row text-center">
		<H2>E-GPMS Admin</H2>
	</div>
	
	<form class="form-horizontal" action="/login" method="post" >	     
    <div class = "row">
		<div class="form-group">
		<div class="col-md-2 text-right"><label for="lbEmail">ID</label></div>
 		<div class="col-md-6">
			<input type="text" name="id" class="form-control" id="inputEmail" placeholder="ID를 입력하세요">
		</div>
 		</div>
    </div>
    <div class = "row">
		<div class="form-group">    
		<div class="col-md-2 text-right"><label for="lbPassword">PASSWORD</label></div>
 		<div class="col-md-6">
			<input type="password" name="password", class="form-control" id="inputPassword" placeholder="패스워드를 입력하세요">
		</div>
 		</div>
    </div>
    
    <div class = "row">
		<div class="form-group">    
		<div class="col-md-2"></div>
		<div class="col-md-2"><a class="btn btn-default btn-block" href="/signup" role="button">회원가입</a></div>
		<div class="col-md-2"><a class="btn btn-default btn-block" href="/findIDOrPassword" role="button">ID/패스워드찾기</a></div>		
 		<div class="col-md-2"><button type="submit" class="btn btn-lg btn-primary btn-block">로그인</button></div>
 		</div>
    </div>
      
	</form>
	
    </div>

    <div class = "row text-center">    
    	<h4>CopyrightⓒE-GATE All Rights Reserved</h4>
    	<h4>관리자: 홍세호 episode@e-gate.co.kr</h4>  
    </div>             
   </div> <!-- /container -->
       
  </body>
</html>