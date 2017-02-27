package br.com.phago.pharmago;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gustavo on 26/02/2017.
 */

public class PharmagoDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "pharmago";  // our database name
    private static final int DB_VERSION =1; // this is the current version of our database

    PharmagoDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        // oldVersion = 0 to create from zero
        updatePharmagoDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // see code to Upgrade
        updatePharmagoDatabase(db, oldVersion, newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // see code to Downgrade
        updatePharmagoDatabase(db, oldVersion, newVersion);
    }

    private static void insertUser( SQLiteDatabase db, String email, String name,
                                        String status, String companyCode, String companyName,
                                        String companyLatitude,String companyLongitude){

        ContentValues userData = new ContentValues();

        userData.put("email", email);
        userData.put("name", name);
        userData.put("status", status);
        userData.put("companyCode", companyCode);
        userData.put("companyName", companyName);
        userData.put("companyLatitude", companyLatitude);
        userData.put("companyLongitude", companyLongitude);

        db.insert("USER",null,userData);
    }


    private static void insertTransaction( SQLiteDatabase db,
                                           int idTransaction,
                                           String relatedCampaignName,
                                           String nature,
                                           int amount){

        ContentValues transactionData = new ContentValues();

        transactionData.put("idTransaction", idTransaction);
        transactionData.put("relatedCampaignName", relatedCampaignName);
        transactionData.put("nature", nature);
        transactionData.put("amount", amount);

        db.insert("TRANSACTION",null,transactionData);
    }

    private static void insertCampaign( SQLiteDatabase db, int idSponsor, int idCampaign,
                                        int sequential, String sponsorName, String eventDate,
                                        int numberOfQuestions,int pointsRightAnswer,
                                        int pointsParticipation, String status, String createdAt){

        ContentValues campaignData = new ContentValues();

        campaignData.put("idSponsor", idSponsor);
        campaignData.put("idCampaign", idCampaign);
        campaignData.put("sequential", sequential);
        campaignData.put("sponsorName", sponsorName);
        campaignData.put("eventDate", eventDate);
        campaignData.put("numberOfQuestions", numberOfQuestions);
        campaignData.put("pointsRightAnswer", pointsRightAnswer);
        campaignData.put("pointsParticipation", pointsParticipation);
        campaignData.put("status", status);
        campaignData.put("createdAt", createdAt);

        db.insert("CAMPAIGN",null,campaignData);
    }

    private void updatePharmagoDatabase(SQLiteDatabase db, int oldVersion, int newVersion){

        if (oldVersion<1){
            db.execSQL(
                    "CREATE TABLE USER (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "email TEXT," +
                            "name TEXT," +
                            "status TEXT," +
                            "companyCode TEXT," +
                            "companyName TEXT," +
                            "companyLatitude TEXT," +
                            "companyLongitude TEXT);");

            db.execSQL(
                    "CREATE TABLE TRANSACTION (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "idTransaction INTEGER," +
                    "relatedCampaignName TEXT," +
                    "nature TEXT," +
                    "amount INTEGER);");

            db.execSQL(
                    "CREATE TABLE CAMPAIGN (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                            "idSponsor INTEGER,"+
                            "idCampaign INTEGER,"+
                            "sequential INTEGER,"+
                            "sponsorName TEXT,"+
                            "eventDate TEXT,"+
                            "numberOfQuestions INTEGER," +
                            "pointsRightAnswer INTEGER," +
                            "pointsParticipation INTEGER," +
                            "statusTEXT," +
                            "createdAt TEXT);");
        }
        // handle each upgrade here
        // suppose you added a new column in version 1
        if (oldVersion<2){
            // this code will run if the user already has the version
            // so insert here the code to add the extra column
            // example: db.execSQL("ALTER TABLE CAMPAIGN ADD COLUMN EXPIRED INT;");
        }

        // downgrade:
        if (oldVersion==3){
            // run code to existing version 3 database...
        }
        if (oldVersion < 6) {
            // run code...
        }
    }
}
