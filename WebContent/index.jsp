<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="issuetracking.*"%>

<%!
private static final DBManager DBManager1 = DBManager.getInstance();

private static final String PAGE_INDEX="http://localhost:8080/ITS_1/index.jsp";
private static final String PAGE_TICKETVIEW="http://localhost:8080/ITS_1/ticketview.jsp";
private static final String PAGE_LOGOUT="http://localhost:8080/ITS_1/logout.jsp"; 

private static final String ACTION_ADD = "add";
private static final String FIELD_TITLE = "title";
private static final String FIELD_DESCRIPTION = "description";

public static String setString(String str, int max) {
    String str2 = str.length() > max ? str.substring(0, max) : str;
    return str2;
};  
%>
<%
	Cookie[] cookies=request.getCookies(); 

//Wenn der Nutzer angemeldet ist wird er zu index.jsp geleitet
if(!DBManager1.checkLogin(Cookies.getValue(cookies,"user"),Cookies.getValue(cookies,"password"))){	
		response.sendRedirect("login.jsp");

}

//Map<Integer, Ticket> tickets = new HashMap<Integer, Ticket>();
Map<String, String> errorMsgs = new HashMap<String, String>();
DBManager1.loadTickets();
Ticket t = new Ticket();
t.setId(DBManager1.getNextId());
t.setTitle(request.getParameter(FIELD_TITLE));
t.setDescription(request.getParameter(FIELD_DESCRIPTION)); 
if (ACTION_ADD.equals(request.getParameter("action"))) {
	errorMsgs=t.validate(); 

}
if(errorMsgs.isEmpty()&&ACTION_ADD.equals(request.getParameter("action"))){
	DBManager1.saveTicket(t);
DBManager1.loadTickets();	
 
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

	User:<%=Cookies.getValue(cookies,"user")%>
	<a href=<%=PAGE_LOGOUT %>> logout </a>
	<h1>New Ticket</h1>  
	<form action="index.jsp" method="post">
		<input type="hidden" name="action" value="<%=ACTION_ADD%>" /> Title:<input
			name="<%=FIELD_TITLE%>" type="text" />
		<%=(errorMsgs.get(FIELD_TITLE) != null) ? errorMsgs.get(FIELD_TITLE) : ""%><br />
		Description:<input name="<%=FIELD_DESCRIPTION%>" type="text" />
		<%=(errorMsgs.get(FIELD_DESCRIPTION) != null) ? errorMsgs.get(FIELD_DESCRIPTION) : ""%><br />
		<input type="submit" />
	</form>

	<h1>Tickets</h1>

	<table>
		<col width="30">
		<col width="100">
		<col width="200">
		<tr>
			<th>ID</th>
			<th>Title</th>
			<th>Description</th>
		</tr>

<%
List<Ticket> tickets=DBManager1.getTickets();
for(Ticket t1 : tickets) {
%>
		<tr>
			<td><%=t1.getId() %></td>
			<td><a href=<%=PAGE_TICKETVIEW + "?ticket_id="+t1.getId() %>>
					<%=t1.getTitle() %>
			</a></td>
			<td><%=setString(t1.getDescription(),25)%><%=(t1.getDescription().length()>25) ? "..." : ""%></td>
		</tr>
<%
}
%>
	</table>


</body>
</html>