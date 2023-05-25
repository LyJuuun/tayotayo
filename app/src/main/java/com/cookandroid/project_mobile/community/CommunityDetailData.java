package com.cookandroid.project_mobile.community;

public class CommunityDetailData {
    private String message;

    public CommunityDetailData(){

    }

    public CommunityDetailData(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }
}
