package org.example.models.json;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthInfo {
    @JsonProperty("session_token")
    private String sessionToken;

    @JsonProperty("last_login")
    private OffsetDateTime lastLogin;

    @JsonProperty("reset_token")
    private String resetToken;

    @JsonProperty("expires")
    private OffsetDateTime expires;

    // Default constructor
    public AuthInfo() {
    }

    // Constructor with all fields
    public AuthInfo(String sessionToken, OffsetDateTime lastLogin, String resetToken, OffsetDateTime expires) {
        this.sessionToken = sessionToken;
        this.lastLogin = lastLogin;
        this.resetToken = resetToken;
        this.expires = expires;
    }

    // Getters and Setters
    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public OffsetDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(OffsetDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public OffsetDateTime getExpires() {
        return expires;
    }

    public void setExpires(OffsetDateTime expires) {
        this.expires = expires;
    }
}