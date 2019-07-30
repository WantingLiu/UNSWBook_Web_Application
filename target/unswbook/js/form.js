/* 
	Form validation function instructions:
	1) include form.js, 'duh'
	3) give the input tag a "validate" class 
	4) add a small tag with "display: none" directly underneath the input tag 
*/

var form_validation = function() {
	$('form').on('submit', function(e){
		e.preventDefault();
		valid = true;
		var fields = $(this).find('.validate');
		console.log(fields);
		for (var i = 0; i < fields.length; i++) {
			if (fields[i].value == '') {
				$(fields[i]).next().show();
				valid = false;
			} else {
				$(fields[i]).next().hide();
			}
		}
		if (valid == true) {
			this.submit();
		}
	});
};

var password_confirmation = function() {
	$('#password_confirmation').on('change', function() {
		if ($('#password')[0].value != this.value) {
			$(this).next().show();
			valid = false;
		} else {
			$(this).next().hide();
			valid = true;
		}
	})
}