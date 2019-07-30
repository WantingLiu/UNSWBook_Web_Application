<!DOCTYPE html>
<html>
<head>
	<title>Register</title>
	<%@ include file="_nav.jsp" %>
	<script src="js/form.js"></script>
	<script src="js/register.js"></script>
</head>
<body>
	<div class="container page-wrap" align="center">
	<div class="card w-75">
  		<div class="card-header text-white gray">
  			<h4>Register</h4>
  		</div>
  		<div class="card-block" align="left">
		<form action="RegisterServlet" method="post" enctype="multipart/form-data">
			<div class="form-group">
				<label for="username">Username</label>
				<input name="username" class="form-control validate" placeholder="Username">
				<small class="form-text text-muted" style="display:none">Please enter a username</small>
				<% if (request.getAttribute("failure") != null && request.getAttribute("failure").equals("username")){ %>
					<small style="color:red">Username taken, please try again.</small>
				<% } %>
			</div>
			<div class="form-group">
				<label for="password">Password</label>
				<input name="password" type="password" class="form-control validate" id="password" placeholder="Password">
				<small class="form-text text-muted" style="display:none">Please enter your password</small>
			</div>
			<div class="form-group">
				<label for="password_confirmation">Confirm Password</label>
				<input type="password" class="form-control" id="password_confirmation" placeholder="Password">
				<small class="form-text text-muted" style="display:none">Password doesn't match</small>
			</div>
			<div class="form-group">
				<label for="first_name">First Name</label>
				<input class="form-control validate" name="first_name" placeholder="Captain">
				<small class="form-text text-muted" style="display:none">Please enter your first name</small>
			</div>
			<div class="form-group">
				<label for="gender">Last Name</label>
				<input class="form-control validate" name="last_name" placeholder="America">
				<small class="form-text text-muted" style="display:none">Please enter your last name</small>
			</div>
			<div class="form-group">
				<br>
				<label for="gender">Date of Birth</label>
				<input class="form-control date" name="date" type="date">
			</div>
			<%
				if (request.getAttribute("failure") != null){
					if (request.getAttribute("failure").equals("password")){
			%>
			<p style="color: red">Passwords do not match, please try again.</p>
			<%} }%>
			<div class="form-group">
				<label for="gender">Gender</label>
				<br>
				<div class="btn-group" data-toggle="buttons">
					<label class="btn btn-secondary"><input name="gender" type="radio" value="male">Male</label>
					<label class="btn btn-secondary"><input name="gender" type="radio" value="female">Female</label>
				</div>
			</div>
			<div class="form-group">
				<label for="email">Email</label>
				<input name="email" type="email" class="form-control validate" placeholder="email">
				<small class="form-text text-muted" style="display:none">Please Enter your email</small>
			</div>
			Upload a profile picture: <input type="file" name="uploadFile" />
			<br/><br/>
			<input type="hidden" name="proc_registration">
		 	<button class="btn btn-primary float-right" type="submit" value="register">Register</button>
		</form>
		</div>
		</div>
	</div>
</body>
<%@ include file="_footer.jsp" %>
</html>
