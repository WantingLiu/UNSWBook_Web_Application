$(document).ready(function() {
	var valid = true;
	$('#password_confirmation, #password').on('change', function() {
		console.log('chnaged');
		if ($('#password')[0].value != $('#password_confirmation')[0].value) {
			$('#password_confirmation').next().show();
			valid = false;
		} else {
			$('#password_confirmation').next().hide();
			valid = true;
		}
	})

	$('form').on('submit', function(e) {
		e.preventDefault();
		if (valid == true) {
			this.submit();
		}
	});
});