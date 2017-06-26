<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="innerMultiUpload" method="post" enctype="multipart/form-data">
		<fieldset>
			<input type="text" name="writer" placeholder="작성자" />
			<input type="file" name="files" multiple="multiple">
			<input type="submit" value="제출" />
		</fieldset>
	</form>	
</body>
</html>