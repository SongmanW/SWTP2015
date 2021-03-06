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
	<a href=${'Controller?action=preparePage&pageName=userpage.jsp&user_id='.concat(sessionScope.user)}>
		${sessionScope.user}</a>
	<a href="Controller?action=logout"> logout </a>&nbsp;
	<a href="Controller?action=preparePage&pageName=index.jsp"> back to
		index </a>

	<h1>New Sprint</h1>
	<form action="Controller" method="post">
		<input type="hidden" name="action" value="addSprint" /> 
		Title:<input name="title" type="text" /> ${errorMsgs.title}<br> 
		Start of Sprint:<br>
		Year<input name="y1" type="text" value="2015" /> ${errorMsgs.title}<br>
		Month<input name="m1" type="text" value="1"/> ${errorMsgs.title}<br>
		Day<input name="d1" type="text" value="1" /> ${errorMsgs.title}<br>
		
		End of Sprint:<br>
		Year<input name="y2" type="text" value="2016" /> ${errorMsgs.title}<br>
		Month<input name="m2" type="text" value="8"/> ${errorMsgs.title}<br>
		Day<input name="d2" type="text" value="8" /> ${errorMsgs.title}<br>
		
	Tickets without sprint :<br>
		<c:forEach items="${nosprinttickets}" var="tick1">
			<input type="checkbox" name="tickids" value="${tick1.id}">${tick1.title}
			<br>
		</c:forEach>
		<input type="submit" value="add sprint">
		
		
		
		
		</form>

	<h1>Sprints</h1>

	<table>
		<col width="100">
		<col width="200">
		<tr>
			<th>Title</th>
			<th>Tickets</th>
			<th>start</th>
			<th>end</th>
		</tr>
		<c:forEach items="${sprints}" var="sprint1">
			<tr>
				<td><a     
					href=${"Controller?action=preparePage&pageName=sprintview.jsp&sprintid=".concat(sprint1.getSprintid())}>
						${sprint1.title} </a></td>
					<td><a     
					href=${"Controller?action=preparePage&pageName=sprintstickets.jsp&sprintid=".concat(sprint1.getSprintid())}>
						tickets </a></td>	
						<td>   
						${sprint1.getStartDateAsString()} </td>
						<td>   
						${sprint1.getEndDateAsString()}</td>		
			</tr>
		</c:forEach>
	</table>
	<br>
	<br>
	<br>
	<a     
					href=${"Controller?action=preparePage&pageName=index.jsp&sprintid=-2"}>
						all tickets </a>
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