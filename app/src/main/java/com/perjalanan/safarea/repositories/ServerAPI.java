package com.perjalanan.safarea.repositories;

public interface ServerAPI {

    String BASE_URL = "https://api.satmaxt.xyz/safarea";
    String API_URL = BASE_URL.concat("/api/");
    String AUTH_LOGIN = API_URL.concat("auth/login");
    String EDIT_ACCOUNT = API_URL.concat("user/");
    String CHANGE_PASSWORD_ACCOUNT = API_URL.concat("user/change-password");
    String CHANGE_ADDRESS_ACCOUNT = API_URL.concat("user/address");
    String SUPPLIER_CATALOG = API_URL.concat("product");

    String BUYER_ALL_DATA = API_URL.concat("buyers");

}
