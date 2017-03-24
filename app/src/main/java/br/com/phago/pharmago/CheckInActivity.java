package br.com.phago.pharmago;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class CheckInActivity extends AppCompatActivity {
    // to SQLite updating
    static final boolean RECREATE_TABLES = false;
    // to check
    public static final String WS_RETURN_OK = "SUCCESSFUL";
    public static final String WS_RETURN_ERROR = "ERROR";

    static final String KEY_EMAIL = "EMAIL";
    static final String KEY_PASSWORD = "PASSWORD";

    // to pass to Intent
    public static final String EXTRA_EMAIL = "EMAIL";
    public static final String EXTRA_PASSWORD = "PASSWORD";
    public static final String EXTRA_USERNAME = "USERNAME";
    public static final int EXTRA_LOGIN_ATTEMPT = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String selectedEmail = intent.getStringExtra(MainActivity.EXTRA_EMAIL);
        String selectedPassword = intent.getStringExtra(MainActivity.EXTRA_PASSWORD);
        EditText eTemail = (EditText) findViewById(R.id.editTextEmailAddress);
        EditText eTpassword = (EditText) findViewById(R.id.editTextPassword);
        eTemail.setText(selectedEmail);
        eTpassword.setText(selectedPassword);

    }

    public void btnLoginClick(View view){
        EditText eTemail = (EditText) findViewById(R.id.editTextEmailAddress);
        EditText eTpassword = (EditText) findViewById(R.id.editTextPassword);
        CheckBox chkEmail = (CheckBox) findViewById(R.id.checkBoxSaveEmail);
        CheckBox chkPassword = (CheckBox) findViewById(R.id.checkBoxSavePassword);

        Button button = (Button) view;

        // save email and password to PreferenceManager, accordingly to user selection
        if (chkEmail.isChecked()&&(eTemail.getText().toString().contains("@"))) {
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("KEY_EMAIL", eTemail.getText().toString()).commit();
            if (chkPassword.isChecked()){
                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("KEY_PASSWORD", eTpassword.getText().toString()).commit();
            }
        }
        UpdateUser(eTemail.getText().toString(), eTpassword.getText().toString());

        Intent intentMain = new Intent(CheckInActivity.super.getApplicationContext(), MainActivity.class);
        intentMain.putExtra(EXTRA_EMAIL, eTemail.getText().toString());                       // fill login editTextEmail
        intentMain.putExtra(EXTRA_PASSWORD, eTpassword.getText().toString());                 // fill login editTextPassword
        startActivity(intentMain);

//        Toast.makeText(this, "Login: "+eTemail.getText(),Toast.LENGTH_SHORT).show();
//        PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(this);
//        User userBd = dbx.getLastUser();
//        dbx.close();
//
//        if ((userBd.getEmail()==eTemail.getText().toString())&&(userBd.getPassword()==eTpassword.getText().toString())){
//            // user in form is the same in the BD
//            // Toast.makeText(this, "User is the same in Database", Toast.LENGTH_SHORT).show();
//
//            Intent intentMain = new Intent(CheckInActivity.super.getApplicationContext(), MainActivity.class);
//            intentMain.putExtra(EXTRA_EMAIL, eTemail.getText().toString());                       // fill login editTextEmail
//            intentMain.putExtra(EXTRA_PASSWORD, eTpassword.getText().toString());                 // fill login editTextPassword
//            startActivity(intentMain);
//
//        } else {
//            Toast.makeText(this, "User is NOT the same in Database... I will try to login", Toast.LENGTH_SHORT).show();
//
////            UpdateUser(eTemail.getText().toString(), eTpassword.getText().toString());
//
//            // reload this activity
//            Intent intentMyself = getIntent();
//            overridePendingTransition(0, 0);
//            intentMyself.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            finish();
//            overridePendingTransition(0, 0);
//            startActivity(intentMyself);
//        }



    }

    public void btnExitClick(View view){
        EditText eTemail = (EditText) findViewById(R.id.editTextEmailAddress);
        EditText eTpassword = (EditText) findViewById(R.id.editTextPassword);
        CheckBox chkEmail = (CheckBox) findViewById(R.id.checkBoxSaveEmail);
        CheckBox chkPassword = (CheckBox) findViewById(R.id.checkBoxSavePassword);

        Button button = (Button) view;

        // save email and password to PreferenceManager, accordingly to user selection
        if (chkEmail.isChecked()&&(eTemail.getText().toString().contains("@"))) {
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("KEY_EMAIL", eTemail.getText().toString()).commit();
            if (chkPassword.isChecked()){
                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("KEY_PASSWORD", eTpassword.getText().toString()).commit();
            }
        }
        finish();



        //       UpdateUser(eTemail.getText().toString(), eTpassword.getText().toString());
//        Toast.makeText(this, "Login: "+eTemail.getText(),Toast.LENGTH_SHORT).show();
//
//        PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(this);
//        User userBd = new User();
//        userBd = dbx.getLastUser();
//        dbx.close();
//        if ((userBd.getEmail()==eTemail.getText().toString())&&(userBd.getPassword()==eTpassword.getText().toString())){
//            // user in form is the same in the BD
//            Toast.makeText(this, "User is the same in Database", Toast.LENGTH_SHORT).show();
//            finish();
//
//        } else {
//            Toast.makeText(this, "User is NOT the same in Database", Toast.LENGTH_SHORT).show();
//        }



    }

    public void onClickCheckBoxEmail(View view){
        CheckBox chkEmail = (CheckBox) findViewById(R.id.checkBoxSaveEmail);
        CheckBox chkPassword = (CheckBox) findViewById(R.id.checkBoxSavePassword);
        CheckBox checkBox = (CheckBox) view;

        if (!chkEmail.isChecked()){
            // email is not checked... do not allow checking password to be saved
            chkPassword.setChecked(false);
        }
    }

    public void onClickCheckBoxPassword(View view){
        CheckBox chkEmail = (CheckBox) findViewById(R.id.checkBoxSaveEmail);
        CheckBox chkPassword = (CheckBox) findViewById(R.id.checkBoxSavePassword);

        CheckBox checkBox = (CheckBox) view;

        if (!chkEmail.isChecked()){
            // email is not checked... do not allow checking password to be saved
            chkPassword.setChecked(false);
        }
    }

    public void UpdateUser(String email, final String password) {
        // @@@
        final PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(this);

        final String TAG = "UpdateUser";
        Log.d(TAG, " process started");
        if (RECREATE_TABLES) {
            dbx.dropTable("pg_user");
            dbx.createTableUser();
        } else {
            dbx.clearTableUser();
        }
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            URL urlObj = createURL(email, password, "doLogin", "123456789");     //format URL to call WS
            String url = urlObj.toString();
            Log.i(TAG, "URL:   " + url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String myJsonString = response.toString();
                    myJsonString = myJsonString.replace("\\\"", "\"");
                    myJsonString = myJsonString.replace("\":\"{", "\":{");
                    myJsonString = myJsonString.replace("}\"}", "}}");
                    // converting to JSONObject
                    JSONObject jsonObjRoot = null;
                    JSONObject jsonObjUser = null;
                    try {
                        jsonObjRoot = new JSONObject(myJsonString);
                        if (jsonObjRoot.getString("status").toString().equals(WS_RETURN_OK)) {
                            jsonObjUser = jsonObjRoot.getJSONObject("json");
                            if (jsonObjUser != null) {
                                User user = new User(jsonObjUser.getString("email"),
                                        password,
                                        jsonObjUser.getString("name"),
                                        jsonObjUser.getString("userAccountStatus"),
                                        jsonObjUser.getString("cpf"),
                                        jsonObjUser.getString("companyCode"),
                                        jsonObjUser.getString("companyName"),
                                        jsonObjUser.getString("companyLatitude"),
                                        jsonObjUser.getString("companyLongitude"));
                                dbx.addUser(user);
                            }
                            // @@@
                            dbx.close();
                        } else {
                            // WS responded with:
                            Log.i(TAG, "WS responded with:   @@@   " + jsonObjRoot.getString("status").toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.getMessage() == null) {
                        Log.i(TAG, " Web Service has failed!" + "\n\n");
                        // TODO: reset your password, create a new account
                    } else {
                        Log.i(TAG, "onErrorResponse(): " + error.getMessage());
                    }
                }
            });
            queue.add(stringRequest);   // replace for the correct object name
        } catch (SQLiteException e) {
            Log.i(TAG, "Service: " + "Database is unavailable!");
        }
    }

    public void loginOk(String email, final String password) {
        // @@@
//        final PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(this);
        final String TAG = "loginOk";
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            URL urlObj = createURL(email, password, "doLogin", "123456789");     //format URL to call WS
            String url = urlObj.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String myJsonString = response.toString();
                    myJsonString = myJsonString.replace("\\\"", "\"");
                    myJsonString = myJsonString.replace("\":\"{", "\":{");
                    myJsonString = myJsonString.replace("}\"}", "}}");
                    // converting to JSONObject
                    JSONObject jsonObjRoot = null;
                    JSONObject jsonObjUser = null;
                    try {
                        jsonObjRoot = new JSONObject(myJsonString);
                        if (jsonObjRoot.getString("status").toString().equals(WS_RETURN_OK)) {
                            Toast.makeText(CheckInActivity.this, "Success Login", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CheckInActivity.this, "Login has failed", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "WS responded with:   @@@   " + jsonObjRoot.getString("status").toString());
                            //
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //
                    }
                }   // end of onResponse event method


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.getMessage() == null) {
                        Log.i(TAG, " Web Service has failed!" + "\n\n");
                        // TODO: reset your password, create a new account
                        //
                    } else {
                        Log.i(TAG, "onErrorResponse(): " + error.getMessage());
                        //
                    }
                }
            });
            queue.add(stringRequest);   // replace for the correct object name
        } catch (SQLiteException e) {
            Log.i(TAG, "Service: " + "Database is unavailable!");
        }
    }

    private URL createURL(String email, String password, String action, String token) {
        String baseURL = "http://www.benben.net.br/apiPharmaGo?";
        // LoginActivity.super.getString(R.string.web_sevice_base_url);
        try {
            // create URL for specified email and password
            String baseUrl = "http://www.benben.net.br/apiPharmaGo?";
            String urlString = baseUrl + "email=" + email + "&password=" + password + "&action=" + action + "&token=" + token;
            // URLEncoder.encode(city, "UTF-8") use caso haja espaços ou outros caracteres especiais em uma string da consulta...

            return new URL(urlString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
