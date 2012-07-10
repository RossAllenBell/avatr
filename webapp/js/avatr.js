if (!window.WebSocket && window.MozWebSocket)
    window.WebSocket = window.MozWebSocket;
if (!window.WebSocket)
    alert('WebSocket not supported by this browser');

function Avatr() {
    
    var WIDTH_UNITS = 1600;
    var HEIGHT_UNITS = 900;
    var ASPECT_RATIO = WIDTH_UNITS / HEIGHT_UNITS;
    
    function renderLoop() {
        var loopStartTime = new Date().getTime()

        context.clearRect(0, 0, WIDTH_UNITS, HEIGHT_UNITS);
        context.fillStyle = '#DDDDDD';
        context.fillRect(0, 0, WIDTH_UNITS, HEIGHT_UNITS);
        
        for ( var i in players) {
            context.beginPath();
            context.arc(players[i].x, players[i].y, 20, 0, 2 * Math.PI, false);
            context.fillStyle = '#888888';
            context.fill();
            context.stroke();
        }
        
        latestTimeoutId = setTimeout(arguments.callee, 60 - (new Date().getTime() - loopStartTime));
    }
    
    function resizeContainerDiv() {
        var containerDiv = $('#containerDiv');
        var newWidth = $(window).width();
        var newHeight = $(window).height();
        if (newWidth == 0 || newHeight == 0) {
            return;
        }
        if (newWidth / newHeight < ASPECT_RATIO) {
            newHeight = newWidth / ASPECT_RATIO;
        } else if (newWidth / newHeight > ASPECT_RATIO) {
            newWidth = newHeight * ASPECT_RATIO;
        }
        containerDiv.css('width', newWidth + 'px');
        containerDiv.css('height', newHeight + 'px');
    }
    
    function windowCoordsToCanvasCoords(x, y) {
        return {
            x : Math.round(x * WIDTH_UNITS / $('#containerDiv').width()),
            y : Math.round(y * HEIGHT_UNITS / $('#containerDiv').height())
        };
    }
    
    document.body.style.margin = 0;
    document.body.style.padding = 0;
    document.body.style.overflow = 'hidden';
    
    var wsURL = document.location.toString().replace('http://', 'ws://').replace('https://', 'wss://') + 'ws';
    console.info('Creating WS to: ' + wsURL);
    var webSocket = new WebSocket(wsURL);
    
    webSocket.onopen = function() {
        console.info('Connected.');
    };
    
    webSocket.onclose = function(message) {
        console.info('Closed: ' + message);
    };
    
    webSocket.onmessage = function(message) {
        console.log('Message> ' + message.data);
        var data = JSON.parse(message.data);
        var messageType = data.messageType;
        
        if (messageType === 'PlayerJoined') {
            for ( var i in players) {
                if (players[i].userId === data.player.userId) {
                    console.error('Player already in game state: ' + data.player.userId);
                    return;
                }
            }
            players.push(data.player);
        } else if (messageType === 'PlayerLocationUpdate') {
            for ( var i in players) {
                if (players[i].userId === data.player.userId) {
                    players[i] = data.player;
                    return;
                }
            }
            console.error('Player not in game state: ' + data.player.userId);
        } else if (messageType === 'PlayerQuit') {
            for ( var i in players) {
                if (players[i].userId === data.player.userId) {
                    players.splice(i, 1);
                    return;
                }
            }
            console.error('Player not in game state: ' + data.player.userId);
        } else {
            console.error('Unknown messageType: ' + messageType);
        }
        
    };
    
    var containerDiv = $('<div>', {
        id : 'containerDiv'
    }).css('width', WIDTH_UNITS + 'px').css('height', HEIGHT_UNITS + 'px');
    $('body').append(containerDiv);
    var canvas = $('<canvas>', {
        id : 'canvas'
    }).attr('width', WIDTH_UNITS).attr('height', HEIGHT_UNITS);
    containerDiv.append(canvas);
    canvas.css('width', '100%');
    canvas.css('height', '100%');
    resizeContainerDiv()
    var context = canvas[0].getContext('2d');
    
    $('#canvas').click(function(e) {
        var canvasCoords = windowCoordsToCanvasCoords(e.pageX, e.pageY);
        webSocket.send(JSON.stringify({
            'messageType' : 'PlayerMoved',
            'x' : canvasCoords.x,
            'y' : canvasCoords.y
        }));
    });
    
    $(window).resize(function() {
        resizeContainerDiv();
    })

    var players = [];
    
    var latestTimeoutId = setTimeout(renderLoop, 60);
    
}