package com.example.test_mysql;

public class List_All_Users {

    public String UserKey;
    public String User_name;
    public String Email;
    public String RegDate;
    public String Avatar_img;
    public int ViewType;

    public String getUserKey() {
        return UserKey;
    }

    public void setUserKey(String userKey) {
        this.UserKey = userKey;
    }

    public String getUser_name() {
        return User_name;
    }

    public void setUser_name(String user_name) {
        User_name = user_name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getRegDate() {
        return RegDate;
    }

    public void setRegDate(String regDate) {
        RegDate = regDate;
    }

    public String getAvatar_img() {
        return Avatar_img;
    }

    public void setAvatar_img(String avatar_img) {
        Avatar_img = avatar_img;
    }

    public int getViewType() {
        return ViewType;
    }

    public void setViewType(int viewType) {
        ViewType = viewType;
    }
}
