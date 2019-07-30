var posts;
var initpost = true;
var profile_poll = function() {
    $.ajax({ 
    		type: 'POST',
		url: 'home',
		success: function(response) {
			var json = JSON.parse(response);
			if (initpost == true) {
				initpost = false;
				console.log(json);
				for (var i = 0; i < json.length; i++) {
					$('#posts').append(postHtml(json[i]));
				}
				dynamize();
			} else if (response != posts) {
				$('#posts').empty();
				for (var i = 0; i < json.length; i++) {
					$('#posts').append(postHtml(json[i]));
				}
				dynamize();
			}
			posts = response;
		},
        complete: function() {
            setTimeout(profile_poll, 10000);
        }
    });
}

profile_poll();

handleRequestError = function (data) {
    alert("ERROR:"+data);
}

var ajaxLike = function(name, id, poster, unlike) {
    var values = {
            'username': name,
            'post_id': id,
            'poster': poster,
            'unlike': unlike
    };
    $.ajax({
        url: "like",
        type: "POST",
        data: values,
        success: function (data) {
        		console.log(data)
        },
        error: function(data) {
            handleRequestError(data);
        }
    });
}

var ajaxExtract = function(content, num, id) {
    var values = {
        'content': content,
        'type':num
    };
    $.ajax({
        url: "extract",
        type: "POST",
        data: values,
        success: function (data) {
            console.log(data);
            $("#extractDiv"+id).html('<p>'+data+'</p>');
        },
        error: function(data) {
            handleRequestError(data);
        }
    });
}
var dynamize = function() {
	$(".ajax").ajaxForm();
	$('#upload_button').click(function() {
		$('#upload').click();
	});
	// toggle comment
	$('.comment').on('click', function() {
		$(this).closest('div').next().toggle();
		return false;
	});
    $("a[name=e_keyword]").on("click",(function(){
        console.log('clicked once')
        id = $(this).attr("id");
        ajaxExtract($(this).parent().parent().children('.post').text(),1,id);
    }));
    $("a[name=e_person]").on("click",(function(){
        id = $(this).attr("id");
        ajaxExtract($(this).parent().parent().children('.post').text(),2,id);
    }));
    $("a[name=e_organisation]").on("click",(function(){
        id = $(this).attr("id");
        ajaxExtract($(this).parent().parent().children('.post').text(),3,id);
    }));
    $("a[name=e_location]").on("click",(function(){
        id = $(this).attr("id");
        ajaxExtract($(this).parent().parent().children('.post').text(),4,id);
    }));
}

var postHtml = function(post) {
	var res = '';
	res += '<div class="card">' +
		'<div class="card-header"><small><a href="profile?username=' + post.left.poster + '">' + post.left.poster + '</a> posted on your friend <a href="profile?username=' + post.right + '">' + post.right + '</a>\'s wall</small></div>' + 
		'<div class="card-block"><b><a href="profile?username=' + post.left.poster + '">' + post.left.poster + '</a></b><br>' + 
		'<small>' + post.left.datetime + '</small><br>';
		if (post.left.pic) {
			res += '<div align="center"><img src="' + post.left.pic + '" style="width:50%; margin:10px; border-radius:2%"><br>';
		} else {
			res += '<div>';
		}
		res += '<div class="post">'+ post.left.content + '</div>';
    res += '<div class="btn-group">';
    res += '<a href="#" class="btn btn-default" name="e_keyword" id="'+post.left.id+'">Keywords</a>';
    res += '<a href="#" class="btn btn-default" name="e_person" id="'+post.left.id+'">Persons</a>';
    res += '<a href="#" class="btn btn-default" name="e_organisation" id="'+post.left.id+'">Organisations</a>';
    res += '<a href="#" class="btn btn-default" name="e_location" id="'+post.left.id+'">Locations</a><br>';
    res += '</div><hr>';
    res += '<div id="extractDiv'+post.left.id+'"></div>';
    res += '<div class="btn-group">';
		if ($.inArray(current_user, post.left.likes)) {
			res += '<a href="#" class="btn btn-default" onclick="ajaxLike(' +  "'" + [post.right, post.left.id, post.left.poster, false].join("','") + '\'); return false">';
			if (post.left.likes) { res += post.left.likes.length + " "}  
			res += '<i class="fa fa-thumbs-o-up" aria-hidden="true"></i> Like</a>';
		} else {
			res += '<input type="hidden" name="unlike" value="true"><a href="#" class="btn btn-default" onclick="ajaxLike(' +  "'" + [post.right, post.left.id, post.left.poster, true].join("','") + '\'); return false;">';
			if (post.left.likes) { res += post.left.likes.length + " "} 
			res += '<i class="fa fa-thumbs-o-up" aria-hidden="true"></i> Unlike</a>';
		}
		res += '<a href="#" class="btn btn-default comment"><i class="fa fa-comment-o" aria-hidden="true"></i> Comments</a></form></div>';
	res += '<div style="display: none"><hr>';
		if (post.left.comments) {
			for (var i = 0; i < post.left.comments.length; i++) {
				res += '<a href="profile?username=' + post.left.comments[i].commenter + '">' + post.left.comments[i].commenter + '</a><br><small>' +
				post.left.comments[i].datetime + '</small><p>' +
				post.left.comments[i].content + '</p>';
			}
		}

	res +=
		'<form class="ajax" action="comment?username=' + post.right + '&id=' + post.left.id + '&poster=' + post.left.poster +
			'" method="post"><textarea rows="4" class="form-control" name="comment" placeholder="Say something..."></textarea><button class="btn btn-primary" type="submit" style="margin-top: 5px">Post</button></form></div></div></div>';
	return res;
}