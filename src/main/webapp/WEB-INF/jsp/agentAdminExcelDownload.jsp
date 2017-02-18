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
			<td>사업자번호</td>
			<td>아이디</td>
			<td>상호</td>
			<td>대표자</td>
			<td>연락처</td>
			<td>주소</td>
			<td>email</td>
			<td>계좌번호</td>
			<td>관리매장수</td>
			<td>등록일</td>
			<td>상태</td>
			<td>권한</td>
		</tr>
	<c:forEach var="agent" items="${agentList}">
		<tr>
   			<td>${agent.getAgentId()}</td>
   			<td>${agent.getCompanyCode()}</td>
   			<td>${agent.getAccount().getId()}</td>
   			<td>${agent.getCompanyName()}</td>    			    			    			
   			<td>${agent.getCeo()}</td>
   			<td>${agent.getContactNum()}</td>
   			<td>${agent.getAddress()}</td>
   			<td>${agent.getEmail()}</td>    			    			    			
   			<td>${agent.getBankAccount()}</td>
   			<td>${agent.getPcbangs().size()}</td>
   			<td>${agent.getCrtDt().toString().substring(0,10)}</td>
   			<td>${agent.getStatus()}</td>    			    			    			
   			<td>${agent.getAccount().getPermission()}</td>
   		</tr>
	</c:forEach>
	</table>
</html>