package com.rossallenbell.avatr.session;

import org.eclipse.jetty.websocket.WebSocket;

import com.rossallenbell.avatr.AvatrWebSocketServlet;
import com.rossallenbell.avatr.domain.Player;
import com.rossallenbell.avatr.json.AvatrJsonMessage;
import com.rossallenbell.avatr.json.incoming.PlayerMovedMessage;

public class WebSocketSession implements WebSocket.OnTextMessage {
    
    private final AvatrWebSocketServlet wsServlet;
    private Connection connection;
    private final String userId;
    private int x, y;
    
    public WebSocketSession(AvatrWebSocketServlet awss, String userId) {
        this.wsServlet = awss;
        this.userId = userId;
        this.x = (int) Math.round(Math.random() * 100);
        this.y = (int) Math.round(Math.random() * 100);
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    @Override
    public void onClose(int closedCode, String message) {
        wsServlet.removeSession(this);
    }
    
    @Override
    public void onOpen(Connection connection) {
        this.setConnection(connection);
        wsServlet.addSession(this);
    }
    
    @Override
    public void onMessage(final String data) {
        AvatrJsonMessage message;
        try {
            message = AvatrJsonMessage.parse(data);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        
        if (message.getClass() == PlayerMovedMessage.class) {
            onPlayerMoved((PlayerMovedMessage) message);
        }
    }
    
    public Player getPlayer() {
        return new Player(userId, x, y);
    }
    
    private void onPlayerMoved(PlayerMovedMessage message) {
        this.x = message.x;
        this.y = message.y;
        wsServlet.updateLocation(this.getPlayer());
    }
}
