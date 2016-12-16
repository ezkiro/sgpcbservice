<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 위 3개의 메타 태그는 *반드시* head 태그의 처음에 와야합니다; 어떤 다른 콘텐츠들은 반드시 이 태그들 *다음에* 와야 합니다 -->
    <title>Login for E-GPMS</title>

    <!-- 부트스트랩 -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- IE8 에서 HTML5 요소와 미디어 쿼리를 위한 HTML5 shim 와 Respond.js -->
    <!-- WARNING: Respond.js 는 당신이 file:// 을 통해 페이지를 볼 때는 동작하지 않습니다. -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    
     <!-- jQuery (부트스트랩의 자바스크립트 플러그인을 위해 필요합니다) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <!-- 모든 컴파일된 플러그인을 포함합니다 (아래), 원하지 않는다면 필요한 각각의 파일을 포함하세요 -->
    <script src="js/bootstrap.min.js"></script>
 
  <script>
	$(document).ready(function(){

		var dublicateCheck = function(item1, value1) {
			var isExist = false;
		    $.get("/member/isExist",
		    		{
		    			item:item1,
		    			value:value1
		    		},
		    		function(data, status){
		    			isExist = data;
		    		}
		    );
		    
		    return isExist;
		}
		
		//check double
		$("#checkDoubleId").click(function(){			
			if($("#inputID").val() === "") return ;
						
			if( dublicateCheck("id", $("#inputID").val())){
				alert("중복된 ID 입니다. 다른 ID를 입력해 주세요.");				
			} else {
				alert("사용가능한 ID 입니다.");
			}
		});
		
		$("#checkDoubleCompanyNumber").click(function(){
			alert("checkDoubleCompanyNumber!!");
		});

		$("#checkDoubleContact").click(function(){
			alert("checkDoubleContact!!");
		});

		$("#checkDoubleEmail").click(function(){
			alert("checkDoubleEmail!!");
		});
		
	});  
  </script> 
    
  </head>  
  <body>	
        
   <div class="container">   
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
			<input type="text" class="form-control" id="inputID" placeholder="ID을 입력하세요">
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
 		<div class="col-md-3"><button type="button" class="btn btn-warning" id="checkDoubleCompanyNumber">중복확인</button></div>
 		</div>
    </div>
    
    <div class="row">
		<div class="form-group">    
		<div class="col-md-2 text-right"><label for="lbCompany">상호</label></div>
 		<div class="col-md-4">
			<input type="text" class="form-control" id="InputCompany" placeholder="">
 		</div>
 		<div class="col-md-6"></div>
 		</div>
    </div>

    <div class="row">
		<div class="form-group">    
		<div class="col-md-2 text-right"><label for="lbCEO">대표자</label></div>
 		<div class="col-md-4">
			<input type="text" class="form-control" id="InputCEO" placeholder="">
 		</div>
 		<div class="col-md-6"></div>
 		</div>
    </div>
    
   <div class="row">
		<div class="form-group">    
		<div class="col-md-2 text-right"><label for="lbContact">연락처</label></div>
 		<div class="col-md-4">
			<input type="text" class="form-control" id="InputContact" placeholder="">
 		</div>
 		<div class="col-md-6"><button type="button" class="btn btn-warning" id="checkDoubleContact">중복확인</button></div>
 		</div>
    </div>

    <div class="row">
		<div class="form-group">    
		<div class="col-md-2 text-right"><label for="lbAddress">주소</label></div>
 		<div class="col-md-6">
			<input type="text" class="form-control" id="InputAddress" placeholder="">
 		</div>
 		<div class="col-md-4"></div>
 		</div>
    </div>

   <div class="row">
		<div class="form-group">    
		<div class="col-md-2 text-right"><label for="lbEmail">EMail</label></div>
 		<div class="col-md-4">
			<input type="text" class="form-control" id="InputEmail" placeholder="">
 		</div>
 		<div class="col-md-6"><button type="button" class="btn btn-warning" id="checkDoubleEmail">중복확인</button></div>
 		</div>
    </div>

   <div class="row">
		<div class="form-group">    
		<div class="col-md-2 text-right"><label for="lbBankAccount">입금계좌</label></div>
 		<div class="col-md-2">
			<select class="form-control" value="은행 ">
			  <option>국민은행</option>
			  <option>우리은행</option>
			  <option>하나은행</option>
			  <option>새마을금고</option>
			  <option>농협</option>
			  <option>직접입력</option>		  
			</select>
 		</div>
 		<div class="col-md-6"><input type="text" class="form-control" id="InputBankAccount" placeholder=""></div>
 		</div>
    </div>

   <div class="row">
		<div class="form-group">    
		<div class="col-md-2"></div>
 		<div class="col-md-8 text-center">
	 		<div class="panel panel-warning">
	  			<div class="panel-heading">약관 동의</div>
	  			<div class="panel-body">
	    			Panel content
	  			</div>
			</div> 		
 		</div>
 		<div class="col-md-2"></div>
 		</div>
    </div>
    
   <div class="row">
		<div class="form-group">    
		<div class="col-md-2"></div>
 		<div class="col-md-4"></div>
 		<div class="col-md-2">
		    <div class="radio">
		  		<label>
		    		<input type="radio" name="optionsRadios" id="optionsNo" value="false" checked>
		    		동의 안 합니다.
		  		</label>
			</div> 		
 		</div>
 		<div class="col-md-4">
		    <div class="radio">
		  		<label>
		    		<input type="radio" name="optionsRadios" id="optionsYES" value="true">
		    		동의  합니다.
		  		</label>
			</div> 		
 		</div> 		
 		</div>
    </div>
    
    <div class="row">
		<div class="form-group">
		<div class="col-md-2"></div>
 		<div class="col-md-6"></div>
 		<div class="col-md-2"><button type="submit" class="btn btn-lg btn-primary btn-block">가입</button></div>
 		<div class="col-md-2"></div>
 		</div>
    </div>
	</form>
                 
   </div> <!-- /container -->    
  </body>
</html>