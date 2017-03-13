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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_COUNTER = "COUNTER";
    public static final String WS_RETURN_OK = "SUCCESSFUL";
    public static final String WS_RETURN_ERROR = "ERROR";
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
        UpdateUser("gcfmelo@gmail.com", "abc123");
        UpdateSponsor("gcfmelo@gmail.com", "abc123");
        UpdateCampaign("gcfmelo@gmail.com", "abc123");
        UpdateQuestionOption("gcfmelo@gmail.com", "abc123");
        UpdateTransaction("gcfmelo@gmail.com", "abc123");
        TestCampaignList();

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
    // this version is updated for new WS at 2017-03-11
    public void UpdateUser(String email, String password) {

        final String TAG = "UpdateUser";
        db = new PgDatabaseHelper(getApplicationContext());

        db.clearTableUser();

        try {

            RequestQueue queue = Volley.newRequestQueue(this);

            URL urlObj = createURL(email, password, "doLogin", "123456789");     //format URL to call WS
            final String mEmail = email;
            final String mPassword = password;
            String url = urlObj.toString();
            Log.i(TAG, "URL:   " + url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String myJsonString = response.toString();
                    Log.i(TAG, "514_TEXT RETURNED:   @@@   " + myJsonString);
                    myJsonString = myJsonString.replace("\\\"", "\"");
                    Log.i(TAG, "516_Removed Escapes:   @@@   " + myJsonString);
                    myJsonString = myJsonString.replace("\":\"{", "\":{");
                    myJsonString = myJsonString.replace("}\"}", "}}");
                    Log.i(TAG, "519_removed \":   @@@   " + myJsonString);

                    // converting to JSONObject
                    JSONObject jsonObjRoot = null;
                    JSONObject jsonObjUser = null;

                    try {
                        jsonObjRoot = new JSONObject(myJsonString);

                        if (jsonObjRoot.getString("status").toString().equals(WS_RETURN_OK)){

                            jsonObjUser = jsonObjRoot.getJSONObject("json");
                            Log.i(TAG, "531_jsonArry: @@@"+jsonObjUser.toString());

                            if (jsonObjUser != null) {
                                //for (int i = 0; i < jsonArry.length(); i++) {
                                String mUsrEmail = jsonObjUser.getString("email").toString();
                                String mUsrName = jsonObjUser.getString("name").toString();
                                String mUsrStatus = jsonObjUser.getString("userAccountStatus").toString();
                                String mUsrCpf = jsonObjUser.getString("cpf").toString();
                                String mUsrCpnyCode = jsonObjUser.getString("companyCode").toString();
                                String mUsrCpnyNme = jsonObjUser.getString("companyName").toString();
                                String mUsrCpnyLat = jsonObjUser.getString("companyLatitude").toString();
                                String mUsrCpnyLon = jsonObjUser.getString("companyLongitude").toString();

                                Log.i(TAG, "Element:   email   @@@ "+ mUsrEmail);
                                Log.i(TAG, "Element:   name @@@ " + mUsrName);
                                Log.i(TAG, "Element:   status @@@ " + mUsrStatus);
                                Log.i(TAG, "Element:   cpf   @@@ " + mUsrCpf);
                                Log.i(TAG, "Element:   companyCode @@@ " + mUsrCpnyCode);
                                Log.i(TAG, "Element:   companyName @@@ " + mUsrCpnyNme);
                                Log.i(TAG, "Element:   companyLatitude   @@@ " + mUsrCpnyLat);
                                Log.i(TAG, "Element:   companyLongitude @@@ " + mUsrCpnyLon);

                                User user = new User(mUsrEmail, mUsrName, mUsrStatus, mUsrCpf, mUsrCpnyCode, mUsrCpnyNme, mUsrCpnyLat, mUsrCpnyLon);

                                db.addUser(user);

                                //}
                            }
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
    // this version is updated for new WS at 2017-03-11
    public void UpdateSponsor(String email, String password) {

        final String TAG = "UpdateSponsor";
        db = new PgDatabaseHelper(getApplicationContext());

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

                        if (jsonObjRoot.getString("status").toString().equals(WS_RETURN_OK)){

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
    public void UpdateCampaign(String email, String password) {

        final String TAG = "UpdateCampaign";
        db = new PgDatabaseHelper(getApplicationContext());


        db.clearTableCampaign();

        try {

            RequestQueue queue = Volley.newRequestQueue(this);

            URL urlObj = createURL(email, password, "findAllCampaigns", "123456789");     //format URL to call WS
            final String mEmail = email;
            final String mPassword = password;
            String url = urlObj.toString();
            Log.i(TAG, "URL:   " + url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String myJsonString = response.toString();
                    Log.i(TAG, "462_TEXT RETURNED:   @@@   " + myJsonString);
                    myJsonString = myJsonString.replace("\\\"", "\"");
                    Log.i(TAG, "464_Removed Escapes:   @@@   " + myJsonString);
                    myJsonString = myJsonString.replace(":\"[{", ":[{");
                    myJsonString = myJsonString.replace("}]\"}", "}]}");
                    Log.i(TAG, "467_removed \":   @@@   " + myJsonString);

                    // converting to JSONObject
                    JSONObject jsonObjRoot = null;
                    JSONArray jsonArry = null;

                    try {
                        jsonObjRoot = new JSONObject(myJsonString);

                        if (jsonObjRoot.getString("status").toString().equals(WS_RETURN_OK)){

                            jsonArry = jsonObjRoot.getJSONArray("json");
                            Log.i(TAG, "328_jsonArry: @@@"+jsonArry.toString());

                            if (jsonArry != null) {
                                for (int i = 0; i < jsonArry.length(); i++) {
                                    String mCpId = jsonArry.getJSONObject(i).getString("idCampaign").toString();
                                    String mSpId = jsonArry.getJSONObject(i).getString("idSponsor").toString();
                                    String mCpTit = jsonArry.getJSONObject(i).getString("title").toString();
                                    String mCpStDt = jsonArry.getJSONObject(i).getString("startDate").toString();
                                    String mCpEndDt = jsonArry.getJSONObject(i).getString("endDate").toString();
                                    String mCpNumQ = jsonArry.getJSONObject(i).getString("numberOfQuestions").toString();
                                    String mCpPtRightAnswer = jsonArry.getJSONObject(i).getString("pointsForRightAnswer").toString();
                                    String mCpPtPartic = jsonArry.getJSONObject(i).getString("pointsForParticipation").toString();
                                    String mCpStatus = jsonArry.getJSONObject(i).getString("status").toString();

                                    Log.i(TAG, "Element:   idCampaign   @@@ ( " + Integer.toString(i)+" ) = "+ mCpId);
                                    Log.i(TAG, "Element:   idSponsor   @@@ ( " + Integer.toString(i)+" ) = "+ mSpId);
                                    Log.i(TAG, "Element:   title @@@ ( " + Integer.toString(i)+" ) = "+ mCpTit);
                                    Log.i(TAG, "Element:   startDate @@@ ( " + Integer.toString(i)+" ) = "+ mCpStDt);
                                    Log.i(TAG, "Element:   endDate   @@@ ( " + Integer.toString(i)+" ) = "+ mCpEndDt);
                                    Log.i(TAG, "Element:   numberOfQuestions   @@@ ( " + Integer.toString(i)+" ) = "+ mCpNumQ);
                                    Log.i(TAG, "Element:   pointsForRightAnswer   @@@ ( " + Integer.toString(i)+" ) = "+ mCpPtRightAnswer);
                                    Log.i(TAG, "Element:   pointsForParticipation   @@@ ( " + Integer.toString(i)+" ) = "+ mCpPtPartic);
                                    Log.i(TAG, "Element:   status   @@@ ( " + Integer.toString(i)+" ) = "+ mCpStatus);

                                    Campaign campaign = new     Campaign(Integer.parseInt(mCpId),
                                                                        Integer.parseInt(mSpId),
                                                                        mCpTit, mCpStDt, mCpEndDt,
                                                                        Integer.parseInt(mCpNumQ),
                                                                        Integer.parseInt(mCpPtRightAnswer),
                                                                        Integer.parseInt(mCpPtPartic),
                                                                        mCpStatus);

                                    db.addCampaign(campaign);
                                    Log.i(TAG, "Element:   @@@   " + Integer.toString(i)+" "+ jsonArry.getJSONObject(i));
                                }  // process the next element from JSON
                            }
                        } else {
                            // WS responded not OK so...
                            if (jsonObjRoot.getString("status").toString().equals(WS_RETURN_ERROR)){
                                Log.i(TAG, "WS responded with: ERROR:  " + jsonObjRoot.getString("errorMessage").toString());
                            } else {
                                Log.i(TAG, "WS responded with:   @@@   " + jsonObjRoot.getString("status").toString() + "  " + jsonObjRoot.getString("errorMessage").toString());
                            }
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
    // this version is updated for new WS at 2017-03-11
    public void UpdateQuestion(String email, String password) {

        final String TAG = "UpdateQuestion";
        db = new PgDatabaseHelper(getApplicationContext());

        db.clearTableQuestion();

        try {

            RequestQueue queue = Volley.newRequestQueue(this);

            URL urlObj = createURL(email, password, "findAllQuestions", "123456789");     //format URL to call WS
            final String mEmail = email;
            final String mPassword = password;
            String url = urlObj.toString();
            Log.i(TAG, "URL:   " + url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String myJsonString = response.toString();
                    Log.i(TAG, "531 RETURNED:   @@@   " + myJsonString);
                    myJsonString = myJsonString.replace("\\\"", "\"");
                    Log.i(TAG, "533_Removed Escapes:   @@@   " + myJsonString);
                    myJsonString = myJsonString.replace(":\"[{", ":[{");
                    myJsonString = myJsonString.replace("}]\"}", "}]}");
                    Log.i(TAG, "536_removed \":   @@@   " + myJsonString);

                    // converting to JSONObject
                    JSONObject jsonObjRoot = null;
                    JSONArray jsonArry = null;

                    try {
                        jsonObjRoot = new JSONObject(myJsonString);

                        if (jsonObjRoot.getString("status").toString().equals(WS_RETURN_OK)){

                            jsonArry = jsonObjRoot.getJSONArray("json");
                            Log.i(TAG, "328_jsonArry: @@@"+jsonArry.toString());

                            if (jsonArry != null) {
                                for (int i = 0; i < jsonArry.length(); i++) {
                                    String mQtId = jsonArry.getJSONObject(i).getString("idQuestion").toString();
                                    String mQtCpId = jsonArry.getJSONObject(i).getString("idCampaign").toString();
                                    String mQtSpId = jsonArry.getJSONObject(i).getString("idSponsor").toString();
                                    String mQtLabel = jsonArry.getJSONObject(i).getString("label").toString();


                                    Log.i(TAG, "Element:   idQuestion   @@@ ( " + Integer.toString(i)+" ) = "+ mQtId);
                                    Log.i(TAG, "Element:   idCampaign   @@@ ( " + Integer.toString(i)+" ) = "+ mQtCpId);
                                    Log.i(TAG, "Element:   idSponsor @@@ ( " + Integer.toString(i)+" ) = "+ mQtSpId);
                                    Log.i(TAG, "Element:   label @@@ ( " + Integer.toString(i)+" ) = "+ mQtLabel);


                                    Question question = new     Question(Integer.parseInt(mQtId),
                                            Integer.parseInt(mQtCpId),
                                            Integer.parseInt(mQtSpId),
                                            mQtLabel);

                                    db.addQuestion(question);
                                    Log.i(TAG, "Element:   @@@   " + Integer.toString(i)+" "+ jsonArry.getJSONObject(i));
                                }  // process the next element from JSON
                            }
                        } else {
                            // WS responded not OK so...
                            if (jsonObjRoot.getString("status").toString().equals(WS_RETURN_ERROR)){
                                Log.i(TAG, "WS responded with: ERROR:  " + jsonObjRoot.getString("errorMessage").toString());
                            } else {
                                Log.i(TAG, "WS responded with:   @@@   " + jsonObjRoot.getString("status").toString() + "  " + jsonObjRoot.getString("errorMessage").toString());
                            }
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
    // this version is updated for new WS at 2017-03-11
    public void UpdateQuestionOption(String email, String password) {

        final String TAG = "UpdateQuestionOption";
        db = new PgDatabaseHelper(getApplicationContext());

        db.clearTableQuestion();
        db.clearTableOption();

        try {

            RequestQueue queue = Volley.newRequestQueue(this);

            URL urlObj = createURL(email, password, "findAllQuestions", "123456789");     //format URL to call WS
            final String mEmail = email;
            final String mPassword = password;
            String url = urlObj.toString();
            Log.i(TAG, "URL:   " + url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String myJsonString = response.toString();
                    Log.i(TAG, "531 RETURNED:   @@@   " + myJsonString);
                    myJsonString = myJsonString.replace("\\\"", "\"");
                    Log.i(TAG, "533_Removed Escapes:   @@@   " + myJsonString);
                    myJsonString = myJsonString.replace(":\"[{", ":[{");
                    myJsonString = myJsonString.replace("}]\"}", "}]}");
                    Log.i(TAG, "536_removed \":   @@@   " + myJsonString);

                    // converting to JSONObject
                    JSONObject jsonObjRoot = null;
                    JSONArray jsonArryQuestion = null;
                    JSONArray jsonArryOptions = null;

                    try {
                        jsonObjRoot = new JSONObject(myJsonString);

                        if (jsonObjRoot.getString("status").toString().equals(WS_RETURN_OK)){

                            jsonArryQuestion = jsonObjRoot.getJSONArray("json");
                            Log.i(TAG, "328_jsonArry: @@@"+jsonArryQuestion.toString());

                            if (jsonArryQuestion != null) {
                                for (int i = 0; i < jsonArryQuestion.length(); i++) {
                                    String mQtId = jsonArryQuestion.getJSONObject(i).getString("idQuestion").toString();
                                    String mQtCpId = jsonArryQuestion.getJSONObject(i).getString("idCampaign").toString();
                                    String mQtSpId = jsonArryQuestion.getJSONObject(i).getString("idSponsor").toString();
                                    String mQtLabel = jsonArryQuestion.getJSONObject(i).getString("label").toString();


                                    Log.i(TAG, "Element:   idQuestion   @@@ ( " + Integer.toString(i)+" ) = "+ mQtId);
                                    Log.i(TAG, "Element:   idCampaign   @@@ ( " + Integer.toString(i)+" ) = "+ mQtCpId);
                                    Log.i(TAG, "Element:   idSponsor @@@ ( " + Integer.toString(i)+" ) = "+ mQtSpId);
                                    Log.i(TAG, "Element:   label @@@ ( " + Integer.toString(i)+" ) = "+ mQtLabel);


                                    Question question = new     Question(Integer.parseInt(mQtId),
                                            Integer.parseInt(mQtCpId),
                                            Integer.parseInt(mQtSpId),
                                            mQtLabel);

                                    db.addQuestion(question);
                                    Log.i(TAG, "Element:   @@@   " + Integer.toString(i)+" "+ jsonArryQuestion.getJSONObject(i));

                                    // lets process each OPTION inside this Question element of jsonArry
                                    // there is one inner JSONArray inside
                                    // lets extract the Options:
                                    jsonArryOptions = jsonArryQuestion.getJSONObject(i).getJSONArray("options");
                                    if (jsonArryOptions != null) {
                                        for (int j = 0; j < jsonArryOptions.length(); j++) {

                                            String mOpSpId = jsonArryOptions.getJSONObject(j).getString("idSponsor").toString();
                                            String mOpCpId = jsonArryOptions.getJSONObject(j).getString("idCampaign").toString();
                                            String mOpQtId = jsonArryOptions.getJSONObject(j).getString("idQuestion").toString();
                                            String mOpSeq = jsonArryOptions.getJSONObject(j).getString("sequential").toString();
                                            String mOpLabel = jsonArryOptions.getJSONObject(j).getString("label").toString();
                                            String mOpRightAnsw = jsonArryOptions.getJSONObject(j).getString("rightAnswer").toString();
                                            String mOpUsrAnsw = jsonArryOptions.getJSONObject(j).getString("userAnswer").toString();

                                            Log.i(TAG, "Element:   idSponsor   @@@ ( " + Integer.toString(i*1000+j)+" ) = "+ mOpSpId);
                                            Log.i(TAG, "Element:   idCampaign   @@@ ( " + Integer.toString(i*1000+j)+" ) = "+ mOpCpId);
                                            Log.i(TAG, "Element:   idQuestion   @@@ ( " + Integer.toString(i*1000+j)+" ) = "+ mOpQtId);
                                            Log.i(TAG, "Element:   sequential   @@@ ( " + Integer.toString(i*1000+j)+" ) = "+ mOpSeq);
                                            Log.i(TAG, "Element:   label   @@@ ( " + Integer.toString(i*1000+j)+" ) = "+ mOpLabel);
                                            Log.i(TAG, "Element:   rightAnswer   @@@ ( " + Integer.toString(i*1000+j)+" ) = "+ mOpRightAnsw);
                                            Log.i(TAG, "Element:   userAnswer   @@@ ( " + Integer.toString(i*1000+j)+" ) = "+ mOpUsrAnsw);



                                            // Option(Integer idSponsor, Integer idCampaign, Integer idQuestion, Integer sequential, String label, String rightAnswer, String userAnswer)
                                            Option option = new Option(Integer.parseInt(mOpSpId),
                                                                        Integer.parseInt(mOpCpId),
                                                                        Integer.parseInt(mOpQtId),
                                                                        Integer.parseInt(mOpSeq),
                                                                        mOpLabel, mOpRightAnsw, mOpUsrAnsw);
                                            db.addOption(option);
                                        }  // process nex Option
                                    }

                                }  // process the next element from JSON
                            }
                        } else {
                            // WS responded not OK so...
                            if (jsonObjRoot.getString("status").toString().equals(WS_RETURN_ERROR)){
                                Log.i(TAG, "WS responded with: ERROR:  " + jsonObjRoot.getString("errorMessage").toString());
                            } else {
                                Log.i(TAG, "WS responded with:   @@@   " + jsonObjRoot.getString("status").toString() + "  " + jsonObjRoot.getString("errorMessage").toString());
                            }
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

    public void UpdateTransaction(String email, String password) {

        final String TAG = "UpdateTransaction";
        db = new PgDatabaseHelper(getApplicationContext());

        db.clearTableTransaction();

        try {

            RequestQueue queue = Volley.newRequestQueue(this);

            URL urlObj = createURL(email, password, "findAllTransactions", "123456789");     //format URL to call WS
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
                    myJsonString = myJsonString.replace(":\"[{", ":[{");
                    myJsonString = myJsonString.replace("}]\"}", "}]}");
                    Log.i(TAG, "314_removed \":   @@@   " + myJsonString);

                    // converting to JSONObject
                    JSONObject jsonObjRoot = null;
                    JSONArray jsonArry = null;

                    try {
                        jsonObjRoot = new JSONObject(myJsonString);

                        if (jsonObjRoot.getString("status").toString().equals(WS_RETURN_OK)){

                            jsonArry = jsonObjRoot.getJSONArray("json");
                            Log.i(TAG, "689_jsonArry: @@@"+jsonArry.toString());
                            /*

                            private String sponsorCode, eventDate, eventDate, nature;
                            private int idCampaign, idTransaction, amount;
                             */

                            if (jsonArry != null) {
                                for (int i = 0; i < jsonArry.length(); i++) {
                                    String mTrCode = jsonArry.getJSONObject(i).getString("sponsorCode").toString();
                                    String mTrDate = jsonArry.getJSONObject(i).getString("eventDate").toString();
                                    String mTrNature = jsonArry.getJSONObject(i).getString("nature").toString();

                                    int mTrIdCp = jsonArry.getJSONObject(i).getInt("idCampaign");
                                    int mTrIdTr = jsonArry.getJSONObject(i).getInt("idTransaction");
                                    int mTrAmount = jsonArry.getJSONObject(i).getInt("amount");

                                    Log.i(TAG, "Element:   idSponsor   @@@ ( " + Integer.toString(i)+" ) = "+ mSpId);
                                    Log.i(TAG, "Element:   sponsorCode @@@ ( " + Integer.toString(i)+" ) = "+ mSpCode);
                                    Log.i(TAG, "Element:   sponsorName @@@ ( " + Integer.toString(i)+" ) = "+ mSpName);


                                    Transaction tr = new Sponsor(Integer.parseInt(mSpId), mSpCode, mSpName);

                                    db.addTransaction(tr);

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

    public void TestCampaignList(){
        final String TAG = "TestCampaignList";
        db = new PgDatabaseHelper(getApplicationContext());
        List<CampaignListClass> cpList = new ArrayList<CampaignListClass>();
        cpList = db.getAllCampaigns();
        for (int i=0; i < cpList.size(); i++){
            Log.i(TAG," item("+Integer.toString(i)+") ===> "+cpList.get(i).toString());
        }
    }


    // TODO UpdateTransactions     findAllTransactions
    // TODO SendUserAnswers

    // Utilities
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
