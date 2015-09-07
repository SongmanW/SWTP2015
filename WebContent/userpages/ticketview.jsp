<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="javax.servlet.http.HttpServletRequest"%>
<%@ page import="javax.servlet.http.HttpServletResponse"%>
<%@ page import="javax.servlet.http.HttpSession"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Ticket view</title>
        <script src="${pageContext.request.contextPath}/script/ticketview.js"></script>
        <script src="${pageContext.request.contextPath}/application.js"></script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/application.css"/>
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

    <h1>The ticket:</h1>
    ID:${t1.id}<br> 
    Title:${t1.title}<br> 
    Description:<br>
    <pre style="display:inline">${t1.description}</pre><br> 
    Date:${t1.creation_date}<br> 
    Author:${t1.author}<br>
    Responsible User:${t1.responsible_user}<br>
    Type:${t1.type}<br> 
    State:${t1.status}<br>
    <span id="estimated_time_display_span">
        Estimated_Time:${t1.estimated_time}</span><br>
    Components: <br>
    <c:forEach items="${t1.components}" var="comp">
        ${comp.name}<br>
    </c:forEach>

    <h1>Pictures:</h1><br>
    <c:forEach items = "${ticket_pictures}" var="pic">
        <img alt="uploaded ${pic.uploadDateAsString} by ${pic.uploader}" src="${pageContext.request.contextPath}/image?file=${pic.pictureId}"><br>
        uploaded ${pic.uploadDateAsString} by ${pic.uploader}<br>
    </c:forEach>

    Attach picture: <br>
    <form method="POST" action="${pageContext.request.contextPath}/uploader" enctype="multipart/form-data" >
        <input type="hidden" name = "ticket_id" value = "${t1.id}" /> 
        <input type="hidden" name="author" value="${pageContext.request.userPrincipal.name}" /> 
        <input type="file" name="file" id="file" /> <br/>
        <input type="submit" value="Upload" name="upload" id="upload" />
    </form>

    <h1>Comments:</h1>
    <table>
        <tr>
            <th>Autor</th><th>Geschrieben am</th><th>Nachricht</th><th>aktionen</th>
        </tr>
    <c:forEach items="${t1.comments}" var="comment1">
        <tr>
            <td>${comment1.author}</td>
            <td>${comment1.dateAsString}</td>
            <td>${comment1.message}</td>
            <td>
                <c:if test="${comment1.author == pageContext.request.userPrincipal.name}"><a href="${pageContext.request.contextPath}/user/commentview.jsp?comment_id=${comment1.cid}"> bearbeiten </a></c:if>
            </td>
        </tr>
    </c:forEach>
    </table>

    <h1>New Comment</h1>
    <form action="user" method="post">
        <input type="hidden" name="action" value="addComment" /> 
        <input type="hidden" name="ticket_id" value="${t1.id}" />
        <input type="hidden" name="date" value="${date2}" /> 
        <input type="hidden" name="author" value="${sessionScope.user}" /> 
        Message:<br><textarea name="message" cols="65" rows="5" wrap="off" style="overflow-y: auto; overflow-x: auto;;font-size:70%"></textarea> ${errorMsgs.message}<br /> 
        <input type="submit" value="add comment">
    </form>


    <h1>Change the ticket</h1>
    <form action="user" method="post">
        <input type="hidden" name="ticket_id" value="${t1.id}" /> 
        <input type="hidden" name="action" value="changeTicket" /> 
        Title:<input name="title" type="text" value="${t1.title}">${errorMsgs.title}<br> 
        Description:<br>
        <textarea name="description" cols="80" rows="7" wrap="off" style="overflow-y: auto; overflow-x: auto;font-size:70%">${t1.description}</textarea> ${errorMsgs.description}<br /> 
        <input type="hidden" name="date" value="${date1}" /> ${errorMsgs.date} 
        <input type="hidden" name="author" value="${sessionScope.user}" />
        Responsible user:
        <select name="responsible_user" id="responsible_user_selection">
            <c:forEach items="${users}" var="user1">
                <option value="${user1.userid}">${user1.userid}</option>
            </c:forEach>
        </select> ${errorMsgs.responsible_user}<br /> 
        Type:
        <select name="type" onchange="showSpan(this)" id="type_selection">
            <option value="bug">bug</option>
            <option value="feature">feature</option>
        </select> ${errorMsgs.type}<br /> 
        State:
        <select name="status"  id="state_selection">
            <option value="open">open</option>
            <option value="closed">closed</option>
            <option value="in_progress">in progress</option>
            <option value="test">test</option>
        </select>${errorMsgs.state}<br> 
        <span id="estimated_time_change_span" style="display: none;">
            Estimated time:<input name="estimated_time" value="${t1.estimated_time}" type="text" />hours  ${errorMsgs.estimated_time}</span><br /> 
        Components <a href="${pageContext.request.contextPath}/user/components.jsp">(addComponents)</a>:<br>
        <c:forEach items="${compids}" var="compid1">
            <input type="checkbox" name="compid" value="${compid1.compid}">${compid1.name}
            <br>
        </c:forEach>
        <input type="submit" value="change the ticket">
    </form>

    <form action="user" method="post">
        <input type="hidden" name="ticket_id" value="${t1.id}" /> 
        <input type="hidden" name="action" value="deleteTicket" /> 
        <input type="submit" value="delete the ticket">
    </form>
</body>
</html>