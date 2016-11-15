package com.gaokao366.gaokao366touser.model.ui.main.bean;

/**
 * Created by guangzai on 16/5/20.
 */
public class HomeMenuBean {

    private String name ;
    private int res;

    public HomeMenuBean(int res,String name){
        this.name = name;
        this.res = res;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
