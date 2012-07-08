package com.rossallenbell.avatr.json.outgoing;

import com.rossallenbell.avatr.domain.Player;
import com.rossallenbell.avatr.json.AvatrJsonMessage;

public class PlayerLocationUpdateMessage extends AvatrJsonMessage {
    
    public Player player;
    
    public PlayerLocationUpdateMessage(Player player) {
        this.player = player;
    }
    
}