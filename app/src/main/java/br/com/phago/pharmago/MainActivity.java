package br.com.phago.pharmago;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_COUNTER = "COUNTER";
    // global variables to hold user data
    public static String user_id, user_name, user_email;
    public static int mCounter = 0;
    // DatabaseHelper
    PgDatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UpdateUser("gcfmelo@gmail.com", "abc123");
        UpdateQuiz("gcfmelo@gmail.com", "abc123");

/*
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView,
                                    View itemView,
                                    int position,
                                    long id) {
                if (position == 0) {

                    // TODO do the "UserMenuActivity" need to handle this Intent in some way?
                    Toast.makeText(MainActivity.this, "You have selected \"User\"", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, UserMenuActivity.class);
                    startActivity(intent);

                }
                if (position == 1) {
                    // TODO do the "AccountMenuActivity" need to handle this Intent in some way?
                    // Intent intent = new Intent(MainActivity.this, AccountMenuActivity.class);
                    //startActivity(intent);
                    Toast.makeText(MainActivity.this, "You have selected \"Account\"", Toast.LENGTH_SHORT).show();
                }
                if (position == 2) {
                    // TODO do the "CampaignsMenuActivity" need to handle this Intent in some way?


                    Toast.makeText(MainActivity.this, "You have selected \"Campaigns\"", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, CampaignsMenuActivity.class);
                    startActivity(intent);
                }
                if (position == 3) {
                    // TODO do the "AboutMenuActivity" need to handle this Intent in some way?
                    //Intent intent = new Intent(MainActivity.this, AboutMenuActivity.class);
                    //startActivity(intent);
                    Toast.makeText(MainActivity.this, "You have selected \"About\"", Toast.LENGTH_SHORT).show();
                }
            }
        };
        ListView listView = (ListView) findViewById(R.id.list_options);
        listView.setOnItemClickListener(itemClickListener);
      */
    }

    /*
    public void onClickStartServices(View view) {

        Intent intent = new Intent(this, getWsData.class);
        intent.setAction(getWsData.ACTION_GET_LOGIN_DATA);
        intent.putExtra(getWsData.EXTRA_EMAIL, "gcfmelo@gmail.com");
        intent.putExtra(getWsData.EXTRA_PASSWORD, "abc123");
        startService(intent);


        Intent intent2 = new Intent(this, getWsData.class);
        intent2.setAction(getWsData.ACTION_GET_TRANSACTIONS_DATA);
        intent2.putExtra(getWsData.EXTRA_EMAIL, "gcfmelo@gmail.com");
        intent2.putExtra(getWsData.EXTRA_PASSWORD, "abc123");
        startService(intent2);

        Intent intent3 = new Intent(this, getWsData.class);
        intent3.setAction(getWsData.ACTION_GET_QUIZ_DATA);
        intent3.putExtra(getWsData.EXTRA_EMAIL, "gcfmelo@gmail.com");
        intent3.putExtra(getWsData.EXTRA_PASSWORD, "abc123");
        startService(intent3);



        // TEST AND DEBUG PURPOSES
        Intent intent4 = new Intent(this, getWsData.class);
        intent4.setAction(getWsData.ACTION_TEST);
        intent4.putExtra(getWsData.EXTRA_EMAIL, "gcfmelo@gmail.com");
        intent4.putExtra(getWsData.EXTRA_PASSWORD, "abc123");
        startService(intent4);

    }
    */


    public void UpdateUser(String email, String password) {

        final String TAG = "UpdateUser: ";
        db = new PgDatabaseHelper(getApplicationContext());

        try {

            RequestQueue queue = Volley.newRequestQueue(this);

            URL urlObj = createURL(email, password, "login", "123456789");     //format URL to call WS
            final String mEmail = email;
            final String mPassword = password;
            String url = urlObj.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String myJsonString = response.toString();
                    User user;
                    Log.i(TAG, "TEXT RETURNED:   @@@   " + myJsonString);
                    if (myJsonString.startsWith("[")) {
                        myJsonString = "{\"transaction\":" + myJsonString + "}";
                    }
                    // converting to JSONObject
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(myJsonString);
                        // constructor: public User(String email, String cpf, String name, String status, String companyCode, String companyName, String companyLatitude, String companyLongitude)
                        user = new User(jsonObj.getString("email"), jsonObj.getString("cpf"), jsonObj.getString("name"),
                                jsonObj.getString("status"), jsonObj.getString("companyCode"),
                                jsonObj.getString("companyName"), jsonObj.getString("companyLatitude"),
                                        jsonObj.getString("companyLongitude"));

                        db.createUser(user);

                        Log.i(TAG, "user created: @@@   " + jsonObj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.i(TAG, "JSON OBJ RETURN: @@@   " + jsonObj.toString());

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error.getMessage() == null) {
                        Log.i(TAG, "login   -  " + "Login has failed!" + "\n\n");
                        // TODO: reset your password, create a new account
                    } else {
                        Log.i(TAG, "login   -  " + "onErrorResponse(): " + error.getMessage());
                    }
                }
            });
            queue.add(stringRequest);   // replace for the correct object name
        } catch (SQLiteException e) {
            Log.i(TAG, "Service: " + "Database is unavailable!");
        }

    }

    public void UpdateQuiz(String email, String password) {

        final String TAG = "UpdateQuiz: ";
        db = new PgDatabaseHelper(getApplicationContext());

        try {

            RequestQueue queue = Volley.newRequestQueue(this);

            URL urlObj = createURL(email, password, "getQuiz", "123456789");     //format URL to call WS
            final String mEmail = email;
            final String mPassword = password;
            String url = urlObj.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String myJsonString = response.toString();
                    Quiz quiz;
                    Log.i(TAG, "TEXT RETURNED:   @@@   " + myJsonString);
                    if (myJsonString.startsWith("[")) {
                        myJsonString = "{\"quiz\":" + myJsonString + "}";
                    }
                    // converting to JSONObject
                    JSONObject jsonObj0 = null;
                    JSONObject jsonObj2 = null;
                    JSONArray jsonArry1 = null;


                    try {
                        jsonObj0 = new JSONObject(myJsonString);
                        Log.i(TAG, "JSON Quiz 0 @@@   " + jsonObj0.toString());
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                    try {
                        jsonArry1 = jsonObj0.getJSONArray("quiz");
                        Log.i(TAG, "JSON Quiz 1 @@@   " + jsonArry1.toString());
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    if (jsonArry1 != null) {
                        for (int i = 0; i < jsonArry1.length(); i++) {
                            try {
                                JSONObject obj = jsonArry1.getJSONObject(i);


                                // constructor: Quiz(int idQuiz, String sponsorCode, String token, String status, String createdAt)
                                quiz = new Quiz(obj.getInt("idQuiz"),obj.getJSONObject("campaign").getString("sponsorCode").toString(),
                                        obj.getString("token"), obj.getString("status"));
                                db.createQuiz(quiz);

                                // TODO remove
                                String txtLog = "idQuiz: "+
                                        obj.getInt("idQuiz") + ", token: " +
                                        obj.getString("token") + ", status: " +
                                        obj.getString("status") +"\n"+
                                        "sponsorCode: "+
                                        obj.getJSONObject("campaign").getString("sponsorCode").toString()+
                                        ", sponsorName: "+
                                        obj.getJSONObject("campaign").getString("sponsorName").toString()+
                                        "\n";

                                Log.i(TAG, "JSON Quiz @@@   " + txtLog);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error.getMessage() == null) {
                        Log.i(TAG, "login   -  " + "Login has failed!" + "\n\n");
                        // TODO: reset your password, create a new account
                    } else {
                        Log.i(TAG, "login   -  " + "onErrorResponse(): " + error.getMessage());
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
