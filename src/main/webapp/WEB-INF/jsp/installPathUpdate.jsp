<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
	<jsp:include page="common.jsp" flush="true"/>

  <script>
	$(document).ready(function(){
						
		var checkValidInput = function() {
			
			if($("#inputPath").val().length === 0 ) {
				alert("Path를 입력해 주세요.");
				return false;				
			}			
			
			return true;
		}		
		
		var setUpForUpdate = function() {
					
			$("#inputID").val("${installPath.getId()}");
			$("#inputGsn").val("${installPath.getGsn()}");
			$("#inputPath").val("${installPath.getPath()}");
			$("#inputType").val("${installPath.getType()}");				
		}
		
		//submit		
		$("#submitItem").click(function(){			
			if(!checkValidInput()) return;
						
		    $.post("/admin/api/installpath/update",
		    		{
		    			id: $("#inputID").val(),
		    			gsn: $("#inputGsn option:selected").val(),
		    			path: $("#inputPath").val(),
		    			type: $("#inputType option:selected").val(),			    			
		    		},
		    		function(data, status){
		    			if(data) {
		    				alert("installpath 수정에 성공하였습니다.");
		    				location.href = '/admin/installpath';
		    			} else {
		    				alert("installpath 수정에 실패했습니다. 다시 시도 하세요.");			    				
		    			}
		    		}
		    );			
		});
		
		setUpForUpdate();
	});  
  </script> 
	
		
  </head>
  <body>
  
<div class="container bg-info">  
	<div class="row text-center">
		<h3>Install Path 정보 입력/수정</h3>
	</div>
	
<form class="form-horizontal">

	<div class="row">
		<div class="form-group">    
			<div class="col-sm-2 text-right"><label for="lbId">ID</label></div>
			<div class="col-sm-2"><input type="text" class="form-control" id="inputID" disabled></div>
		</div>
	</div>

	<div class="row">
		<div class="form-group">    
			<div class="col-sm-2 text-right"><label for="lbGsn">GSN</label></div>
			<div class="col-sm-4">
				<select class="form-control" id ="inputGsn">
				<c:forEach var="game" items="${gameList}">
					<option value="${game.getGsn()}">${game.getName()}</option>
				</c:forEach>	
				</select>						
			</div>
		</div>
	</div>

	<div class="row">
		<div class="form-group">    
			<div class="col-sm-2 text-right"><label for="lbCategory">Category</label></div>
			<div class="col-sm-2">
				<select class="form-control" id ="inputType">
					<option value="exe">EXE</option>
					<option value="ver">VER </option>
				</select>						
			</div>
		</div>
	</div>

	<div class="row">
		<div class="form-group">    
			<div class="col-sm-2 text-right"><label for="lbPath">Path</label></div>
			<div class="col-sm-8"><input type="text" class="form-control" id="inputPath" placeholder="ex) /games/game_name"></div>
		</div>
	</div>
   
    <div class="row">
		<div class="form-group">
		<div class="col-sm-2"></div>
 		<div class="col-sm-2"><button type="button" class="btn btn-lg btn-primary btn-block" id="submitItem">저장</button></div>
 		<div class="col-sm-2"><a class="btn btn-lg btn-default btn-block" href="/admin/installpath" role="button">취소</a></div>
 		<div class="col-sm-2"></div> 		
 		</div>
    </div>
	
</form>
</div>

  </body>
</html>