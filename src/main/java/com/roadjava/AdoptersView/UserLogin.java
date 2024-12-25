package com.roadjava.AdoptersView;

public class UserLogin {
    private String username;
    private String password;
    private boolean loggedIn;

    // 构造函数
    public UserLogin(String username, String password) {
        this.username = username;
        this.password = password;
        this.loggedIn = false;
    }

    // 获取用户名
    public String getUsername() {
        return username;
    }

    // 设置用户名
    public void setUsername(String username) {
        this.username = username;
    }

    // 获取密码
    public String getPassword() {
        return password;
    }

    // 设置密码
    public void setPassword(String password) {
        this.password = password;
    }

    // 获取登录状态
    public boolean isLoggedIn() {
        return loggedIn;
    }

    // 设置登录状态
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}

