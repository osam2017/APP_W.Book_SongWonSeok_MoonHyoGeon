package com.example.administrator.wbook;

/**
 * Created by Administrator on 2017-10-18.
 */

public class Book {
    private int num;
    private String title;
    private int likenum;
    private int num_pf_s;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {this.num = num;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLikenum() {
        return likenum;
    }

    public void setLikenum(int likenum) {
        this.likenum = likenum;
    }

    public int getNum_pf_s() {
        return num_pf_s;
    }

    public void setNum_pf_s(int num_pf_s) {
        this.num_pf_s = num_pf_s;
    }
}
