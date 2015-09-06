<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="issuetracking.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Components</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
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
            <li class="active"><a href="${pageContext.request.contextPath}/user/components.jsp">Components</a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li><a href="${pageContext.request.contextPath}/user/userpage.jsp"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>
		${pageContext.request.userPrincipal.name}</a></li>
            <li><a href="${pageContext.request.contextPath}/user/?action=logout">logout</a></li>
        </ul>
    </div>
</nav>
<a class="btn btn-success" role="button" data-toggle="collapse" href="#new-component" aria-expanded="false" aria-controls="new-component">
            <span class="glyphicon glyphicon-plus"></span>new Component
        </a>
        <div class="collapse well" id="new-component">
	<h1>New Component</h1>
	<form action="user" method="post">
		<input type="hidden" name="action" value="addComponent" /> 
		Title:<input name="comp_id" type="text" /> ${errorMsgs.title}<br> 
		Description:<br><textarea name="description" cols="65" rows="5" wrap="off" style="overflow-y: auto; overflow-x: auto;;font-size:70%"></textarea> ${errorMsgs.description}<br /> 
		<input type="submit" value="add component">
	</form>
        </div>

	<h1>Components</h1>

	<table>
		<col width="100">
		<col width="200">
		<tr>
			<th>Title</th>
			<th>Description</th>
		</tr>
		<c:forEach items="${components}" var="comp1">
			<tr>
				<td><a
					href="${pageContext.request.contextPath.concat("/user/componentview.jsp?compid=").concat(comp1.compid)}">
						${comp1.compid} </a></td>
				<td>${fn:length(comp1.description) gt 25 ? fn:substring(comp1.description, 0, 25).concat("..."):comp1.description}
				</td>
			</tr>
		</c:forEach>
	</table>
	<br>
	
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