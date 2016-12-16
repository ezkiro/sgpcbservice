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
    
  </head>
  <body>	
        
   <div class="container">   

	<H1>Login for E-GPMS</H1>
	     
    <div class = "row">
		<div class="col-md-4"></div>
 		<div class="col-md-4">  		
			<form>
			  <div class="form-group">
			    <label for="exampleInputEmail1">이메일 주소</label>
			    <input type="email" class="form-control" id="exampleInputEmail1" placeholder="이메일을 입력하세요">
			  </div>
			  <div class="form-group">
			    <label for="exampleInputPassword1">암호</label>
			    <input type="password" class="form-control" id="exampleInputPassword1" placeholder="암호">
			  </div>
			  <a class="btn btn-lg btn-default" href="/signup" role="button">SignUP</a>
			  <button type="submit" class="btn btn-lg btn-primary">로그인</button>
			</form>
 		</div>
 		<div class="col-md-4"></div>		            
    </div>
                 
   </div> <!-- /container -->
       
  </body>
</html>