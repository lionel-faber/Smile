package me.lionelfaber.jsontest;

/**
 * Created by lionel on 14/10/17.
 */

public class Info {

    int _id;
    String title;
    String infoType;
    String subInfos;
    String date;

    public Info() {
    }

    public Info(int _id, String title, String infoType, String subInfos, String date) {
        this._id = _id;
        this.title = title;
        this.infoType = infoType;
        this.subInfos = subInfos;
        this.date = date;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getSubInfos() {
        return subInfos;
    }

    public void setSubInfos(String subInfos) {
        this.subInfos = subInfos;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
