<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
	<jsp:include page="common.jsp" flush="true"/>

  <script>
	$(document).ready(function(){
		
		var updateMode = false;
		
		$("#frmGame").attr("action","/admin/game/new");
		
		var checkValidInput = function() {
			
			if($("#inputGsn").val().length === 0 ) {
				alert("GSN을 입력해 주세요.");
				return false;				
			}			

			if($("#inputName").val().length === 0 ) {
				alert("게임명을  입력해 주세요.");
				return false;				
			}
			
			if($("#inputMajor").val().length === 0 ) {
				alert("Major버전을  입력해 주세요.");
				return false;				
			}
			
			return true;
		}		
		
		var setUpForUpdate = function() {
			
			var gsn = "${game.getGsn()}";
		
			if(gsn !== "") {
				updateMode = true;
				$("#inputGsn").val("${game.getGsn()}");
				$("#inputName").val("${game.getName()}");
				$("#inputMajor").val("${game.getMajor()}");
				$("#inputMinor").val("${game.getMinor()}");
				
				$("#frmGame").attr("action","/admin/game/update");			
			}
		}
		
		//submit		
		$("#submitGame").click(function(){			
			if(!checkValidInput()) return;
			
			//add mode
			if(!updateMode) {
				
			    $.post("/admin/game/new",
			    		{
			    			gsn: $("#inputGsn").val(),
			    			name:$("#inputName").val(),
			    			major: $("#inputMajor").val(),
			    			minor: $("#inputMinor").val()
			    		},
			    		function(data, status){
			    			if(data) {
			    				alert("Game 등록에 성공하였습니다.");
			    				location.href = '/admin/game';
			    			} else {
			    				alert("Game 등록에 실패했습니다. 다시 시도 하세요.");
			    			}
			    		}
			    );
			} else {
				
			    $.post("/admin/game/update",
			    		{
			    			gsn: $("#inputGsn").val(),
			    			name:$("#inputName").val(),
			    			major: $("#inputMajor").val(),
			    			minor: $("#inputMinor").val()
			    		},
			    		function(data, status){
			    			if(data) {
			    				alert("Game 수정에 성공하였습니다.");
			    				location.href = '/admin/game';
			    			} else {
			    				alert("Game 수정에 실패했습니다. 다시 시도 하세요.");			    				
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
	<div class="row">
		<div class="form-group">
			<div class="col-md-2 text-right"><label for="lbGsn">GSN</label></div>
	<c:set var="aGame" value="${game}" />
	<c:choose>
		<c:when test="${!empty aGame}">
			<div class="col-md-2"><input type="text" class="form-control" id="inputGsn" placeholder="" disabled></div>
		</c:when>
		<c:otherwise>
			<div class="col-md-2"><input type="text" class="form-control" id="inputGsn" placeholder=""></div>	
		</c:otherwise>
	</c:choose>			
			<div class="col-md-2"></div>
			<div class="col-md-2"></div>
		</div>
   </div>
	<div class="row">
		<div class="form-group">    
			<div class="col-md-2 text-right"><label for="lbName">게임명</label></div>
			<div class="col-md-4"><input type="text" class="form-control" id="inputName" placeholder=""></div>
			<div class="col-md-4"></div>
		</div>
	</div>

	<div class="row">
		<div class="form-group">    
			<div class="col-md-2 text-right"><label for="lbMajor">Major버전</label></div>
			<div class="col-md-2"><input type="text" class="form-control" id="inputMajor" placeholder=""></div>
			<div class="col-md-4"></div>
		</div>
	</div>
   
	<div class="row">
		<div class="form-group">    
			<div class="col-md-2 text-right"><label for="lbMinor">Minor버전</label></div>
			<div class="col-md-2"><input type="text" class="form-control" id="inputMinor" placeholder=""></div>
			<div class="col-md-4"></div>
		</div>
	</div>

    <div class="row">
		<div class="form-group">
		<div class="col-md-2"></div>
 		<div class="col-md-2"><button type="button" class="btn btn-lg btn-primary btn-block" id="submitGame">저장</button></div>
 		<div class="col-md-2"><a class="btn btn-lg btn-default btn-block" href="/admin/game" role="button">취소</a></div>
 		<div class="col-md-2"></div> 		
 		</div>
    </div>
	
</form>
</div>

  </body>
</html>