<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="issuetracking.*"%>

<%! 
%>
<%
Cookie[] cookies=request.getCookies(); 

Cookies.getCookie(cookies,"user").setMaxAge(0);
Cookies.getCookie(cookies,"password").setMaxAge(0);
response.addCookie(Cookies.getCookie(cookies,"user"));
response.addCookie(Cookies.getCookie(cookies,"password"));
response.sendRedirect("login.jsp");

       
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>



</body>
</html>