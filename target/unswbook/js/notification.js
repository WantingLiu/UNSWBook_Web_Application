/**
 * Handles sending of notifications using ajax
 */

$(document).ready(function() {
	// when user clicks on 'addFriend' button, let NotificationServlet generate a notification and add to database
	$('.addFriend').click(function(){
		var toUser = $(this).attr("id");
		var type = "friendReq";
		// call NotificationServlet
		$.ajax({
			type: "POST",
			url: "notification",
			data: "toUser=" + toUser + "&type=" + type
		});
		// change add friend button to added
		$(this).replaceWith('<button class="btn btn-default" type="submit"> Request sent <i class="fa fa-check"></i></button>')
	});

	// when user clicks on 'acceptFriend' button, let FriendRequestServlet generate a notification and add to database
	$('.acceptFriend').click(function(){
		var notificationId = $(this).attr("id");
		var type = "reqAccepted";
		// call FriendRequestServlet
		$.ajax({
			type: "POST",
			url: "friendRequest",
			data: "notificationId=" + notificationId,
			success: function(toUser){
				// call NotificationServlet
				$.ajax({
					type: "POST",
					url: "notification",
					data: "toUser=" + toUser + "&type=" + type
				});
			}
		});
		// change accept button to accepted
		$(this).replaceWith('<button class="btn btn-secondary btn-sm" type="submit"> Accepted <i class="fa fa-check"></i></button>');
	});
	// TODO: do the same thing for banning users, likes, comments 
});
