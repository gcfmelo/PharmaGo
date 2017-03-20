package br.com.phago.pharmago;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class OptionActivity extends AppCompatActivity {
    public static final boolean RECREATE_TABLES = false;
    public static final String KEY_COUNTER = "COUNTER";
    public static final String WS_RETURN_OK = "SUCCESSFUL";
    public static final String WS_RETURN_ERROR = "ERROR";
    private static final String TAG = OptionActivity.class.getSimpleName();

    // DatabaseHelper
    PgDatabaseHelper dbx;
    //List of Campaign objects
//    private List<Campaign> campaignList = new ArrayList<>();
    private List<Option> optionList = new ArrayList<Option>();
    // migrated to other activity
//    private List<CampaignDetailListClass> campaignDetailList = new ArrayList<CampaignDetailListClass>();
    // ArrayAdapter for bind Campaign objects to a ListView
    private OptionArrayAdapter optionArrayAdapter;
    private ListView optionListView; // displays Campaign info

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        final String selectedCampaignId = intent.getStringExtra(QuestionActivity.EXTRA_CAMPAIGN_ID);
        final String selectedQuestionId = intent.getStringExtra(QuestionActivity.EXTRA_QUESTION_ID);
        final String selectedCampaignParticipationEnabled = intent.getStringExtra(QuestionActivity.ENABLE_PARTICIPATION_YN);


        // Capture the layout's TextView and set the string as its text
//        TextView textView = (TextView) findViewById(R.id.textView);
//        textView.setText(message);

        Log.i(TAG, "... onCreate");
        dbx = new PgDatabaseHelper(getApplicationContext());

        optionList = dbx.getOptionsByQuestionAndCampaignId(Integer.parseInt(selectedCampaignId), Integer.parseInt(selectedQuestionId));

        dbx.closeDB();

        Toast.makeText(this, " BD returned " + Integer.toString(optionList.size()) + " questions", Toast.LENGTH_SHORT).show();
        // TODO DELETE ME
        // for debugging purposes
        if (optionList.size() < 1) {
            Log.i(TAG, "a lista de opções está vazia");
            // getFakeCampaignsList();
            //finish();
        }

        // create ArrayAdapter to bind xxxList to the xxxxListView (
        optionListView = (ListView) findViewById(R.id.optionListView);
        optionArrayAdapter = new OptionArrayAdapter(this, optionList);
        optionListView.setAdapter(optionArrayAdapter);
        optionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                view.setSelected(true);
                //Toast.makeText(MainActivity.this, "Clicked at item " + Integer.toString(position) + " - " + campaignList.get(position).getCampaignName(), Toast.LENGTH_SHORT).show();

                // load the Activity with detailed info about the User Selected Questions
                String selectedOptionId = " | id = | " + optionList.get(position).getSequential().toString() + " |";
                String selectedOptionLabel = "| Option Label = | " + optionList.get(position).getLabel() + "|";
                String toast_value;
                String initial_user_answer = optionList.get(position).getUserAnswer();
                String current_user_answer;

                int campaign_id;

                if (selectedCampaignParticipationEnabled.equals("Y")) {
                    if (selectedOptionId != null) {
                        toast_value = selectedOptionId + "\n" + selectedOptionLabel;
                        if (optionList.get(position).getUserAnswer().equals("U")) {
                            current_user_answer = "N";
                            Toast.makeText(OptionActivity.this, "Answer changed from 'U' to 'N'", Toast.LENGTH_SHORT).show();
                            // updateUserAnswer(...)
                            } else if (optionList.get(position).getUserAnswer().equals("Y")) {
                            current_user_answer = "N";
                            Toast.makeText(OptionActivity.this, "Answer changed from 'Y' to 'N'", Toast.LENGTH_SHORT).show();
                            // updateUserAnswer(...)
                                } else if (optionList.get(position).getUserAnswer().equals("N")) {
                            current_user_answer = "Y";
                            Toast.makeText(OptionActivity.this, "Answer changed from 'N' to 'Y'", Toast.LENGTH_SHORT).show();
                            // updateUserAnswer(...)
                            }
                        } else {
                        toast_value = "NULL";
                        }

                    Toast.makeText(OptionActivity.this, toast_value, Toast.LENGTH_LONG).show();
                }
            }
        });

//        UpdateSponsor("gcfmelo@gmail.com", "abc123");
//        UpdateUser("gcfmelo@gmail.com", "abc123");
//        UpdateCampaign("gcfmelo@gmail.com", "abc123");
//        UpdateQuestionOption("gcfmelo@gmail.com", "abc123");
//        UpdateTransaction("gcfmelo@gmail.com", "abc123");

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_menu_op0) {
//            Toast.makeText(this, "You selected Option 0 id: " + Integer.toString(id), Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        if (id == R.id.action_menu_op1) {
//            Toast.makeText(this, "You selected Option 1  id: " + Integer.toString(id), Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        if (id == R.id.action_menu_op2) {
//            Toast.makeText(this, "You selected Option 2: RELOAD App", Toast.LENGTH_SHORT).show();
//
//            Intent intent = getIntent();
//            overridePendingTransition(0, 0);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            finish();
//            overridePendingTransition(0, 0);
//            startActivity(intent);
//
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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
                                        jsonObjUser.getString("name"),
                                        jsonObjUser.getString("userAccountStatus"),
                                        jsonObjUser.getString("cpf"),
                                        jsonObjUser.getString("companyCode"),
                                        jsonObjUser.getString("companyName"),
                                        jsonObjUser.getString("companyLatitude"),
                                        jsonObjUser.getString("companyLongitude"));
                                dbx.addUser(user);
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

    public void UpdateSponsor(String email, String password) {
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
        List<CampaignListClass> cpList = dbx.getAllCampaigns();
        for (int i = 0; i < cpList.size(); i++) {
            Log.i(TAG, " item(" + Integer.toString(i) + ") ===> " + cpList.get(i).toString());
        }
    }
    public void TestSponsorList() {
        final String TAG = "TestSponsorList";
        Log.d(TAG, " process started");
        // dbx = new PgDatabaseHelper(getApplicationContext());
        //ArrayList<CampaignListClass> cpList = new ArrayList<CampaignListClass>();
        List<Sponsor> spList = dbx.getAllSponsors();
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
//        for (int i = 1; i < 20; i++) {
//            CampaignListClass mCpl = new CampaignListClass();
//            mCpl.setCampaignName("Campanha " + Integer.toString(i));
//            mCpl.setCampaignId(i);
//            mCpl.setSponsorName("Patrocinador " + Integer.toString(i));
//            mCpl.setStartDate("2017-01-0" + Integer.toString(i));
//            if (i <= 10) {
//                mCpl.setCampaignStatus("Closed");
//            } else {
//                mCpl.setCampaignStatus("Open");
//            }
//
//            campaignList.add(mCpl);
//            Log.i("DEBUG DEBUG AND DEBUG", mCpl.toString());
//        }


    }


}
