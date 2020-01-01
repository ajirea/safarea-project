package my.id.satria.safarea.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import my.id.satria.safarea.data.User;

public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();

        spEditor.putInt("id", user.getId());
        spEditor.putString("username", user.getUsername());
        spEditor.putString("email", user.getEmail());
        spEditor.putString("name", user.getName());
        spEditor.putString("password", user.getPassword());
        spEditor.putString("storeName", user.getStoreName());
        spEditor.putString("phone", user.getPhone());
        spEditor.putString("avatar", user.getAvatar());
        spEditor.putString("token", user.getToken());
        spEditor.putString("address", user.getAddress());
        spEditor.putString("village", user.getVillage());
        spEditor.putString("district", user.getDistrict());
        spEditor.putString("city", user.getCity());
        spEditor.putString("province", user.getProvince());
        spEditor.putString("postalCode", user.getPostalCode());
        spEditor.putString("created_at", user.getCreatedAt());
        spEditor.putBoolean("isAdmin", user.getAdmin());
        spEditor.apply();
    }

    public User getLoggedInUser() {
        User user = new User(
                userLocalDatabase.getInt("id", 0),
                userLocalDatabase.getString("username", ""),
                userLocalDatabase.getString("email", ""),
                userLocalDatabase.getString("name", ""),
                userLocalDatabase.getString("password", ""),
                userLocalDatabase.getString("token", ""),
                userLocalDatabase.getString("storeName", ""),
                userLocalDatabase.getString("phone", ""),
                userLocalDatabase.getString("avatar", "")
        );

        user.setAddress(userLocalDatabase.getString("address", ""));
        user.setVillage(userLocalDatabase.getString("village", ""));
        user.setDistrict(userLocalDatabase.getString("district", ""));
        user.setCity(userLocalDatabase.getString("city", ""));
        user.setProvince(userLocalDatabase.getString("province", ""));
        user.setPostalCode(userLocalDatabase.getString("postalCode", ""));
        user.setAdmin(userLocalDatabase.getBoolean("isAdmin", false));
        user.setCreatedAt(userLocalDatabase.getString("createdAt", ""));

        return user;
    }

    public Boolean isUserLoggedIn() {
        return userLocalDatabase.getBoolean("loggedIn", false);
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.apply();
    }

    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.apply();
    }

}
