<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="issuetracking.*"%>

<% 
DBManager DBManager1 = DBManager.getInstance();
if (DBManager1.checkLogin((String)request.getSession().getAttribute("user"),
					(String)request.getSession().getAttribute("password"))) {
				request.getRequestDispatcher("Controller?action=preparePage&pageName=index.jsp").forward(
						request, response);
			}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	${sessionScope.test}
	${regSuccess}
	<h1>Login</h1> 
	<form action="Controller" method="post">
		<input type="hidden" name="action" value="login"/>
		Username:<input name="useridinput" type="text"/>${errorMsgsLogin.useridinput}<br />
		Password:<input name="passwordinput" type="text"/>${errorMsgsLogin.passwordinput}<br />
		<input type="submit" value="login">
	</form>
	
	<h1>Register</h1>
	<form action="Controller" method="post">
		<input type="hidden" name="action" value="register"/>
		Username:<input name="useridinput" type="text"/>${errorMsgsReg.useridinput}<br />
		Password:<input name="passwordinput" type="text"/>${errorMsgsReg.passwordinput}<br />
		<input type="submit" value="register"> 
	</form><br />
	
	<a href="Controller?action=preparePage&pageName=users.jsp"> Edit Users (Adminpage)</a>

	<h1>Session attributes:</h1>
<%
  for (Enumeration<String> e = session.getAttributeNames(); e.hasMoreElements(); ) {     
    String attribName = (String) e.nextElement();
    Object attribValue = session.getAttribute(attribName);
%>
<BR><%= attribName %> - <%= attribValue %>

<%
}
%>
<%
Cookie[] cookies=request.getCookies(); 
%>	
	
<h1>Request-Cookies</h1>
	<%
		for(Cookie c1 : cookies) {
	%>
	<%=c1.getName()%>=
	<%=c1.getValue()%>
	<br />

	<%
		};
	%>
	<h1>Parameters</h1>
	<%
		for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements(); ) {     
		    String attribName = e.nextElement();
		    String[] attribValues = request.getParameterValues(attribName);
		    String allValues="";
		    for(String s:attribValues){
		    	allValues=allValues+" "+s;
		    }
	%>
	<%=attribName%>= <%=allValues%><br />
	<%
		};
	%>

</body>
</html>