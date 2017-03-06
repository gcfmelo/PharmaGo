package br.com.phago.pharmago;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Gustavo on 05/03/2017.
 * *
 * // TIPS to debug SQLite databases ...
 * // adb root         //// to get logger as root in adb
 * // adb -e shell
 * // su /// prompt = '$' if you are not rooted root, if you are rooted your prompt will be '#'
 * //                  run 'su' command to change to #
 * // cd /data/data/br.com.phago.pharmago/databases/
 * // ls -l
 * // rm <filename>   ////  to DELETE a FILE
 * // sqlite3 phago.db
 * // .tables
 * // .schema <tablename>
 * // SELECT * FROM <tablename>;
 * // DROP TABLE <tablename>;
 * // .help
 */


public class PgDatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "PgDatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "phago.db";

    // Common column names (used in more than one table)
    private static final String KEY_ID = "_id";
    private static final String KEY_CREATED_AT = "created_at";

    // Table Names - pg_user
    private static final String TABLE_USER = "pg_user";
    // Table Names - pg_transaction
    private static final String TABLE_TRANSACTION = "pg_transaction";
    // Table Names - pg_quiz
    private static final String TABLE_QUIZ = "pg_quiz";
    // Table Names - pg_sponsor and temp_pg_sponsor
    private static final String TABLE_SPONSOR = "pg_sponsor";
    private static final String TABLE_TEMP_SPONSOR = "temp_pg_sponsor";
    // Table Names - pg_campaign
    private static final String TABLE_CAMPAIGN = "pg_campaign";
    // Table Names - pg_question
    private static final String TABLE_CAMPAIGN_QUESTIONS = "pg_question";
    // Table Names - pg_option
    private static final String TABLE_CAMPAIGN_QUESTIONS_OPTIONS = "pg_option";


    // Table: pg_user - Field Names
    private static final String FIELD_USER_NAME = "name";
    private static final String FIELD_USER_EMAIL = "email";
    private static final String FIELD_USER_CPF = "cpf";
    private static final String FIELD_USER_STATUS = "status";
    private static final String FIELD_USER_COMPANY_CODE = "companyCode";
    private static final String FIELD_USER_COMPANY_NAME = "companyName";
    private static final String FIELD_USER_COMPANY_LATITUDE = "companyLatitude";
    private static final String FIELD_USER_COMPANY_LONGITUDE = "companyLongitude";

    // Table Create Statements  -  pg_user
    private static final String CREATE_TABLE_USER = "CREATE TABLE " +
            TABLE_USER + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
            FIELD_USER_NAME + " TEXT," +
            FIELD_USER_EMAIL + " TEXT, " +
            FIELD_USER_CPF + "TEXT, " +
            FIELD_USER_STATUS + "TEXT, " +
            FIELD_USER_COMPANY_CODE + "TEXT, " +
            FIELD_USER_COMPANY_NAME + "TEXT, " +
            FIELD_USER_COMPANY_LATITUDE + "TEXT, " +
            FIELD_USER_COMPANY_LONGITUDE + "TEXT, " +
            KEY_CREATED_AT + " TEXT" + ");";

    /**
     * {
     * "email":"gcfmelo@gmail.com",
     * "name":"GUSTAVO MELO",
     * "status":"ENABLE",
     * "cpf":"31201032415",
     * "companyCode":"05511842000121",
     * "companyName":"SOLANGE ALVES VICENTE DROGARIA - ME",
     * "companyLatitude":"-22.8185009",
     * "companyLongitude":"-47.0930623"
     * }
     */


    // Table: pg_transaction - Field Names
    private static final String FIELD_TRANSACTION_ID_TRANSACTION = "idTransaction";
    private static final String FIELD_TRANSACTION_ID_CAMPAIGN = "idCampaign";
    private static final String FIELD_TRANSACTION_CAMPAIGN_TITLE = "title";
    private static final String FIELD_TRANSACTION_SPONSOR_CODE = "sponsorCode";
    private static final String FIELD_TRANSACTION_EVENT_DATE = "eventDate";
    private static final String FIELD_TRANSACTION_NATURE = "nature";
    private static final String FIELD_TRANSACTION_AMOUNT = "amount";

    // Table Create Statements  -  pg_transaction
    private static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE " +
            TABLE_TRANSACTION + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
            FIELD_TRANSACTION_ID_TRANSACTION + " TEXT, " +
            FIELD_TRANSACTION_ID_CAMPAIGN + " TEXT, " +
            FIELD_TRANSACTION_CAMPAIGN_TITLE + " TEXT, " +
            FIELD_TRANSACTION_SPONSOR_CODE + " TEXT, " +
            FIELD_TRANSACTION_EVENT_DATE + " TEXT, " +
            FIELD_TRANSACTION_NATURE + " TEXT, " +
            FIELD_TRANSACTION_AMOUNT + " INTEGER, " +
            KEY_CREATED_AT + " TEXT" + ");";

    /**
     [{"idTransaction":1, "eventDate":"2017-02-28", "title":"Campanha Aspirina Verde", "nature":"C", "amount":100 }]
     */

    /**
     * [
     * {
     * "idQuiz":1,
     * "campaign":{
     * "sponsorCode":"05080120000160",
     * "sponsorName":"Laboratório ABCD",
     * "startDate":"2017-02-28",
     * "endDate":"2017-03-31",
     * "numberOfQuestions":2,
     * "pointsForRightAnswer":50,
     * "pointsForParticipation":100,
     * "questions":[
     * {
     * "questionLabel":"Qual a cor da febre que está na moda?",
     * "options":[
     * {
     * "sequential":1,
     * "optionLabel":"azul",
     * "rightAnswer":false
     * },
     * {
     * "sequential":2,
     * "optionLabel":"amarela",
     * "rightAnswer":true
     * },
     * {
     * "sequential":3,
     * "optionLabel":"vermelha",
     * "rightAnswer":false
     * },
     * {
     * "sequential":4,
     * "optionLabel":"branca",
     * "rightAnswer":false
     * }
     * ]
     * },
     * {
     * "questionLabel":"A peste xxxxx matou mais que a febre amarela. Qual a cor de xxxxx?",
     * "options":[
     * {
     * "sequential":1,
     * "optionLabel":"verde",
     * "rightAnswer":false
     * },
     * {
     * "sequential":2,
     * "optionLabel":"lilás",
     * "rightAnswer":false
     * },
     * {
     * "sequential":3,
     * "optionLabel":"marrom",
     * "rightAnswer":false
     * },
     * {
     * "sequential":4,
     * "optionLabel":"negra",
     * "rightAnswer":true
     * },
     * {
     * "sequential":5,
     * "optionLabel":"listrada",
     * "rightAnswer":false
     * }
     * ]
     * }
     * ]
     * },
     * "token":"cb3130e1-8ad4-4b61-a305-65f6d1c53800",
     * "status":"Respondido"
     * },
     * {
     * "idQuiz":2,
     * "campaign":{
     * "sponsorCode":"05080120000160",
     * "sponsorName":"Laboratório ABCD",
     * "startDate":"2017-02-28",
     * "endDate":"2017-03-31",
     * "numberOfQuestions":1,
     * "pointsForRightAnswer":12,
     * "pointsForParticipation":35,
     * "questions":[
     * {
     * "questionLabel":"Como ganhar dinheiro aprendendo sobre o medicamentos?",
     * "options":[
     * {
     * "sequential":1,
     * "optionLabel":"Vendendo borboletas",
     * "rightAnswer":false
     * },
     * {
     * "sequential":2,
     * "optionLabel":"Enxugando gelo",
     * "rightAnswer":false
     * },
     * {
     * "sequential":3,
     * "optionLabel":"Aderindo ao PharmaGo",
     * "rightAnswer":true
     * },
     * {
     * "sequential":4,
     * "optionLabel":"Penteando macaco",
     * "rightAnswer":false
     * }
     * ]
     * }
     * ]
     * },
     * "token":"dc1f749f-db25-4c3f-a21f-76916b1e8f20",
     * "status":"Aguardando Resposta"
     * }
     * ]
     */


    // sponsorId INTEGER, sponsorCode TEXT, sponsorName TEXT

    // Table: pg_sponsor - Field Names
    private static final String FIELD_SPONSOR_ID = "sponsorId";
    private static final String FIELD_SPONSOR_CODE = "sponsorCode";
    private static final String FIELD_SPONSOR_NAME = "sponsorName";

    // Table Create Statements  -  pg_sponsor
    private static final String CREATE_TABLE_SPONSOR = "CREATE TABLE " +
            TABLE_SPONSOR + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
            FIELD_SPONSOR_CODE + " TEXT, " +
            FIELD_SPONSOR_NAME + " TEXT );";

    // Table Create Statements  -  pg_sponsor
    private static final String CREATE_TABLE_TEMP_SPONSOR = "CREATE TABLE " +
            TABLE_TEMP_SPONSOR + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
            FIELD_SPONSOR_CODE + " TEXT, " +
            FIELD_SPONSOR_NAME + " TEXT);";


    // idQuiz* INTEGER, sponsorCode* TEXT, token TEXT, status TEXT
    // Array of 'campaign' objects

    // Table: pg_quiz - Field Names
    private static final String FIELD_QUIZ_ID = "idQuiz";
    private static final String FIELD_QUIZ_SPONSOR_CODE = "sponsorCode";
    private static final String FIELD_QUIZ_TOKEN = "token";
    private static final String FIELD_QUIZ_STATUS = "status";

    // Table Create Statements  -  pg_quiz
    private static final String CREATE_TABLE_QUIZ = "CREATE TABLE " +
            TABLE_QUIZ + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
            FIELD_QUIZ_ID + " INTEGER, " +
            FIELD_QUIZ_SPONSOR_CODE + " TEXT, " +
            FIELD_QUIZ_TOKEN + " TEXT, " +
            FIELD_QUIZ_STATUS + " TEXT, " +
            KEY_CREATED_AT + " TEXT" + ");";


    // idCampaign* INTEGER, sponsorCode* TEXT, idQuiz* INTEGER, campaignName TEXT, startDate DATETIME, endDate DATETIME, numberOfQuestions INTEGER,
    // pointsForRightAnswer INTEGER, pointsForParticipation INTEGER
    // Array of 'questions' objects
    //
    // CREATE TABLE pg_campaign(_id INTEGER PRIMARY KEY, idCampaign TEXT, sponsorCode TEXT, sponsorName TEXT,
    // startDate TEXT, endDate TEXT, numberOfQuestions INTEGER, pointsForRightAnswer INTEGER, pointsForParticipation INTEGER, status TEXT);


    // Table: pg_campaign - Field Names
    private static final String FIELD_CAMPAIGN_ID = "idCampaign";
    private static final String FIELD_CAMPAIGN_SPONSOR_CODE = "sponsorCode";
    private static final String FIELD_CAMPAIGN_SPONSOR_NAME = "sponsorName";
    private static final String FIELD_CAMPAIGN_START_DATE = "startDate";
    private static final String FIELD_CAMPAIGN_END_DATE = "endDate";
    private static final String FIELD_CAMPAIGN_NUMBER_OF_QUESTIONS = "numberOfQuestions";
    private static final String FIELD_CAMPAIGN_POINTS_RIGHT_ANSWER = "pointsForRightAnswer";
    private static final String FIELD_CAMPAIGN_POINTS_PARTICIPATION = "pointsForParticipation";
    private static final String FIELD_CAMPAIGN_STATUS = "status";

    // Table Create Statements  -  pg_campaign

    //CREATE TABLE pg_campaign(_id INTEGER PRIMARY KEY, idCampaign TEXT, sponsorCode TEXT, sponsorName TEXT,
    // startDate TEXT, endDate TEXT,
    // numberOfQuestions INTEGER, pointsForRightAnswer INTEGER, pointsForParticipation INTEGER,
    // status TEXT);


    private static final String CREATE_TABLE_CAMPAIGN = "CREATE TABLE " +
            TABLE_CAMPAIGN + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
            FIELD_CAMPAIGN_ID + " TEXT, " +
            FIELD_CAMPAIGN_SPONSOR_CODE + " TEXT, " +
            FIELD_CAMPAIGN_SPONSOR_NAME + " TEXT, " +
            FIELD_CAMPAIGN_START_DATE + " TEXT, " +
            FIELD_CAMPAIGN_END_DATE + " TEXT, " +
            FIELD_CAMPAIGN_NUMBER_OF_QUESTIONS + " INTEGER, " +
            FIELD_CAMPAIGN_POINTS_RIGHT_ANSWER + " INTEGER, " +
            FIELD_CAMPAIGN_POINTS_PARTICIPATION + " INTEGER, " +
            FIELD_CAMPAIGN_STATUS + " TEXT" + ");";


    // idQuestion* INTEGER, idCampaign* INTEGER, questionLabel TEXT
    // Array of 'options' objects

    // Table: pg_question - Field Names
    private static final String FIELD_CAMPAIGN_QUESTION_ID = "idQuestion";
    private static final String FIELD_CAMPAIGN_QUESTION_CAMPAIGN_ID = "idCampaign";
    private static final String FIELD_CAMPAIGN_QUESTION_LABEL = "questionLabel";

    // Table Create Statements  -  pg_question
    private static final String CREATE_TABLE_CAMPAIGN_QUESTIONS = "CREATE TABLE " +
            TABLE_CAMPAIGN_QUESTIONS + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
            FIELD_CAMPAIGN_QUESTION_ID + " INTEGER, " +
            FIELD_CAMPAIGN_QUESTION_CAMPAIGN_ID + " INTEGER, " +
            FIELD_CAMPAIGN_QUESTION_LABEL + " TEXT, " +
            KEY_CREATED_AT + " TEXT" + ");";


    // idOption* INTEGER, idQuestion* INTEGER, sequential INTEGER, optionLabel TEXT, rightAnswer INTEGER, userAnswer INTEGER

    // Table: pg_option - Field Names
    private static final String FIELD_CAMPAIGN_QUESTION_OPTION_ID = "idOption";
    private static final String FIELD_CAMPAIGN_QUESTION_OPTION_QUESTION_ID = "idQuestion";
    private static final String FIELD_CAMPAIGN_QUESTION_OPTION_SEQUENTIAL = "sequential";
    private static final String FIELD_CAMPAIGN_QUESTION_OPTION_LABEL = "optionLabel";
    private static final String FIELD_CAMPAIGN_QUESTION_OPTION_IS_RIGHT_ANSWER = "rightAnswer";
    private static final String FIELD_CAMPAIGN_QUESTION_OPTION_USER_ANSWER = "userAnswer";

    // Table Create Statements  -  pg_option
    private static final String CREATE_TABLE_CAMPAIGN_QUESTIONS_OPTIONS = "CREATE TABLE " +
            TABLE_CAMPAIGN_QUESTIONS_OPTIONS + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
            FIELD_CAMPAIGN_QUESTION_OPTION_ID + " INTEGER, " +
            FIELD_CAMPAIGN_QUESTION_OPTION_QUESTION_ID + " INTEGER, " +
            FIELD_CAMPAIGN_QUESTION_OPTION_SEQUENTIAL + " INTEGER, " +
            FIELD_CAMPAIGN_QUESTION_OPTION_LABEL + " TEXT, " +
            FIELD_CAMPAIGN_QUESTION_OPTION_IS_RIGHT_ANSWER + " INTEGER, " +
            FIELD_CAMPAIGN_QUESTION_OPTION_USER_ANSWER + " INTEGER, " +
            KEY_CREATED_AT + " TEXT" + ");";



    /* TODO - CRUD (Create, Read, Update and Delete) Operations
        *Update local User data by WS_ACTION = "login"
        *if action 'login' is successfull, then find record with _ID=1
        * if find is not successful then AddUser(...)
        * else, UpdateUser(...)
        * it is not allowed to have more than one user in one device
        * IF WS action 'getQuiz' is successful,
            * DELETE * FROM 'TABLE_QUIZ'
            * DELETE * FROM 'TABLE_SPONSOR'
            * DELETE * FROM 'TABLE_CAMPAIGN'
            * DELETE * FROM 'TABLE_CAMPAIGN_QUESTIONS'
            * DELETE * FROM 'TABLE_CAMPAIGN_QUESTIONS_OPTIONS'
            *
        * and then ADD the returned data from WS
        * TABLE_QUIZ: is at level 1 of returned WS_ACTION = 'getQuiz'
        *
        * Iterate at top level of WS return and INSERT a record for each
        * Update local User data by WS_ACTION = "getQuiz"
        * if getQuiz is successful, DELETE * FROM 'TABLE_QUIZ' and then ADD the returned data from WS
        * AddQuiz(idQuiz, token, status)
        *
        * Iterate by Quiz and find "campaign" ArrayList.
        * In each "campaign", in it's top level object there are information to fill two tables: TABLE_SPONSOR and TABLE_CAMPAIGN
        *
        * TABLE_SPONSOR:
        * AddSponsor(sponsorId INTEGER, sponsorCode TEXT, sponsorName TEXT)
        * TABLE_CAMPAIGN:
        * AddCampaign(sponsorCode, sponsorName,
        * startDate,endDate, numberOfQuestions,
        * pointsForRightAnswer, pointsForParticipation)
        * Iterate inside 'campaign' and find 'question' ArrayList.
        * In each 'question' in it's top level object there are information to fill table: TABLE_CAMPAIGN_QUESTION
        * AddCampaignQuestion(idQuestion, idCampaign**, questionLabel)
        * (**) parent
                * Iterate inside 'question' and find 'options' ArrayList.
                * In each 'options' there are information to fill table: TABLE_CAMPAIGN_QUESTIONS_OPTIONS
                * AddCampaignQuestion (idCampaign**, idQuestion**, ...,optionLabel, rightAnswer, userAnswer=-1)
                * (**) parent
                * We will also need UpdateUserAnswer(userCPF, token, quizId, campaignId, questionId,
                * optionId, optionSequential, userAnswer(=0 for false or =1 for true).
                * IF WS action 'getTransactions' is successful,
                * DELETE * FROM 'TABLE_TRANSACTION'
                * AddTransaction(...)
    */

    // Constructor
    public PgDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_SPONSOR);
        db.execSQL(CREATE_TABLE_QUIZ);
        db.execSQL(CREATE_TABLE_CAMPAIGN);
        db.execSQL(CREATE_TABLE_CAMPAIGN_QUESTIONS);
        db.execSQL(CREATE_TABLE_CAMPAIGN_QUESTIONS_OPTIONS);
        db.execSQL(CREATE_TABLE_TRANSACTION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // oldVersion = 0 to create from zero
        updatePgDatabase(db, 0, DATABASE_VERSION);

    }

    public long createUser(User user) {

        SQLiteDatabase db = this.getWritableDatabase();

// constructor:
// User(String String email, String cpf, String name, String companyCode, String companyName, String companyLatitude, String companyLongitude)

        ContentValues values = new ContentValues();
        values.put(FIELD_USER_EMAIL, user.getEmail());
        values.put(FIELD_USER_CPF, user.getCpf());
        values.put(FIELD_USER_NAME, user.getName());
        values.put(FIELD_USER_COMPANY_CODE, user.getCompanyCode());
        values.put(FIELD_USER_COMPANY_NAME, user.getCompanyName());
        values.put(FIELD_USER_COMPANY_LATITUDE, user.getCompanyLatitude());
        values.put(FIELD_USER_COMPANY_LONGITUDE, user.getCompanyLongitude());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long user_id = db.insert(TABLE_USER, null, values);

        return user_id;
    }

    public long createQuiz(Quiz quiz) {

        SQLiteDatabase db = this.getWritableDatabase();

        // constructor: Quiz(int idQuiz, String sponsorCode, String token, String status, String createdAt)
        ContentValues values = new ContentValues();
        values.put(FIELD_QUIZ_ID, quiz.getIdQuiz());
        values.put(FIELD_QUIZ_SPONSOR_CODE, quiz.getSponsorCode());
        values.put(FIELD_QUIZ_TOKEN, quiz.getToken());
        values.put(FIELD_QUIZ_STATUS, quiz.getStatus());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long quiz_id = db.insert(TABLE_QUIZ, null, values);

        return quiz_id;

    }

    public long createCampaign(Campaign campaign) {

        SQLiteDatabase db = this.getWritableDatabase();

// constructor:
// User(String String email, String cpf, String name, String companyCode, String companyName, String companyLatitude, String companyLongitude)
        ContentValues values = new ContentValues();
        values.put(FIELD_CAMPAIGN_ID, campaign.getIdCampaign());
        values.put(FIELD_CAMPAIGN_SPONSOR_CODE, campaign.getSponsorCode());
        values.put(FIELD_CAMPAIGN_SPONSOR_NAME, campaign.getSponsorName());
        values.put(FIELD_CAMPAIGN_START_DATE, campaign.getStartDate());
        values.put(FIELD_CAMPAIGN_END_DATE, campaign.getEndDate());
        values.put(FIELD_CAMPAIGN_NUMBER_OF_QUESTIONS, campaign.getNumberOfQuestions());
        values.put(FIELD_CAMPAIGN_POINTS_RIGHT_ANSWER, campaign.getPointsForRightAnswer());
        values.put(FIELD_CAMPAIGN_POINTS_PARTICIPATION, campaign.getPointsForParticipation());

        // insert row
        long camp_id = db.insert(TABLE_CAMPAIGN, null, values);

        return camp_id;
    }

    public long createSponsor(Sponsor sponsor) {

        SQLiteDatabase db = this.getWritableDatabase();

        // constructor: Quiz(int idQuiz, String sponsorCode, String token, String status, String createdAt)
        ContentValues values = new ContentValues();
        values.put(FIELD_SPONSOR_CODE, sponsor.getSponsorCode());
        values.put(FIELD_SPONSOR_NAME, sponsor.getSponsorName());

        // insert row
        String TempTableSponsor = "temp_"+TABLE_SPONSOR;
        long sponsor_id = db.insert(TempTableSponsor, null, values);

        return sponsor_id;

    }

    public Sponsor getSponsorByCode(String sponsorCode){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT sponsorName from " + TABLE_SPONSOR +" WHERE sponsorCode = '"+ sponsorCode + "';";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Sponsor sp = new Sponsor();
        sp.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        sp.setSponsorCode(c.getString(c.getColumnIndex("sponsorCode")));
        sp.setSponsorName(c.getString(c.getColumnIndex("sponsorName")));
        return sp;
    }

    /*
 * getting all Sponsors
 * */
    public List<Sponsor> getAllSponsors() {
        List<Sponsor> sponsors = new ArrayList<Sponsor>();
        String selectQuery = "SELECT  * FROM " + TABLE_SPONSOR;

        //Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Sponsor sp = new Sponsor();
                sp.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                sp.setSponsorCode(c.getString(c.getColumnIndex("sponsorCode")));
                sp.setSponsorName(c.getString(c.getColumnIndex("sponsorName")));

                // adding to todo list
                sponsors.add(sp);
            } while (c.moveToNext());
        }

        return sponsors;
    }

    /**
     * get datetime
     */

    public void dropTable(String TableName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        //closeDB();
    }

    public void createTableSponsor(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_TABLE_SPONSOR);
        //closeDB();
    }

    public void createTableCampaign(){
        SQLiteDatabase db = this.getWritableDatabase();
        dropTable(TABLE_CAMPAIGN);
        db.execSQL(CREATE_TABLE_CAMPAIGN);
        //closeDB();
    }

    public void cleanTableSponsor(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE * FROM "+TABLE_SPONSOR);
        //closeDB();
    }

    public void insertTempDataIntoTableSponsor(){
        SQLiteDatabase db = this.getWritableDatabase();
        String myQuery = "INSERT INTO "+ TABLE_SPONSOR + " (sponsorCode, sponsorName) SELECT DISTINCT sponsorCode,sponsorName FROM "+TABLE_TEMP_SPONSOR;
        db.execSQL(myQuery);
        //closeDB();
    }

    public void createTableTempSponsor(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_TABLE_TEMP_SPONSOR);
        //closeDB();
    }

    private void updatePgDatabase(SQLiteDatabase db, int oldVersion, int newVersion){

        if (oldVersion<1) {
            // handle each upgrade here
            // suppose you added a new column in version 1

            // on upgrade drop older tables
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPONSOR);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAMPAIGN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAMPAIGN_QUESTIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAMPAIGN_QUESTIONS_OPTIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);

            // create new tables
            onCreate(db);
        }
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

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}
