package com.rossallenbell.avatr.session;

import org.eclipse.jetty.websocket.WebSocket;

import com.rossallenbell.avatr.AvatrWebSocketServlet;
import com.rossallenbell.avatr.dao.PlayerDAO;
import com.rossallenbell.avatr.domain.Player;
import com.rossallenbell.avatr.json.AvatrJsonMessage;
import com.rossallenbell.avatr.json.incoming.PlayerIdentificationMessage;
import com.rossallenbell.avatr.json.incoming.PlayerMovedMessage;

public class WebSocketSession implements WebSocket.OnTextMessage {
    
    private final AvatrWebSocketServlet wsServlet;
    private Connection connection;
    private Player player;
    
    public WebSocketSession(AvatrWebSocketServlet awss) {
        this.wsServlet = awss;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    @Override
    public void onClose(int closedCode, String message) {
        System.out.println("WS session closed");
        wsServlet.removeSession(this);
    }
    
    @Override
    public void onOpen(Connection connection) {
        System.out.println("WS session opened");
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
        } else if (message.getClass() == PlayerIdentificationMessage.class) {
            onPlayerIdentified((PlayerIdentificationMessage) message);
        }
    }
    
    public Player getPlayer() {
        return player;
    }
    
    private void onPlayerMoved(PlayerMovedMessage message) {
        player.setX(message.x);
        player.setY(message.y);
        wsServlet.updateLocation(player);
        new PlayerDAO().persistPlayer(player);
    }
    
    private void onPlayerIdentified(PlayerIdentificationMessage message){
        player = new PlayerDAO().loadPlayer(message.emailAddress);
        if(player == null){
            System.out.println("Player load failed");
            this.connection.close();
            return;
        }
        wsServlet.sessionIdentified(this);
        wsServlet.updateLocation(player);
        System.out.println("Player identified: " + player.getEmailAddress());
    }
}
