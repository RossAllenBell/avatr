package com.rossallenbell.avatr;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

import com.rossallenbell.avatr.domain.Player;
import com.rossallenbell.avatr.json.AvatrJsonMessage;
import com.rossallenbell.avatr.json.outgoing.PlayerLocationUpdateMessage;
import com.rossallenbell.avatr.json.outgoing.PlayerJoinedMessage;
import com.rossallenbell.avatr.json.outgoing.PlayerQuitMessage;
import com.rossallenbell.avatr.session.WebSocketSession;

public class AvatrWebSocketServlet extends WebSocketServlet {
    
    private static final long serialVersionUID = 1L;
    
    private final Set<WebSocketSession> members = new CopyOnWriteArraySet<WebSocketSession>();
    
    private static int userId = 1;
    
    public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
        return new WebSocketSession(this, userId++ + "");
    }
    
    private void messageMembers(AvatrJsonMessage message) {
        for (WebSocketSession member : members) {
            messageMember(member, message);
        }
    }
    
    private void messageMember(WebSocketSession member, AvatrJsonMessage message) {
        try {
            member.getConnection().sendMessage(message.toJson());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void addSession(WebSocketSession wsSession) {
        for (WebSocketSession member : members) {
            messageMember(wsSession, new PlayerJoinedMessage(member.getPlayer()));
        }
        this.members.add(wsSession);
        messageMembers(new PlayerJoinedMessage(wsSession.getPlayer()));
    }
    
    public void removeSession(WebSocketSession wsSession) {
        this.members.remove(wsSession);
        messageMembers(new PlayerQuitMessage(wsSession.getPlayer()));
    }
    
    public void updateLocation(Player player) {
        messageMembers(new PlayerLocationUpdateMessage(player));
    }
    
}