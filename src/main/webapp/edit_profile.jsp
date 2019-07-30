<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="_nav.jsp"%>
<title>Edit Profile</title>
</head>
<script src="js/edit.js"></script>
<body>

	<div class="container page-wrap" align="center">
		<br>

		<div class="card w-75 ">
			<div class="card-header text-white gray">
				<h4>Edit Profile</h4>
			</div>
			<div class="card-block" align="left">
				<form action="edit_profile" method="post"
					enctype="multipart/form-data">

					<div class="form-group">
						<label for="password">Password</label> <input name="password"
							type="password" class="form-control" id="password"
							placeholder="Password"> <small
							class="form-text text-muted" style="display: none">Please
							enter your new password</small>
					</div>

					<div class="form-group">
						<label for="password_confirmation">Confirm Password</label> <input
							type="password" class="form-control" id="password_confirmation"
							placeholder="Password"> <small
							class="form-text text-muted" style="display: none">Password
							doesn't match</small>
					</div>
					<div class="form-group">
						<label for="first_name">First Name</label> <input
							class="form-control" name="first_name" placeholder="Captain">
						<small class="form-text text-muted" style="display: none">Please
							enter your new first name</small>
					</div>
					<div class="form-group">
						<label for="gender">Last Name</label> <input class="form-control"
							name="last_name" placeholder="America"> <small
							class="form-text text-muted" style="display: none">Please
							enter your new last name</small>
					</div>
					<div class="form-group">
						<br> <label for="gender">Date of Birth</label> <input
							class="form-control date" name="date" type="date">
					</div>
					<div class="form-group">
						<label for="gender">Gender</label> <br>
						<div class="btn-group" data-toggle="buttons">
							<label class="btn btn-secondary"><input name="gender"
								type="radio" value="male">Male</label> <label
								class="btn btn-secondary"><input name="gender"
								type="radio" value="female">Female</label>
						</div>
					</div>
					<div class="form-group">
						<label for="email">Email</label> <input name="email" type="email"
							class="form-control" placeholder="email"> <small
							class="form-text text-muted" style="display: none">Please
							Enter new your email</small>
					</div>
					<div class="form-group">
						<label>Avatar</label> <input type="file" class="form-control-file"
							name="uploadFile">
					</div>
					<button class="btn btn-primary float-right" type="submit" value="edit">Edit</button>
				</form>
			</div>
		</div>
	</div>
</body>
<%@ include file="_footer.jsp"%>
</html>