<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="issuetracking.*"%>
<%@ page import="java.util.*"%>

<%!private static final DBManager DBManager1 = DBManager.getInstance();

	private static final String PAGE_INDEX = "http://localhost:8080/ITS_1/index.jsp";
	private static final String PAGE_TICKETVIEW = "http://localhost:8080/ITS_1/ticketview.jsp";
	private static final String PAGE_LOGOUT = "http://localhost:8080/ITS_1/logout.jsp";

	private static final String FIELD_TICKET_ID = "ticket_id";
	private static final String FIELD_TITLE = "title";
	private static final String FIELD_DESCRIPTION = "description";
	private static final String ACTION_CHANGE = "change";
	private static final String ACTION_DELETE = "delete";%>

<%
	Cookie[] cookies=request.getCookies(); 
if(!DBManager1.checkLogin(Cookies.getValue(cookies,"user"),Cookies.getValue(cookies,"password"))){	
	response.sendRedirect("login.jsp");
}

Ticket t1 =DBManager1.getTicketById(Integer.parseInt(request.getParameter(FIELD_TICKET_ID)));
Ticket t1Update =new Ticket();
t1Update.setId(t1.getId());
t1Update.setTitle(request.getParameter(FIELD_TITLE));
t1Update.setDescription(request.getParameter(FIELD_DESCRIPTION));

Map<String, String> errorMsgs = new HashMap<String, String>();
if (ACTION_CHANGE.equals(request.getParameter("action"))) 
	errorMsgs=t1Update.validate(); 
if(errorMsgs.isEmpty()&&ACTION_CHANGE.equals(request.getParameter("action"))){

DBManager1.updateTicket(t1Update);
DBManager1.loadTickets();	
t1 =DBManager1.getTicketById(Integer.parseInt(request.getParameter(FIELD_TICKET_ID)));
}
if(ACTION_DELETE.equals(request.getParameter("action"))){
DBManager1.deleteTicket(t1Update);	
	response.sendRedirect("index.jsp");
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	User:<%=Cookies.getValue(cookies,"user")%><a href=<%=PAGE_LOGOUT%>>
		logout </a> &nbsp;
	<a href=<%=PAGE_INDEX%>> back to index </a>
	<h1>Das Ticket:</h1>
	ID=<%=t1.getId()%>
	<br /> Title=<%=t1.getTitle()%>
	<br /> Description=
	<%=t1.getDescription()%><br />

	<h1>Change the Ticket</h1>
	<form action="ticketview.jsp" method="post">
		<input type="hidden" name="ticket_id"
			value="<%=request.getParameter(FIELD_TICKET_ID)%>" /> <input
			type="hidden" name="action" value="<%=ACTION_CHANGE%>" /> Title:<input
			name=<%=FIELD_TITLE%> type="text" />
		<%=(errorMsgs.get(FIELD_TITLE) != null) ? errorMsgs.get(FIELD_TITLE) : ""%><br />

		Description:<input name=<%=FIELD_DESCRIPTION%> type="text" />
		<%=(errorMsgs.get(FIELD_DESCRIPTION) != null) ? errorMsgs.get(FIELD_DESCRIPTION) : ""%><br />
		<input type="submit" value="change the Ticket">
	</form>


	<form action="ticketview.jsp" method="post">
		<input type="hidden" name="ticket_id"
			value="<%=request.getParameter(FIELD_TICKET_ID)%>" /> <input
			type="hidden" name="action" value="<%=ACTION_DELETE%>" /> <input
			type="submit" value="delete the Ticket">
	</form>

</body>
</html>