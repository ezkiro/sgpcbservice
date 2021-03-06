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
				$("#inputEnable").val("${game.getEnable()}");
				$("#inputName").val("${game.getName()}");
				$("#inputVerifyType").val("${game.getVerifyType().toString()}");		
				$("#inputMajor").val("${game.getMajor()}");
				$("#inputMinor").val("${game.getMinor()}");
				$("#inputExeFile").val("${game.getExeFile()}");
				$("#inputDirName").val("${game.getDirName()}");
				$("#inputVerFile").val("${game.getVerFile()}");
				$("#inputVerFileFmt").val("${game.getVerFileFmt()}");				
				
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
			    			enable: $("#inputEnable").val(),
			    			name:$("#inputName").val(),
			    			verify_type:$("#inputVerifyType option:selected").val(),			    			
			    			major: $("#inputMajor").val(),
			    			minor: $("#inputMinor").val(),
			    			exe_file: $("#inputExeFile").val(),
			    			dir_name: $("#inputDirName").val(),
			    			ver_file: $("#inputVerFile").val(),
			    			ver_file_fmt: $("#inputVerFileFmt option:selected").val()			    			
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
                            enable: $("#inputEnable").val(),
			    			name:$("#inputName").val(),
			    			verify_type:$("#inputVerifyType option:selected").val(),			    			
			    			major: $("#inputMajor").val(),
			    			minor: $("#inputMinor").val(),
			    			exe_file: $("#inputExeFile").val(),
			    			dir_name: $("#inputDirName").val(),
			    			ver_file: $("#inputVerFile").val(),
			    			ver_file_fmt: $("#inputVerFileFmt option:selected").val()			    			
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
		<h3>Game 정보 입력/수정</h3>
	</div>
	
<form class="form-horizontal">
	<div class="row">
		<div class="form-group">
			<div class="col-sm-2 text-right"><label for="lbGsn">GSN</label></div>
	<c:set var="aGame" value="${game}" />
	<c:choose>
		<c:when test="${!empty aGame}">
			<div class="col-sm-2"><input type="text" class="form-control" id="inputGsn" placeholder="" disabled></div>
		</c:when>
		<c:otherwise>
			<div class="col-sm-2"><input type="text" class="form-control" id="inputGsn" placeholder="필수"></div>	
		</c:otherwise>
	</c:choose>			
		</div>
   </div>

	<div class="row">
		<div class="form-group">
			<div class="col-sm-2 text-right"><label for="lbEnable">사용여부</label></div>
			<div class="col-sm-2">
				<select class="form-control" id ="inputEnable">
					<option value="Y">사용</option>
					<option value="N">미사용</option>
				</select>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="form-group">    
			<div class="col-sm-2 text-right"><label for="lbName">게임명</label></div>
			<div class="col-sm-4"><input type="text" class="form-control" id="inputName" placeholder="필수"></div>
		</div>
	</div>

	<div class="row">
		<div class="form-group">    
			<div class="col-sm-2 text-right"><label for="lbVerifyType">확인방법</label></div>
			<div class="col-sm-2">
				<select class="form-control" id ="inputVerifyType">
					<option value="INSTALL">설치만</option>
					<option value="VERFILE">버전 체크 </option>
					<option value="VERDATE">파일 수정날짜 체크</option>
				</select>						
			</div>
		</div>
	</div>

	<div class="row">
		<div class="form-group">    
			<div class="col-sm-2 text-right"><label for="lbMajor">Major버전</label></div>
			<div class="col-sm-4"><input type="text" class="form-control" id="inputMajor" placeholder="필수"></div>
		</div>
	</div>
   
	<div class="row">
		<div class="form-group">    
			<div class="col-sm-2 text-right"><label for="lbMinor">Minor버전</label></div>
			<div class="col-sm-4"><input type="text" class="form-control" id="inputMinor" placeholder="exe file의 수정날짜 or 버전파일 체크인 경우는 이전 버전"></div>
		</div>
	</div>

	<div class="row">
		<div class="form-group">    
			<div class="col-sm-2 text-right"><label for="lbExeFile">실행파일명</label></div>
			<div class="col-sm-4"><input type="text" class="form-control" id="inputExeFile" placeholder="필수"></div>
		</div>
	</div>

	<div class="row">
		<div class="form-group">    
			<div class="col-sm-2 text-right"><label for="lbDir">상위dir</label></div>
			<div class="col-sm-4"><input type="text" class="form-control" id="inputDirName" placeholder=""></div>
		</div>
	</div>

	<div class="row">
		<div class="form-group">    
			<div class="col-sm-2 text-right"><label for="lbVerFile">버전파일명</label></div>
			<div class="col-sm-4"><input type="text" class="form-control" id="inputVerFile" placeholder=""></div>
		</div>
	</div>

	<div class="row">
		<div class="form-group">    
			<div class="col-sm-2 text-right"><label for="lbVerFileFmt">버전파일포맷</label></div>
			<div class="col-sm-2">
				<select class="form-control" id ="inputVerFileFmt">
					<option value="JSON">JSON</option>
					<option value="XML">XML</option>
					<option value="INI">INI</option>
					<option value="BIN">BIN</option>
					<option value="EPIC">EPIC</option>
				</select>						
			</div>
		</div>
	</div>


    <div class="row">
		<div class="form-group">
		<div class="col-sm-2"></div>
 		<div class="col-sm-2"><button type="button" class="btn btn-lg btn-primary btn-block" id="submitGame">저장</button></div>
 		<div class="col-sm-2"><a class="btn btn-lg btn-default btn-block" href="/admin/game" role="button">취소</a></div>
 		<div class="col-sm-2"></div> 		
 		</div>
    </div>
	
</form>
</div>

  </body>
</html>