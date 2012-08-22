package com.rossallenbell.avatr.domain;

public class Player {
    
    private final String emailAddress;
    private int x;
    private int y;
    
    public Player(String emailAddress, int x, int y){
        this.emailAddress = emailAddress;
        this.setX(x);
        this.setY(y);
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
}
