package com.rossallenbell.avatr.json.outgoing;

import com.rossallenbell.avatr.domain.Player;
import com.rossallenbell.avatr.json.AvatrJsonMessage;

public class PlayerQuitMessage extends AvatrJsonMessage {

    public Player player;

    public PlayerQuitMessage(Player player) {
        this.player = player;
    }
    
}
