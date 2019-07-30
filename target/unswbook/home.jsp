<%@ page import="java.awt.image.BufferedImage" %>
<%@ page import="javax.imageio.ImageIO" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.ByteArrayOutputStream" %>
<%@ page import="javax.xml.bind.DatatypeConverter" %>
<%@ page import="java.io.IOException" %>
<%@ page import="org.apache.commons.lang3.tuple.*"%>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
	<title>Home</title>
	<%@ include file="_nav.jsp" %>
	<script src="js/home_polling.js"></script>
</head>
<script>
	var current_user = `${current_user.getUsername()}`;
</script>
<body>
	<div class="container page-wrap">
		<form action="home" method="post" enctype="multipart/form-data" style="margin-top:20px; margin-bottom:30px;" onsubmit="setTimeout(function () { window.location.reload(); }, 20)">
			<input id="upload" type="file" name="pic" accept="image/*" style="display:none">
			<div class="form-group">
				<div>
					<div class="btn-group" data-toggle="buttons">
						<a href="#" class="btn btn-default"><i class="fa fa-pencil" aria-hidden="true"></i> Create Post</a>
						<a href="#" class="btn btn-default" id="upload_button"><i class="fa fa-picture-o" aria-hidden="true"></i> Upload Image</a>
					</div>
				</div>
				<textarea rows="4" class="form-control" name="content" placeholder="Say something..."></textarea>
				<small class="form-text text-muted" style="display:none">You have to write something.</small>
			</div>
		 	<button class="btn btn-primary" type="submit">Post</button>
		</form>
		<div id="posts"></div>
	</div>
</body>
<%@ include file="_footer.jsp" %>
</html>
