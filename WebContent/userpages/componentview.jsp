<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ page import="javax.servlet.http.HttpServletRequest"%>
<%@ page import="javax.servlet.http.HttpServletResponse"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Component View</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    </head>
    <body BACKGROUND="${pageContext.request.contextPath}/triangular.png"/>
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/credits.jsp">SWTP-Team</a>
            </div>
            <ul class="nav navbar-nav">
                <li><a href="${pageContext.request.contextPath}/user/sprints.jsp">Sprints</a></li>
                <li><a href="${pageContext.request.contextPath}/user/index.jsp">Tickets</a></li>
                <li><a href="${pageContext.request.contextPath}/user/components.jsp">Components</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="${pageContext.request.contextPath}/user/userpage.jsp"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>
                        ${pageContext.request.userPrincipal.name}</a></li>
                <li><a href="${pageContext.request.contextPath}/user/?action=logout">logout</a></li>
            </ul>
        </div>
    </nav>
    <h1>The component:</h1>
    ComponentID=${c1.compid}<br> 
    ComponentName=${c1.name}<br> 
    Description=${c1.description}<br> 

    <h1>Change the component</h1>
    <form action="user" method="post">
        <input type="hidden" name="compid" value="${c1.compid}" /> 
        <input type="hidden" name="action" value="changeComponent" />
        Name:<input name="name" type="text" value="${c1.name}"/>${errorMsgs.name}<br/>
        Description:<input name="description" type="text" value="${c1.description}"/>${errorMsgs.description}<br />
        <input type="submit" value="change the component">
    </form>


    <form action="user" method="post">
        <input type="hidden" name="comp_id" value="${c1.compid}" /> 
        <input type="hidden" name="action" value="deleteComponent" /> 
        <input type="submit" value="delete the component">
    </form>
</body>
</html>