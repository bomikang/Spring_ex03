<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>
<%
	System.out.println("Home.jsp");
%>

<P>  The time on the server is ${serverTime}. </P>
<p>${result }</p>
</body>
</html>
