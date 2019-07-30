var notifications;
var init = true;
var pollServer = function() {
    $.ajax({ 
    		type: 'POST',
		url: 'notification',
		data: 'type=polling',
		success: function(response) {
			var json = JSON.parse(response).reverse();
			var length = json.length;
			if (init == true) {
				init = false;
				console.log(json)
				for (var i = 0; i < json.length; i++) {
					$('.drop-content').append(notificationHtml(json[i]));
				}
				$('.notification-length').text(length);
			} else if (response != notifications) {
				if (length != 0) {
					$('.fa-bell').css('color', '#ffd035');
				} else {
					$('.fa-bell').css('color', '');
				}
				$('.notification-length').text(length);
				$('.drop-content').empty();
				for (var i = 0; i < json.length; i++) {
					$('.drop-content').append(notificationHtml(json[i]));
				}
//				$('.dropdown-menu').show();s\
			}
			notifications = response;
		},
        complete: function() {
            setTimeout(pollServer, 10000);
        }
    });
}

pollServer();

var markAsRead = function(username) {
    var values = { 'username': username, 'type': 'read' };
    $.ajax({
        url: "notification",
        type: "POST",
        data: values,
        success: function(data) {
        		console.log(values)
        		console.log(data);
        }
    });
}

var notificationHtml = function(notification) {
	if (notification.type == "reqAccepted") {
		return '<li><a class="dropdown-item" href="#"><div><i>Notification</i><p><i class="fa fa-users" aria-hidden="true"></i> ' + notification.fromUser + ' accepted your friend request</p><div class="dropdown-divider"></div></div></a></li>';
	} else if (notification.type == "like") {
		return '<li><a class="dropdown-item" href="#"><div><i>Notification</i><p><i class="fa fa-thumbs-up" aria-hidden="true"></i> ' + notification.fromUser + ' liked your post.</p><div class="dropdown-divider"></div></div></a></li>';
	} else if (notification.type == "comment") {
		return '<li><a class="dropdown-item" href="#"><div><i>Notification</i><p><i class="fa fa-comment" aria-hidden="true"></i> ' + notification.fromUser + ' commented on your post.<div class="dropdown-divider"></div></div></a></li>';
	} else if (notification.type == "friendReq") {
		return '<li><a class="dropdown-item" href="#"><div><i>Notification</i><p><i class="fa fa-users" aria-hidden="true"></i> ' + notification.fromUser + ' sent you a friend request</p><div class="dropdown-divider"></div></div></a></li>';
	} else if (notification.type == "bullyPost") {
		return '<li><a class="dropdown-item" href="#"><div><i>Notification</i><p><i class="fa fa-users" aria-hidden="true"></i> ' + notification.fromUser + ' has made a bully post</p><div class="dropdown-divider"></div></div></a></li>';
	}
}