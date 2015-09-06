<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="java.util.*"%>
<%@ page import="issuetracking.*"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User overview</title>
<link rel="stylesheet" type="text/css" href="application.css"/>
</head>
<body BACKGROUND="${pageContext.request.contextPath}/triangular.png"/>
	<a href="login.jsp"> back to login </a>
	
 	${regSuccess}
	<h1>Register</h1>
	<form action="Controller" method="post">
		<input type="hidden" name="action" value="register_from_users"/>
		Username:<input name="useridinput" type="text"/>${errorMsgsReg.useridinput}<br />
		Password:<input name="passwordinput" type="text"/>${errorMsgsReg.passwordinput}<br />
		<input type="submit" value="register">
	</form>

	<h1>Users</h1>
	<table>
		<col width="30">
		<col width="100">
		<col width="200">
		<tr>
			<th>Userid</th>
			<th>Password</th>
		</tr>
		<c:forEach items="${users}" var="user1">
			<tr>
			<td>
				<a href=${"admin/userview.jsp?user_id=".concat(user1.userid)}>
				${user1.userid}</a>
			</td>
			<td>${user1.password}</td>
			</tr>
		</c:forEach>

	</table>

<!-- development -->
<br>
<br>
<br>
<br>
<br>
<hr>

<b>Session attributes:</b><br>
<% 
for (Enumeration<String> e = session.getAttributeNames(); e.hasMoreElements(); ) {     
    String attribName = (String) e.nextElement();
    Object attribValue = session.getAttribute(attribName);
	%>
	<%= attribName %> = <%= attribValue %><br>
	<%
}
%>

<b>Parameters:</b>  <br>
<%
for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements(); ) {     
	String attribName = e.nextElement();
	String[] attribValues = request.getParameterValues(attribName);
	String allValues="";
	for(String s:attribValues){
		allValues=allValues+" "+s;
	}
	%>
	<%=attribName%> = <%=allValues%><br />
	<%
};
%>
		
<b>Cookies (Request):</b><br>
<%
Cookie[] cookies=request.getCookies();
if(cookies!=null)
	for(Cookie c1 : cookies) {
		%>
		<%=c1.getName()%> = <%=c1.getValue()%><br />
		<%
	};
%>

</body>
</html>