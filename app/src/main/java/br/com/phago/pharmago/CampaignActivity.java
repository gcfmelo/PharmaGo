package br.com.phago.pharmago;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class CampaignActivity extends AppCompatActivity {
    public static final boolean RECREATE_TABLES = false;
    public static final String WS_RETURN_OK = "SUCCESSFUL";
    public static final String WS_RETURN_ERROR = "ERROR";
    public static final String EXTRA_CAMPAIGN_ID = "br.com.phago.pharmago.CAMPAIGN_ID";
    public static final String ENABLE_PARTICIPATION_YN = "br.com.phago.pharmago.ENABLE_PARTICIPATION_YN";

    private static final String TAG = CampaignActivity.class.getSimpleName();
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

    // default values to settings for first time use
    int mCounter = 0;
    String mUsername = "";
    String mEmail = "";
    String mPassword = "";

    private List<CampaignListClass> campaignList = new ArrayList<CampaignListClass>();
    // ArrayAdapter for bind Campaign objects to a ListView
    private CampaignArrayAdapter campaignArrayAdapter;
    private ListView campaignListView; // displays Campaign info

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String selectedEmail = intent.getStringExtra(MainActivity.EXTRA_EMAIL);
        String selectedPassword = intent.getStringExtra(MainActivity.EXTRA_PASSWORD);



        // variables to hold values of settings in the BEGINNING of this session

        // DEVELOPMENT TOOL 1:
        //        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("KEY_EMAIL", "gcfmelo@gmail.com").commit();
        //        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("KEY_PASSWORD", "abc123").commit();
        //        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("KEY_USERNAME", "Gustavo Melo").commit();
        //        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt("KEY_COUNTER", 0).commit();

        // DEVELOPMENT TOOL 2:  RESET PREFERENCES
        //        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("KEY_EMAIL", "").commit();
        //        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("KEY_PASSWORD", "").commit();
        //        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("KEY_USERNAME", "").commit();
        //        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt("KEY_COUNTER", 0).commit();

        // DEVELOPMENT TOOL 3:   CLEAR ALL PREFS
//        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();

        // bVariableName is a BEGINNING value of the Variable retrieved from PreferenceManager
        String bEmail = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("KEY_EMAIL", KEY_EMAIL_NOT_FOUND_VALUE);
        String bUsername = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("KEY_PASSWORD", KEY_USERNAME_NOT_FOUND_VALUE);
        String bPassword = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("KEY_USERNAME", KEY_PASSWORD_NOT_FOUND_VALUE);
        int bCounter = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("KEY_COUNTER", KEY_COUNTER_NOT_FOUND_VALUE);

        Toast.makeText(this, bEmail + "\n"+ bUsername + "\n" + bPassword +"\n" + Integer.toString(bCounter), Toast.LENGTH_SHORT).show();

//
//        if ((!bEmail.equals(KEY_EMAIL_NOT_FOUND_VALUE))&&!(bPassword.equals(KEY_PASSWORD_NOT_FOUND_VALUE))&&(bEmail.contains("@"))) {
//            // there is already an email and a password in PreferenceManager
//            Intent intent = new Intent(CampaignActivity.super.getApplicationContext(), CheckInActivity.class);
//            intent.putExtra(EXTRA_EMAIL, mEmail);                       // fill login editTextEmail
//            intent.putExtra(EXTRA_PASSWORD, mPassword);                 // fill login editTextPassword
////            intent.putExtra(EXTRA_LOGIN_ATTEMPT, 10);     // show in login layout
//            startActivity(intent);
//
//        }

        //1)   a method to check if there is user data on android client
        //1.1) read DATA in SharedPreferences named 'settings'

        SharedPreferences settings = getPreferences(MODE_PRIVATE);


        if (!selectedEmail.contains("@")) {
            //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(KEY_EMAIL, selectedEmail);
            editor.putString(KEY_PASSWORD, selectedPassword);
            editor.putString(KEY_USERNAME, bUsername);
            editor.putInt(KEY_COUNTER, bCounter);
            editor.commit();

            // open CheckInActivity Intent to ask the user for login data, then
            // we will use (mEmail,mPassword) to try login again
            Intent intentCheckIn= new Intent(CampaignActivity.super.getApplicationContext(), CheckInActivity.class);
            intentCheckIn.putExtra(EXTRA_EMAIL, selectedEmail);                       // fill login editTextEmail
            intentCheckIn.putExtra(EXTRA_PASSWORD, selectedPassword);                 // fill login editTextPassword
            startActivity(intentCheckIn);

        } else {


            //PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound");
            //PreferenceManager.getDefaultSharedPreferences(context).edit().putString("MYLABEL", "myStringToSave").commit();

            // open CheckInActivity Intent to ask the user for login data, then
            // we will use (mEmail,mPassword) to try login again
            Intent intentCheckIn= new Intent(CampaignActivity.super.getApplicationContext(), CheckInActivity.class);
            intentCheckIn.putExtra(EXTRA_EMAIL, mEmail);                       // fill login editTextEmail
            intentCheckIn.putExtra(EXTRA_PASSWORD, mPassword);                 // fill login editTextPassword
            startActivity(intentCheckIn);

        }


        Toast.makeText(this, "Counter :  " + Integer.toString(mCounter), Toast.LENGTH_SHORT).show();
        if (mEmail.contains("@")){
            // we will use (mEmail, mPassword)  to try login
            Toast.makeText(this, "There is an email in local 'settings'" + mEmail, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "NOT FOUND email in local 'settings'", Toast.LENGTH_SHORT).show();
            // open CheckInActivity Intent to ask the user for login data, then
            // we will use (mEmail,mPassword) to try login
            // this logic will be implemented in CheckInActivity
            // if the user can not succeed in login, may use [EXIT] button do close App
            // and receive a splash screen with support information
        }


        mCounter++;


        // TODO use intent to pass login data
        final String iEmail = "gcfmelo@gmail.com";
        final String iPassword = "abc123";
//        LoginActivity();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Log.i(TAG, "... onCreate");

        // TODO GET THE CAMPAIGN LIST TO USE IN THE LAYOUT - ADJUST THIS

        dbx = PgDatabaseHelper.getInstance(this);
        campaignList = dbx.getAllCampaigns();
        dbx.close();

        Toast.makeText(this, " BD returned " + Integer.toString(campaignList.size()) + " campaigns", Toast.LENGTH_SHORT).show();
        // TODO DELETE ME
        // for debugging purposes
        if (campaignList.size() < 1) {
            Log.i(TAG, "a lista de campanhas está vazia");
            getFakeCampaignsList();
            //finish();
        }

        // create ArrayAdapter to bind weatherList to the weatherListView (
        campaignListView = (ListView) findViewById(R.id.campaignListView);
        campaignArrayAdapter = new CampaignArrayAdapter(this, campaignList);
        campaignListView.setAdapter(campaignArrayAdapter);
        campaignListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                view.setSelected(true);
                //Toast.makeText(MainActivity.this, "Clicked at item " + Integer.toString(position) + " - " + campaignList.get(position).getCampaignName(), Toast.LENGTH_SHORT).show();

                // load the Activity with detailed info about the User Selected Campaign
                String selectedCampaignName = campaignList.get(position).getCampaignName() + " | id = | " + campaignList.get(position).getCampaignId() + " |";
                selectedCampaignName = selectedCampaignName + "\n |Status=|" + campaignList.get(position).getCampaignStatus()+"|";
                String toast_value, status, campaign_id;

                if (selectedCampaignName != null) {
                    toast_value = selectedCampaignName;
                    status = campaignList.get(position).getCampaignStatus();
                    campaign_id= Integer.toString(campaignList.get(position).getCampaignId());
                    Intent intent = new Intent(CampaignActivity.super.getApplicationContext(), QuestionActivity.class);
                    intent.putExtra(EXTRA_CAMPAIGN_ID, campaign_id);

                    if (status.equals("Aguardando Resposta")){
                        toast_value = toast_value + "\n |Intent=|" + "Participate";
                        Log.d(TAG,"Aguardando Resposta");
                        intent.putExtra(ENABLE_PARTICIPATION_YN, "Y");
                    } else {
                        toast_value = toast_value + "\n |Intent=|" + "Show Details";
                        Log.d(TAG,"Finalizada");
                        intent.putExtra(ENABLE_PARTICIPATION_YN, "N");
                    }

                    startActivity(intent);
                }
                else {
                    toast_value = "NULL";
                }
                Toast.makeText(CampaignActivity.this, toast_value, Toast.LENGTH_LONG).show();
            }     // End of "onItemClick"
        });

        UpdateSponsor(selectedEmail, selectedPassword);
        UpdateUser(selectedEmail, selectedPassword);
        UpdateCampaign(selectedEmail, selectedPassword);
        UpdateQuestionOption(selectedEmail, selectedPassword);
        UpdateTransaction(selectedEmail, selectedPassword);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_menu_op0) {
            Toast.makeText(this, "You selected Option 0 id: " + Integer.toString(id), Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_menu_op1) {
            Toast.makeText(this, "You selected Option 1  id: " + Integer.toString(id), Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_menu_op2) {
            Toast.makeText(this, "You selected Option 2: RELOAD App", Toast.LENGTH_SHORT).show();

            Intent intent = getIntent();
            overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    public void onClickStartServices(View view) {

        Intent intent = new Intent(this, getWsData.class);
        intent.setAction(getWsData.ACTION_GET_LOGIN_DATA);
        intent.putExtra(getWsData.EXTRA_EMAIL, iEmail);
        intent.putExtra(getWsData.EXTRA_PASSWORD, iPassword);
        startService(intent);


        Intent intent2 = new Intent(this, getWsData.class);
        intent2.setAction(getWsData.ACTION_GET_TRANSACTIONS_DATA);
        intent2.putExtra(getWsData.EXTRA_EMAIL, iEmail);
        intent2.putExtra(getWsData.EXTRA_PASSWORD, iPassword);
        startService(intent2);

        Intent intent3 = new Intent(this, getWsData.class);
        intent3.setAction(getWsData.ACTION_GET_QUIZ_DATA);
        intent3.putExtra(getWsData.EXTRA_EMAIL, iEmail);
        intent3.putExtra(getWsData.EXTRA_PASSWORD, iPassword);
        startService(intent3);



        // TEST AND DEBUG PURPOSES
        Intent intent4 = new Intent(this, getWsData.class);
        intent4.setAction(getWsData.ACTION_TEST);
        intent4.putExtra(getWsData.EXTRA_EMAIL, iEmail);
        intent4.putExtra(getWsData.EXTRA_PASSWORD, iPassword;
        startService(intent4);

    }
    */
    public void openQuestion(View view) {
        // Do something in response to button


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

    public void TestCampaignList() {
        final String TAG = "TestCampaignList";
        Log.d(TAG, " process started");
        // dbx = new PgDatabaseHelper(getApplicationContext());
        //ArrayList<CampaignListClass> cpList = new ArrayList<CampaignListClass>();
        PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(this);
        List<CampaignListClass> cpList = dbx.getAllCampaigns();
        //dbx.closeDB();
        for (int i = 0; i < cpList.size(); i++) {
            Log.i(TAG, " item(" + Integer.toString(i) + ") ===> " + cpList.get(i).toString());
        }
    }
    public void TestSponsorList() {
        final String TAG = "TestSponsorList";
        Log.d(TAG, " process started");
        // dbx = new PgDatabaseHelper(getApplicationContext());
        //ArrayList<CampaignListClass> cpList = new ArrayList<CampaignListClass>();
        PgDatabaseHelper dbx = PgDatabaseHelper.getInstance(this);
        List<Sponsor> spList = dbx.getAllSponsors();
        //dbx.closeDB();
        for (int i = 0; i < spList.size(); i++) {
            Log.i(TAG, " item(" + Integer.toString(i) + ") ===> " + spList.get(i).toString());
        }
    }

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

    void getFakeCampaignsList() {
        // for debugging
        for (int i = 1; i < 20; i++) {
            CampaignListClass mCpl = new CampaignListClass();
            mCpl.setCampaignName("Campanha " + Integer.toString(i));
            mCpl.setCampaignId(i);
            mCpl.setSponsorName("Patrocinador " + Integer.toString(i));
            mCpl.setStartDate("2017-01-0" + Integer.toString(i));
            if (i <= 10) {
                mCpl.setCampaignStatus("Closed");
            } else {
                mCpl.setCampaignStatus("Open");
            }

            campaignList.add(mCpl);
            Log.i("DEBUG DEBUG AND DEBUG", mCpl.toString());
        }


    }

}
