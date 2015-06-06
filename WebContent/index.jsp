<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="issuetracking.*"%>

<% 
DBManager DBManager1 = DBManager.getInstance();
// Wenn der Nutzer nicht angemeldet ist wird er zu login.jsp geleitet
if (!DBManager1.checkLogin((String) request.getSession()
		.getAttribute("user"), (String) request.getSession()
		.getAttribute("password"))) {
	request.getRequestDispatcher("login.jsp").forward(request,
			response);
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<style>
table, th, td {
	border: 1px solid black;
	border-collapse: collapse;
}

th, td {
	padding: 5px;
}

th {
	text-align: left;
}
</style>
</head>

<body>

	User:
	<a
		href=${'Controller?action=preparePage&pageName=userpage.jsp&user_id='.concat(sessionScope.user)}>
		${sessionScope.user}</a>
	<a href="Controller?action=logout"> logout </a>

	<h1>New Ticket</h1>
	<form action="Controller" method="post">
		<input type="hidden" name="action" value="addTicket" /> 
		Title:<input name="title" type="text" /> ${errorMsgs.title}<br /> 
		Description:<input name="description" type="text" /> ${errorMsgs.description}<br /> 
		<input type="hidden" name="date" value="${date1}" /> 
		<input type="hidden" name="author" value="${sessionScope.user}" /> 
		Responsible user:<select name="responsible_user">
		<c:forEach items="${users}" var="user1">
			<option value="${user1.userid}">${user1.userid}</option>
		</c:forEach>
		</select> ${errorMsgs.responsible_user}<br /> Type:<select name="type">
			<option value="bug">bug</option>
			<option value="feature">feature</option>
		</select> ${errorMsgs.type}<br /> State:<select name="state">
			<option value="open">open</option>
			<option value="closed">closed</option>
			<option value="in progress">in progress</option>
			<option value="test">test</option>
		</select> ${errorMsgs.state}<br /> Estimated time:<input name="estimated_time"
			type="text" />(only for features) ${errorMsgs.estimated_time}<br />
		<input type="submit" value="add ticket">
	</form>

	<h1>Tickets</h1>

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
		<c:forEach items="${tickets}" var="ticket1">
			<tr>

				<td>${ticket1.id}</td>
				<td><a
					href=${"Controller?action=preparePage&pageName=ticketview.jsp&ticket_id=".concat(ticket1.id)}>
						${ticket1.title} </a></td>
				<td>${ticket1.type}</td>
				<td>${fn:length(ticket1.description) gt 25 ? fn:substring(ticket1.description, 0, 25).concat("..."):ticket1.description}
				</td>
			</tr>
			<option value="${user1.userid}">${user1.userid}</option>
		</c:forEach>
	</table>



	<h1>Session attributes:</h1>
	<%
		for (Enumeration<String> e = session.getAttributeNames(); e.hasMoreElements(); ) {     
		    String attribName = (String) e.nextElement();
		    Object attribValue = session.getAttribute(attribName);
	%>
	<BR><%=attribName%>
	-
	<%=attribValue%>

	<%
		}
	%>
	<%
		Cookie[] cookies=request.getCookies();
	%>

	<h1>Request-Cookies</h1>
	<%
		for(Cookie c1 : cookies) {
	%>
	<%=c1.getName()%>=
	<%=c1.getValue()%>
	<br />

	<%
		};
	%>
	<h1>Parameters</h1>
	<%
		for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements(); ) {     
		    String attribName = e.nextElement();
		    String[] attribValues = request.getParameterValues(attribName);
		    String allValues="";
		    for(String s:attribValues){
		    	allValues=allValues+" "+s;
		    }
	%>
	<%=attribName%>=
	<%=allValues%><br />
	<%
		};
	%>
</body>
</html>