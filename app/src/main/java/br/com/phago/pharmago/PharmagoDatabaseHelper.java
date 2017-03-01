package br.com.phago.pharmago;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Gustavo on 26/02/2017.
 */

public class PharmagoDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "pharmago.db";  // our database name
    private static final int DB_VERSION = 1; // this is the current version of our database
    // TABLE NAMES
    private static final String TABLE_USER = "user";
    /**
    {
        "email":"gcfmelo@gmail.com",
        "name":"GUSTAVO MELO",
        "status":"ENABLE",
        "cpf":"31201032415",
        "companyCode":"05511842000121",
        "companyName":"SOLANGE ALVES VICENTE DROGARIA - ME",
        "companyLatitude":"-22.8185009",
        "companyLongitude":"-47.0930623"
    }
     */
    private static final String TABLE_TRANSACTION = "transaction";
    /**
        [
            {
            " idTransaction":1,
            "eventDate":"2017-02-28",
            "title":"Campanha Aspirina Verde",
            "nature":"C",
            "amount":100
            }
        ]
     */
    private static final String TABLE_QUIZ = "quiz";
    /**
    [
   {
      "idQuiz":1,
      "campaign":{
         "sponsorCode":"05080120000160",
         "sponsorName":"Laboratório ABCD",
         "startDate":"2017-02-28",
         "endDate":"2017-03-31",
         "numberOfQuestions":2,
         "pointsForRightAnswer":50,
         "pointsForParticipation":100,
         "questions":[
            {
               "questionLabel":"Qual a cor da febre que está na moda?",
               "options":[
                  {
                     "sequential":1,
                     "optionLabel":"azul",
                     "rightAnswer":false
                  },
                  {
                     "sequential":2,
                     "optionLabel":"amarela",
                     "rightAnswer":true
                  },
                  {
                     "sequential":3,
                     "optionLabel":"vermelha",
                     "rightAnswer":false
                  },
                  {
                     "sequential":4,
                     "optionLabel":"branca",
                     "rightAnswer":false
                  }
               ]
            },
            {
               "questionLabel":"A peste xxxxx matou mais que a febre amarela. Qual a cor de xxxxx?",
               "options":[
                  {
                     "sequential":1,
                     "optionLabel":"verde",
                     "rightAnswer":false
                  },
                  {
                     "sequential":2,
                     "optionLabel":"lilás",
                     "rightAnswer":false
                  },
                  {
                     "sequential":3,
                     "optionLabel":"marrom",
                     "rightAnswer":false
                  },
                  {
                     "sequential":4,
                     "optionLabel":"negra",
                     "rightAnswer":true
                  },
                  {
                     "sequential":5,
                     "optionLabel":"listrada",
                     "rightAnswer":false
                  }
               ]
            }
         ]
      },
      "token":"cb3130e1-8ad4-4b61-a305-65f6d1c53800",
      "status":"Respondido"
   },
   {
      "idQuiz":2,
      "campaign":{
         "sponsorCode":"05080120000160",
         "sponsorName":"Laboratório ABCD",
         "startDate":"2017-02-28",
         "endDate":"2017-03-31",
         "numberOfQuestions":1,
         "pointsForRightAnswer":12,
         "pointsForParticipation":35,
         "questions":[
            {
               "questionLabel":"Como ganhar dinheiro aprendendo sobre o medicamentos?",
               "options":[
                  {
                     "sequential":1,
                     "optionLabel":"Vendendo borboletas",
                     "rightAnswer":false
                  },
                  {
                     "sequential":2,
                     "optionLabel":"Enxugando gelo",
                     "rightAnswer":false
                  },
                  {
                     "sequential":3,
                     "optionLabel":"Aderindo ao PharmaGo",
                     "rightAnswer":true
                  },
                  {
                     "sequential":4,
                     "optionLabel":"Penteando macaco",
                     "rightAnswer":false
                  }
               ]
            }
         ]
      },
      "token":"dc1f749f-db25-4c3f-a21f-76916b1e8f20",
      "status":"Aguardando Resposta"
   }
]
     */

    private static final String QUIZ_CAMPAIGN = "quiz_campaign";
    private static final String QUIZ_CAMPAIGN_QUESTION = "quiz_campaign_question";
    private static final String QUIZ_CAMPAIGN_QUESTION_OPTION = "quiz_campaign_question_option";

    private static final String FIELD_USER_EMAIL = "email";
    private static final String FIELD_USER_CPF = "cpf";
    private static final String FIELD_USER_NAME = "name";

    private static final String FIELD_TRANSACTION_ID = "idTransaction";
    private static final String FIELD_TRANSACTION_EVENT_DATE = "eventDate";
    private static final String FIELD_TRANSACTION_TITLE = "title";
    private static final String FIELD_TRANSACTION_NATURE = "nature";
    private static final String FIELD_TRANSACTION_AMOUNT = "amount";


    // to be defined

    // constructor
    public PharmagoDatabaseHelper(Context context){
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


    // methods for USER

    //The following methods are responsible for creating, updating, and deleting the records:

    public void recreateUserTable(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_USER + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "email TEXT, " +
                        "name TEXT, " +
                        "status TEXT, " +
                        "cpf TEXT, "+
                        "companyCode TEXT, " +
                        "companyName TEXT, " +
                        "companyLatitude TEXT, " +
                        "companyLongitude TEXT); ");
    }

    public void saveUserRecord(String email, String name, String status, String cpf,
                               String companyCode , String companyName, String companyLatitude,
                               String companyLongitude){

        long id = findUserId(email);
        if (id>0) {
            updateUserRecord(id, email, name, status, cpf, companyCode,
                    companyName, companyLatitude, companyLongitude);

        } else {
            addUserRecord(email, name, status, cpf, companyCode , companyName, companyLatitude, companyLongitude);
        }
    }

    public long addUserRecord(String email, String name, String status, String cpf,
                              String companyCode , String companyName, String companyLatitude,
                              String companyLongitude){

        SQLiteDatabase db = getWritableDatabase();

        // to assure we have a unique user on the table we will drop and recreate TABLE_USER
        recreateUserTable(db);

        ContentValues values = new ContentValues();

        values.put("email", email);
        values.put("name", name);
        values.put("status", status);
        values.put("cpf", cpf);
        values.put("companyCode", companyCode);
        values.put("companyName", companyName);
        values.put("companyLatitude", companyLatitude);
        values.put("companyLongitude", companyLongitude);

        long retVal = db.insert(TABLE_USER, null, values);

        db.close();
        return retVal;
    }

    public int updateUserRecord(long id, String email, String name, String status, String cpf,
                                String companyCode , String companyName, String companyLatitude,
                                String companyLongitude) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("_id", id);
        values.put(email, email);
        values.put(name, name);
        values.put(status, status);
        values.put(cpf, cpf);
        values.put(companyCode, companyCode);
        values.put(companyName, companyName);
        values.put(companyLatitude, companyLatitude);
        values.put(companyLongitude, companyLongitude);

        return db.update(TABLE_USER, values, "_id = ?", new String[]{String.valueOf(id)});
    }

    public int deleteUserRecord(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_USER, "_id = ?", new String[]{String.valueOf(id)});
    }

    // And these methods handle reading the information from the database:

    public long findUserId(String email) {
        long returnVal = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id FROM " + TABLE_USER + " WHERE " + FIELD_USER_EMAIL + " = ?", new String[]{email});
        Log.i("findUserID","getCount()="+cursor.getCount());
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            returnVal = cursor.getInt(0);
        }
        return returnVal;
    }

    public String getUserCpf(long id) {
        String returnVal = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+FIELD_USER_CPF+" FROM " + TABLE_USER + " WHERE _id = ?", new String[]{String.valueOf(id)});
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            returnVal = cursor.getString(0);
        }
        return returnVal;
    }

    public String getUserName(long id) {
        String returnVal = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+FIELD_USER_NAME+" FROM " + TABLE_USER + " WHERE _id = ?", new String[]{String.valueOf(id)});
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            returnVal = cursor.getString(0);
        }
        return returnVal;
    }

    public Cursor getUserList() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT _id, " + FIELD_USER_CPF + " FROM " + TABLE_USER + " ORDER BY " + FIELD_USER_CPF + " ASC";
        return db.rawQuery(query, null);
    }

    public int getUserCount() {
        long returnVal=0;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT _id, " + FIELD_USER_CPF + " FROM " + TABLE_USER + " ORDER BY " + FIELD_USER_CPF + " ASC";
        Cursor cursor = db.rawQuery(query, null);
        return (int) cursor.getCount();
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////    methods for TRANSACTION
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //The following methods are responsible for creating, updating, and deleting the records:

    public void recreateTransactionTable(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS "+TABLE_TRANSACTION+" (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "email TEXT, " +
                        "name TEXT, " +
                        "status TEXT, " +
                        "cpf TEXT, "+
                        "companyCode TEXT, " +
                        "companyName TEXT, " +
                        "companyLatitude TEXT, " +
                        "companyLongitude TEXT); ");
    }

    public void saveTransactionRecord(int idTransaction, String eventDate, String title,
                                String nature, int amount){

        long id = findTransactionId(idTransaction);
        if (id>0) {
            updateTransactionRecord(id, idTransaction, eventDate, title,
                    nature, amount);

        } else {
            addTransactionRecord(idTransaction, eventDate, title,
                    nature, amount);
        }
    }

    public long addTransactionRecord(int idTransaction, String eventDate, String title,
                                     String nature, int amount){

        SQLiteDatabase db = getWritableDatabase();

        // to assure we have a unique user on the table we will drop and recreate TABLE_USER
        recreateTransactionTable(db);

        ContentValues values = new ContentValues();

        values.put("idTransaction", idTransaction);
        values.put("eventDate", eventDate);
        values.put("title", title);
        values.put("nature", nature);
        values.put("amount", amount);

        long retVal = db.insert(TABLE_TRANSACTION, null, values);

        db.close();
        return retVal;
    }

    public int updateTransactionRecord(long id, int idTransaction, String eventDate, String title,
                                       String nature, int amount) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("_id", id);
        values.put("idTransaction", idTransaction);
        values.put("eventDate", eventDate);
        values.put("title", title);
        values.put("nature", nature);
        values.put("amount", amount);

        return db.update(TABLE_TRANSACTION, values, "_id = ?", new String[]{String.valueOf(id)});
    }

    public int deleteTransactionRecord(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_TRANSACTION, "_id = ?", new String[]{String.valueOf(id)});
    }






    // And these methods handle reading the information from the database:

    public long findTransactionId(int idTransaction) {
        long returnVal = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id FROM " + TABLE_TRANSACTION + " WHERE " + FIELD_TRANSACTION_ID + " = ?", new String[]{String.valueOf(idTransaction)});
        Log.i(FIELD_TRANSACTION_ID,"getCount()="+cursor.getCount());
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            returnVal = cursor.getInt(0);
        }
        return returnVal;
    }

    public String getTransactionEventDate(int idTransaction) {
        String returnVal = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+FIELD_TRANSACTION_EVENT_DATE+" FROM " + TABLE_TRANSACTION + " WHERE "+ FIELD_TRANSACTION_ID + " = ?", new String[]{String.valueOf(idTransaction)});
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            returnVal = cursor.getString(0);
        }
        return returnVal;
    }

    public String getTransactionTitle(int idTransaction) {
        String returnVal = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+FIELD_TRANSACTION_TITLE+" FROM " + TABLE_TRANSACTION + " WHERE "+ FIELD_TRANSACTION_ID + " = ?", new String[]{String.valueOf(idTransaction)});
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            returnVal = cursor.getString(0);
        }
        return returnVal;
    }

    public String getTransactionNature(int idTransaction) {
        String returnVal = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+FIELD_TRANSACTION_NATURE+" FROM " + TABLE_TRANSACTION + " WHERE "+ FIELD_TRANSACTION_ID + " = ?", new String[]{String.valueOf(idTransaction)});
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            returnVal = cursor.getString(0);
        }
        return returnVal;
    }

    public int getTransactionAmount(int idTransaction) {
        int returnVal = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+FIELD_TRANSACTION_AMOUNT+" FROM " + TABLE_TRANSACTION + " WHERE "+ FIELD_TRANSACTION_ID + " = ?", new String[]{String.valueOf(idTransaction)});
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            returnVal = Integer.parseInt(cursor.getString(0));
        }
        return returnVal;
    }


    public Cursor getTransactionList() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT _id, " + FIELD_TRANSACTION_ID + " FROM " + TABLE_TRANSACTION + " ORDER BY " + FIELD_TRANSACTION_ID + " ASC";
        return db.rawQuery(query, null);
    }

    public int getTransactionCount() {
        long returnVal=0;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT _id, " + FIELD_TRANSACTION_ID + " FROM " + TABLE_TRANSACTION + " ORDER BY " + FIELD_TRANSACTION_ID + " ASC";
        Cursor cursor = db.rawQuery(query, null);
        return (int) cursor.getCount();
    }




///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////    methods for QUIZes
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////






///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////    methods for ALL TABLES
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void updatePharmagoDatabase(SQLiteDatabase db, int oldVersion, int newVersion){

        if (oldVersion<1){

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS USER (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "email TEXT, " +
                            "name TEXT, " +
                            "status TEXT, " +
                            "cpf TEXT, "+
                            "companyCode TEXT, " +
                            "companyName TEXT, " +
                            "companyLatitude TEXT, " +
                            "companyLongitude TEXT); ");
            /**
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS TRANSACTION (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "idTransaction INTEGER, " +
                        "eventDate TEXT, " + "title TEXT, "+
                        "nature TEXT, " +
                        "amount INTEGER); ");

            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS QUIZ (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                            "idQuiz INTEGER, "+
                            "idCampaign INTEGER, "+
                            "token TEXT, "+
                            "status TEXT);");

            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS QUIZ_CAMPAIGN (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                            "idQuiz INTEGER, "+
                            "idCampaign INTEGER, "+
                            "token TEXT, "+
                            "sponsorCode TEXT, "+
                            "sponsorName TEXT, "+
                            "startDate TEXT, "+
                            "startDate TEXT, "+
                            "pointsForRightAnswer INTEGER, "+
                            "numberOfQuestions INTEGER, "+
                            "pointsForParticipation INTEGER);");
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS QUIZ_CAMPAIGN_QUESTION (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                            "idQuestion INTEGER, "+
                            "idQuiz INTEGER, "+
                            "idCampaign INTEGER, "+
                            "token TEXT, "+
                            "questionLabel TEXT);");

            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS QUIZ_CAMPAIGN_QUESTION_OPTION (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                            "sequential INTEGER, "+
                            "idQuestion INTEGER, "+
                            "idQuiz INTEGER, "+
                            "idCampaign INTEGER, "+
                            "token TEXT, "+
                            "optionLabel TEXT, "+
                            "userAnswer TEXT, "+
                            "rightAnswer TEXT);");
            */

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
