<%@ page import="java.awt.image.BufferedImage"%>
<%@ page import="javax.imageio.ImageIO"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.ByteArrayInputStream"%>
<%@ page import="java.io.ByteArrayOutputStream"%>
<%@ page import="javax.xml.bind.DatatypeConverter"%>
<%@ page import="java.io.IOException"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
<title>Profile</title>
<%@ include file="_nav.jsp"%>
</head>
<script src="js/profile_polling.js"></script>
<script>
	var current_user = `${current_user.getUsername()}`;
	var user = `${user.getUsername()}`;
</script>
<body>
	<div class="container page-wrap">
		<div class="row">
			<div class="col-sm-5">
				<div class="card">
					<div class="card-header text-white gray">
						Profile <c:if test = "${user.getUsername() == current_user.getUsername()}"><a href="edit_profile"><i class="fa fa-pencil float-right" aria-hidden="true" style="color:white; margin-top:2px"></i></a></c:if> 
					</div>
					<div class="card-block">
						<img class="img-responsive avatar" width=200px height=200px style="border-radius: 50%" src="${user.getPic()}"><br><br>
						<b>${user.getFirstName()} ${user.getLastName()}</b>
						<c:if test= "${user.getGender() == 'male'}">
							<i class="fa fa-mars" aria-hidden="true" style="color:#1a237e"></i>
						</c:if>
						<c:if test= "${user.getGender() == 'female'}">
							<i class="fa fa-venus" aria-hidden="true" style="color:pink"></i>
						</c:if>
						<br><br>
						<i class="fa fa-birthday-cake" aria-hidden="true"></i> ${user.getBirthDate()} <br>
						<i class="fa fa-envelope-o" aria-hidden="true"></i> ${user.getEmail()} <br>
					</div>
				</div>
				<div class="card">					
					<div class="card-header text-white gray">
						Friends <i class="fa fa-address-book-o" aria-hidden="true"></i>
							${fn:length(user.getFriends())}
					</div>
					<ul class="list-group list-group-flush">
						<c:forEach items="${user.getFriends()}" var="friend">
							<li class="list-group-item">
								<c:if test="${current_user.getUsername().equals(user.getUsername())}">
								<form action="friendDelete" method="post">
									<input type="hidden" name="friendname" value="${friend}">
									<button class="btn btn-primary d-flex justify-content-end" name="deleteFriend" type="submit"><i class="fa fa-trash-o" aria-hidden="true"></i></button>
								</form>
								</c:if>
								<a href="profile?username=${friend}" style="margin-left:10px"><b>${friend}</b></a>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
			<div class="col-sm-7">
				<c:if test="${current_user.getFriends().contains(user.getUsername()) || current_user.getUsername() == user.getUsername()}">
				<form class="new_post" enctype="multipart/form-data" action="profile" method="post" style="margin-top:10px; margin-bottom:25px" onsubmit="setTimeout(function () { window.location.reload(); }, 20)">
					<input id="upload" type="file" name="pic" accept="image/*" style="display: none">
					<div class="form-group">
					<div>
						<div class="btn-group" data-toggle="buttons">
							<a href="#" class="btn btn-default"><i class="fa fa-pencil"
								aria-hidden="true"></i> Create Post</a> <a href="#"
								class="btn btn-default" id="upload_button"><i
								class="fa fa-picture-o" aria-hidden="true"></i> Upload Image</a>
						</div>
					</div>
					<textarea rows="4" class="form-control" name="content" placeholder="Say something..."></textarea>
					<small class="form-text text-muted" style="display: none">You have to write something.</small>
					<input type="hidden" name="username" value="${user.getUsername()}">
					</div>
					<div align="right">
						<button class="btn btn-primary" style="margin-left: 15px" type="submit">Post</button>
					</div>
				</form>
				</c:if>
				<div id="posts"></div>
			</div>
		</div>
	</div>
</body>
<%@ include file="_footer.jsp"%>
</html>
