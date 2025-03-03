package com.Shopping.dream_shop.request;

import jakarta.validation.constraints.NotNull;

public class SignUpRequest {

    @NotNull
    private String email;
    @NotNull
    private  String password;

}
