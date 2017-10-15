package me.lionelfaber.jsontest;

/**
 * Created by lionel on 7/19/17.
 */

public class Link {
    int _id;
    String title;
    String link;
    String scode;
    String type;

    // Empty constructor
    public Link(){

    }
    // constructor
    public Link(int id, String title, String link, String scode, String type){
        this._id = id;
        this.title = title;
        this.link = link;
        this.scode = scode;
        this.type = type;
    }

    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting title
    public String getTitle(){
        return this.title;
    }

    // setting title
    public void setTitle(String title){
        this.title = title;
    }

    // getting phone number
    public String getLink(){
        return this.link;
    }

    public void setLink(String link){
        this.link = link;
    }

    public String getScode()
    {
        return  this.scode;
    }

    public void setScode(String scode){
        this.scode = scode;
    }

    public String getType() { return this.type; }

    public void setType(String type) { this.type = type; }

}
