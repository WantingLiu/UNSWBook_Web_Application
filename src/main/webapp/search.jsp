<%@ page import="DAOs.User" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
<title>Search</title>
<%@ include file="_nav.jsp"%>
<link rel="stylesheet" href="css/search_result.css">
<script src="js/notification_handler.js"></script>
</head>
<body>
	<div class="container page-wrap row">
		<div class="col-sm-4 sidebar">
			<ul>
				<h6>Filter By:</h6>
				<form action="search" method="post">
					<input type="hidden" name="advanced_search" value="true">
					<div class="form-group">
						<label for="first_name">First Name</label><br>
						<input class="form-control" name="first_name">
					</div>
					<div class="form-group">
						<label for="last_name">Last Name</label><br>
						<input class="form-control" name="last_name">
					</div>
					<div class="form-group">
						<label for="gender">Gender</label> <br>
						<div class="btn-group" data-toggle="buttons">
							<label class="btn btn-secondary">
								<input name="gender" type="radio" value="male">
								<i class="fa fa-mars" aria-hidden="true"></i>
							</label>
							<label class="btn btn-secondary">
								<input name="gender" type="radio" value="female">
								<i class="fa fa-venus" aria-hidden="true"></i>
							</label>
						</div>
					</div>
					<div class="form-group">
						<br> <label for="date">Date of Birth</label> <input
							class="form-control date" name="date" type="date">
					</div>
					<button class="btn btn-primary" type="submit">Advanced Search</button>
				</form>
			</ul>
		</div>
		<!-- populate search results -->
		<div class="col-sm-8">
			<c:forEach items="${users}" var="user">
				<div class="user row">
					<img  class="img-responsive avatar" height="85px" width="85px" style="border-radius:50%; margin-right:15px" src="${user.getPic()}">
					<div class="row col-sm-6">
						<div>
							<div>
								<p>
									<a href="profile?username=${user.getUsername()}">${user.getFirstName()}
										${user.getLastName()}</a>
								<p>
							</div>
							<div>
								<small>UNSW</small>
							</div>
						</div>
					</div>
					<div>
						<c:if test="${current_user.getAdmin()}">
							<c:if test="${not user.getBanned()}">
								<button id="${user.getUsername()}" class="toggleBan btn btn-default" name="ban" type="button"
									style="text-align: right"><i class="fa fa-ban" aria-hidden="true"></i> BAN</button>
							</c:if>
							<c:if test="${user.getBanned()}">
								<button id="${user.getUsername()}" class="toggleBan btn btn-default" name="unban"  type="button"
									style="text-align: right">UNBAN</button>
							</c:if>
							<br>
							<small><a href="userHistory?username=${user.getUsername()}">user report</a></small>
						</c:if>
						<c:if test="${not current_user.getAdmin()}">
							<c:if test="${!current_user.getFriends().contains(user.getUsername())}">
								<c:if test="${!users_requests_sent.contains(user.getUsername())}">
									<c:if test="${!current_user.getUsername().equals(user.getUsername())}">
										<button id="${user.getUsername()}" class="addFriend btn btn-primary" name="${user.getEmail()}" type="button">
											<i class="fa fa-user-plus" aria-hidden="true"></i>
										</button>
									</c:if>
								</c:if>
								<c:if test="${users_requests_sent.contains(user.getUsername())}">
									<button class="btn btn-primary" type="submit">
										Request sent <i class="fa fa-check"></i>
									</button>
								</c:if>
							</c:if>
							<c:if test="${current_user.getFriends().contains(user.getUsername())}">
								<button class="btn btn-primary" type="submit">
									Already friends <i class="fa fa-users"></i>
								</button>
							</c:if>
						</c:if>
					</div>
				</div>
			</c:forEach>
		</div>
	</div>
</body>
<%@ include file="_footer.jsp" %>
</html>