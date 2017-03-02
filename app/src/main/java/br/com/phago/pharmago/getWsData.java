package br.com.phago.pharmago;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 * ***************************************************************************
 *  This Intent Service will invoke Web Services
 *  with INTENT to put all returned data into local SQLlite DataBase
 *  INTENT EXTRA parameters will define Service BEHAVIOUR
 *  Candidate Parameters are:
 *  user data: email (EXTRA_EMAIL), cpf (EXTRA_CPF), password (EXTRA_PASSWORD)
 *  Action data: ACTION_REQUEST [ "login", "getCampaigns"]
 *  Context data: token (provisioned for potential future use)
 *  **************************************************************************
 */

public class getWsData extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_GET_LOGIN_DATA = "login";
    public static final String ACTION_GET_TRANSACTIONS_DATA = "getTransaction";
    public static final String ACTION_GET_QUIZ_DATA = "getQuiz";
    //public static final String ACTION_SAVE_ANSWERS_DATA = "saveAnswers";


    // TODO: Rename parameters
    public static final String EXTRA_EMAIL = "br.com.phago.pharmago.extra.PARAM1";
   // public static final String EXTRA_CPF = "br.com.phago.pharmago.extra.PARAM2";
    public static final String EXTRA_PASSWORD = "br.com.phago.pharmago.extra.PARAM2";

    public getWsData() {
        super("getWsData");
    }

    /**
     * Starts this service to perform action getLogin with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionGetLogin(Context context, String email, String password) {
        Intent intent = new Intent(context, getWsData.class);
        intent.setAction(ACTION_GET_LOGIN_DATA);

        // TODO maybe we need to change to EXTRA_CPF and password, beacause email can be changed
        intent.putExtra(EXTRA_EMAIL, email);
        intent.putExtra(EXTRA_PASSWORD, password);
        //intent.putExtra(EXTRA_CPF, cpf);

        context.startService(intent);
    }

    /**
     * Starts this service to perform action getCampaigns with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionGetCampaigns(Context context, String email, String password) {
        Intent intent = new Intent(context, getWsData.class);
        intent.setAction(ACTION_GET_TRANSACTIONS_DATA);
        intent.putExtra(EXTRA_EMAIL, email);
        intent.putExtra(EXTRA_PASSWORD, password);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action getCampaigns with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionGetQuiz(Context context, String email, String password) {
        Intent intent = new Intent(context, getWsData.class);
        intent.setAction(ACTION_GET_QUIZ_DATA);
        intent.putExtra(EXTRA_EMAIL, email);
        intent.putExtra(EXTRA_PASSWORD, password);
        context.startService(intent);
    }


// here goes the code to run when service is called
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_LOGIN_DATA.equals(action)) {
                final String email = intent.getStringExtra(EXTRA_EMAIL);
                final String password = intent.getStringExtra(EXTRA_PASSWORD);
                handleActionGetLogin(email, password);
            } else if ((ACTION_GET_TRANSACTIONS_DATA).equals(action)) {
                final String email = intent.getStringExtra(EXTRA_EMAIL);
                final String password = intent.getStringExtra(EXTRA_PASSWORD);
                handleActionGetTransactions(email, password);
            }  else if ((ACTION_GET_QUIZ_DATA).equals(action)) {
                final String email = intent.getStringExtra(EXTRA_EMAIL);
                final String password = intent.getStringExtra(EXTRA_PASSWORD);
                handleActionGetTransactions(email, password);
            }
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Handle action getLogin in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGetLogin(String email, String password) {

        final PharmagoDatabaseHelper mDB;
        final SQLiteOpenHelper pharmagoDatabaseHelper = new PharmagoDatabaseHelper(this);
        final SQLiteDatabase db = pharmagoDatabaseHelper.getWritableDatabase();

        try {

            mDB = new PharmagoDatabaseHelper(this);

            RequestQueue queue = Volley.newRequestQueue(this);

            URL urlObj = createURL(email, password, "login", "123456789");     //format URL to call WS
            final String mEmail = email;
            final String mPassword = password;
            String url = urlObj.toString();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        ///////////////////////////////////////////////////////////
                        //  TODO UPDATE USER LOGIN DATA IN SQlite
                        ///////////////////////////////////////////////////////////

                        Log.v("User count", Integer.toString(mDB.getUserCount()));

                        mDB.addUserRecord(
                                  response.getString("email")
                                , response.getString("name")
                                , response.getString("status")
                                , response.getString("cpf")
                                , response.getString("companyCode")
                                , response.getString("companyName")
                                , response.getString("companyLatitude")
                                , response.getString("companyLongitude")
                        );

                        Log.v("User CPF   @@@@@@@@   ", mDB.getUserCpf(1));
                        Log.v("User Name  @@@@@@@@   ", mDB.getUserName(1));
                        Log.v("User Count @@@@@@@@   ", Integer.toString(mDB.getUserCount()));

                        mDB.close();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }  // onResponse
            }

                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error.getMessage() == null) {
                        Log.v("login   -  ", "Login has failed!" + "\n\n");
                        // TODO: reset your password, create a new account
                    } else {
                        Log.v("login   -  ", "onErrorResponse(): " + error.getMessage());
                    }
                }
            });
            queue.add(jsonObjectRequest);   // replace for the correct object name
        } catch (SQLiteException e) {
            Log.v("Service: ", "Database is unavailable!");
            pharmagoDatabaseHelper.close();
        }
        pharmagoDatabaseHelper.close();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Handle action handleActionGetTransactions in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGetTransactions(String email, String password) {
        final PharmagoDatabaseHelper mDB;
        final SQLiteOpenHelper pharmagoDatabaseHelper = new PharmagoDatabaseHelper(this);
        final SQLiteDatabase db = pharmagoDatabaseHelper.getWritableDatabase();
        final boolean useText = true;


        try {
        mDB = new PharmagoDatabaseHelper(this);

        mDB.recreateTransactionTable(db);

        RequestQueue queue = Volley.newRequestQueue(this);

        URL urlObj = createURL(email, password, "getTransactions", "123456789");     //format URL to call WS
        //final String mEmail = email;
        //final String mPassword = password;
        String url = urlObj.toString();

        if (useText) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String myJsonString = response.toString();
                    Log.v("TEXT RETURNED:   @@@   ",myJsonString);
                    if (myJsonString.startsWith("[")){
                        myJsonString="{\"transaction\":"+myJsonString+"}";
                    }
                    // converting to JSONObject
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(myJsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.v("JSON OBJ RETURN: @@@   ",jsonObj.toString());

                    JSONArray tr = null;
                    try {
                        tr = jsonObj.getJSONArray("transaction");
                        Log.v("transaction: @@@   ",tr.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (tr != null) {

                        for (int i=0;i< tr.length(); i++){
                            try {
                                JSONObject obj = tr.getJSONObject(i);
                                // TODO remove
                                String txtLog = obj.getString("sponsorCode")+
                                        obj.getInt("idCampaign")+
                                        obj.getInt("idTransaction")+
                                        obj.getString("eventDate")+
                                        obj.getString("title")+
                                        obj.getString("nature")+
                                        obj.getInt("amount");

                                mDB.addTransactionRecord(
                                        obj.getString("sponsorCode"),
                                        obj.getInt("idCampaign"),
                                        obj.getInt("idTransaction"),
                                        obj.getString("eventDate"),
                                        obj.getString("title"),
                                        obj.getString("nature"),
                                        obj.getInt("amount"));

                                Log.v("sponsorCode: @@@   ",obj.getString("sponsorCode"));
                                Log.v("idCampaign: @@@   ",obj.getString("idCampaign"));
                                Log.v("idTransaction: @@@   ",obj.getString("idTransaction"));
                                Log.v("eventDate : @@@   ",obj.getString("eventDate"));
                                Log.v("title : @@@   ",obj.getString("title"));
                                Log.v("nature: @@@   ",obj.getString("nature"));
                                Log.v("amount: @@@   ",obj.getString("amount"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        mDB.close();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error.getMessage() == null) {
                        Log.v("TRANSACTION ERROR: @@@ ","Transactions Sync has Failed!");
                        // TODO: retrieve your password
                    } else {
                        Log.v("ERROR RETURNED:    @@@ ", error.getMessage());
                    }
                }
            });
            queue.add(stringRequest);
        } else {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        ///////////////////////////////////////////////////////////
                        //  TODO UPDATE USER LOGIN DATA IN SQlite
                        ///////////////////////////////////////////////////////////

                        Log.v("Transaction count", Integer.toString(mDB.getTransactionCount()));

                        mDB.addTransactionRecord(
                                response.getString("sponsorCode")
                                ,Integer.parseInt(response.getString("idCampaign"))
                                ,Integer.parseInt(response.getString("idTransaction"))
                                , response.getString("eventDate")
                                , response.getString("title")
                                , response.getString("nature")
                                , Integer.parseInt(response.getString("amount"))
                        );

                        Log.v("Trans ID      @@@@@", mDB.getTransactionTitle(Integer.parseInt(response.getString("idTransaction"))));
                        Log.v("Trans Date @@@@@@@@   ", mDB.getTransactionEventDate(Integer.parseInt(response.getString("idTransaction"))));
                        Log.v("Trans Count   @@@@@", Integer.toString(mDB.getTransactionCount()));

                        mDB.close();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }  // onResponse
            }

                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error.getMessage() == null) {
                        Log.v("login   -  ", "Login has failed!" + "\n\n");
                        // TODO: reset your password, create a new account
                    } else {
                        Log.v("login   -  ", "onErrorResponse(): " + error.getMessage());
                    }
                }
            });
            queue.add(jsonObjectRequest);   // replace for the correct object name
        }

        }catch (SQLiteException e) {
            Log.v("Service: ", "Database is unavailable!");
            pharmagoDatabaseHelper.close();
        }

        pharmagoDatabaseHelper.close();

    }



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
private void handleActionGetQuiz(String email, String password) {
    // TODO: SQLite connection

    try {
        //final SQLiteOpenHelper pharmagoDatabaseHelper = new PharmagoDatabaseHelper(this);
        //final SQLiteDatabase db = pharmagoDatabaseHelper.getWritableDatabase();
        RequestQueue queue = Volley.newRequestQueue(this);

        URL urlObj = createURL(email, password, "getQuiz", "123456789");     //format URL to call WS
        final String mEmail = email;
        final String mPassword = password;
        String url = urlObj.toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String txtMsg = "";
                    String j_email = response.getString("email");
                    String j_name = response.getString("name");
                    String j_status = response.getString("status");
                    String j_cpf = response.getString("cpf");
                    String j_companyCode = response.getString("companyCode");
                    String j_companyName = response.getString("companyName");
                    String j_companyLatitude = response.getString("companyLatitude");
                    String j_companyLongitude = response.getString("companyLongitude");

                    Log.v("@@@@@@@@@@","\n-------------------------------------------------------------------------\n\n");
                    txtMsg = "Username: " + j_name + "\n"
                            + "Email: " + j_email + "\n"
                            + "CPF: " + j_cpf + "\n"
                            + "Status: " + j_status + "\n"
                            + "Company Code (CNPJ): " + j_companyCode + "\n"
                            + "Company Name: " + j_companyName + "\n"
                            + "Company Latitude: " + j_companyLatitude + "\n"
                            + "Company Longitude: " + j_companyLongitude + "\n"
                            + "\n-------------------------------------------------------------------------\n\n";

                    Log.v("WS action = login", " FROM JSON OBJECT\n\n" + txtMsg + "\n\n");




                    ///////////////////////////////////////////////////////////
                    //  TODO UPDATE USER LOGIN DATA IN SQlite
                    ///////////////////////////////////////////////////////////

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }  // onResponse
        }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null) {
                    Log.v("login   -  ", "Login has failed!" + "\n\n");
                    // TODO: reset your password, create a new account
                } else {
                    Log.v("login   -  ", "onErrorResponse(): " + error.getMessage());
                }
            }
        });
        queue.add(jsonObjectRequest);   // replace for the correct object name
    } catch (SQLiteException e) {
        Log.v("Service: ", "Database is unavailable!");
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
