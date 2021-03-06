package com.perjalanan.safarea.repositories;

public interface ServerAPI {

    String BASE_URL = "https://api.satmaxt.xyz/safarea";
    String API_URL = BASE_URL.concat("/api/");
    String AUTH_LOGIN = API_URL.concat("auth/login");
    String EDIT_ACCOUNT = API_URL.concat("user/");
    String CHANGE_PASSWORD_ACCOUNT = API_URL.concat("user/change-password");
    String CHANGE_ADDRESS_ACCOUNT = API_URL.concat("user/address");
    String SUPPLIER_CATALOG = API_URL.concat("product");
    String DROPSHIPPER = API_URL.concat("product/dropshipper/");
    String ORDER = API_URL.concat("order");
    String RECENT = API_URL.concat("recent-order");
    String PROFIT = API_URL.concat("profit");
    String STORE_URL = BASE_URL.concat("/store/");

    String BUYER_ALL_DATA = API_URL.concat("buyers");

}
