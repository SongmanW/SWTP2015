<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="issuetracking.*"%>
 
<%! 
private static final DBManager DBManager1 = DBManager.getInstance();

private static final String PAGE_INDEX="http://localhost:8080/ITS_1/index.jsp";
private static final String PAGE_TICKETVIEW="http://localhost:8080/ITS_1/ticketview.jsp";
 
private static final String ACTION_LOGIN = "login";
private static final String ACTION_REGISTER = "register";
private static final String FIELD_USERIDINPUT = "useridinput";
private static final String FIELD_PASSWORDINPUT = "passwordinput";

public Map<String, String> validateUserRegistration(String useridinput, String passwordinput) {
	Map<String, String> errorMsg = new HashMap<String, String>();
	if (useridinput == null || useridinput.trim().equals(""))// field is empty
		errorMsg.put("useridinput", "useridinput darf nicht leer sein!");
	if (DBManager1.containsUser(useridinput))// username taken
		errorMsg.put("useridinput", "Der Username wird schon verwendet!");
	if (passwordinput == null || passwordinput.trim().equals(""))// field is empty
		errorMsg.put("passwordinput", "Passwort darf nicht leer sein!");
	return errorMsg;
}

public Map<String, String> validateUserLogin(String useridinput, String passwordinput) {
	Map<String, String> errorMsg = new HashMap<String, String>();
	if (useridinput == null || useridinput.trim().equals(""))// field is empty
		errorMsg.put("useridinput", "useridinput darf nicht leer sein!");
	if (passwordinput == null || passwordinput.trim().equals(""))// field is empty
		errorMsg.put("passwordinput", "Passwort darf nicht leer sein!");
	return errorMsg;
}


%>  
<%
Cookie[] cookies=request.getCookies(); 
if(DBManager1.checkLogin(Cookies.getValue(cookies,"user"),Cookies.getValue(cookies,"password"))){	
%>
<jsp:forward page="index.jsp" />
<% 
}

	Map<String, String> errorMsgsLogin = new HashMap<String, String>();
	Map<String, String> errorMsgsReg = new HashMap<String, String>();
	String regSuccess=null;
    String useridinput = request.getParameter("useridinput");    
    String passwordinput = request.getParameter("passwordinput");
    
    if (ACTION_REGISTER.equals(request.getParameter("action"))) 
    errorMsgsReg=validateUserRegistration(useridinput,passwordinput); 
    if(errorMsgsReg.isEmpty()&&ACTION_REGISTER.equals(request.getParameter("action"))){
    DBManager1.registerUser(useridinput,passwordinput);
    regSuccess="Du wurdest registriert";
    }
    if (ACTION_LOGIN.equals(request.getParameter("action"))) 
        errorMsgsLogin=validateUserLogin(useridinput,passwordinput); 
    if(errorMsgsLogin.isEmpty()&&ACTION_LOGIN.equals(request.getParameter("action"))){
        if(DBManager1.checkLogin(useridinput,passwordinput)){
        	Cookie cookie1=new Cookie("user",useridinput);
        	Cookie cookie2=new Cookie("password",passwordinput);
        	response.addCookie(cookie1);
        	response.addCookie(cookie2);

		response.sendRedirect("index.jsp");
        }else{
        	errorMsgsLogin.put("useridinput","Wrong username or password");
        	
        }

   }
    

%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
<%=(regSuccess != null) ? regSuccess : ""%>
<h1>Login</h1> 
	<form action="login.jsp" method="post">
		<input type="hidden" name="action" value="<%=ACTION_LOGIN%>"/>
		Username:<input name="<%=FIELD_USERIDINPUT%>" type="text"/>
		<%=(errorMsgsLogin.get(FIELD_USERIDINPUT) != null) ? errorMsgsLogin.get(FIELD_USERIDINPUT) : ""%><br />
		Password:<input name="<%=FIELD_PASSWORDINPUT%>" type="text"/>
		<%=(errorMsgsLogin.get(FIELD_PASSWORDINPUT) != null) ? errorMsgsLogin.get(FIELD_PASSWORDINPUT) : ""%><br />
		<input type="submit"/> 
	</form>
	<h1>Register</h1>
	<form action="login.jsp" method="post">
		<input type="hidden" name="action" value="<%=ACTION_REGISTER%>"/>
		Username:<input name="<%=FIELD_USERIDINPUT%>" type="text"/>
		<%=(errorMsgsReg.get(FIELD_USERIDINPUT) != null) ? errorMsgsReg.get(FIELD_USERIDINPUT) : ""%><br />
		Password:<input name="<%=FIELD_PASSWORDINPUT%>" type="text"/>
		<%=(errorMsgsReg.get(FIELD_PASSWORDINPUT) != null) ? errorMsgsReg.get(FIELD_PASSWORDINPUT) : ""%><br />
		<input type="submit"/> 
	</form>
	
</body>
</html>