package com.cookandroid.project_mobile.community;

public class CommunityData {
    private String text;
    private String content;
    private String uri;
    private long key;

    public CommunityData(String text, String content, String uri, long key){
        this.text = text;
        this.content = content;
        this.uri = uri;
        this.key = key;
    }

    public CommunityData(){
        this("", "", "", 0);
    }

    public String getText(){
        return text;
    }
    public void setText(){
        this.text = text;
    }

    public String getContent(){
        return content;
    }
    public void setContent(){
        this.content = content;
    }

    public String getUri(){
        return uri;
    }
    public void setUri(){
        this.uri = uri;
    }

    public long getKey(){
        return key;
    }
    public void setKey(){
        this.key = key;
    }
}
