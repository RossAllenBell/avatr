package com.rossallenbell.avatr.domain;

public class Player {
    
    private String userId;
    private int x;
    private int y;
    
    public Player(String userId, int x, int y){
        this.setUserId(userId);
        this.setX(x);
        this.setY(y);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
