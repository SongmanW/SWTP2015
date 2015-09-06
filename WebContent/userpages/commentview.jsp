<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="issuetracking.*"%>
<%@ page import="java.util.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Kommentaransicht</title>
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
	<h1>The comment:</h1>
	CommentID=${c1.cid}<br>
	Ticket= ${c1.tid}<br>
	Author=${c1.author} <br>
	Creation date=${c1.creation_date} <br>
	Message=${c1.message} <br>
	
	<h1>Change the comment</h1>
	<form action="user" method="post"> 
		<input type="hidden" name="action" value="changeComment" /> 
		<input type="hidden" name="comment_id" value="${c1.cid}" />
		Message:<input name="message" type="text" />${errorMsgs.message}<br />
		<input type="submit" value="change the comment">
	</form>

	<form action="user" method="post">
		<input type="hidden" name="cid" value="${c1.cid}" />  
		<input type="hidden" name="ticket_id" value="${c1.tid}" />  
		<input type="hidden" name="action" value="deleteComment" /> 
		<input type="submit" value="delete the comment">
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