<%@ page language="java" contentType="application/vnd.ms-excel; charset=euc-kr"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
response.setHeader("Content-Description", "JSP Generated Data");   
response.setHeader("Content-Disposition", "attachment;filename=excelResult.html");
%>

<html>
	<table>
		<tr>
			<td>구분</td>
			<td>상호</td>
			<td>주소</td>				
			<td>IPstart</td>
			<td>IPend</td>			
			<td>관리IP수</td>
			<td>확인IP수</td>
		<c:forEach var="game" items="${targetGameList}">
			<td>${game.getName()}</td>
			<td>설치유무(Y/N)</td>
		</c:forEach>																																																						
		</tr>
	<c:forEach var="pcbGamePatchResult" items="${pcbGamePatchResultList}">
		<tr>
            <td>${pcbGamePatchResult.getPcbang().getPcbId()}</td>
            <td>${pcbGamePatchResult.getPcbang().getCompanyName()}</td>
            <td>${pcbGamePatchResult.getPcbang().getAddress()}</td>
            <td>${pcbGamePatchResult.getPcbang().getIpStart()}</td>
            <td>${pcbGamePatchResult.getPcbang().getIpEnd()}</td>
            <td>${pcbGamePatchResult.getPcbang().getIpTotal()}</td>
            <td>${pcbGamePatchResult.getCheckIPCnt()}</td>
		<c:forEach var="game" items="${targetGameList}">
			<td>${pcbGamePatchResult.getGamePatchMap().get(game.getGsn())}</td>
			<td>${pcbGamePatchResult.isMissionCompleteGame(game).toString()}</td>
		</c:forEach>																																																						
	  		</tr>
	</c:forEach>
	</table>
</html>