<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="issuetracking.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Tickets</title>
 <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
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
            <li class="active"><a href="${pageContext.request.contextPath}/user/index.jsp">Tickets</a></li>
            <li><a href="${pageContext.request.contextPath}/user/components.jsp">Components</a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li><a href="${pageContext.request.contextPath}/user/userpage.jsp"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>
		${pageContext.request.userPrincipal.name}</a></li>
            <li><a href="${pageContext.request.contextPath}/user/?action=logout">logout</a></li>
        </ul>
    </div>
</nav>
        
        <a class="btn btn-primary" role="button" data-toggle="collapse" href="#new-ticket" aria-expanded="false" aria-controls="new-ticket">new Ticket</a>
        <div class="collapse" id="new-ticket">
	<h1>New Ticket</h1>
        <form action="user" method="post">
		<input type="hidden" name="sprintid" value="-1" /><!--${thissprintsid} -->
		<input type="hidden" name="action" value="addTicket" /> 
		Title:<input name="title" type="text" /> ${errorMsgs.title}<br> 
		Description:<br><textarea name="description" cols="65" rows="5" wrap="off" style="overflow-y: auto; overflow-x: auto;;font-size:70%"></textarea> ${errorMsgs.description}<br /> 
		<input type="hidden" name="date" value="${date1}" /> 
		<input type="hidden" name="author" value="${sessionScope.user}" /> 
		Responsible user:
		<select name="responsible_user">
			<c:forEach items="${users}" var="user1">
				<option value="${user1.userid}">${user1.userid}</option>
			</c:forEach>
		</select> ${errorMsgs.responsible_user}<br> 
		Type:<select name="type" onchange="showSpan(this)">
			<option value="bug">bug</option>
			<option value="feature">feature</option>
		</select> ${errorMsgs.type}<br> State:<select name="state">
			<option value="open">open</option>
			<option value="closed">closed</option>
			<option value="in_progress">in progress</option>
			<option value="test">test</option>
		</select> ${errorMsgs.state}<br> 
		<span id="estimated_time_change_span" style="display: none;">
			Estimated time:<input name="estimated_time" type="text" />${errorMsgs.estimated_time}
		</span><br />
		Components <a href="${pageContext.request.contextPath}/user/components.jsp">(addComponents)</a>:<br>
		<c:forEach items="${compids}" var="compid1">
			<input type="checkbox" name="compid" value="${compid1.compid}">${compid1.compid}
			<br>
		</c:forEach>
		<input type="submit" value="add ticket">
	</form>
        </div>

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
			<th>SprintID</th>
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
				<td>${ticket1.sprintid}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath.concat("/user/?action=deleteTicket&ticket_id=").concat(ticket1.id)}" role="button" class="btn btn-danger">
                                        <span class="glyphicon glyphicon-remove-sign" aria-hidden="true"></span>remove
                                    </a>
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
			<th>SprintID</th>
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
				<td>${ticket1.sprintid}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath.concat("/user/?action=deleteTicket&ticket_id=").concat(ticket1.id)}" role="button" class="btn btn-danger">
                                        <span class="glyphicon glyphicon-remove-sign" aria-hidden="true"></span>remove
                                    </a>
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
			<th>SprintID</th>
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
				<td>${ticket1.sprintid}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath.concat("/user/?action=deleteTicket&ticket_id=").concat(ticket1.id)}" role="button" class="btn btn-danger">
                                        <span class="glyphicon glyphicon-remove-sign" aria-hidden="true"></span>remove
                                    </a>
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
			<th>SprintID</th>
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
				<td>${ticket1.sprintid}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath.concat("/user/?action=deleteTicket&ticket_id=").concat(ticket1.id)}" role="button" class="btn btn-danger">
                                        <span class="glyphicon glyphicon-remove-sign" aria-hidden="true"></span>remove
                                    </a>
                                </td>
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