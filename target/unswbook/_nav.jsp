<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="css/nav.css">
<link rel="stylesheet" href="css/main.css">
<script src="js/jquery-3.2.1.min.js"></script>
<script src="js/jquery.form.min.js"></script>
<script src="js/tether.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/nav_polling.js"></script>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>

<nav class="navbar navbar-toggleable-md navbar-inverse bg-inverse py-1">
	<button class="navbar-toggler navbar-toggler-right" type="button"
		data-toggle="collapse" data-target="#top_navbar"
		aria-controls="navbarSupportedContent" aria-expanded="false"
		aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	<h1 class="navbar-brand mb-0"><b><img src="assets/unsw.png" height="35px"> UNSW Book</b></h1>
	<% if (session.getAttribute("username") != null) { %>
	
	<div id="top_navbar" class="navbar-collapse collapse">
		<ul class="navbar-nav mr-auto">
			<form class="form-inline my-2 my-lg-0" action="search" method="post">
				<input name="search" class="form-control mr-sm-2" type="text" placeholder="Search">
				<button class="btn btn-primary my-2 my-sm-0" type="submit"><i class="fa fa-search" aria-hidden="true"></i></button>
			</form>
		</ul>
		<ul class="navbar-nav ml-auto">
			<li class="nav-item"><a class="nav-link" href="profile"><img src="${current_user.getPic()}" style="width:25px; height:25px; border-radius:50%"> ${current_user.getFirstName()}</a></li> 
			<span class="divider" style="color:white"></span>
			<li class="nav-item"><a class="nav-link" href="home"><i class="fa fa-home" aria-hidden="true"></i> Home</a></li>
			<li class="nav-item dropdown">
				<a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> 
					<i class="fa fa-bell" aria-hidden="true"></i> <span class="notification-length"></span>
				</a>
				<div class="dropdown-menu dropdown-menu-right">
					<div class="dropdown-item text-center" href="#">
						<span class="notification-length"></span> notifications
					</div>
					<div class="dropdown-divider"></div>
					<div class="drop-content"></div>
					<div class="dropdown-divider"></div>
					<a class="dropdown-item text-center" href="notification"><i class="fa fa-eye"></i> See all notifications</a>
					<small><a class="dropdown-item text-center" href="#" onclick="markAsRead('${username}')" style="color:#ffd035">Mark all as read</a></small>
				</div>
			</li>
			<li class="nav-item"><a class=" nav-link" href="logout">Logout <i class="fa fa-sign-out" aria-hidden="true"></i></a></li>
		</ul>
	</div>
	<% } else { %>
	<form class="form-inline my-2 my-lg-0" action="login"
		method="post"><%@ include file="_login.jsp"%></form>
	<% } %>
</nav>
<br>