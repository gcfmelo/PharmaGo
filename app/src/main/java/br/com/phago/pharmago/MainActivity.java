package br.com.phago.pharmago;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    public static final boolean RECREATE_TABLES = false;
    public static final String KEY_COUNTER = "COUNTER";
    public static final String WS_RETURN_OK = "SUCCESSFUL";
    public static final String WS_RETURN_ERROR = "ERROR";
    // global variables to hold user data
    public static String user_id, user_name, user_email;
    public static int mCounter = 0;

    ResponseElton responseElton = new ResponseElton();
    // DatabaseHelper
    PgDatabaseHelper dbx;
    //List of Campaign objects
    //private List<Campaign> campaignList = new ArrayList<>();
    private List<CampaignListClass> campaignList = new ArrayList<CampaignListClass>();
    // ArrayAdapter for bind Campaign objects to a ListView
    private CampaignArrayAdapter campaignArrayAdapter;
    private ListView campaignListView; // displays Campaign info

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String TAG = "Main Activity";
        Log.i(TAG, "... onCreate");
        dbx = new PgDatabaseHelper(getApplicationContext());
        // Update local databases
//        UpdateUser("gcfmelo@gmail.com", "abc123");
//        UpdateSponsor("gcfmelo@gmail.com", "abc123");

        Log.i(TAG, "... onCreate");
//        UpdateSponsor("gcfmelo@gmail.com", "abc123");
        //TestSponsorList();
//        UpdateUser("gcfmelo@gmail.com", "abc123");
        UpdateCampaign("gcfmelo@gmail.com", "abc123");
//        UpdateQuestionOption("gcfmelo@gmail.com", "abc123");
//        UpdateTransaction("gcfmelo@gmail.com", "abc123");


        // TODO GET THE CAMPAIGN LIST TO USE IN THE LAYOUT - ADJUST THIS
        campaignList = dbx.getAllCampaigns();
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
                Toast.makeText(MainActivity.this, "Clicked at item " + Integer.toString(position) + " - " + campaignList.get(position).getCampaignName(), Toast.LENGTH_SHORT).show();
                // load the Activity with detailed info about the User Selected Campaign
            }
        });
        List<Sponsor> sp = dbx.getAllSponsors();
        Toast.makeText(this, " BD returned " + Integer.toString(sp.size()) + " sponsors", Toast.LENGTH_LONG).show();
        //dbx.closeDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
            Toast.makeText(this, "You selected Option 2 id: " + Integer.toString(id), Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

                        if (jsonObjRoot.getString("status").toString().equals(WS_RETURN_OK)) {

                            jsonObjUser = jsonObjRoot.getJSONObject("json");
                            Log.i(TAG, "531_jsonArry: @@@" + jsonObjUser.toString());

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

                                Log.i(TAG, "Element:   email   @@@ " + mUsrEmail);
                                Log.i(TAG, "Element:   name @@@ " + mUsrName);
                                Log.i(TAG, "Element:   status @@@ " + mUsrStatus);
                                Log.i(TAG, "Element:   cpf   @@@ " + mUsrCpf);
                                Log.i(TAG, "Element:   companyCode @@@ " + mUsrCpnyCode);
                                Log.i(TAG, "Element:   companyName @@@ " + mUsrCpnyNme);
                                Log.i(TAG, "Element:   companyLatitude   @@@ " + mUsrCpnyLat);
                                Log.i(TAG, "Element:   companyLongitude @@@ " + mUsrCpnyLon);

                                User user = new User(mUsrEmail, mUsrName, mUsrStatus, mUsrCpf, mUsrCpnyCode, mUsrCpnyNme, mUsrCpnyLat, mUsrCpnyLon);

                                dbx.addUser(user);

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

    public void UpdateSponsor(String email, String password) {

        final String TAG = "UpdateSponsor";
        Log.d(TAG, " process started");

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

                        if (jsonObjRoot.getString("status").equals(WS_RETURN_OK)) {

                            jsonArry = jsonObjRoot.getJSONArray("json");
                            Log.i(TAG, "328_jsonArry: @@@" + jsonArry.toString());

                            for (int i = 0; i < jsonArry.length(); i++) {

                                Sponsor sponsor = new Sponsor(jsonArry.getJSONObject(i).getInt("idSponsor"),
                                        jsonArry.getJSONObject(i).getString("sponsorCode"),
                                        jsonArry.getJSONObject(i).getString("sponsorName"));

                                dbx.addSponsor(sponsor);

                                Log.i(TAG, "Element:   @@@   " + Integer.toString(i) + " " + jsonArry.getJSONObject(i));
                                //
                                // implement table update here
                                //
                            }


                        } else {
                            // WS responded with:
                            Log.i(TAG, "WS responded with:   @@@   " + jsonObjRoot.getString("status").toString());
                        }

                        // drop table pg_sponsor
                        //dbx.dropTable("pg_sponsor");

                        // create a new table pg_sponsor
                        //dbx.createTableSponsor();

                        // create a temp table for sponsor
                        // dbx.createTableTempSponsor();

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

    public void UpdateCampaign_old(String email, String password) {

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

                        if (jsonObjRoot.getString("status").toString().equals(WS_RETURN_OK)) {

                            jsonArry = jsonObjRoot.getJSONArray("json");
                            Log.i(TAG, "328_jsonArry: @@@" + jsonArry.toString());

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

                                    Log.i(TAG, "Element:   idCampaign   @@@ ( " + Integer.toString(i) + " ) = " + mCpId);
                                    Log.i(TAG, "Element:   idSponsor   @@@ ( " + Integer.toString(i) + " ) = " + mSpId);
                                    Log.i(TAG, "Element:   title @@@ ( " + Integer.toString(i) + " ) = " + mCpTit);
                                    Log.i(TAG, "Element:   startDate @@@ ( " + Integer.toString(i) + " ) = " + mCpStDt);
                                    Log.i(TAG, "Element:   endDate   @@@ ( " + Integer.toString(i) + " ) = " + mCpEndDt);
                                    Log.i(TAG, "Element:   numberOfQuestions   @@@ ( " + Integer.toString(i) + " ) = " + mCpNumQ);
                                    Log.i(TAG, "Element:   pointsForRightAnswer   @@@ ( " + Integer.toString(i) + " ) = " + mCpPtRightAnswer);
                                    Log.i(TAG, "Element:   pointsForParticipation   @@@ ( " + Integer.toString(i) + " ) = " + mCpPtPartic);
                                    Log.i(TAG, "Element:   status   @@@ ( " + Integer.toString(i) + " ) = " + mCpStatus);

                                    Campaign campaign = new Campaign(Integer.parseInt(mCpId),
                                            Integer.parseInt(mSpId),
                                            mCpTit, mCpStDt, mCpEndDt,
                                            Integer.parseInt(mCpNumQ),
                                            Integer.parseInt(mCpPtRightAnswer),
                                            Integer.parseInt(mCpPtPartic),
                                            mCpStatus);

                                    dbx.addCampaign(campaign);
                                    Log.i(TAG, "Element:   @@@   " + Integer.toString(i) + " " + jsonArry.getJSONObject(i));
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

                        //        UpdateQuestionOption("gcfmelo@gmail.com", "abc123");
//        UpdateTransaction("gcfmelo@gmail.com", "abc123");
//        TestCampaignList();

                        ////////////////////////////////////////////////////////////////
//
//                        SQLiteDatabase db1 = dbx.getWritableDatabase();
//                        String selectQuery1 = "SELECT * from pg_campaign";
//                        Cursor c1 = db1.rawQuery(selectQuery1, null);
//                        c1.moveToFirst();
//                        Log.i("teste", Integer.toString(c1.getCount()));
//                        c1.close();
//                        db1.close();

                        /////////////////////////////////////////////////////////////////////////////


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
            //        UpdateQuestionOption("gcfmelo@gmail.com", "abc123");
//        UpdateTransaction("gcfmelo@gmail.com", "abc123");
//        TestCampaignList();

            ////////////////////////////////////////////////////////////////

            SQLiteDatabase db1 = dbx.getWritableDatabase();
            String selectQuery1 = "SELECT * from pg_campaign";
            Cursor c1 = db1.rawQuery(selectQuery1, null);
            c1.moveToFirst();
            Log.i("teste", Integer.toString(c1.getCount()));
            c1.close();
            db1.close();

            /////////////////////////////////////////////////////////////////////////////
            // TODO ADJUST THIS

            queue.add(stringRequest);   // replace for the correct object name
        } catch (SQLiteException e) {
            Log.i(TAG, "Service: " + "Database is unavailable!");
        }

    }

    public void UpdateCampaign(String email, String password) {

        final String TAG = "UpdateCampaign";

        if (RECREATE_TABLES) {
            dbx.dropTable("pg_campaign");
            dbx.createTableCampaign();
        } else {
            dbx.clearTableCampaign();
        }



        // URL url = createURL(email, password, "findAllCampaigns", "123456789");     //format URL to call WS


        HttpsURLConnection https = null;


        try {
            URL url = this.createURL("gcfmelo@gmail.com", "abc123", "findAllCampaigns", "123456789") ;

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader inx = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line1;
                while ((line1 = inx.readLine()) != null) {
                    System.out.println(line1);
                    Log.e(TAG, line1);
                }
            } finally {
                urlConnection.disconnect();
            }
//            https = (HttpsURLConnection) url.openConnection();
//            https.setReadTimeout(10000);
//            https.setConnectTimeout(15000);
//            https.setRequestMethod("POST");
//            https.setDoInput(true);
//            https.setDoOutput(true);
//            OutputStream os = https.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//            writer.write(this.getQuery(nvps));
//            writer.flush();
//            writer.close();
//            os.close();
            https.connect();
//            BufferedReader in = new BufferedReader(new InputStreamReader(https.getInputStream()));
//            String line1;
//            while ((line1 = in.readLine()) != null) {
//                System.out.println(line1);
//                Log.e(TAG, line1);
//            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }





//        String jsonElton;
//        ResponseElton responseElton1 = new ResponseElton();
//        com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd").create();
//        responseElton1 = gson.fromJson(myJsonString, ResponseElton.class);
//
//
//            com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd").create();
//            ArrayList<CampaignBean> beans = gson.fromJson(responseElton.getJson(), new TypeToken<ArrayList<CampaignBean>>() {
//            }.getType());
//
//            for (CampaignBean bean : beans) {
//                Log.i("BEAN ", bean.getIdCampaign().toString() + " - " + bean.getTitle());
//                for (QuestionBean q : bean.getQuestions()) {
//                    Log.i("QUESTION BEAN ", q.getLabel());
//                    for (OptionBean o : q.getOptions()) {
//                        Log.i("OPTION BEAN ", o.getLabel());
//                    }
//                }
//            }
//



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

                        if (jsonObjRoot.getString("status").toString().equals(WS_RETURN_OK)) {

                            jsonArryQuestion = jsonObjRoot.getJSONArray("json");
                            Log.i(TAG, "328_jsonArry: @@@" + jsonArryQuestion.toString());

                            if (jsonArryQuestion != null) {
                                for (int i = 0; i < jsonArryQuestion.length(); i++) {
                                    String mQtId = jsonArryQuestion.getJSONObject(i).getString("idQuestion").toString();
                                    String mQtCpId = jsonArryQuestion.getJSONObject(i).getString("idCampaign").toString();
                                    String mQtSpId = jsonArryQuestion.getJSONObject(i).getString("idSponsor").toString();
                                    String mQtLabel = jsonArryQuestion.getJSONObject(i).getString("label").toString();


                                    Log.i(TAG, "Element:   idQuestion   @@@ ( " + Integer.toString(i) + " ) = " + mQtId);
                                    Log.i(TAG, "Element:   idCampaign   @@@ ( " + Integer.toString(i) + " ) = " + mQtCpId);
                                    Log.i(TAG, "Element:   idSponsor @@@ ( " + Integer.toString(i) + " ) = " + mQtSpId);
                                    Log.i(TAG, "Element:   label @@@ ( " + Integer.toString(i) + " ) = " + mQtLabel);


                                    Question question = new Question(Integer.parseInt(mQtId),
                                            Integer.parseInt(mQtCpId),
                                            Integer.parseInt(mQtSpId),
                                            mQtLabel);

                                    dbx.addQuestion(question);
                                    Log.i(TAG, "Element:   @@@   " + Integer.toString(i) + " " + jsonArryQuestion.getJSONObject(i));

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

                                            Log.i(TAG, "Element:   idSponsor   @@@ ( " + Integer.toString(i * 1000 + j) + " ) = " + mOpSpId);
                                            Log.i(TAG, "Element:   idCampaign   @@@ ( " + Integer.toString(i * 1000 + j) + " ) = " + mOpCpId);
                                            Log.i(TAG, "Element:   idQuestion   @@@ ( " + Integer.toString(i * 1000 + j) + " ) = " + mOpQtId);
                                            Log.i(TAG, "Element:   sequential   @@@ ( " + Integer.toString(i * 1000 + j) + " ) = " + mOpSeq);
                                            Log.i(TAG, "Element:   label   @@@ ( " + Integer.toString(i * 1000 + j) + " ) = " + mOpLabel);
                                            Log.i(TAG, "Element:   rightAnswer   @@@ ( " + Integer.toString(i * 1000 + j) + " ) = " + mOpRightAnsw);
                                            Log.i(TAG, "Element:   userAnswer   @@@ ( " + Integer.toString(i * 1000 + j) + " ) = " + mOpUsrAnsw);


                                            // Option(Integer idSponsor, Integer idCampaign, Integer idQuestion, Integer sequential, String label, String rightAnswer, String userAnswer)
                                            Option option = new Option(Integer.parseInt(mOpSpId),
                                                    Integer.parseInt(mOpCpId),
                                                    Integer.parseInt(mOpQtId),
                                                    Integer.parseInt(mOpSeq),
                                                    mOpLabel, mOpRightAnsw, mOpUsrAnsw);
                                            dbx.addOption(option);
                                        }  // process nex Option
                                    }

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

        //dbx.dropTable("pg_transaction");
        //dbx.createTableTransaction();
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

                        if (jsonObjRoot.getString("status").toString().equals(WS_RETURN_OK)) {

                            jsonArry = jsonObjRoot.getJSONArray("json");
                            Log.i(TAG, "689_jsonArry: @@@" + jsonArry.toString());
                            /*
                            Transaction(String sponsorCode, String eventDate, String title, String nature, int idCampaign, int idTransaction, int amount)
                             */

                            if (jsonArry != null) {
                                for (int i = 0; i < jsonArry.length(); i++) {
                                    String mTrSpCode = jsonArry.getJSONObject(i).getString("sponsorCode").toString();
                                    String mTrDate = jsonArry.getJSONObject(i).getString("eventDate").toString();
                                    String mTrTitle = jsonArry.getJSONObject(i).getString("title").toString();
                                    String mTrNature = jsonArry.getJSONObject(i).getString("nature").toString();

                                    int mTrIdCp = jsonArry.getJSONObject(i).getInt("idCampaign");
                                    int mTrIdTr = jsonArry.getJSONObject(i).getInt("idTransaction");
                                    int mTrAmount = jsonArry.getJSONObject(i).getInt("amount");

                                    Log.i(TAG, "Element:   sponsorCode   @@@ ( " + Integer.toString(i) + " ) = " + mTrSpCode);
                                    Log.i(TAG, "Element:   eventDate @@@ ( " + Integer.toString(i) + " ) = " + mTrDate);
                                    Log.i(TAG, "Element:   title @@@ ( " + Integer.toString(i) + " ) = " + mTrTitle);
                                    Log.i(TAG, "Element:   nature @@@ ( " + Integer.toString(i) + " ) = " + mTrNature);
                                    Log.i(TAG, "Element:   idCampaign @@@ ( " + Integer.toString(i) + " ) = " + mTrIdCp);
                                    Log.i(TAG, "Element:   idTransaction @@@ ( " + Integer.toString(i) + " ) = " + mTrIdTr);
                                    Log.i(TAG, "Element:   amount @@@ ( " + Integer.toString(i) + " ) = " + mTrAmount);


                                    Transaction tr = new Transaction(mTrSpCode, mTrDate, mTrTitle, mTrNature, mTrIdCp, mTrIdTr, mTrAmount);

                                    dbx.addTransaction(tr);

                                    Log.i(TAG, "Element:   @@@   " + Integer.toString(i) + " " + jsonArry.getJSONObject(i));
                                    //
                                    // implement table update here
                                    //

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
        for (int i = 1; i < 20; i++) {
            CampaignListClass mCpl = new CampaignListClass();
            mCpl.setCampaignName("Campanha " + Integer.toString(i));
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

    class ResponseElton {

        private String status;
        private String errorMessage;
        private String json;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getJson() {
            return json;
        }

        public void setJson(String json) {
            this.json = json;
        }
    }

    public class OptionBean {
        private Integer idSponsor;
        private Integer idCampaign;
        private Integer idQuestion;
        private Integer sequential;
        private String label;
        private String rightAnswer;
        private String userAnswer;

        public Integer getIdSponsor() {
            return idSponsor;
        }

        public void setIdSponsor(Integer idSponsor) {
            this.idSponsor = idSponsor;
        }

        public Integer getIdCampaign() {
            return idCampaign;
        }

        public void setIdCampaign(Integer idCampaign) {
            this.idCampaign = idCampaign;
        }

        public Integer getIdQuestion() {
            return idQuestion;
        }

        public void setIdQuestion(Integer idQuestion) {
            this.idQuestion = idQuestion;
        }

        public Integer getSequential() {
            return sequential;
        }

        public void setSequential(Integer sequential) {
            this.sequential = sequential;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getRightAnswer() {
            return rightAnswer;
        }

        public void setRightAnswer(String rightAnswer) {
            this.rightAnswer = rightAnswer;
        }

        public String getUserAnswer() {
            return userAnswer;
        }

        public void setUserAnswer(String userAnswer) {
            this.userAnswer = userAnswer;
        }
    }

    public class QuestionBean {
        private Integer idSponsor;
        private Integer idCampaign;
        private Integer idQuestion;
        private String label;
        private ArrayList<OptionBean> options;

        public Integer getIdSponsor() {
            return idSponsor;
        }

        public void setIdSponsor(Integer idSponsor) {
            this.idSponsor = idSponsor;
        }

        public Integer getIdCampaign() {
            return idCampaign;
        }

        public void setIdCampaign(Integer idCampaign) {
            this.idCampaign = idCampaign;
        }

        public Integer getIdQuestion() {
            return idQuestion;
        }

        public void setIdQuestion(Integer idQuestion) {
            this.idQuestion = idQuestion;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public ArrayList<OptionBean> getOptions() {
            return options;
        }

        public void setOptions(ArrayList<OptionBean> options) {
            this.options = options;
        }
    }

    public class CampaignBean {
        private Integer idCampaign;
        private Integer idSponsor;
        private String title;
        private java.sql.Date startDate;
        private java.sql.Date endDate;
        private Integer numberOfQuestions;
        private Integer pointsForRightAnswer;
        private Integer pointsForParticipation;
        private String status;
        private ArrayList<QuestionBean> questions;

        public Integer getIdCampaign() {
            return idCampaign;
        }

        public void setIdCampaign(Integer idCampaign) {
            this.idCampaign = idCampaign;
        }

        public Integer getIdSponsor() {
            return idSponsor;
        }

        public void setIdSponsor(Integer idSponsor) {
            this.idSponsor = idSponsor;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public Integer getNumberOfQuestions() {
            return numberOfQuestions;
        }

        public void setNumberOfQuestions(Integer numberOfQuestions) {
            this.numberOfQuestions = numberOfQuestions;
        }

        public Integer getPointsForRightAnswer() {
            return pointsForRightAnswer;
        }

        public void setPointsForRightAnswer(Integer pointsForRightAnswer) {
            this.pointsForRightAnswer = pointsForRightAnswer;
        }

        public Integer getPointsForParticipation() {
            return pointsForParticipation;
        }

        public void setPointsForParticipation(Integer pointsForParticipation) {
            this.pointsForParticipation = pointsForParticipation;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public ArrayList<QuestionBean> getQuestions() {
            return questions;
        }

        public void setQuestions(ArrayList<QuestionBean> questions) {
            this.questions = questions;
        }
    }

}
