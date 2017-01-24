<%@ page language="java" contentType="application/vnd.ms-excel; charset=euc-kr"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
response.setHeader("Content-Description", "JSP Generated Data");   
response.setHeader("Content-Disposition", "attachment;filename=ServieRequestList.xls");
%>

<html>
	<table>
		<tr>
			<td>구분</td>
			<td>대표자</td>
			<td>상호</td>
			<td>주소</td>				
			<td>IP대역</td>
			<td>submask</td>
			<td>관리업체1</td>
			<td>관리업체2</td>				
			<td>관리IP수</td>
			<td>확인IP수</td>
			<td>지급대상(Y/N)</td>
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
	  			<td>${pcbGamePatchResult.getPcbang().getIpStart()} - ${pcbGamePatchResult.getPcbang().getIpEnd()}</td>
	  			<td>${pcbGamePatchResult.getPcbang().getSubmask()}</td>
	  			<td>${pcbGamePatchResult.getPcbang().getAgent().getCompanyName()}</td>
	  			<td>${pcbGamePatchResult.getPcbang().getCompanyCode()}</td>    			
	  			<td>${pcbGamePatchResult.getPcbang().getIpTotal()}</td>
	 				<td>${pcbGamePatchResult.getCheckIPCnt()}</td>
	  			<td>${pcbGamePatchResult.getIsPaymentPcbang().toString()}</td>
		<c:forEach var="game" items="${targetGameList}">
			<td>${pcbGamePatchResult.getGamePatchMap().get(game.getGsn())}</td>
		</c:forEach>																																																						
	  		</tr>
	</c:forEach>
	</table>
</html>