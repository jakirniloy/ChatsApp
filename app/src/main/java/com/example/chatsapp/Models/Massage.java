package com.example.chatsapp.Models;

public class Massage {
    private String messageId,message,senderId;
    private  long timestamp;
    private int feelings=-1;

    public Massage() {
    }

    public Massage(String message, String senderId, long timestamp) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getFeelings() {
        return feelings;
    }
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setFeelings(int feelings) {
        this.feelings = feelings;
    }

}
