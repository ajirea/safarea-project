package com.perjalanan.safarea.repositories;

public class ServerAPI {

    public static final String BASE_URL = "https://api.satmaxt.xyz/safarea";
    public static final String API_URL = BASE_URL.concat("/api/");
    public static final String AUTH_LOGIN = API_URL.concat("auth/login");
    public static final String EDIT_ACCOUNT = API_URL.concat("user/");
    public static final String CHANGE_PASSWORD_ACCOUNT = API_URL.concat("user/change-password");
    public static final String CHANGE_ADDRESS_ACCOUNT = API_URL.concat("user/address");

}
