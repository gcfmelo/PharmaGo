package br.com.phago.pharmago;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final boolean RECREATE_TABLES = false;
    public static final String WS_RETURN_OK = "SUCCESSFUL";
    public static final String WS_RETURN_ERROR = "ERROR";
    public static final String EXTRA_CAMPAIGN_ID = "br.com.phago.pharmago.CAMPAIGN_ID";
    public static final String ENABLE_PARTICIPATION_YN = "br.com.phago.pharmago.ENABLE_PARTICIPATION_YN";

    private static final String TAG = MainActivity.class.getSimpleName();
    public static PgDatabaseHelper dbx;

    // to save USER DATA in SharedPreferences
    static final String KEY_COUNTER = "COUNTER";     // utilization counter
    static final String KEY_USERNAME = "USERNAME";
    static final String KEY_EMAIL = "EMAIL";
    static final String KEY_PASSWORD = "PASSWORD";

    // to save USER DATA in SharedPreferences
    static final int KEY_COUNTER_NOT_FOUND_VALUE = 1;     // utilization counter
    static final String KEY_USERNAME_NOT_FOUND_VALUE = "username not found";
    static final String KEY_EMAIL_NOT_FOUND_VALUE = "e-mail  not found";
    static final String KEY_PASSWORD_NOT_FOUND_VALUE = "password not found";

    // to use with Intent
    public static final String EXTRA_EMAIL = "EMAIL";
    public static final String EXTRA_PASSWORD = "PASSWORD";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        setContentView(R.layout.activity_splash_screen);
//        Limpar PreferedManager
//        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().commit();
//
        Intent intent = getIntent();
        String selectedEmail = intent.getStringExtra(MainActivity.EXTRA_EMAIL);
        String selectedPassword = intent.getStringExtra(MainActivity.EXTRA_PASSWORD);
        if (selectedEmail == null) {
            selectedEmail = "";
            selectedPassword = "";
        }

        if (selectedEmail == "" || selectedPassword == "" || !selectedEmail.contains("@")) {
            // user credentials passed as EXTRA parameters ARE NOT usable
            // retrieve user data from PreferenceManager
            String bEmail = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("KEY_EMAIL", KEY_EMAIL_NOT_FOUND_VALUE);
            String bUsername = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("KEY_PASSWORD", KEY_USERNAME_NOT_FOUND_VALUE);
            String bPassword = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("KEY_USERNAME", KEY_PASSWORD_NOT_FOUND_VALUE);
            int bCounter = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("KEY_COUNTER", KEY_COUNTER_NOT_FOUND_VALUE);

            if (bEmail == null || bPassword == null || !selectedEmail.contains("@")) {
                // if user data from PreferenceManager IS NOT potentially valid open CheckInActivity with blank credentials
                Intent intentCheckIn = new Intent(MainActivity.super.getApplicationContext(), CheckInActivity.class);
                intentCheckIn.putExtra(EXTRA_EMAIL, "");
                intentCheckIn.putExtra(EXTRA_PASSWORD, "");
                startActivity(intentCheckIn);
                finish();
            } else {
                // user data from PreferenceManager IS potentially valid open CheckInActivity with that credentials
                Intent intentCheckIn = new Intent(MainActivity.super.getApplicationContext(), CampaignActivity.class);
                intentCheckIn.putExtra(EXTRA_EMAIL, bEmail);
                intentCheckIn.putExtra(EXTRA_PASSWORD, bPassword);
                startActivity(intentCheckIn);
                finish();
            }

        } else {
            // user credentials passed as EXTRA parameters ARE potentially valid open CheckInActivity with that credentials
            //
            Intent intentCheckIn = new Intent(MainActivity.super.getApplicationContext(), CheckInActivity.class);
            intentCheckIn.putExtra(EXTRA_EMAIL, selectedEmail);
            intentCheckIn.putExtra(EXTRA_PASSWORD, selectedPassword);
            startActivity(intentCheckIn);
            finish();
        }


    }
}

// variables to hold values of settings in the BEGINNING of this session

// DEVELOPMENT TOOL 1:
//                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("KEY_EMAIL", "gcfmelo@gmail.com").commit();
//                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("KEY_PASSWORD", "abc123").commit();
//                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("KEY_USERNAME", "Gustavo Melo").commit();
//                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt("KEY_COUNTER", 0).commit();

// DEVELOPMENT TOOL 2:  RESET PREFERENCES
//        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("KEY_EMAIL", "").commit();
//        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("KEY_PASSWORD", "").commit();
//        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("KEY_USERNAME", "").commit();
//        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt("KEY_COUNTER", 0).commit();

// DEVELOPMENT TOOL 3:   CLEAR ALL PREFS
//        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();


