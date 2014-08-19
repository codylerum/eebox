var webSocket = null;

function startWebSocket() {
	if (webSocket == null || webSocket.readyState != 1) {
		try {
			var interest = $('#pageInterest').data('interest');
			var host = location.host;
			var protocol = location.protocol == 'http:' ? 'ws://' : 'wss://';
			var rootPath = 's/ws/global/';
			var path = interest !== undefined ? rootPath + interest : rootPath;
			webSocket = new WebSocket(protocol + host + contextRoot + path);
			webSocket.onerror = function(event) {
				console.log('WebSocket error');
			}
			webSocket.onopen = function(event) {
				console.log('WebSocket opened');
				$('#wsconnectstatus').text('Connected').css('color', 'green');
			}
			webSocket.onclose = function(event) {
				console.log('WebSocket closed with reason code: ' + event.code);
				$('#wsconnectstatus').text('Disconnected').css('color', 'red');
			}
			webSocket.onmessage = function(event) {
				var msg = event.data;
				$('#processingMessages').append('<li>' + msg + '</li>');
			}
			if (webSocketPingInterval == null) {
				webSocketPingInterval = setInterval(sendWebSocketPing, 60000);
			}
			$(window).on('online', startWebSocket);
			$(window).on('offline', closeWebSocket);
		} catch (error) {
			console.error(error);
			$('#wsconnectstatus').text('Error').css('color', 'orange');
			console.log('Failed to Start WebSocket');
		}
	}
}

function closeWebSocket() {
	if (webSocket != null && webSocket.readyState == 1) {
		try {
			webSocket.close();
		} catch (error) {
			console.log('Error closing WebSocket');
		}
	}
}

var webSocketPingInterval;

function sendWebSocketPing() {
	startWebSocket();
	if (webSocket != null && webSocket.readyState == 1) {
		try {
			webSocket.send("ping");
		} catch (error) {
			console.log('Failed to send ping');
		}
	}
}