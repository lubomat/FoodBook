package com.cookBook.CookBook.security;

public class AuthenticationRequest {
    private String usernameOrEmail;  // Zmiana nazwy pola

    private String password;

    public String getUsernameOrEmail() {  // Getter dla usernameOrEmail
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {  // Setter dla usernameOrEmail
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
