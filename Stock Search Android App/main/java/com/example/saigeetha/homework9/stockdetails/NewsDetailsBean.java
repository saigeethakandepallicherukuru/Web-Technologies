package com.example.saigeetha.homework9.stockdetails;

/**
 * Created by SaiGeetha on 5/5/2016.
 */
public class NewsDetailsBean {

    String title;
    String desc;
    String source;
    String date;
    public void NewsDetailBean(){

    }
    public NewsDetailsBean(String title,String desc,String source,String date) {
        this.title=title;
        this.desc=desc;
        this.source=source;
        this.date=date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
