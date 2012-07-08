package com.rossallenbell.avatr.json.outgoing;

import com.rossallenbell.avatr.domain.Player;
import com.rossallenbell.avatr.json.AvatrJsonMessage;

public class PlayerJoinedMessage extends AvatrJsonMessage {
    
    public Player player;
    
    public PlayerJoinedMessage(Player player){
        this.player = player;
    }
    
}
