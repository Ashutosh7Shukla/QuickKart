package com.Shopping.dream_shop.request;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

public class    LoginRequest {
    @NotNull
    private String email;
    @NotNull
    private  String password;

    public @NotNull String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    public @NotNull String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }
}
