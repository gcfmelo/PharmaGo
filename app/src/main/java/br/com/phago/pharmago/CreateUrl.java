package br.com.phago.pharmago;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Gustavo on 24/02/2017.
 */

public class CreateUrl {

    private URL uurl;
    private String url;
    private String baseUrl="";
    private String email;
    private String password;
    private String action;
    private String api_token;

    // constructor with 5 String parameters
    public CreateUrl(String baseUrl, String email, String password, String action, String api_token) throws MalformedURLException {
        this.baseUrl = baseUrl;
        this.email = email;
        this.password = password;
        this.action = action;
        this.api_token = api_token;
        this.uurl = new URL(baseUrl + "email=" + email + "&password=" + password + "&action=" + action + "&token=" + api_token);
        this.url = uurl.toString();
    }

    // constructor with 3 String parameters
    private CreateUrl(String email, String password, String action) throws MalformedURLException {
        this.email = email;
        this.password = password;
        this.action = action;
        baseUrl="http://www.benben.net.br/apiPharmaGo?";
        api_token="123456789";
        uurl = new URL(baseUrl + "email=" + email + "&password=" + password + "&action=" + action + "&token=" + api_token);
        url = uurl.toString();
    }

    public URL getUrlObject() {
        // retuns an URL object
        return uurl;
    }
    public String getUrl() {
        // retuns an URL object
        return url;
    }

    @Override
    public String toString() {
        // retuns an URL as a String object
        return url.toString();
    }
};


