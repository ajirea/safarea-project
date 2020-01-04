package com.perjalanan.safarea.repositories;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class RequestGlobalHeaders {
    private Map<String, String> headers = new HashMap<>();
    private UserLocalStore userLocalStore;
    private Context context;

    public  RequestGlobalHeaders(Context context) {
        this.context = context;
        userLocalStore = new UserLocalStore(context);
    }

    public static Map<String, String> get(Context context) {
        RequestGlobalHeaders rgh = new RequestGlobalHeaders(context);
        rgh.getHeaders().put("Accept", "application/json");
        rgh.getHeaders().put("Content-Type", "application/x-www-form-urlencoded");

        if(rgh.getUserLocalStore().isUserLoggedIn())
            rgh.getHeaders().put("api_token", rgh.getUserLocalStore().getLoggedInUser().getToken());

        return rgh.getHeaders();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public UserLocalStore getUserLocalStore() {
        return userLocalStore;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
