<!DOCTYPE html>
<html>
<head>
<title>Login</title>
<%@ include file="_nav.jsp"%>
<script src="js/form.js"></script>
<script>
	$(document).ready(function() {
		form_validation();
	});
</script>
</head>
<body>
	<div class="container page-wrap">
		<h1 class="text-info text-center" >
			<c:if test = "${failure == 'password'}">
				<div class="alert alert-warning" role="alert">
					<h4 class="text-center" ><strong> Warning!</strong> Incorrect username or password!</h4>
				</div>
			</c:if>
		</h1>
		<div align="center">
			<a href="register.jsp">
				<button class="btn btn-primary" type="submit">Register Here!</button><br>
				<small>Don't have an account?</small>
			</a>
		</div>
	</div>
</body>
<%@ include file="_footer.jsp"%>
</html>
