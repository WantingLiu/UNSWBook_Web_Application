<!DOCTYPE html>
<html>
<head>
<title>Notifications</title>
<%@ include file="_nav.jsp"%>
<script src="js/notification_handler.js"></script>
</head>
<body>
	<div class="container page-wrap">
		<br>
		<div class="card">
			<div class="card-header text-white gray">
				<b>Notifications
			</div>
			<div class="card-block">
				<c:forEach items="${notifications}" var="notification">
					<div class=""card-block">
					<c:if test="${notification.getType() == 'friendReq'}">
						<h4>Friend Request</h4>
						<small>${notification.getDatetime()}</small>
						<br>
						<p>${notification.getFromUser()}</p>
						<button id="${notification.getObjectId()}"
							class="acceptFriend btn btn-primary btn-sm" type="submit">
							Accept <i class="fa fa-user-plus" aria-hidden="true"></i>
						</button>
					</c:if>
					<c:if test="${notification.getType() == 'reqAccepted'}">
						<small>${notification.getDatetime()}</small>
						<br>
						${notification.getFromUser()} accepted your friend request.<br>
					</c:if>
					<c:if test="${notification.getType() == 'like'}">
						<small>${notification.getDatetime()}</small>
						<br>
						${notification.getFromUser()} liked your post.<br>
					</c:if>
					<c:if test="${notification.getType() == 'comment'}">
						<small>${notification.getDatetime()}</small>
						<br>
						${notification.getFromUser()} commented on you post.<br>
					</c:if>
					<c:if test="${notification.getType() == 'bullyPost'}">
						<small>${notification.getDatetime()}</small>
						<br>
						${notification.getFromUser()} posted a message contains any keywords related to Bullying.<br>
					</c:if>
					</div>
					<hr>
				</c:forEach>
			</div>
		</div>
	</div>
</body>
<%@ include file="_footer.jsp"%>
</html>