<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<style>
#dropbox{
	width:400px;
	height:300px;
	border:1px dotted gray;
	overflow: auto;
}
#dropbox img{
	width:100px;
	height:130px;
	margin:5px;
}
#dropbox div.item{
	width:100px;
	float:left;
	position:relative;
	margin:5px;
}
#dropbox button.del{
	position:absolute;
	right:3px;
	top:3px;
	font-weight:bold;
}
</style>
</head>
<body>
	<form action="dragUpload" method="post" enctype="multipart/form-data" id="form1" >
		<input type="text" name="writer" id="writer" placeholder="작성자(밑에 네모칸에 이미지를 드래그 해보십시오)" />
		<input type="submit" />
	</form>
	
	<div id="dropbox"></div>
	
	<script>
		var formdata = new FormData(); //Controller로 파일을 넘기기 위한 formdata
	
		$("#dropbox").on("dragenter dragover", function(event){
			event.preventDefault(); //링크 방지
		});
		$("#dropbox").on("drop", function(event){
			event.preventDefault(); //링크 방지
			
			/* 드래그를 했을 시 넘어오는 파일 중
				event.originalEvent : jquery에서 받은 event의 원본 javascript이벤트 */
			var files = event.originalEvent.dataTransfer.files;
			var file = files[0];
			console.log(file);
			
			/* 드래그한 이미지를 dropbox에 넣기 */
			var reader = new FileReader();
			reader.addEventListener("load", function(){
				var $img = $("<img>");
				$img.attr("src", reader.result);
				$("#dropbox").append($img);
			});
			if(file){ //파일이 존재하면 reader가 file을 읽게 된다
				reader.readAsDataURL(file);

				if(formdata != null){
					/* formdata로 이미지 파일을 저장 
						=> 여러개를 드래그 하였을 시를 대비하여 list로 받아온다고 생각해야함*/
					formdata.append("files", file);
				}
			}
		});
		
		/* submit할 때 */
		$("#form1").on("submit", function(event){
			event.preventDefault();
			formdata.append("writer", $("#writer").val());
			
			$.ajax({
				url:"dragUpload",
				data:formdata,
				processData:false, //formdata를 사용할 때는 processData:false, contentType:false 필수
				contentType:false,
				type:"post",
				success:function(data){
					console.log("썸네일 이미지 : "+data);
					
					$("#dropbox").empty();
					$(data).each(function(i, obj){
						/* 이미지 위에 div를 씌워 그 위에 X버튼을 넣음 */
						var $div = $("<div>").addClass("item");
						var $img = $("<img>");
						$img.attr("src", "displayFile?fileName="+obj);
						$img.attr("alt", obj); //delete를 위하여 파일이름심기
						$div.append($img);
						
						var $button = $("<button>").addClass("del").text("X"); //오른쪽 상단 X버튼
						$div.append($button);
						
						$("#dropbox").append($div);
					});
				}
			});
		});
		
		/* X버튼을 클릭하면 각 이미지가 실제로 삭제되도록 */
		$(document).on("click", "button.del", function(){
			var $button = $(this);
			var file = $(this).parent("div").find("img").attr("alt");
			
			$.ajax({
				url:"deleteFile",
				type:"post",
				data:{fileName : file},
				success:function(result){
					$button.parent("div").remove();
				}
			});
		});
	</script>
</body>
</html>