<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="javax.servlet.http.HttpServletRequest"%>
<%@ page import="javax.servlet.http.HttpServletResponse"%>
<%@ page import="javax.servlet.http.HttpSession"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Statistik</title>
        <script src="application.js"></script>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/application.css"/>
    </head>

    <body BACKGROUND="${pageContext.request.contextPath}/triangular.png"/>
    <a href="login.jsp"> back to login </a>


    <h1>Statistics:</h1>
    Sprints:${sprints}<br>
    Closed Tickets:${closedtickets}/${tickets}<br> 
    Comments:${comments}<br> 
    Components:${components}<br>
    Users:${users}<br> 
</body>
</html>