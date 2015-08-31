<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="issuetracking.*"%>
<%@ page import="java.util.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User view</title>
</head>
<body>
	<a href="Controller?action=preparePage&pageName=admin/users.jsp"> back to userlist </a>
	<h1>The user:</h1>
	UserID=${u1.userid}<br> 
	Password=${u1.password}<br> 
	
	<h1>Change the user</h1>
	<form action="Controller" method="post">
		<input type="hidden" name="user_id" value="${u1.userid}" /> 
		<input type="hidden" name="action" value="changeUser" /> 
		Password:<input name="passwordinput" type="text" />${errorMsgs.passwordinput}<br />
		<input type="submit" value="change the user">
	</form>


	<form action="Controller" method="post">
		<input type="hidden" name="user_id" value="${u1.userid}" /> 
		<input type="hidden" name="action" value="deleteUser" /> 
		<input type="submit" value="delete the user">
	</form>

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