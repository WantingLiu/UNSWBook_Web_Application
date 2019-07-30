<!DOCTYPE html>
<html>
<head>
<title>User History</title>
<%@ include file="_nav.jsp"%>
</head>
<body>
	<div class="container page-wrap">
		<div class="row">
			<div class="col-sm-4 sidebar">
				<h6>Filter By:</h6>
				<form action="userHistory" method="post">
					<input type="hidden" name="username" value="${username}">
					<div class="form-group">
					    <label for="type">Type</label>
					    <select class="form-control" name="type">
					    		<option value="all">All</option>
					    		<option value="register">Register</option>
					    		<option value=friendReq>Friend Request</option>
					    		<option value="reqAccepted">Friendship Formed</option>
					    		<option value="post">Post</option>
					    		<option value="comment">Comment</option>
					    		<option value="like">Like</option>
					    		<option value="unlike">Unlike</option>
					    		<option value="ban">Ban</option>
					    		<option value="unban">Unban</option>
								<option value="bullyPost">Post Relates To Bullying</option>
					    </select>
				  	</div>
					<div class="form-group"><br>
						<label for="gender">From</label>
						<input class="form-control date" name="from" type="date">
					</div>
					<div class="form-group"><br>
						<label for="gender">To</label>
						<input class="form-control date" name="to" type="date">
					</div>
					<button class="btn btn-primary" type="submit">Filter User Histories</button>
				</form>
			</div>
			<!-- populate search results -->
			<div class="col-sm-8">
				<c:forEach items="${userHistories}" var="userHistory">
					<div>
						<small>${ userHistory.getDatetime() }</small><br>
						<c:if test="${userHistory.getType() == 'register'}">
							${userHistory.getUsername()} registered.
						</c:if>
						<c:if test="${userHistory.getType() == 'friendReq'}">
							${userHistory.getUsername()} sent ${userHistory.getUsername_opt()} a friend request.
						</c:if>
						<c:if test="${userHistory.getType() == 'reqAccepted'}">
							${userHistory.getUsername()} became friends with ${userHistory.getUsername_opt()}.
						</c:if>
						<c:if test="${userHistory.getType() == 'acceptReq'}">
							${userHistory.getUsername()} accepted ${userHistory.getUsername_opt()}'s friend request.
						</c:if>
						<c:if test="${userHistory.getType() == 'post'}">
							${userHistory.getUsername()} posted on ${userHistory.getUsername_opt()}'s wall.
						</c:if>
						<c:if test="${userHistory.getType() == 'comment'}">
							${userHistory.getUsername()} commented on ${userHistory.getUsername_opt()}'s post.
						</c:if>
						<c:if test="${userHistory.getType() == 'like'}">
							${userHistory.getUsername()} liked ${userHistory.getUsername_opt()}'s post.
						</c:if>
						<c:if test="${userHistory.getType() == 'unlike'}">
							${userHistory.getUsername()} unliked ${userHistory.getUsername_opt()}'s post.
						</c:if>
						<c:if test="${userHistory.getType() == 'ban'}">
							${userHistory.getUsername()} was banned.
						</c:if>
						<c:if test="${userHistory.getType() == 'unban'}">
							${userHistory.getUsername()} was unbanned.
						</c:if>
						<c:if test="${userHistory.getType() == 'bullyPost'}">
							${userHistory.getUsername()} posted a message contains any keywords related to Bullying.
						</c:if>
					</div>
					<hr>
				</c:forEach>
			</div>
		</div>
	</div>
</body>
<%@ include file="_footer.jsp" %>
</html>