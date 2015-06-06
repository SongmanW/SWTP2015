<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="issuetracking.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>

<% 
DBManager DBManager1 = DBManager.getInstance();
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
</head>
<body>

	User:
	<a
		href=${'Controller?action=preparePage&pageName=userpage.jsp&user_id='.concat(sessionScope.user)}>
		${sessionScope.user}</a> &nbsp;
	<a href="Controller?action=logout"> logout </a> &nbsp;
	<a href="Controller?action=preparePage&pageName=index.jsp"> back to
		index </a>


	<h1>The ticket:</h1>
	ID=${t1.id}<br> 
	Title=${t1.title}<br> 
	Description=${t1.description}<br> 
	Date=${t1.date}<br> 
	Author=${t1.author}<br>
	Responsible User=${t1.responsible_user}<br>
	Type=${t1.type}<br> 
	State=${t1.state}<br> 
	Estimated_Time= (to do....)


	<h1>Change the ticket</h1>
	<form action="Controller" method="post">
		<input type="hidden" name="ticket_id" value="${t1.id}" /> 
		<input type="hidden" name="action" value="changeTicket" /> 
		Title:<input name="title" type="text" /> ${errorMsgs.title}<br /> 
		Description:<input name="description" type="text" /> ${errorMsgs.description}<br /> 
		<input type="hidden" name="date" value="${date1}" /> ${errorMsgs.date} 
		<input type="hidden" name="author" value="${sessionScope.user}" />
		Responsible user:
		<select name="responsible_user">
			<c:forEach items="${users}" var="user1">
				<option value="${user1.userid}">${user1.userid}</option>
			</c:forEach>
		</select> ${errorMsgs.responsible_user}<br /> 
		Type:
		<select name="type">
			<option value="bug">bug</option>
			<option value="feature">feature</option>
		</select> ${errorMsgs.type}<br /> 
		State:
		<select name="state">
			<option value="open">open</option>
			<option value="closed">closed</option>
			<option value="in progress">in progress</option>
			<option value="test">test</option>
		</select><br> 
		Estimated time:<input name="estimated_time" type="text" />(onlyfor features) ${errorMsgs.estimated_time}<br /> 
		<input type="submit" value="change the ticket">
	</form>


	<form action="Controller" method="post">
		<input type="hidden" name="ticket_id" value="${t1.id}" /> 
		<input type="hidden" name="action" value="deleteTicket" /> 
		<input type="submit" value="delete the ticket">
	</form>
	
	<h1>Request-Cookies</h1>
	<%
	Cookie[] cookies=request.getCookies(); 
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