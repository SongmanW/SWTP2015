<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="javax.servlet.http.HttpServletRequest"%>
<%@ page import="javax.servlet.http.HttpServletResponse"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Settings</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/application.css"/>
    </head>
    <body BACKGROUND="${pageContext.request.contextPath}/triangular.png"/>
    <a href="${pageContext.request.contextPath}/login.jsp"> back to login </a>

    ${regSuccess}
    <h1>Upload Directory</h1>
    <br>
    <form action="admin" method="post">
		<input type="hidden" name="action" value="changefilespath">
		<input name="path" type="text"><br>
		<input type="submit" value="change path">
	</form>
</body>
</html>