<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	작성자 : ${writer } <br>
	파일이름 : ${filename }	<br>
	<img src="displayFile?fileName=${filename}" width="500px" />
	
	<!-- 브라우저가 이미지의 파일 경로를 가져오려는데 src가 서버의 command이면 server에 display시킴 -->
</body>
</html>