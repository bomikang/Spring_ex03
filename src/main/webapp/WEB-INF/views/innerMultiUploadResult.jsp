<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	작성자 : ${writer } <br>
	<c:forEach items="${files }" var="item">
	파일이름 : ${item }	<br>
	<img src="${item }" width="500px" />
	</c:forEach>
</body>
</html>