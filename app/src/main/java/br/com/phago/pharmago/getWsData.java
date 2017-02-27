package br.com.phago.pharmago;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
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
    public static final String ACTION_GET_LOGIN_DATA = "get login data";
    public static final String ACTION_GET_CAMPAIGNS_DATA = "get campaigns data";
    // private static final String ACTION_BAZ = "br.com.phago.pharmago.action.BAZ";

    // TODO: Rename parameters
    public static final String EXTRA_EMAIL = "br.com.phago.pharmago.extra.PARAM1";
   // public static final String EXTRA_CPF = "br.com.phago.pharmago.extra.PARAM2";
    public static final String EXTRA_PASSWORD = "br.com.phago.pharmago.extra.PARAM2";

    public getWsData() {
        super("getWsData");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
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
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionGetCampaigns(Context context, String email, String password) {
        Intent intent = new Intent(context, getWsData.class);
        intent.setAction(ACTION_GET_CAMPAIGNS_DATA);
        intent.putExtra(EXTRA_EMAIL, email);
        intent.putExtra(EXTRA_PASSWORD, password);
        //intent.putExtra(EXTRA_CPF, cpf);

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
            } else if (ACTION_GET_CAMPAIGNS_DATA.equals(action)) {
                final String email = intent.getStringExtra(EXTRA_EMAIL);
                final String password = intent.getStringExtra(EXTRA_PASSWORD);
                handleActionGetCampaigns(email, password);
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
        // TODO: Handle action get Login


        RequestQueue queue = Volley.newRequestQueue(this);

        URL urlObj = createURL(email, password, "login", "123456789");     //format URL to call WS
        final String mEmail = email;
        final String mPassword = password;

        String url = urlObj.toString();

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      /*
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String mResponse = response;
                //    Log.v("getLoginService","\n\n\n"+"reading STRING format mResponse: "+mResponse+"\n\n\n");


                // using log to test the service
                Log.v("getLoginService", "##############################################################################");
                Log.v("getLoginService", (" with email:"+mEmail+" and password: "+mPassword));
                        Log.v("getLoginService", "##############################################################################");
                Log.v("getLoginService", mResponse);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null) {
                    // use log for errors
                    Log.v("getLoginService","Login has failed!");
                    // TODO: retrieve your password
                } else {
                    // use log for errors
                    Log.v("getLoginService",("onErrorResponse(): " + error.getMessage()));
                }
            }
        });
        queue.add(stringRequest);
    }

*/
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// ws using volley with JsonObject


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String txtMsg = "";
                    String j_name = response.getString("name");
                    String j_status = response.getString("status");
                    String j_companyCode = response.getString("companyCode");
                    String j_companyName = response.getString("companyName");
                    String j_companyLatitude = response.getString("companyLatitude");
                    String j_companyLongitude = response.getString("companyLongitude");
                    String j_transactions = response.getString("transactions");


                    txtMsg = "FROM JSON OBJECT WE HAVE: \n\n"
                            + "Username: " + j_name + "\n"
                            + "Status: " + j_status + "\n"
                            + "Company Code (CNPJ): " +j_companyCode+ "\n"
                            + "Company Name: " + j_companyName + "\n"
                            + "Company Latitude: " + j_companyLatitude + "\n"
                            + "Company Longitude: " +j_companyLongitude + "\n"
                            + "Transactions: " + j_transactions + "\n"
                            + "\n-------------------------------------------------------------------------\n\n";

                    Log.v("getCampaignService", " FROM JSON OBJECT"+txtMsg + "\n\n");

                    // dealing with the transactions Array

                    JSONArray tr = response.getJSONArray("transactions");
                    for (int i=0;i< tr.length(); i++){
                        JSONObject obj = tr.getJSONObject(i);
                    }

                    String txtTrans = "";
                    int intTotalPoints =0;

                    for (int i=0;i< tr.length(); i++){
                        txtTrans=txtTrans+"id: "+tr.getJSONObject(i).getInt("idTransaction")+"\n";
                        txtTrans=txtTrans+"Campaign: "+tr.getJSONObject(i).getString("title")+"\n";
                        txtTrans=txtTrans+"Nature: "+tr.getJSONObject(i).getString("nature")+"\n";
                        txtTrans=txtTrans+"Amount: "+tr.getJSONObject(i).getInt("amount")+"\n\n";
                        intTotalPoints += tr.getJSONObject(i).getInt("amount");
                    }
                    txtMsg = txtMsg+"JSON ARRAY FORMATED TRANSACTIONS RETURNED: \n\n"
                            +txtTrans+"-------------------------------------------------------------------------\n"
                            +"** TRANSACTION COUNT: "+Integer.toString(tr.length())+" transactions"+"\n-------------------------------------------------------------------------\n"
                            +"** TOTAL AMOUNT:      "+Integer.toString(intTotalPoints)+" points"+"\n-------------------------------------------------------------------------\n\n";

                    Log.v("getCampaignService", " DEALING WITH TRANSACTIONS: FROM JSON ARRAY"+txtMsg + "\n\n");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

                    , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null) {
                    Log.v("getCampaignService", "Login has failed!"+"\n\n");
                    // TODO: reset your password, create a new account
                } else {
                    Log.v("getCampaignService","onErrorResponse(): " + error.getMessage());
                }
            }
        });
        queue.add(jsonObjectRequest);   // replace for the correct object name
    }

        //throw new UnsupportedOperationException("Not yet implemented");



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Handle action handleActionGetCampaigns in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGetCampaigns(String email, String password) {
        // TODO: Handle action get Campaigns
        // open a local DB in write mode
            // see SQLlite Ch.11
        // // get WS data
            // use Volley
        // // put WS data into local DB
            // see SQLlite Ch.11
        // close local DB
        //

        // using log to test the service
        // variables used to call intent service
        //Log.v("getCampaignService", "##############################################################################");
        //Log.v("getCampaignService", ("with email:"+email+" and password: "+password));
        //Log.v("getCampaignService", "##############################################################################");

        RequestQueue queue = Volley.newRequestQueue(this);

        final String mEmail = email;
        final String mPassword = password;
        URL urlObj = createURL(email, password, "getCampaigns", "123456789");     //format URL to call WS
        String url = urlObj.toString();




/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// ws using volley with String Object



        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String mResponse = response;
                // using log to test the service
                Log.v("getCampaignService", "##############################################################################");
                Log.v("getCampaignService", ("with email:"+mEmail+" and password: "+mPassword));
                Log.v("getCampaignService", "##############################################################################");
                Log.v("getCampaignService", mResponse);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null) {
                    // use log for errors
                    Log.v("getCampaignService","Login has failed!");
                    // TODO: retrieve your password
                } else {
                    // use log for errors
                    Log.v("getCampaignService",("onErrorResponse(): " + error.getMessage()));
                }
            }
        });
        queue.add(stringRequest);
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// ws using volley with JsonObject

        /*
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String txtMsg = "";
                    int j_sponsorId = response.getInt("idLaboratory");
                    int j_campaignId = response.getInt("idCampaign");
                    int j_sequentialNumber = response.getInt("sequential");
                    String j_sponsorName = response.getString("nameOfLaboratory");
                    String j_campaignStartDate = response.getString("eventDateOfCampaign");
                    int j_numberOfQuestions = response.getInt("questionsOfCampaign");
                    int j_pointsForRightAnswer = response.getInt("pointsForRightAnswer");
                    int j_pointsForParticipation = response.getInt("pointsForParticipation");
                    String j_status = response.getString("status");
                    String j_createdAt = response.getString("createdAt");


                    txtMsg="FROM JSON OBJECT WE HAVE: \n\n"
                            +"Sponsor Id: "+Integer.toString(j_sponsorId)+"\n"
                            +"Campaign Id: "+Integer.toString(j_campaignId)+"\n"
                            +"Sequential Number: "+Integer.toString(j_sequentialNumber)+"\n"
                            +"Sponsor Name: "+j_sponsorName+"\n"
                            +"Start Date: "+j_campaignStartDate+"\n"
                            +"Number of Questions: "+Integer.toString(j_numberOfQuestions)+"\n"
                            +"Points for right answers: "+Integer.toString(j_pointsForRightAnswer)+"\n"
                            +"Points for Participation: "+Integer.toString(j_pointsForParticipation)+"\n"
                            +"Status: "+j_status+"\n"
                            +"Campaign Created At: "+j_createdAt+"\n"
                            +"\n-------------------------------------------------------------------------\n\n";

                    Log.v("getCampaignService", txtMsg+"\n\n");


                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

                    , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null) {
                    Log.v("getCampaignService", "Login has failed!"+"\n\n");
                    // TODO: reset your password, create a new account
                } else {
                    Log.v("getCampaignService","onErrorResponse(): " + error.getMessage());
                }
            }
        });
        queue.add(jsonObjectRequest);   // replace for the correct object name
    }

        //throw new UnsupportedOperationException("Not yet implemented");
*/

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
