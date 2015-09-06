<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ page import="javax.servlet.http.HttpServletRequest"%>
<%@ page import="javax.servlet.http.HttpServletResponse"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>User view</title>
    </head>
    <body BACKGROUND="${pageContext.request.contextPath}/triangular.png"/>
    <a href="${pageContext.request.contextPath}/admin/users.jsp"> back to userlist </a>
    <h1>The user:</h1>
    UserID=${u1.userid}<br> 
    Password=${u1.password}<br> 

    <h1>Change the user</h1>
    <form action="admin" method="post">
        <input type="hidden" name="user_id" value="${u1.userid}" /> 
        <input type="hidden" name="action" value="changeUser" /> 
        Password:<input name="passwordinput" type="text" />${errorMsgs.passwordinput}<br />
        <input type="submit" value="change the user">
    </form>


    <form action="admin" method="post">
        <input type="hidden" name="user_id" value="${u1.userid}" /> 
        <input type="hidden" name="action" value="deleteUser" /> 
        <input type="submit" value="delete the user">
    </form>
</body>
</html>