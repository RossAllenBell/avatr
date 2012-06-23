package com.rossallenbell.avatr;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

import com.google.gson.Gson;

public class AvatrWebSocketServlet extends WebSocketServlet {
    
    private static final long serialVersionUID = 1L;
    
    private final Set<AvatrWebSocket> members = new CopyOnWriteArraySet<AvatrWebSocket>();
    
    private static int userId = 1;

    public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
        return new AvatrWebSocket(userId++);
    }
    
    private void updateMembers(){
    	Gson gson = new Gson();
    	PlayerStruct[] players = new PlayerStruct[members.size()];
    	int i = 0;
    	for(AvatrWebSocket member : members){
    		players[i++] = member.getPLayerStruct();
    	}
    	String message = gson.toJson(players);
    	try {
    		for(AvatrWebSocket member : members){
				member.connection.sendMessage(message);
			}
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    class AvatrWebSocket implements WebSocket.OnTextMessage {
        
        Connection connection;
        int userId, x, y;
        
        public AvatrWebSocket(int userId){
        	this.userId = userId;
        	this.x = (int) Math.round(Math.random() * 100);
        	this.y = (int) Math.round(Math.random() * 100);;
        }

        @Override
        public void onClose(int closedCode, String message) {
            members.remove(this);
            updateMembers();
        }

        @Override
        public void onOpen(Connection connection) {
            this.connection = connection;
            members.add(this);
            updateMembers();
        }
        
        @Override
        public void onMessage(final String data) {
        	PlayerStruct playerStruct = new Gson().fromJson(data, PlayerStruct.class);
        	this.x = playerStruct.x;
        	this.y = playerStruct.y;
        	updateMembers();
        }
        
        public PlayerStruct getPLayerStruct(){
        	return new PlayerStruct(userId, x, y);
        }
        
    }
    
    public class PlayerStruct {
    	int userId, x, y;
    	
    	public PlayerStruct(int userId, int x, int y){
    		this.userId = userId;
    		this.x = x;
    		this.y = y;
    	}
    }
    
}