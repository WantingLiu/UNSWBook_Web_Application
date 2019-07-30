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
<style>
body {
	background-color: black;
}
</style>
<body>
	<div class="container page-wrap">
		<br>
		<c:if test = "${failure == 'password'}">
		<div class="container">
			<br>
			<div class="alert alert-danger" role="alert">
				<h2 class="text-center"  ><strong class="text-center">YOU ARE NOT AN ADMIN. GET OUT OF HERE! </strong><a href="login"> <button class="btn btn-warning" type="submit"> UNSWBook</button></a></h2> 
				
			</div>
		</div>
		</c:if>
	</div>
</body>
<%@ include file="_footer.jsp"%>
</html>