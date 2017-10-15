package me.lionelfaber.jsontest;

/**
 * Created by lionel on 15/10/17.
 */

public class Subject {

    private String name;
    private String code;
    private int thumbnail;

    public Subject() {
    }

    public Subject(String name, String code, int thumbnail) {
        this.name = name;
        this.code = code;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
