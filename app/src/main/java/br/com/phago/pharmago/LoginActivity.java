package br.com.phago.pharmago;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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


public class LoginActivity extends AppCompatActivity {
    private static String rqType = "JSON";   // JSON or TXT
    private static boolean rqDebug = true;
    static final String KEY_COUNTER = "COUNTER";
    private int mCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences settings = getPreferences(MODE_PRIVATE);

        int defaultCounter = 0;
        mCounter = settings.getInt(KEY_COUNTER, defaultCounter);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_COUNTER,mCounter);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCounter=savedInstanceState.getInt(KEY_COUNTER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // persist data locally
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(KEY_COUNTER, mCounter);
        editor.commit();
    }

    public void sendRequest(View view) {
        final TextView textViewResult = (TextView) findViewById(R.id.textViewResult);
        EditText emailEditText = (EditText) findViewById(R.id.editTextEmail);
        final EditText passwordEditText = (EditText) findViewById(R.id.editTextPassword);

        mCounter++;

        Toast.makeText(this, "Count: "+ Integer.toString(mCounter)+" clicks", Toast.LENGTH_SHORT).show();
        //
        RequestQueue queue = Volley.newRequestQueue(this);

        URL urlObj = createURL(emailEditText.getText().toString(), passwordEditText.getText().toString(), "login", getString(R.string.api_token));     //format URL to call WS

        String url = urlObj.toString();

        //String url ="https://www.packtpub.com/";
        if (rqType == "TXT" || rqType == "TEXT") {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    textViewResult.setText("TEXT RETURNED: \n\n"+response.substring(0, 500));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error.getMessage() == null) {
                        textViewResult.setText("Login has failed!");
                        // TODO: retrieve your password
                    } else {
                        textViewResult.setText("onErrorResponse(): " + error.getMessage());
                    }
                }
            });
            queue.add(stringRequest);
        } else {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
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

                        txtMsg="FROM JSON OBJECT WE HAVE: \n\n"
                                +"Name: "+j_name+"\n"
                                +"Status: "+j_status+"\n"
                                +"CNPJ: "+j_companyCode+"\n"
                                +"Company: "+j_companyName+"\n"
                                +"Latitude: "+j_companyLatitude+"\n"
                                +"Longitude: "+j_companyLongitude+"\n"
                                +"\n-------------------------------------------------------------------------\n"
                                +"JSON OBJECT.toString() RETURNED:"
                                +"\n-------------------------------------------------------------------------\n"
                                +response.toString()
                                +"\n-------------------------------------------------------------------------\n\n";

                        textViewResult.setText(txtMsg);

                        // converting Transactions to a List

                        JSONArray tr = response.getJSONArray("transactions");
                        for (int i=0;i< tr.length(); i++){
                            JSONObject obj = tr.getJSONObject(i);
                        }
                        //txtMsg = txtMsg+"JSON ARRAY TRANSACTIONS RETURNED: \n\n"+tr.toString()+"\n-------------------------------------------------------------------------\n\n";
                        //textViewResult.setText(txtMsg);
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

                        textViewResult.setText(txtMsg);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error.getMessage() == null) {
                        textViewResult.setText("Login has failed!");
                        // TODO: reset your password, create a new account
                    } else {
                        textViewResult.setText("onErrorResponse(): " + error.getMessage());
                    }
                }
            });
            queue.add(jsonObjectRequest);
        }
        //   you can also return a JsonArrayRequest.

    }


    private URL createURL(String email, String password, String action, String token) {
        String baseURL = LoginActivity.super.getString(R.string.web_sevice_base_url);
        try {
            // create URL for specified email and password
            String baseUrl = LoginActivity.super.getString(R.string.web_sevice_base_url);
            String urlString = baseUrl + "email=" + email + "&password=" + password + "&action=" + action + "&token=" + token;
            // URLEncoder.encode(city, "UTF-8") use caso haja espaços ou outros caracteres especiais em uma string da consulta...

            return new URL(urlString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
