if (!window.WebSocket && window.MozWebSocket)
    window.WebSocket=window.MozWebSocket;
if (!window.WebSocket)
    alert("WebSocket not supported by this browser");

function Avatr() {
    
    function renderLoop() {
        var loopStartTime = new Date().getTime()
        
        context.clearRect (0 , 0,  canvas.width,  canvas.width);
        
        for(var i in players){	
	        context.beginPath();
	        context.arc(players[i].x, players[i].y, 20, 0, 2 * Math.PI, false);
	        context.fillStyle = "#888888";
	        context.fill();
	        context.stroke();
        }

        latestTimeoutId = setTimeout(arguments.callee, 60 - (new Date().getTime() - loopStartTime));
    }
        
    document.body.style.overflow = 'hidden';
    
    var wsURL = document.location.toString().replace('http://','ws://').replace('https://','wss://') + "ws";
    console.info("Creating WS to: " + wsURL);
    var webSocket = new WebSocket(wsURL);
    
    webSocket.onopen = function() {
        console.info("Connected.");
    };
    
    webSocket.onclose = function(message) {
        console.info("Closed: " + message);
    };
    
    webSocket.onmessage = function(message) {
        console.log("Message> " + message.data);
        players = JSON.parse(message.data);
    };
    
    var containerDiv = $('<div>', {id: 'containerDiv'});
    $('body').append(containerDiv);
    containerDiv.empty();
    var canvas = $('<canvas>', {id: 'canvas'});
    containerDiv.append(canvas);
    canvas = document.getElementById('canvas'); 
    canvas.width = $(document).width();
    canvas.height = $(document).height();
    var context = canvas.getContext("2d");
    
     $("#canvas").click(function(e){        
        webSocket.send(JSON.stringify({'userId': 0, 'x': e.pageX-$("#canvas").offset().left, 'y': e.pageY-$("#canvas").offset().top}));
    });
    
    var players = [];
    
    var latestTimeoutId = setTimeout(renderLoop, 60);
    
}