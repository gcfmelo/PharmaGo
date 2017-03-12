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

        final String TAG = "Main Activity";
        Log.i(TAG,"... onCreate");
        //UpdateUser("gcfmelo@gmail.com", "abc123");
        //UpdateSponsorAndQuiz("gcfmelo@gmail.com", "abc123");
        UpdateSponsor("gcfmelo@gmail.com", "abc123");
        //UpdateCampaign("gcfmelo@gmail.com", "abc123");
        //UpdateQuestion("gcfmelo@gmail.com", "abc123");

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
                    Log.i(TAG, "TEXT RETURNED:   135 @@@   " + myJsonString);
                    if (myJsonString.startsWith("[")) {
                        myJsonString = "{\"transaction\":" + myJsonString + "}";
                    }
                    // converting to JSONObject
                    JSONObject jsonObj = null;
                    try {

                        // drop table pg_user
                        db.dropTable("pg_user");

                        // create a new table pg_user
                        db.createTableUser();

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

                    Log.i(TAG, "JSON OBJ RETURN: 153 @@@   " + jsonObj.toString());

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

    public void UpdateSponsorAndQuiz(String email, String password) {

        final String TAG = "UpdateSponsorAndQuiz";
        db = new PgDatabaseHelper(getApplicationContext());
        try {

            RequestQueue queue = Volley.newRequestQueue(this);

            URL urlObj = createURL(email, password, "getQuiz", "123456789");     //format URL to call WS
            final String mEmail = email;
            final String mPassword = password;
            String url = urlObj.toString();
            Log.i(TAG, "URL:   " + url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String myJsonString = response.toString();
                    Quiz quiz;
                    Sponsor sponsor;
                    Log.i(TAG, "TEXT RETURNED:   @@@   " + myJsonString);
                    if (myJsonString.startsWith("[")) {
                        myJsonString = "{\"quiz\":" + myJsonString + "}";
                    }
                    // converting to JSONObject
                    JSONObject jsonObjRoot = null;
                    JSONArray jsonArryQuiz = null;

                    try {
                        jsonObjRoot = new JSONObject(myJsonString);

                        // drop table pg_sponsor
                        db.dropTable("pg_sponsor");

                        // create a new table pg_sponsor
                        db.createTableSponsor();

                        // create a temp table for sponsor
                        db.createTableTempSponsor();

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                    try {
                        // top level array of Quizes
                        jsonArryQuiz = jsonObjRoot.getJSONArray("quiz");
                        Log.i(TAG, "JSON Quiz @@@   " + jsonArryQuiz.toString());
                        Log.i(TAG, "JSON Quiz @@@   # quiz " + Integer.toString(jsonArryQuiz.length()));

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    if (jsonArryQuiz != null) {
                        Log.v(TAG, "231 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                        for (int i = 0; i < jsonArryQuiz.length(); i++) {    // for each quiz
                            try {
                                // each element of the Array of quizes is a JSONObject, a specific Quiz
                                Log.v(TAG, " 235 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                                JSONObject obj = jsonArryQuiz.getJSONObject(i);

                                // calling constructors:
                                Log.v(TAG, " 239 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                                quiz = new Quiz(obj.getInt("idQuiz"), obj.getJSONObject("campaign").getString("sponsorCode").toString(),
                                        obj.getString("token"), obj.getString("status"));
                                db.createQuiz(quiz);
                                // Sponsor data are provided as fields (tags) in JSON Object of a campaign
                                sponsor = new Sponsor(obj.getJSONObject("campaign").getString("sponsorCode").toString(), obj.getJSONObject("campaign").getString("sponsorName").toString());
                                db.createSponsor(sponsor);
                                Log.v(TAG, " 245 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    // to eliminate duplicate sponsors...
                    // recreate table pg_sponsor with distinct rows from temp_pg_sponsor (removing duplicated records)
                    Log.i(TAG, "285: db.insertTempDataIntoTableSponsor()");
                    db.insertTempDataIntoTableSponsor();
                    // drop temp table temp_pg_sponsor
                    Log.i(TAG, "258: db.dropTable('temp_pg_sponsor')");
                    db.dropTable("temp_pg_sponsor");

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

    // this version is updated for new WS at 2017-03-11
    public void UpdateSponsor(String email, String password) {

        final String TAG = "UpdateSponsor";
        db = new PgDatabaseHelper(getApplicationContext());

        // db.dropTable("pg_sponsor");
        // db.createTableSponsor();
        db.clearTableSponsor();

        try {

            RequestQueue queue = Volley.newRequestQueue(this);

            URL urlObj = createURL(email, password, "findAllSponsors", "123456789");     //format URL to call WS
            final String mEmail = email;
            final String mPassword = password;
            String url = urlObj.toString();
            Log.i(TAG, "URL:   " + url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String myJsonString = response.toString();
                    Log.i(TAG, "309_TEXT RETURNED:   @@@   " + myJsonString);
                    myJsonString = myJsonString.replace("\\\"", "\"");
                    Log.i(TAG, "311_Removed Escapes:   @@@   " + myJsonString);
                    myJsonString = myJsonString.replace("\"[{", "[{");
                    myJsonString = myJsonString.replace("]\"}", "]}");
                    Log.i(TAG, "314_removed \":   @@@   " + myJsonString);



                    // converting to JSONObject
                    JSONObject jsonObjRoot = null;
                    JSONArray jsonArry = null;

                    try {
                        jsonObjRoot = new JSONObject(myJsonString);

                        if (jsonObjRoot.getString("status").toString().equals("SUCCESSFUL")){

                            jsonArry = jsonObjRoot.getJSONArray("json");
                            Log.i(TAG, "328_jsonArry: @@@"+jsonArry.toString());

                            if (jsonArry != null) {
                                for (int i = 0; i < jsonArry.length(); i++) {
                                    String mSpId = jsonArry.getJSONObject(i).getString("idSponsor").toString();
                                    String mSpCode = jsonArry.getJSONObject(i).getString("sponsorCode").toString();
                                    String mSpName = jsonArry.getJSONObject(i).getString("sponsorName").toString();

                                    Log.i(TAG, "Element:   idSponsor   @@@ ( " + Integer.toString(i)+" ) = "+ mSpId);
                                    Log.i(TAG, "Element:   sponsorCode @@@ ( " + Integer.toString(i)+" ) = "+ mSpCode);
                                    Log.i(TAG, "Element:   sponsorName @@@ ( " + Integer.toString(i)+" ) = "+ mSpName);


                                    Sponsor sponsor = new Sponsor(Integer.parseInt(mSpId), mSpCode, mSpName);

                                    db.addSponsor(sponsor);

                                    Log.i(TAG, "Element:   @@@   " + Integer.toString(i)+" "+ jsonArry.getJSONObject(i));
                                    //
                                    // implement table update here
                                    //

                                }

                                }


                        } else {
                            // WS responded with:
                            Log.i(TAG, "WS responded with:   @@@   " + jsonObjRoot.getString("status").toString());
                        }







                        // drop table pg_sponsor
                        //db.dropTable("pg_sponsor");

                        // create a new table pg_sponsor
                        //db.createTableSponsor();

                        // create a temp table for sponsor
                        // db.createTableTempSponsor();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //try {
                        // top level array of Quizes

//                        jsonArry = jsonObjRoot.getJSONArray("json");
//                        jsonArryQuiz = jsonObjRoot.getJSONArray("quiz");
//                        Log.i(TAG, "JSON Quiz @@@   " + jsonArryQuiz.toString());
//                        Log.i(TAG, "JSON Quiz @@@   # quiz " + Integer.toString(jsonArryQuiz.length()));

                    //} catch (JSONException e1) {
                       // e1.printStackTrace();
                    //}
//                    if (jsonArryQuiz != null) {
//                        Log.v(TAG, "231 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//                        for (int i = 0; i < jsonArryQuiz.length(); i++) {    // for each quiz
//                            try {
//                                // each element of the Array of quizes is a JSONObject, a specific Quiz
//                                Log.v(TAG, " 235 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//                                JSONObject obj = jsonArryQuiz.getJSONObject(i);
//
//                                // calling constructors:
//                                Log.v(TAG, " 239 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//                                quiz = new Quiz(obj.getInt("idQuiz"), obj.getJSONObject("campaign").getString("sponsorCode").toString(),
//                                        obj.getString("token"), obj.getString("status"));
//                                db.createQuiz(quiz);
//                                // Sponsor data are provided as fields (tags) in JSON Object of a campaign
//                                sponsor = new Sponsor(obj.getJSONObject("campaign").getString("sponsorCode").toString(), obj.getJSONObject("campaign").getString("sponsorName").toString());
//                                db.createSponsor(sponsor);
//                                Log.v(TAG, " 245 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }

                    // to eliminate duplicate sponsors...
                    // recreate table pg_sponsor with distinct rows from temp_pg_sponsor (removing duplicated records)
//                    Log.i(TAG, "285: db.insertTempDataIntoTableSponsor()");
//                    db.insertTempDataIntoTableSponsor();
                    // drop temp table temp_pg_sponsor
//                    Log.i(TAG, "258: db.dropTable('temp_pg_sponsor')");
//                    db.dropTable("temp_pg_sponsor");

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


    public void UpdateCampaign_x(String email, String password) {

        final String TAG = "UpdateCampaign";
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
                    Sponsor sponsor;
                    Campaign campaign;
                    Question question;
                    QuestionOption questionOption;

                    Log.i(TAG, "TEXT RETURNED:   @@@   " + myJsonString);
                    if (myJsonString.startsWith("[")) {
                        myJsonString = "{\"quiz\":" + myJsonString + "}";
                    }
                    // converting to JSONObject
                    JSONObject jsonObjRoot = null;
                    JSONArray jsonArryQuiz = null;

                    try {
                        jsonObjRoot = new JSONObject(myJsonString);

                        // drop table pg_sponsor
                        db.dropTable("pg_campaign");
                        // create a new table pg_sponsor, pg_campaign, ...
                        db.createTableCampaign();

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                    try {
                        // top level array of Quizes
                        jsonArryQuiz = jsonObjRoot.getJSONArray("quiz");

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    if (jsonArryQuiz != null) {
                        Log.v(TAG, " 239 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                        for (int i = 0; i < jsonArryQuiz.length(); i++) {    // for each quiz
                            try {
                                // each element of the Array of quizes is a JSONObject, a specific Quiz
                                Log.v(TAG, " 242 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                                JSONObject obj = jsonArryQuiz.getJSONObject(i);
                                Log.v(TAG, " 249 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                                // campaigns are JSON Objects composing a Quiz
                                campaign = new Campaign(obj.getJSONObject("campaign").getString("sponsorCode").toString(),
                                        obj.getJSONObject("campaign").getString("sponsorName").toString(),
                                        obj.getJSONObject("campaign").getString("startDate").toString(),
                                        obj.getJSONObject("campaign").getString("endDate").toString(),
                                        obj.getJSONObject("campaign").getInt("numberOfQuestions"),
                                        obj.getJSONObject("campaign").getInt("pointsForRightAnswer"),
                                        obj.getJSONObject("campaign").getInt("pointsForParticipation"),
                                        obj.getString("status").toString());
                                Log.v(TAG, " 349 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                                db.createCampaign(campaign);

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

    // this version is updated for new WS at 2017-03-11
    public void UpdateCampaign(String email, String password) {

        final String TAG = "UpdateCampaign";
        db = new PgDatabaseHelper(getApplicationContext());

        // db.dropTable("pg_sponsor");
        // db.createTableSponsor();
        db.clearTableSponsor();

        try {

            RequestQueue queue = Volley.newRequestQueue(this);

            URL urlObj = createURL(email, password, "findAllSponsors", "123456789");     //format URL to call WS
            final String mEmail = email;
            final String mPassword = password;
            String url = urlObj.toString();
            Log.i(TAG, "URL:   " + url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String myJsonString = response.toString();
                    Log.i(TAG, "309_TEXT RETURNED:   @@@   " + myJsonString);
                    myJsonString = myJsonString.replace("\\\"", "\"");
                    Log.i(TAG, "311_Removed Escapes:   @@@   " + myJsonString);
                    myJsonString = myJsonString.replace("\"[{", "[{");
                    myJsonString = myJsonString.replace("]\"}", "]}");
                    Log.i(TAG, "314_removed \":   @@@   " + myJsonString);



                    // converting to JSONObject
                    JSONObject jsonObjRoot = null;
                    JSONArray jsonArry = null;

                    try {
                        jsonObjRoot = new JSONObject(myJsonString);

                        if (jsonObjRoot.getString("status").toString().equals("SUCCESSFUL")){

                            jsonArry = jsonObjRoot.getJSONArray("json");
                            Log.i(TAG, "328_jsonArry: @@@"+jsonArry.toString());

                            if (jsonArry != null) {
                                for (int i = 0; i < jsonArry.length(); i++) {
                                    String mSpId = jsonArry.getJSONObject(i).getString("idSponsor").toString();
                                    String mSpCode = jsonArry.getJSONObject(i).getString("sponsorCode").toString();
                                    String mSpName = jsonArry.getJSONObject(i).getString("sponsorName").toString();

                                    Log.i(TAG, "Element:   idSponsor   @@@ ( " + Integer.toString(i)+" ) = "+ mSpId);
                                    Log.i(TAG, "Element:   sponsorCode @@@ ( " + Integer.toString(i)+" ) = "+ mSpCode);
                                    Log.i(TAG, "Element:   sponsorName @@@ ( " + Integer.toString(i)+" ) = "+ mSpName);


                                    Sponsor sponsor = new Sponsor(Integer.parseInt(mSpId), mSpCode, mSpName);

                                    db.addSponsor(sponsor);

                                    Log.i(TAG, "Element:   @@@   " + Integer.toString(i)+" "+ jsonArry.getJSONObject(i));
                                    //
                                    // implement table update here
                                    //

                                }

                            }


                        } else {
                            // WS responded with:
                            Log.i(TAG, "WS responded with:   @@@   " + jsonObjRoot.getString("status").toString());
                        }







                        // drop table pg_sponsor
                        //db.dropTable("pg_sponsor");

                        // create a new table pg_sponsor
                        //db.createTableSponsor();

                        // create a temp table for sponsor
                        // db.createTableTempSponsor();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //try {
                    // top level array of Quizes

//                        jsonArry = jsonObjRoot.getJSONArray("json");
//                        jsonArryQuiz = jsonObjRoot.getJSONArray("quiz");
//                        Log.i(TAG, "JSON Quiz @@@   " + jsonArryQuiz.toString());
//                        Log.i(TAG, "JSON Quiz @@@   # quiz " + Integer.toString(jsonArryQuiz.length()));

                    //} catch (JSONException e1) {
                    // e1.printStackTrace();
                    //}
//                    if (jsonArryQuiz != null) {
//                        Log.v(TAG, "231 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//                        for (int i = 0; i < jsonArryQuiz.length(); i++) {    // for each quiz
//                            try {
//                                // each element of the Array of quizes is a JSONObject, a specific Quiz
//                                Log.v(TAG, " 235 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//                                JSONObject obj = jsonArryQuiz.getJSONObject(i);
//
//                                // calling constructors:
//                                Log.v(TAG, " 239 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//                                quiz = new Quiz(obj.getInt("idQuiz"), obj.getJSONObject("campaign").getString("sponsorCode").toString(),
//                                        obj.getString("token"), obj.getString("status"));
//                                db.createQuiz(quiz);
//                                // Sponsor data are provided as fields (tags) in JSON Object of a campaign
//                                sponsor = new Sponsor(obj.getJSONObject("campaign").getString("sponsorCode").toString(), obj.getJSONObject("campaign").getString("sponsorName").toString());
//                                db.createSponsor(sponsor);
//                                Log.v(TAG, " 245 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }

                    // to eliminate duplicate sponsors...
                    // recreate table pg_sponsor with distinct rows from temp_pg_sponsor (removing duplicated records)
//                    Log.i(TAG, "285: db.insertTempDataIntoTableSponsor()");
//                    db.insertTempDataIntoTableSponsor();
                    // drop temp table temp_pg_sponsor
//                    Log.i(TAG, "258: db.dropTable('temp_pg_sponsor')");
//                    db.dropTable("temp_pg_sponsor");

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

    public void UpdateQuestion(String email, String password) {

        final String TAG = "UpdateQuestion";
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
                    Sponsor sponsor;
                    Campaign campaign;
                    Question question;
                    QuestionOption questionOption;

                    Log.i(TAG, "TEXT RETURNED:   @@@   " + myJsonString);
                    if (myJsonString.startsWith("[")) {
                        myJsonString = "{\"quiz\":" + myJsonString + "}";
                    }
                    // converting to JSONObject
                    JSONObject jsonObjRoot = null;
                    JSONArray jsonArryQuiz = null;

                    JSONArray jsonArryQuestion = null;
                    JSONObject jsonObjQuestion = null;
                    JSONArray jsonArryQuestionOptions = null;


                    try {
                        jsonObjRoot = new JSONObject(myJsonString);

                        // drop table pg_sponsor, pg_campaign, ...
                        db.dropTable("pg_question");
                        //db.dropTable("pg_option");

                        // create a new table pg_sponsor, pg_campaign, ...
                        db.createTableQuestion();
                        //db.createTableQuestionOption();

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                    try {
                        // top level array of Quizes
                        jsonArryQuiz = jsonObjRoot.getJSONArray("quiz");

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    if (jsonArryQuiz != null) {
                        for (int i = 0; i < jsonArryQuiz.length(); i++) {    // for each quiz
                            try {
                                // each element of the Array of quizes is a JSONObject, a specific Quiz
                                JSONObject obj = jsonArryQuiz.getJSONObject(i);

                                // calling constructors:
                                /*quiz = new Quiz(obj.getInt("idQuiz"), obj.getJSONObject("campaign").getString("sponsorCode").toString(),
                                        obj.getString("token"), obj.getString("status"));
                                */
                                // campaigns are JSON Objects composing a Quiz
                                /*campaign = new Campaign(obj.getJSONObject("campaign").getString("sponsorCode").toString(),
                                        obj.getJSONObject("campaign").getString("sponsorName").toString(),
                                        obj.getJSONObject("campaign").getString("startDate").toString(),
                                        obj.getJSONObject("campaign").getString("endDate").toString(),
                                        obj.getJSONObject("campaign").getInt("numberOfQuestions"),
                                        obj.getJSONObject("campaign").getInt("pointsForRightAnswer"),
                                        obj.getJSONObject("campaign").getInt("pointsForParticipation"),
                                        obj.getString("status").toString());
                                        */
                                // Sponsor data are provided as fields (tags) in JSON Object of a campaign
                                // sponsor = new Sponsor(obj.getJSONObject("campaign").getString("sponsorCode").toString(), obj.getJSONObject("campaign").getString("sponsorName").toString());

                                // Every campaign contains an JSON Array of JSON Objects defining QUESTIONS
                                jsonArryQuestion = obj.getJSONObject("campaign").getJSONArray("questions");
                                if (jsonArryQuestion != null){
                                    for (int j = 0; j < jsonArryQuestion.length(); j++) {    // for each Question
                                        JSONObject qtn = jsonArryQuestion.getJSONObject(i);
                                        // calling constructor for Question
                                        // Question(String sponsorCode, String startDate, int numberOfQuestions,
                                        //                 int pointsForRightAnswer, int pointsForParticipation,
                                        //                                   int seqNumber, String questionLabel)

                                        question = new Question(obj.getJSONObject("campaign").getString("sponsorCode").toString(),
                                                                    obj.getJSONObject("campaign").getString("startDate").toString(),
                                                                    obj.getJSONObject("campaign").getInt("numberOfQuestions"),
                                                                    obj.getJSONObject("campaign").getInt("pointsForRightAnswer"),
                                                                    obj.getJSONObject("campaign").getInt("pointsForParticipation"),
                                                                    j,
                                                                    qtn.getJSONObject("questionLabel").toString());

                                        db.createQuestion(question);

                                        JSONArray opts = qtn.getJSONArray("options");

                                        for (int k = 0; k < qtn.length(); k++) {    // for each Option (inside a question)
                                            JSONObject opt = opts.getJSONObject(i);
                                            // TODO call the option constructor
                                            // option = new Option(....);
                                            // add option to database
                                            // db.createOption(option);
                                        }

                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    // to eliminate duplicate sponsors...
                    // recreate table pg_sponsor with distinct rows from temp_pg_sponsor (removing duplicated records)

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


    // Utility


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
