package com.gamehub;

public class UserSession {
    private static UserSession instance;
    private int userId;
    private String username;

    private UserSession() {
        this.userId = -1;
        this.username = null;
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void login(int userId, String username) {
        this.userId = userId;
        this.username = username;
        System.out.println("Session created for: " + username);
    }

    public void logout() {
        System.out.println("Logging out: " + username);
        this.userId = -1;
        this.username = null;
    }

    public boolean isLoggedIn() {
        return userId > 0;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
