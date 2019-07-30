/**
 * Handles sending of notifications and emails using ajax:
 *  - adding friend (search.jsp)
 *  - accepting friend request (notifications.jsp)
 *  - banning users
 *  - TODO: do the same thing for liking posts, making posts (write PostServlet.java first?)
 *  
 */

$(document).ready(function(){
	// when user clicks on 'addFriend' button, let NotificationServlet generate a notification and add to database
	$('.addFriend').click(function(){
		// local variables to be passed to servlets
		var toUser = $(this).attr("id");
		var type = "friendReq";
		
		// TODO: use placeholder email as toUserEmail for now
		var toUserEmail = $(this).attr("name");
		//var toUserEmail = "robertos.nachos@gmail.com";
	
		// call NotificationServlet
		$.ajax({
			type: "POST",
			url: "notification",
			data: "toUser=" + toUser + "&type=" + type,
			success: function(notificationId){
				// call EmailServlet, given notificationId (to construct accept friend request url)
				$.ajax({
					type: "POST",
					url: "email",
					data: "emailType=" + type + "&email=" + toUserEmail + "&notificationId=" + notificationId
				});
			}
		});
		// change add friend button to added
		$(this).replaceWith('<button class="btn btn-primary" type="button"> Request sent <i class="fa fa-check"></i></button>')
	});

	// when user clicks on 'acceptFriend' button, let FriendRequestServlet generate a notification and add to database
	$('.acceptFriend').click(function(){
		// local variables to be passed to servlets
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
		$(this).replaceWith('<button class="btn btn-primary btn-sm" type="button"> Accepted <i class="fa fa-check"></i></button>');
	});
	
	// when user (admin) clicks on 'toggleBan' button, call BanServlet
	$('.toggleBan').click(function(){
		// local variables to be passed to servlets
		var username = $(this).attr("id");
		var type = $(this).attr("name");
		// call BanServlet
		$.ajax({
			type: "POST",
			url: "ban",
			data: "username=" + username + "&type=" + type
		});
		// change ban button to unban and vice versa
		if (type == "ban") {
			$(this).prop("name", "unban")
			$(this).html("UNBAN");
		} else {
			$(this).prop("name", "ban")
			$(this).html('<i class="fa fa-ban" aria-hidden="true"></i>  BAN');
		}
	});
});