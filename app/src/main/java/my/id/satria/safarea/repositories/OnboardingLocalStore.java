package my.id.satria.safarea.repositories;

import android.content.Context;
import android.content.SharedPreferences;

public class OnboardingLocalStore {

    public static final String SP_NAME = "ONBOARDING_PREFS";
    private SharedPreferences onboardingLocalDatabase;

    public OnboardingLocalStore(Context context) {
        onboardingLocalDatabase = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void setOpenedOnboarding(Boolean status) {
        SharedPreferences.Editor mEditor = onboardingLocalDatabase.edit();
        mEditor.putBoolean("isOpenedOnboarding", status);
        mEditor.apply();
    }

    public Boolean isOpenedOnboarding() {
        return onboardingLocalDatabase.getBoolean("isOpenedOnboarding", false);
    }
}
