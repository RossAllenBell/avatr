package com.rossallenbell.avatr;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class AvatrWebSocketServlet extends WebSocketServlet {
    
    private static final long serialVersionUID = 1L;
    
    private final Set<AvatrWebSocket> members = new CopyOnWriteArraySet<AvatrWebSocket>();

    public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
        return new AvatrWebSocket();
    }
    
    class AvatrWebSocket implements WebSocket.OnTextMessage {
        
        Connection connection;

        @Override
        public void onClose(int closedCode, String message) {
            members.remove(this);
        }

        @Override
        public void onOpen(Connection connection) {
            this.connection = connection;
            members.add(this);
        }
        
        @Override
        public void onMessage(final String data) {
            for(AvatrWebSocket member : members){
                try {
                    member.connection.sendMessage(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        
        
    }
    
}