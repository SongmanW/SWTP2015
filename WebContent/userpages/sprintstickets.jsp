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
        <title>Sprint tickets</title>
        <script src="${pageContext.request.contextPath}/application.js"></script>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/application.css"/>
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


    <h1>Open Tickets</h1>

    <table>
        <col width="30">
        <col width="100">
        <col width="30">
        <col width="200">
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Type</th>
            <th>Description</th>
        </tr>
        <c:forEach items="${tickets_open}" var="ticket1">
            <tr>
                <td>${ticket1.id}</td>
                <td><a
                        href="${pageContext.request.contextPath.concat("/user/ticketview.jsp?ticket_id=").concat(ticket1.id)}">
                        ${ticket1.title} </a></td>
                <td>${ticket1.type}</td>
                <td>${fn:length(ticket1.description) gt 25 ? fn:substring(ticket1.description, 0, 25).concat("..."):ticket1.description}
                </td>

            </tr>
        </c:forEach>
    </table>
    <br>

    <h1>Tickets In Progress</h1>

    <table>
        <col width="30">
        <col width="100">
        <col width="30">
        <col width="200">
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Type</th>
            <th>Description</th>
        </tr>
        <c:forEach items="${tickets_inprogress}" var="ticket1">
            <tr>
                <td>${ticket1.id}</td>
                <td><a
                        href="${pageContext.request.contextPath.concat("/user/ticketview.jsp?ticket_id=").concat(ticket1.id)}">
                        ${ticket1.title} </a></td>
                <td>${ticket1.type}</td>
                <td>${fn:length(ticket1.description) gt 25 ? fn:substring(ticket1.description, 0, 25).concat("..."):ticket1.description}
                </td>
            </tr>
        </c:forEach>
    </table>
    <br>

    <h1>Tickets In Testing</h1>

    <table>
        <col width="30">
        <col width="100">
        <col width="30">
        <col width="200">
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Type</th>
            <th>Description</th>
        </tr>
        <c:forEach items="${tickets_test}" var="ticket1">
            <tr>
                <td>${ticket1.id}</td>
                <td><a
                        href="${pageContext.request.contextPath.concat("/user/ticketview.jsp?ticket_id=").concat(ticket1.id)}">
                        ${ticket1.title} </a></td>
                <td>${ticket1.type}</td>
                <td>${fn:length(ticket1.description) gt 25 ? fn:substring(ticket1.description, 0, 25).concat("..."):ticket1.description}
                </td>
            </tr>
        </c:forEach>
    </table>
    <br>

    <h1>Closed Tickets</h1>

    <table>
        <col width="30">
        <col width="100">
        <col width="30">
        <col width="200">
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Type</th>
            <th>Description</th>
        </tr>
        <c:forEach items="${tickets_closed}" var="ticket1">
            <tr>
                <td>${ticket1.id}</td>
                <td><a
                        href="${pageContext.request.contextPath.concat("/user/ticketview.jsp?ticket_id=").concat(ticket1.id)}">
                        ${ticket1.title} </a></td>
                <td>${ticket1.type}</td>
                <td>${fn:length(ticket1.description) gt 25 ? fn:substring(ticket1.description, 0, 25).concat("..."):ticket1.description}
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>