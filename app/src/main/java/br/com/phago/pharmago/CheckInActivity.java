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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CheckInActivity extends AppCompatActivity {
    // to SQLite updating
    static final boolean RECREATE_TABLES = true;
    // to check
    public static final String WS_RETURN_OK = "SUCCESSFUL";
    public static final String WS_RETURN_ERROR = "ERROR";

    // to save USER DATA in SharedPreferences
    static final String KEY_COUNTER = "COUNTER";     // utilization counter
    static final String KEY_USERNAME = "USERNAME";
    static final String KEY_EMAIL = "EMAIL";
    static final String KEY_PASSWORD = "PASSWORD";
    static final int KEY_COUNTER_NOT_FOUND_VALUE = 0;     // utilization counter
    static final String KEY_USERNAME_NOT_FOUND_VALUE = "username not found";
    static final String KEY_EMAIL_NOT_FOUND_VALUE = "e-mail  not found";
    static final String KEY_PASSWORD_NOT_FOUND_VALUE = "password not found";

    // to pass to Intent
    public static final String EXTRA_EMAIL = "EMAIL";
    public static final String EXTRA_PASSWORD = "PASSWORD";
    public static final String EXTRA_USERNAME = "USERNAME";
    public static final int EXTRA_LOGIN_ATTEMPT = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        // retrieve user data from PreferenceManager
        String bEmail = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(KEY_EMAIL, KEY_EMAIL_NOT_FOUND_VALUE);
        String bUsername = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(KEY_USERNAME, KEY_USERNAME_NOT_FOUND_VALUE);
        String bPassword = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(KEY_PASSWORD, KEY_PASSWORD_NOT_FOUND_VALUE);
        int bCounter = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(KEY_COUNTER, KEY_COUNTER_NOT_FOUND_VALUE);

        // Get the Intent that started this activity and extract the strings
        Intent intent = getIntent();
        String selectedEmail = intent.getStringExtra(MainActivity.EXTRA_EMAIL);
        String selectedPassword = intent.getStringExtra(MainActivity.EXTRA_PASSWORD);
        EditText eTemail = (EditText) findViewById(R.id.editTextEmailAddress);
        EditText eTpassword = (EditText) findViewById(R.id.editTextPassword);
        CheckBox chkEmail = (CheckBox) findViewById(R.id.checkBoxSaveEmail);
        CheckBox chkPassword = (CheckBox) findViewById(R.id.checkBoxSavePassword);
        eTemail.setText(selectedEmail);
        eTpassword.setText(selectedPassword);
        // default is checked
        chkEmail.setChecked(true);
        chkPassword.setChecked(true);


        if  (selectedEmail==null){
            selectedEmail="";
            selectedPassword="";
        }
        if ((selectedEmail.contains("@") && selectedPassword.length()>0)){
            eTemail.setText(selectedEmail);
            eTpassword.setText(selectedPassword);
            btnLoginClick(this.findViewById(R.id.buttonLogin));
        } else if (bEmail.contains("@")&&(bPassword.length()>0)){
            eTemail.setText(bEmail);
            eTpassword.setText(bPassword);
            btnLoginClick(this.findViewById(R.id.buttonLogin));
        }
    }

    public void btnLoginClick(View view){
        EditText eTemail = (EditText) findViewById(R.id.editTextEmailAddress);
        final EditText eTpassword = (EditText) findViewById(R.id.editTextPassword);
        CheckBox chkEmail = (CheckBox) findViewById(R.id.checkBoxSaveEmail);
        CheckBox chkPassword = (CheckBox) findViewById(R.id.checkBoxSavePassword);

        Button button = (Button) view;

        // save email and password to PreferenceManager, accordingly to user selection
        if (chkEmail.isChecked()&&(eTemail.getText().toString().contains("@"))) {
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(KEY_EMAIL, eTemail.getText().toString()).commit();
            if (chkPassword.isChecked()){
                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(KEY_PASSWORD, eTpassword.getText().toString()).commit();
            }
        }

        ///////////////////////////////////////////////////////////////////////////////////
        // @@@
        final String mEmail = eTemail.getText().toString();
        final String mPassword = eTpassword.getText().toString();

        final PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(this);
        final String TAG = "btnLoginClick";
        Log.d(TAG, " process started");
        if (RECREATE_TABLES) {
            dbx.dropTable("pg_user");
            dbx.createTableUser();
        } else {
            dbx.clearTableUser();
        }

        ///////////////////////////////////////////
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            URL urlObj = createURL(mEmail, mPassword, "doLogin", "123456789");     //format URL to call WS
            String url = urlObj.toString();
            Log.i(TAG, "URL:   " + url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String myJsonString = response;
                    myJsonString = myJsonString.replace("\\\"", "\"");
                    myJsonString = myJsonString.replace("\":\"{", "\":{");
                    myJsonString = myJsonString.replace("}\"}", "}}");
                    // converting to JSONObject
                    JSONObject jsonObjRoot = null;
                    JSONObject jsonObjUser = null;
                    try {
                        jsonObjRoot = new JSONObject(myJsonString);
                        if (jsonObjRoot.getString("status").equals(WS_RETURN_OK)) {

                            jsonObjUser = jsonObjRoot.getJSONObject("json");
                            if (jsonObjUser != null) {
                                User user = new User(jsonObjUser.getString("email"),
                                        mPassword,
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

                            UpdateCampaign(mEmail, mPassword);
                            UpdateUser(mEmail, mPassword);
                            UpdateSponsor(mEmail, mPassword);
                            UpdateQuestionOption(mEmail, mPassword);
                            UpdateTransaction(mEmail, mPassword);

//
                            // save email and password to PreferenceManager, accordingly to user selection
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(KEY_EMAIL, mEmail).commit();
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(KEY_PASSWORD, mPassword).commit();


                            Intent intentMain = new Intent(CheckInActivity.super.getApplicationContext(), CampaignActivity.class);
                            intentMain.putExtra(EXTRA_EMAIL, mEmail);                       // fill login editTextEmail
                            intentMain.putExtra(EXTRA_PASSWORD, mPassword);                 // fill login editTextPassword
                            startActivity(intentMain);


                            finish();

                        } else {
                            // WS responded with:
                            Log.i(TAG, "WS responded with:   @@@   " + jsonObjRoot.getString("status").toString());
                            Toast.makeText(CheckInActivity.this, "Try again... if not working, please contact support at suport@phago.com.br", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CheckInActivity.this, "Try again... if not working, please contact support at suport@phago.com.br", Toast.LENGTH_SHORT).show();

                    }
                }

            }, new Response.ErrorListener() {
                
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.getMessage() == null) {
                        Log.i(TAG, " Web Service has failed!" + "\n\n");
                        // TODO: reset your password, create a new account
                        Toast.makeText(CheckInActivity.this, "Try again... if not working, please contact support at suport@phago.com.br", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i(TAG, "onErrorResponse(): " + error.getMessage());
                        Toast.makeText(CheckInActivity.this, "Try again... if not working, please contact support at suport@phago.com.br", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            queue.add(stringRequest);   // replace for the correct object name
        } catch (SQLiteException e) {
            Log.i(TAG, "Service: " + "Database is unavailable!");
            Toast.makeText(CheckInActivity.this, "Try again... if not working, please contact support at suport@phago.com.br", Toast.LENGTH_SHORT).show();
        }
        ///////////////////////////////////////////////////////////////////////////////////










        // se conseguir, salva as credenciais localmente e inicia CampaignActivity
        // se não conseguir manda um Toast pedindo para tentar novamente

        // UpdateUser(eTemail.getText().toString(), eTpassword.getText().toString());

//        Intent intentMain = new Intent(CheckInActivity.super.getApplicationContext(), MainActivity.class);
//        intentMain.putExtra(EXTRA_EMAIL, eTemail.getText().toString());                       // fill login editTextEmail
//        intentMain.putExtra(EXTRA_PASSWORD, eTpassword.getText().toString());                 // fill login editTextPassword
//        startActivity(intentMain);

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
//        EditText eTemail = (EditText) findViewById(R.id.editTextEmailAddress);
//        EditText eTpassword = (EditText) findViewById(R.id.editTextPassword);
//        CheckBox chkEmail = (CheckBox) findViewById(R.id.checkBoxSaveEmail);
//        CheckBox chkPassword = (CheckBox) findViewById(R.id.checkBoxSavePassword);
//
//        Button button = (Button) view;

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
