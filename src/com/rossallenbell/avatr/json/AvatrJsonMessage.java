package com.rossallenbell.avatr.json;

import com.google.gson.Gson;

public class AvatrJsonMessage {

    private String messageType;
    
    public String getMessageType() {
        return messageType;
    }

    public String toJson(){
        messageType = getMessageTypeFromClass();
        return new Gson().toJson(this);
    }
    
    public String getMessageTypeFromClass(){
        return getMessageTypeFromClass(this.getClass());
    }
    
    public static String getMessageTypeFromClass(Class<? extends AvatrJsonMessage> messageClass){
        return messageClass.getSimpleName().replaceAll("Message", "");
    }
    
    public static AvatrJsonMessage parse(String data) throws ClassNotFoundException{
        AvatrJsonMessage message = new Gson().fromJson(data, AvatrJsonMessage.class);
        return (AvatrJsonMessage) new Gson().fromJson(data, Class.forName("com.rossallenbell.avatr.json.incoming." + message.messageType + "Message"));
    }
    
}
