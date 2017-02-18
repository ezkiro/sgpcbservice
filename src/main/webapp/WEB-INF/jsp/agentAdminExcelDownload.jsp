<%@ page language="java" contentType="application/vnd.ms-excel; charset=euc-kr"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
response.setHeader("Content-Description", "JSP Generated Data");   
response.setHeader("Content-Disposition", "attachment;filename=excelResult.html");
%>

<html>
	<table>
		<tr>
			<td>����</td>
			<td>����ڹ�ȣ</td>
			<td>���̵�</td>
			<td>��ȣ</td>
			<td>��ǥ��</td>
			<td>����ó</td>
			<td>�ּ�</td>
			<td>email</td>
			<td>���¹�ȣ</td>
			<td>���������</td>
			<td>�����</td>
			<td>����</td>
			<td>����</td>
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