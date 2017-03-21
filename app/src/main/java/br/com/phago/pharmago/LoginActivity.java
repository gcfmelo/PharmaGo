package br.com.phago.pharmago;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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


public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_EMAIL = "EMAIL";
    public static final String EXTRA_PASSWORD = "PASSWORD";
    public static final String EXTRA_USERNAME = "USERNAME";

    static final boolean RECREATE_TABLES = true;
    public static final String WS_RETURN_OK = "SUCCESSFUL";
    public static final String WS_RETURN_ERROR = "ERROR";

    private static final String TAG = LoginActivity.class.getSimpleName();
    static final String KEY_COUNTER = "COUNTER";
    static final String KEY_USERNAME = "USERNAME";
    static final String KEY_EMAIL = "EMAIL";
    static final String KEY_PASSWORD = "PASSWORD";


    private int mCounter = 0;
    private String mUsername = "";
    private String mEmail = "";
    private String mPassword = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        int defaultCounter = 0;
        String defaultUsername = "User";
        String defaultEmail = "";
        String defaultPassword = "";
        mCounter = settings.getInt(KEY_COUNTER, defaultCounter);
        mUsername = settings.getString(KEY_USERNAME, defaultUsername);
        mEmail = settings.getString(KEY_EMAIL, defaultEmail);
        mPassword = settings.getString(KEY_PASSWORD, defaultPassword);

        PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(this);

        UpdateUser("gcfmelo@gmail.com", "abc123");

        UpdateSponsor("gcfmelo@gmail.com", "abc123");
        UpdateUser("gcfmelo@gmail.com", "abc123");
        UpdateCampaign("gcfmelo@gmail.com", "abc123");
        UpdateQuestionOption("gcfmelo@gmail.com", "abc123");
        UpdateTransaction("gcfmelo@gmail.com", "abc123");


        User user_ini = dbx.getLastUser();
        dbx.close();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_COUNTER, mCounter);
        outState.putString(KEY_USERNAME, mUsername);
        outState.putString(KEY_EMAIL, mEmail);
        outState.putString(KEY_PASSWORD, mPassword);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCounter = savedInstanceState.getInt(KEY_COUNTER);
        mUsername = savedInstanceState.getString(KEY_USERNAME);
        mEmail = savedInstanceState.getString(KEY_EMAIL);
        mPassword = savedInstanceState.getString(KEY_PASSWORD);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // persist data locally
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(KEY_COUNTER, mCounter);
        editor.putString(KEY_USERNAME, mUsername);
        editor.putString(KEY_EMAIL, mEmail);
        editor.putString(KEY_PASSWORD, mPassword);

        editor.commit();
    }

    public void sendRequest(View view) {
        final TextView textViewResult = (TextView) findViewById(R.id.textViewResult);
        EditText emailEditText = (EditText) findViewById(R.id.editTextEmail);
        final EditText passwordEditText = (EditText) findViewById(R.id.editTextPassword);

        mCounter++;
        String txtMsg = "";
        if (mCounter==1){
            txtMsg = txtMsg + "This is your first login since PharmaGo App instalation in this machine";
        }
        txtMsg = txtMsg + "\n";
        txtMsg = txtMsg + "Please visit our homepage for support at http://www.phago.com.br";
        textViewResult.setText(txtMsg);
        Toast.makeText(this, "Count: "+ Integer.toString(mCounter)+" clicks", Toast.LENGTH_SHORT).show();

        PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(this);
        User user_before_send = dbx.getLastUser();

        if (user_before_send.getEmail().equals(emailEditText.toString())){
            Intent intent = new Intent(LoginActivity.super.getApplicationContext(), MainActivity.class);
            intent.putExtra(EXTRA_EMAIL, mEmail);
            intent.putExtra(EXTRA_PASSWORD, mPassword);
            intent.putExtra(EXTRA_USERNAME, mUsername);
            startActivity(intent);

        } else {
            UpdateUser(emailEditText.toString(), passwordEditText.toString());

            UpdateSponsor(emailEditText.toString(), passwordEditText.toString());
            UpdateUser(emailEditText.toString(), passwordEditText.toString());
            UpdateCampaign(emailEditText.toString(), passwordEditText.toString());
            UpdateQuestionOption(emailEditText.toString(), passwordEditText.toString());
            UpdateTransaction(emailEditText.toString(), passwordEditText.toString());
        }

    }
    public boolean loginUser(String email, final String password) {
        // @@@
        final PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(this);
        User user = dbx.getLastUser();
        dbx.close();

        if ((user.getEmail().equals(email))&&(user.getPassword().equals(password))){
            return true;
        } else {
            return false;
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

    public void UpdateSponsor(String email, String password) {
        // @@@
        final PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(this);

        final String TAG = "UpdateSponsor";
        if (RECREATE_TABLES) {
            dbx.dropTable("pg_sponsor");
            dbx.createTableSponsor();
        } else {
            dbx.clearTableSponsor();
        }
        try {
            URL urlObj = createURL(email, password, "findAllSponsors", "123456789");     //format URL to call WS
            String url = urlObj.toString();
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String myJsonString = response;
                    myJsonString = myJsonString.replace("\\\"", "\"");
                    myJsonString = myJsonString.replace("\"[{", "[{");
                    myJsonString = myJsonString.replace("]\"}", "]}");
                    // converting to JSONObject
                    JSONObject jsonObjRoot = null;
                    JSONArray jsonArry = null;
                    try {
                        jsonObjRoot = new JSONObject(myJsonString);
                        if (jsonObjRoot.getString("status").equals(WS_RETURN_OK)) {
                            jsonArry = jsonObjRoot.getJSONArray("json");
                            for (int i = 0; i < jsonArry.length(); i++) {
                                Sponsor sponsor = new Sponsor(jsonArry.getJSONObject(i).getInt("idSponsor"),
                                        jsonArry.getJSONObject(i).getString("sponsorCode"),
                                        jsonArry.getJSONObject(i).getString("sponsorName"));
                                dbx.addSponsor(sponsor);
                            }
                            // @@@
                            dbx.close();
                        } else {
                            // WS responded with:
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

    public void UpdateCampaign(String email, String password) {
        // @@@
        final PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(this);

        final String TAG = "UpdateCampaign";
        Log.d(TAG, " process started");
        if (RECREATE_TABLES) {
            dbx.dropTable("pg_campaign");
            dbx.createTableCampaign();
        } else {
            dbx.clearTableCampaign();
        }
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            URL urlObj = createURL(email, password, "findAllCampaigns", "123456789");     //format URL to call WS
            String url = urlObj.toString();
            Log.i(TAG, "URL:   " + url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String myJsonString = response.toString();
                    myJsonString = myJsonString.replace("\\\"", "\"");
                    myJsonString = myJsonString.replace(":\"[{", ":[{");
                    myJsonString = myJsonString.replace("}]\"}", "}]}");
                    // converting to JSONObject
                    JSONObject jsonObjRoot = null;
                    JSONArray jsonArry = null;
                    try {
                        jsonObjRoot = new JSONObject(myJsonString);
                        if (jsonObjRoot.getString("status").toString().equals(WS_RETURN_OK)) {
                            jsonArry = jsonObjRoot.getJSONArray("json");
                            if (jsonArry != null) {
                                for (int i = 0; i < jsonArry.length(); i++) {
                                    Campaign campaign = new Campaign(jsonArry.getJSONObject(i).getInt("idCampaign"),
                                            jsonArry.getJSONObject(i).getInt("idSponsor"),
                                            jsonArry.getJSONObject(i).getString("title"),
                                            jsonArry.getJSONObject(i).getString("startDate"),
                                            jsonArry.getJSONObject(i).getString("endDate"),
                                            jsonArry.getJSONObject(i).getInt("numberOfQuestions"),
                                            jsonArry.getJSONObject(i).getInt("pointsForRightAnswer"),
                                            jsonArry.getJSONObject(i).getInt("pointsForParticipation"),
                                            jsonArry.getJSONObject(i).getString("status"));
                                    dbx.addCampaign(campaign);
                                }  // process the next element from JSON
                            }
                            // @@@
                            dbx.close();
                        } else {
                            // WS responded not OK so...
                            if (jsonObjRoot.getString("status").toString().equals(WS_RETURN_ERROR)) {
                                Log.i(TAG, "WS responded with: ERROR:  " + jsonObjRoot.getString("errorMessage").toString());
                            } else {
                                Log.i(TAG, "WS responded with:   @@@   " + jsonObjRoot.getString("status").toString() + "  " + jsonObjRoot.getString("errorMessage").toString());
                            }
                        }
                    } catch (JSONException e) {
                        Log.i("ERRO JSON ", e.getMessage());
                    }
                }

            }
                    , new Response.ErrorListener() {
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

    public void UpdateQuestionOption(String email, String password) {
        // @@@
        final PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(this);

        final String TAG = "UpdateQuestionOption";
        Log.d(TAG, " process started");
        if (RECREATE_TABLES) {
            dbx.dropTable("pg_question");
            dbx.dropTable("pg_option");
            dbx.createTableQuestion();
            dbx.createTableOption();
        } else {
            dbx.clearTableQuestion();
            dbx.clearTableOption();
        }
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            URL urlObj = createURL(email, password, "findAllQuestions", "123456789");     //format URL to call WS
            String url = urlObj.toString();
            Log.i(TAG, "URL:   " + url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String myJsonString = response.toString();
                    myJsonString = myJsonString.replace("\\\"", "\"");
                    myJsonString = myJsonString.replace(":\"[{", ":[{");
                    myJsonString = myJsonString.replace("}]\"}", "}]}");
                    // converting to JSONObject
                    JSONObject jsonObjRoot = null;
                    JSONArray jsonArryQuestion = null;
                    JSONArray jsonArryOptions = null;
                    try {
                        jsonObjRoot = new JSONObject(myJsonString);
                        if (jsonObjRoot.getString("status").toString().equals(WS_RETURN_OK)) {
                            jsonArryQuestion = jsonObjRoot.getJSONArray("json");
                            if (jsonArryQuestion != null) {
                                for (int i = 0; i < jsonArryQuestion.length(); i++) {       // Itereate QUESTIONS
                                    Question question = new Question(jsonArryQuestion.getJSONObject(i).getInt("idQuestion"),
                                            jsonArryQuestion.getJSONObject(i).getInt("idCampaign"),
                                            jsonArryQuestion.getJSONObject(i).getInt("idSponsor"),
                                            jsonArryQuestion.getJSONObject(i).getString("label"));
                                    dbx.addQuestion(question);
                                    // lets process each OPTION inside this Question element of jsonArry
                                    // there is one inner JSONArray inside
                                    // lets extract the Options:
                                    jsonArryOptions = jsonArryQuestion.getJSONObject(i).getJSONArray("options");
                                    List<Option> mOpList = new ArrayList<Option>();
                                    if (jsonArryOptions != null) {
                                        for (int j = 0; j < jsonArryOptions.length(); j++) {    // this loop iterate OPTIONS
                                            // Option(Integer idSponsor, Integer idCampaign, Integer idQuestion, Integer sequential, String label, String rightAnswer, String userAnswer)
                                            Option option = new Option(jsonArryOptions.getJSONObject(j).getInt("idSponsor"),
                                                    jsonArryOptions.getJSONObject(j).getInt("idCampaign"),
                                                    jsonArryOptions.getJSONObject(j).getInt("idQuestion"),
                                                    jsonArryOptions.getJSONObject(j).getInt("sequential"),
                                                    jsonArryOptions.getJSONObject(j).getString("label"),
                                                    jsonArryOptions.getJSONObject(j).getString("rightAnswer"),
                                                    jsonArryOptions.getJSONObject(j).getString("userAnswer"));
                                            dbx.addOption(option);
                                        }  // process next OPTION
                                    }
                                }  // process the next QUESTION from JSON
                            }
                            // @@@
                            dbx.close();
                        } else {
                            // WS responded not OK so...
                            if (jsonObjRoot.getString("status").toString().equals(WS_RETURN_ERROR)) {
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
        // @@@
        final PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(this);

        final String TAG = "UpdateTransaction";
        Log.d(TAG, " process started");
        if (RECREATE_TABLES) {
            dbx.dropTable("pg_transaction");
            dbx.createTableTransaction();
        } else {
            dbx.clearTableTransaction();
        }
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            URL urlObj = createURL(email, password, "findAllTransactions", "123456789");     //format URL to call WS
            String url = urlObj.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String myJsonString = response.toString();
                    myJsonString = myJsonString.replace("\\\"", "\"");
                    myJsonString = myJsonString.replace(":\"[{", ":[{");
                    myJsonString = myJsonString.replace("}]\"}", "}]}");
                    // converting to JSONObject
                    JSONObject jsonObjRoot = null;
                    JSONArray jsonArry = null;
                    try {
                        jsonObjRoot = new JSONObject(myJsonString);
                        if (jsonObjRoot.getString("status").toString().equals(WS_RETURN_OK)) {
                            jsonArry = jsonObjRoot.getJSONArray("json");
                            if (jsonArry != null) {
                                for (int i = 0; i < jsonArry.length(); i++) {
                                    Transaction tr = new Transaction(jsonArry.getJSONObject(i).getString("sponsorCode"),
                                            jsonArry.getJSONObject(i).getString("eventDate"),
                                            jsonArry.getJSONObject(i).getString("title"),
                                            jsonArry.getJSONObject(i).getString("nature"),
                                            jsonArry.getJSONObject(i).getInt("idCampaign"),
                                            jsonArry.getJSONObject(i).getInt("idTransaction"),
                                            jsonArry.getJSONObject(i).getInt("amount"));
                                    dbx.addTransaction(tr);
                                    Log.i(TAG, "Element:   @@@   " + Integer.toString(i) + " " + jsonArry.getJSONObject(i));
                                }
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
