package com.rossallenbell.avatr.dao;

public abstract class DataAccessObject {
    
    private final static String DELIMITER = "!@#";
    
    protected String delimit(String... strings){
        StringBuilder s = new StringBuilder();
        for(int i=0; i<strings.length; i++){
            s.append(strings[i]);
            if(i<strings.length-1){
                s.append(DELIMITER);
            }
        }
        return s.toString();
    }
    
    protected String getSpace(){
        return getClass().getSimpleName().replaceAll("DAO", "");
    }
    
}
