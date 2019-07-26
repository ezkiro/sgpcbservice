<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
	<jsp:include page="common.jsp" flush="true"/>
	<script>
	$(document).ready(function(){
				
		$("#allGameCheck").click(function(){
			if ($(this).is(":checked")) {
				$(".select_subject input").prop('checked', true);				
			} else {
				$(".select_subject input").prop('checked', false);
			}			
		});

		$("#inputSerachKey").click(function(){
			if ($(this).val() == "all") {
				$("#inputSerachValue").val("");			
			}			
		});
	
		var checkValidSearch = function() {								
			//id 체크
			if ($("#inputSerachKey").val() != "all" && $("#inputSerachValue").val().length === 0 ) {
				alert("조회할 값을 입력하세요.");
				return false;				
			}
			
			if ($("#inputSerachKey").val() == "patchYN" && !($("#inputSerachValue").val() == "Y" || $("#inputSerachValue").val() == "N")) {
				alert("입력오류! 지급대상은 'Y' 또는 'N'를  입력하세요.");
				return false;								
			}
			
			return true;
		}
				
		$("form").submit(function(event){
			
			if (!checkValidSearch()) {
				event.preventDefault();
			}
			
			var checkedGames = [];
			
			$("input[name=gameCheck]:checked").each(function() {
				checkedGames.push($(this).val());				
			});
			
			$("input[name=checked_games]").val(checkedGames);
			
			$("input[name=search_key]").val($("#inputSerachKey option:selected").val());
			
			$("input[name=search_value]").val($("#inputSerachValue").val());	
			
		});
		
		var searchKey = "${search_key}";
		var searchValue = "${search_value}";
		
		if (searchKey.length > 0) {
			$("#inputSerachKey").val(searchKey);
		}
		
		if (searchValue.length > 0) {
			$("#inputSerachValue").val(searchValue);
		}
		
		$("#excelDownload").click(function(){
			if (searchKey.length > 0 && searchValue.length > 0) {
				location.href='/admin/gamepatch/excel?search_key=' + searchKey + '&search_value=' + searchValue;				
			} else {
				location.href='/admin/gamepatch/excel';
			}
		});		
		
	});  
	</script>
		
  </head>
  <body>
	<div class="container"> 
  	<div class="row">
  		<c:set var="permission" value="${permission}" />
		<ul class="nav nav-pills">
	<c:choose>
		<c:when test="${permission eq 'ADMIN'}">
		  <li role="presentation"><a href="/admin/agent">업체관리</a></li>
		  <li role="presentation" class="active"><a href="#">설치/패치관리</a></li>
		  <li role="presentation"><a href="/statistics">통계</a></li>
		  <li role="presentation"><a href="/admin/pcbang">가맹점관리</a></li>
		  <li role="presentation"><a href="/admin/game">Game관리</a></li>
		  <li role="presentation"><a href="/admin/installpath">InstallPath관리</a></li>
		  <li role="presentation"><a href="/logout">로그아웃</a></li>	
		</c:when>
		<c:otherwise>
		  <li role="presentation" class="active"><a href="#">설치/패치관리</a></li>
          <li role="presentation"><a href="/statistics">통계</a></li>
          <li role="presentation"><a href="/member/myagent">내정보</a></li>
		  <li role="presentation"><a href="/logout">로그아웃</a></li>		  		
		</c:otherwise>
	</c:choose>	
		</ul>
  	</div>
  	<hr>

    <div class="row">
    <div class="col-md-9">

	<div class="form-group bg-info">
	    <div class="row">
			<div class="col-md-2 text-right">
				<label for="lbGame">게임선택</label>
			</div>
			
			<div class="col-md-2">
				<label class="checkbox-inline">
					<input type="checkbox" id="allGameCheck" value="all">전체	
				</label>		
			</div>
			<div class="col-md-8">
				<ul class="select_subject">
				<c:forEach var="game" items="${gameList}">
					<label class="checkbox-inline">
						<input type="checkbox" name="gameCheck" value="${game.getGsn()}"> ${game.getName()}
					</label>
				</c:forEach>
				</ul>																																																				
			</div>
		</div>
	</div>
	
	<form class="form-horizontal" action="/admin/gamepatch" method="post">
		<input type="hidden" name="checked_games" value="" />
		<input type="hidden" name="search_key" value="" />
		<input type="hidden" name="search_value" value="" />
	  	
        <div class="form-group bg-info">
            <div class="row">
            <div class="col-md-2 text-right">
                <label for="lbID">조회조건</label>
            </div>
            <div class="col-md-2">
                <select class="form-control" id ="inputSerachKey">
                  <option value="all">전체</option>
                  <option value="agentName">관리업체1</option>
                  <option value="companyCode">관리업체2</option>
                  <option value="companyName">상호</option>
                  <option value="ipRange">IP</option>
                  <option value="patchYN">지급대상</option>
                </select>
            </div>
            <div class="col-md-4">
                <input type="text" class="form-control" id="inputSerachValue" placeholder="">
            </div>
            <div class="col-md-3">
                <button type="submit" class="btn btn-primary btn-block" id="search">조회</button>
            </div>
            </div>
        </div>
	</form>
	</div>

    <div class="col-md-3">
  	<div class="form-group">
		<div class="col-md-10 col-md-offset-2 bg-warning">
			<label for="lbTotal">등록 PC방 : ${pcbGamePatchResultList.size()} 개</label>
			<br>
			<label for="lbPay">정산 PC방 : ${paymentPcbCnt} 개</label>
		</div>
	</div>
    </div>

	</div><!-- row -->
	</div><!-- container -->	
	
		<table class="table table-bordered table-hover">
			<tr class="info">
				<td>구분</td>
				<td>PSN</td>
				<td>상호</td>
				<td>주소</td>				
				<td>IPstart</td>
				<td>IPend</td>				
				<td>submask</td>
				<td>관리업체1</td>
				<td>관리업체2</td>				
				<td>관리IP수</td>
				<td>확인IP수</td>
				<td>정산대상(Y/N)</td>
			<c:forEach var="game" items="${targetGameList}">
				<td>${game.getName()}</td>
			</c:forEach>																																																						
			</tr>

		<c:forEach var="pcbGamePatchResult" items="${pcbGamePatchResultList}">
			<tr>
    			<td>${pcbGamePatchResult.getPcbang().getPcbId()}</td>
    			<td>${pcbGamePatchResult.getPcbang().getCeo()}</td>
    			<td>${pcbGamePatchResult.getPcbang().getCompanyName()}</td>
    			<td>${pcbGamePatchResult.getPcbang().getAddress()}</td>    			    			    			
    			<td>${pcbGamePatchResult.getPcbang().getIpStart()}</td>
    			<td>${pcbGamePatchResult.getPcbang().getIpEnd()}</td>    			
    			<td>${pcbGamePatchResult.getPcbang().getSubmask()}</td>
    			<td>${pcbGamePatchResult.getPcbang().getAgent().getCompanyName()}</td>
    			<td>${pcbGamePatchResult.getPcbang().getCompanyCode()}</td>    			
    			<td>${pcbGamePatchResult.getPcbang().getIpTotal()}</td>
   				<td><a class="btn btn-success" href="/admin/pcbgamepatch/detail?pcb_id=${pcbGamePatchResult.getPcbang().getPcbId()}" role="button">${pcbGamePatchResult.getCheckIPCnt()}</a></td>    			
    			<td>${pcbGamePatchResult.getIsPaymentPcbang().toString()}</td>
			<c:forEach var="game" items="${targetGameList}">
				<td>${pcbGamePatchResult.getGamePatchMap().get(game.getGsn())}</td>
			</c:forEach>																																																						
    		</tr>
		</c:forEach>
		</table>
	<br>
	<br>
	<div class="form-group">
		<div class="col-md-2 col-md-offset-6"><button type="button" class="btn btn-default btn-block" id="excelDownload">Excel download</button></div>
	</div>
		
  </body>
</html>