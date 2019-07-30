<!DOCTYPE html>
<meta charset="utf-8">
<style>

.node {}

.link { stroke: #999; stroke-opacity: .6; stroke-width: 1px; }

</style>
<%@ include file="_nav.jsp"%>
<script src="http://d3js.org/d3.v4.min.js" type="text/javascript"></script>
<script src="http://d3js.org/d3-selection-multi.v1.js"></script>
<script>
var users = `${users}`
// Reference: http://bl.ocks.org/fancellu/2c782394602a93921faff74e594d1bb1
var json;

var ajaxAllUsers = function() {
    $.ajax({
        url: "graph",
        type: "POST",
        success: function (response) {
        		data = JSON.parse(response);
        		console.log(data);
        		graphAll(data);
        },
        error: function(response) {
            console.log("error", response);
        }
    });
}

ajaxAllUsers();

var ajaxUserInfo = function(username) {
    var values = {
		'username': username,
		'user_details': true
    };
    $.ajax({
        url: "profile",
        type: "POST",
        data: values,
        success: function (response) {
        		data = JSON.parse(response);
        		console.log(data);
        		graph(data);
        },
        error: function(response) {
            handleRequestError(response);
        }
    });
}

var pureAjaxUserInfo = function(username) {
    var values = {
		'username': username,
		'user_details': true
    };
    $.ajax({
        url: "profile",
        type: "POST",
        data: values,
        async: !1,
        success: function (response) {
        		data = JSON.parse(response);
        },
        error: function(response) {
            handleRequestError(response);
        }
    });
    return data;
}

var graphAll = function(data) {
	var graph_data = { "nodes": [], "links": [] };
	var lookup = {};
	var index = 0;
	for (var i = 0; i < data.length; i++) {
		lookup[data[i].username] = index;
		graph_data["nodes"].push({"name": data[i].firstName + " " + data[i].lastName, "label": "user", "id": index++});
	}
	
	for (var i = 0; i < data.length; i++) {
		if (data[i].friends) {
			for (var j = 0; j < data[i].friends.length; j++) {
				graph_data["links"].push({"source": lookup[data[i].username], "target": lookup[data[i].friends[j]], "type": "friends"});
			}
		}
	}
	
    var colors = d3.scaleOrdinal(d3.schemeCategory10);
    var svg = d3.select("svg"),
        width = +svg.attr("width"),
        height = +svg.attr("height"),
        node,
        link;
    d3.selectAll("svg > *").remove();
    
    svg.append('defs').append('marker')
        .attrs({'id':'arrowhead',
            'viewBox':'-0 -5 10 10',
            'refX':18,
            'refY':0,
            'orient':'auto',
            'markerWidth':5,
            'markerHeight':5,
            'xoverflow':'visible'})
        .append('svg:path')
        .attr('d', 'M 0,-5 L 10 ,0 L 0,5')
        .attr('fill', '#999')
        .style('stroke','none');

    var simulation = d3.forceSimulation()
        .force("link", d3.forceLink().id(function (d) {return d.id;}).distance(100).strength(1))
        .force("charge", d3.forceManyBody())
        .force("center", d3.forceCenter(1000 / 2, 800 / 2));
	
    update(graph_data.links, graph_data.nodes);
    
    function update(links, nodes) {
        link = svg.selectAll(".link")
            .data(links)
            .enter()
            .append("line")
            .attr("class", "link")
            .attr('marker-end','url(#arrowhead)')

        link.append("title")
            .text(function (d) {return d.type;});

        edgepaths = svg.selectAll(".edgepath")
            .data(links)
            .enter()
            .append('path')
            .attrs({
                'class': 'edgepath',
                'fill-opacity': 0,
                'stroke-opacity': 0,
                'id': function (d, i) {return 'edgepath' + i}
            })
            .style("pointer-events", "none");

        edgelabels = svg.selectAll(".edgelabel")
            .data(links)
            .enter()
            .append('text')
            .style("pointer-events", "none")
            .attrs({
                'class': 'edgelabel',
                'id': function (d, i) {return 'edgelabel' + i},
                'font-size': 10,
                'fill': '#aaa'
            });

        edgelabels.append('textPath')
            .attr('xlink:href', function (d, i) {return '#edgepath' + i})
            .style("text-anchor", "middle")
            .style("pointer-events", "none")
            .attr("startOffset", "50%")
            .text(function (d) {return d.type});

        node = svg.selectAll(".node")
            .data(nodes)
            .enter()
            .append("g")
            .attr("class", "node")
            .call(d3.drag()
                    .on("start", dragstarted)
                    .on("drag", dragged)
                    //.on("end", dragended)
            );

        node.append("circle")
            .attr("r", function (d, i) {return 5; })
            .style("fill", function (d, i) {return colors(i);})
		
        node.append("text")
            .attr("dy", -3)
            .text(function (d) {return d.name;});//+":"+d.label;});

        simulation
            .nodes(nodes)
            .on("tick", ticked);

        simulation.force("link")
            .links(links);
    }

    function ticked() {
        link
            .attr("x1", function (d) {return d.source.x;})
            .attr("y1", function (d) {return d.source.y;})
            .attr("x2", function (d) {return d.target.x;})
            .attr("y2", function (d) {return d.target.y;});

        node
            .attr("transform", function (d) {return "translate(" + d.x + ", " + d.y + ")";});

        edgepaths.attr('d', function (d) {
            return 'M ' + d.source.x + ' ' + d.source.y + ' L ' + d.target.x + ' ' + d.target.y;
        });

        edgelabels.attr('transform', function (d) {
            if (d.target.x < d.source.x) {
                var bbox = this.getBBox();

                rx = bbox.x + bbox.width / 2;
                ry = bbox.y + bbox.height / 2;
                return 'rotate(180 ' + rx + ' ' + ry + ')';
            }
            else {
                return 'rotate(0)';
            }
        });
    }

    function dragstarted(d) {
        if (!d3.event.active) simulation.alphaTarget(0.3).restart()
        d.fx = d.x;
        d.fy = d.y;
    }

    function dragged(d) {
        d.fx = d3.event.x;
        d.fy = d3.event.y;
    }
    
    $('g > text').hide();
    $('g').on('click', function(){
    		$(this).find('text').toggle();
    })
}

var graph_keywords = function(data, keyword) {
	var graph_data = { "nodes": [], "links": [] };
	var index = 1;
	for (var i = 0; i < data.length; i++) {
		if (data[i].wall) {
			for (var j = 0; j < data[i].wall.length; j++) {
				if (data[i].wall[j].content.indexOf(keyword) > -1) {
					var main = index;
					graph_data["nodes"].push({"name": data[i].wall[j].content, "label": "", "id": index++});
					graph_data["nodes"].push({"name": data[i].wall[j].poster, "label": "", "id": index});
					graph_data["links"].push({"source": main, "target": index++, "type": "poster"});
					graph_data["nodes"].push({"name": data[i].wall[j].datetime, "label": "", "id": index});
					graph_data["links"].push({"source": main, "target": index++, "type": "datetime"});
					
					
				}
			}
		}
	}
    
    	
    var colors = d3.scaleOrdinal(d3.schemeCategory10);
    var svg = d3.select("svg"),
        width = +svg.attr("width"),
        height = +svg.attr("height"),
        node,
        link;
    d3.selectAll("svg > *").remove();
    
    svg.append('defs').append('marker')
        .attrs({'id':'arrowhead',
            'viewBox':'-0 -5 10 10',
            'refX':18,
            'refY':0,
            'orient':'auto',
            'markerWidth':5,
            'markerHeight':5,
            'xoverflow':'visible'})
        .append('svg:path')
        .attr('d', 'M 0,-5 L 10 ,0 L 0,5')
        .attr('fill', '#999')
        .style('stroke','none');

    var simulation = d3.forceSimulation()
        .force("link", d3.forceLink().id(function (d) {return d.id;}).distance(100).strength(1))
        .force("charge", d3.forceManyBody())
        .force("center", d3.forceCenter(1000 / 2, 800 / 2));
	
    update(graph_data.links, graph_data.nodes);
    
    function update(links, nodes) {
        link = svg.selectAll(".link")
            .data(links)
            .enter()
            .append("line")
            .attr("class", "link")
            .attr('marker-end','url(#arrowhead)')

        link.append("title")
            .text(function (d) {return d.type;});

        edgepaths = svg.selectAll(".edgepath")
            .data(links)
            .enter()
            .append('path')
            .attrs({
                'class': 'edgepath',
                'fill-opacity': 0,
                'stroke-opacity': 0,
                'id': function (d, i) {return 'edgepath' + i}
            })
            .style("pointer-events", "none");

        edgelabels = svg.selectAll(".edgelabel")
            .data(links)
            .enter()
            .append('text')
            .style("pointer-events", "none")
            .attrs({
                'class': 'edgelabel',
                'id': function (d, i) {return 'edgelabel' + i},
                'font-size': 10,
                'fill': '#aaa'
            });

        edgelabels.append('textPath')
            .attr('xlink:href', function (d, i) {return '#edgepath' + i})
            .style("text-anchor", "middle")
            .style("pointer-events", "none")
            .attr("startOffset", "50%")
            .text(function (d) {return d.type});

        node = svg.selectAll(".node")
            .data(nodes)
            .enter()
            .append("g")
            .attr("class", "node")
            .call(d3.drag()
                    .on("start", dragstarted)
                    .on("drag", dragged)
                    //.on("end", dragended)
            );

        node.append("circle")
            .attr("r", function (d, i) {return 5; })
            .style("fill", function (d, i) {return colors(i);})
		
        node.append("text")
            .attr("dy", -3)
            .text(function (d) {return d.name;});//+":"+d.label;});

        simulation
            .nodes(nodes)
            .on("tick", ticked);

        simulation.force("link")
            .links(links);
    }

    function ticked() {
        link
            .attr("x1", function (d) {return d.source.x;})
            .attr("y1", function (d) {return d.source.y;})
            .attr("x2", function (d) {return d.target.x;})
            .attr("y2", function (d) {return d.target.y;});

        node
            .attr("transform", function (d) {return "translate(" + d.x + ", " + d.y + ")";});

        edgepaths.attr('d', function (d) {
            return 'M ' + d.source.x + ' ' + d.source.y + ' L ' + d.target.x + ' ' + d.target.y;
        });

        edgelabels.attr('transform', function (d) {
            if (d.target.x < d.source.x) {
                var bbox = this.getBBox();

                rx = bbox.x + bbox.width / 2;
                ry = bbox.y + bbox.height / 2;
                return 'rotate(180 ' + rx + ' ' + ry + ')';
            }
            else {
                return 'rotate(0)';
            }
        });
    }

    function dragstarted(d) {
        if (!d3.event.active) simulation.alphaTarget(0.3).restart()
        d.fx = d.x;
        d.fy = d.y;
    }

    function dragged(d) {
        d.fx = d3.event.x;
        d.fy = d3.event.y;
    }
    
    $('g > text').hide();
    $('g').on('click', function(){
    		$(this).find('text').toggle();
    })
}

var graph = function(data) {
	var users = {}
	users[data.username] = 1;
// 	var graph_data = { "nodes": [{"name": data.username, "label": "user", "id": 1}, {"name": "", "label": "", id: 2}, {"name": "", "label": "", id: 3}, {"name": "", "label": "", id: 4}], "links": [{"source": 1, "target": 2, "type": "attributes"}, {"source": 1, "target": 3, "type": "friends"}, {"source": 1, "target": 4, "type": "wall"}] };
	var graph_data = { "nodes": [{"name": data.username, "label": "user", "id": 1}, {"name": "", "label": "", id: 2}, {"name": "", "label": "", id: 3}], "links": [{"source": 1, "target": 2, "type": "attributes"}, {"source": 1, "target": 3, "type": "wall"}] };

	var index = 4;
 	// add attributes
	graph_data["nodes"].push({"name": data.birthDate, "label": "", "id": index});
	graph_data["links"].push({"source": 2, "target": index++, "type": "has_DOB"});
	graph_data["nodes"].push({"name": data.gender, "label": "", "id": index});
	graph_data["links"].push({"source": 2, "target": index++, "type": "has_gender"});
	graph_data["nodes"].push({"name": data.firstName, "label": "", "id": index});
	graph_data["links"].push({"source": 2, "target": index++, "type": "has_name"});
	
	// add posts
	for (var i = 0; i < data.wall.length; i++) {
		graph_data["nodes"].push({"name": data.wall[i].content, "label": "", "id": index});
		graph_data["links"].push({"source": 3, "target": index, "type": "has_post"});
		var k = index++;
		graph_data["nodes"].push({"name": data.wall[i].poster, "label": "poster", "id": index});
		graph_data["links"].push({"source": k, "target": index++, "type": "posted_by"});
		if (data.wall[i].likes) {
			for (var j = 0; j < data.wall[i].likes.length; j++) {
				graph_data["nodes"].push({"name": data.wall[i].likes[j], "label": "likes", "id": index});
				graph_data["links"].push({"source": k, "target": index++, "type": "liked_by"});
			}
		}
		if (data.wall[i].comments) {
			for (var j = 0; j < data.wall[i].comments.length; j++) {
				graph_data["nodes"].push({"name": data.wall[i].comments[j].content, "label": "comment", "id": index});
				graph_data["links"].push({"source": k, "target": index++, "type": "has_comment"});
				
				graph_data["nodes"].push({"name": data.wall[i].comments[j].commenter, "label": "user", "id": index});
				graph_data["links"].push({"source": index-1, "target": index++, "type": "commented_by"});
			}
		}
	}
	
    var colors = d3.scaleOrdinal(d3.schemeCategory10);

    var svg = d3.select("svg"),
        width = +svg.attr("width"),
        height = +svg.attr("height"),
        node,
        link;
    d3.selectAll("svg > *").remove();
    
    svg.append('defs').append('marker')
        .attrs({'id':'arrowhead',
            'viewBox':'-0 -5 10 10',
            'refX':18,
            'refY':0,
            'orient':'auto',
            'markerWidth':5,
            'markerHeight':5,
            'xoverflow':'visible'})
        .append('svg:path')
        .attr('d', 'M 0,-5 L 10 ,0 L 0,5')
        .attr('fill', '#999')
        .style('stroke','none');

    var simulation = d3.forceSimulation()
        .force("link", d3.forceLink().id(function (d) {return d.id;}).distance(100).strength(1))
        .force("charge", d3.forceManyBody())
        .force("center", d3.forceCenter(1000 / 2, 800 / 2));
	
    update(graph_data.links, graph_data.nodes);
    
    function update(links, nodes) {
        link = svg.selectAll(".link")
            .data(links)
            .enter()
            .append("line")
            .attr("class", "link")
            .attr('marker-end','url(#arrowhead)')

        link.append("title")
            .text(function (d) {return d.type;});

        edgepaths = svg.selectAll(".edgepath")
            .data(links)
            .enter()
            .append('path')
            .attrs({
                'class': 'edgepath',
                'fill-opacity': 0,
                'stroke-opacity': 0,
                'id': function (d, i) {return 'edgepath' + i}
            })
            .style("pointer-events", "none");

        edgelabels = svg.selectAll(".edgelabel")
            .data(links)
            .enter()
            .append('text')
            .style("pointer-events", "none")
            .attrs({
                'class': 'edgelabel',
                'id': function (d, i) {return 'edgelabel' + i},
                'font-size': 10,
                'fill': '#aaa'
            });

        edgelabels.append('textPath')
            .attr('xlink:href', function (d, i) {return '#edgepath' + i})
            .style("text-anchor", "middle")
            .style("pointer-events", "none")
            .attr("startOffset", "50%")
            .text(function (d) {return d.type});

        node = svg.selectAll(".node")
            .data(nodes)
            .enter()
            .append("g")
            .attr("class", "node")
            .call(d3.drag()
                    .on("start", dragstarted)
                    .on("drag", dragged)
                    //.on("end", dragended)
            );

        node.append("circle")
            .attr("r", function (d, i) {if (d.id == 1) { return 10; } else { return 5; }})
            .style("fill", function (d, i) {return colors(i);})
		
        node.append("text")
            .attr("dy", -3)
            .text(function (d) {return d.name;});//+":"+d.label;});

        simulation
            .nodes(nodes)
            .on("tick", ticked);

        simulation.force("link")
            .links(links);
    }

    function ticked() {
        link
            .attr("x1", function (d) {return d.source.x;})
            .attr("y1", function (d) {return d.source.y;})
            .attr("x2", function (d) {return d.target.x;})
            .attr("y2", function (d) {return d.target.y;});

        node
            .attr("transform", function (d) {return "translate(" + d.x + ", " + d.y + ")";});

        edgepaths.attr('d', function (d) {
            return 'M ' + d.source.x + ' ' + d.source.y + ' L ' + d.target.x + ' ' + d.target.y;
        });

        edgelabels.attr('transform', function (d) {
            if (d.target.x < d.source.x) {
                var bbox = this.getBBox();

                rx = bbox.x + bbox.width / 2;
                ry = bbox.y + bbox.height / 2;
                return 'rotate(180 ' + rx + ' ' + ry + ')';
            }
            else {
                return 'rotate(0)';
            }
        });
    }

    function dragstarted(d) {
        if (!d3.event.active) simulation.alphaTarget(0.3).restart()
        d.fx = d.x;
        d.fy = d.y;
    }

    function dragged(d) {
        d.fx = d3.event.x;
        d.fy = d3.event.y;
    }
    
    $('g > text').hide();
    $('g').on('click', function(){
    		$(this).find('text').toggle();
    })
}

var graph_friends = function(data) {
    	var users = {}
    	var index = 1;
    	users[data.username] = index;
    	var graph_data = { "nodes": [{"name": data.username, "label": "user", "id": index++}], "links": [] }
    	for (var i = 0; i < data.friends.length; i++) {
    		if (!users[data.friends[i]]) {
    			graph_data["nodes"].push({"name": data.friends[i], "label": "user", "id": index});
    			graph_data["links"].push({"source": 1, "target": index, "type": "friends"});
    			users[data.friends[i]] = index++;
    		} else {
    			graph_data["links"].push({"source": 1, "target": users[data.friends[i]], "type": "friends"});
    		}
    		var friends_of_friend = pureAjaxUserInfo(data.friends[i]).friends;
    		for (var j = 0; j < friends_of_friend.length; j++) {
    			if (!users[friends_of_friend[j]]) {
    				graph_data["nodes"].push({"name": friends_of_friend[j], "label": "user", "id": index});
    				graph_data["links"].push({"source": users[data.friends[i]], "target": index, "type": "friends"});
    				graph_data["links"].push({"source": index, "target": users[data.friends[i]], "type": ""});
    				users[friends_of_friend[j]] = index++;
    			}
    		}
    	}
    	
    var colors = d3.scaleOrdinal(d3.schemeCategory10);

    var svg = d3.select("svg"),
        width = +svg.attr("width"),
        height = +svg.attr("height"),
        node,
        link;
    d3.selectAll("svg > *").remove();
    
    svg.append('defs').append('marker')
        .attrs({'id':'arrowhead',
            'viewBox':'-0 -5 10 10',
            'refX':18,
            'refY':0,
            'orient':'auto',
            'markerWidth':5,
            'markerHeight':5,
            'xoverflow':'visible'})
        .append('svg:path')
        .attr('d', 'M 0,-5 L 10 ,0 L 0,5')
        .attr('fill', '#999')
        .style('stroke','none');

    var simulation = d3.forceSimulation()
        .force("link", d3.forceLink().id(function (d) {return d.id;}).distance(100).strength(1))
        .force("charge", d3.forceManyBody())
        .force("center", d3.forceCenter(1000 / 2, 800 / 2));
	
    update(graph_data.links, graph_data.nodes);
    
    function update(links, nodes) {
        link = svg.selectAll(".link")
            .data(links)
            .enter()
            .append("line")
            .attr("class", "link")
            .attr('marker-end','url(#arrowhead)')

        link.append("title")
            .text(function (d) {return d.type;});

        edgepaths = svg.selectAll(".edgepath")
            .data(links)
            .enter()
            .append('path')
            .attrs({
                'class': 'edgepath',
                'fill-opacity': 0,
                'stroke-opacity': 0,
                'id': function (d, i) {return 'edgepath' + i}
            })
            .style("pointer-events", "none");

        edgelabels = svg.selectAll(".edgelabel")
            .data(links)
            .enter()
            .append('text')
            .style("pointer-events", "none")
            .attrs({
                'class': 'edgelabel',
                'id': function (d, i) {return 'edgelabel' + i},
                'font-size': 10,
                'fill': '#aaa'
            });

        edgelabels.append('textPath')
            .attr('xlink:href', function (d, i) {return '#edgepath' + i})
            .style("text-anchor", "middle")
            .style("pointer-events", "none")
            .attr("startOffset", "50%")
            .text(function (d) {return d.type});

        node = svg.selectAll(".node")
            .data(nodes)
            .enter()
            .append("g")
            .attr("class", "node")
            .call(d3.drag()
                    .on("start", dragstarted)
                    .on("drag", dragged)
                    //.on("end", dragended)
            );

        node.append("circle")
            .attr("r", function (d, i) {return 5; })
            .style("fill", function (d, i) {return colors(i);})
		
        node.append("text")
            .attr("dy", -3)
            .text(function (d) {return d.name;});//+":"+d.label;});

        simulation
            .nodes(nodes)
            .on("tick", ticked);

        simulation.force("link")
            .links(links);
    }

    function ticked() {
        link
            .attr("x1", function (d) {return d.source.x;})
            .attr("y1", function (d) {return d.source.y;})
            .attr("x2", function (d) {return d.target.x;})
            .attr("y2", function (d) {return d.target.y;});

        node
            .attr("transform", function (d) {return "translate(" + d.x + ", " + d.y + ")";});

        edgepaths.attr('d', function (d) {
            return 'M ' + d.source.x + ' ' + d.source.y + ' L ' + d.target.x + ' ' + d.target.y;
        });

        edgelabels.attr('transform', function (d) {
            if (d.target.x < d.source.x) {
                var bbox = this.getBBox();

                rx = bbox.x + bbox.width / 2;
                ry = bbox.y + bbox.height / 2;
                return 'rotate(180 ' + rx + ' ' + ry + ')';
            }
            else {
                return 'rotate(0)';
            }
        });
    }

    function dragstarted(d) {
        if (!d3.event.active) simulation.alphaTarget(0.3).restart()
        d.fx = d.x;
        d.fy = d.y;
    }

    function dragged(d) {
        d.fx = d3.event.x;
        d.fy = d3.event.y;
    }
    
    $('g > text').hide();
    $('g').on('click', function(){
    		$(this).find('text').toggle();
    })
}

</script>
<body>
<div class="container">
	<h4>Search by:</h4>
	Username: <input id="username" class="form-control" name="username">
	<button class="btn" type="submit" onclick="graph(pureAjaxUserInfo($('#username').val() ));">User Info</button>
	<button class="btn" type="submit" onclick="graph_friends(pureAjaxUserInfo($('#username').val() ));">Friends of friend</button>
	<br>
	Keywords: <input id="post" class="form-control" name="post">
	<button class="btn" type="submit" onclick="graph_keywords(data, $('#post').val());">Search</button>
</div>
<svg id="canvas" style="width:100%; height:100%"></svg>
</body>
