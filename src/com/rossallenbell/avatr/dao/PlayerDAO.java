package com.rossallenbell.avatr.dao;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.couchbase.client.CouchbaseClient;
import com.rossallenbell.avatr.domain.Player;

public class PlayerDAO extends DataAccessObject {

    private static final String X = "x";
    private static final String Y = "y";
    
    private CouchbaseClient client;
    
    public PlayerDAO(){
        client = DatabaseAccess.getDatabaseClient();
    }
    
    public Player loadPlayer(String emailAddress) {
        System.out.println("Loading player: " + emailAddress);
        InternetAddress emailAddr;
        try {
            emailAddr = new InternetAddress(emailAddress);
            emailAddr.validate();
        } catch (AddressException e) {
            System.out.println("Invalid email address, aborting load");
            return null;
        }
        String playerId = emailAddr.getAddress().toLowerCase();
        
        Object exists = client.get(getKeyForPlayerName(playerId));
        int x = 100;
        int y = 100;
        if(exists == null){
            System.out.println("Creating new player: " + playerId);
            client.set(getKeyForPlayerName(playerId), 0, playerId);
            client.set(getKeyForX(playerId), 0, x);
            client.set(getKeyForY(playerId), 0, y);
        } else {
            x = (Integer) client.get(getKeyForX(playerId));
            y = (Integer) client.get(getKeyForY(playerId));
        }
        
        System.out.println("Player loaded: " + emailAddress + " [" + x + "," + y + "]");
        return new Player(playerId, x, y);
    }
    
    public void persistPlayer(Player player){
        client.set(getKeyForPlayerName(player.getEmailAddress()), 0, player.getEmailAddress());
        client.set(getKeyForX(player.getEmailAddress()), 0, player.getX());
        client.set(getKeyForY(player.getEmailAddress()), 0, player.getY());
    }
    
    private String getKeyForPlayerName(String playerId){
        return delimit(getSpace(), playerId);
    }
    
    private String getKeyForX(String playerId){
        return delimit(getSpace(), playerId, X);
    }
    
    private String getKeyForY(String playerId){
        return delimit(getSpace(), playerId, Y);
    }
    
}
