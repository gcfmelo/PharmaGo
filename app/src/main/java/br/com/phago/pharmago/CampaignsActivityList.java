package br.com.phago.pharmago;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class CampaignsActivityList extends Activity {

    public static final String EXTRA_CAMPAIGN_ID = "idCampaign";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaigns_list);

        // get the activity from the intent

        int idCampaign = (Integer) getIntent().getExtras().get(EXTRA_CAMPAIGN_ID);

        // create a cursor

        try {
            SQLiteOpenHelper pharmagoDatabaseHelper = new PharmagoDatabaseHelper(this);
            SQLiteDatabase db = pharmagoDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("CAMPAIGN",
                    new String []{ "_id", "idCampaign","sponsorName", "createdAt", "status"},
                    "_id = ?",
                    new String []{Integer.toString(idCampaign)},
                    null, null, null);

            // Move to the first record in the Cursor
            if (cursor.moveToFirst()){
                // Get the Campaign Detail
                //int myCampaignId = cursor.getInt(0);
                int myCampaignExternalId = cursor.getInt(1);
                String mySponsorName = cursor.getString(2);
                String mCreatedAt = cursor.getString(3);
                String myStatus = cursor.getString(4);

                // TODO adjust the Campaign view form. Create the TextViews with the @id s bellow

                // Populate the Campaign List
                TextView camp = (TextView) findViewById(R.id.campaign_id);
                camp.setText(Integer.toString(myCampaignExternalId));
                TextView sponsor = (TextView) findViewById(R.id.campaign_sponsor_name);
                sponsor.setText(mySponsorName);
                TextView datecreated = (TextView) findViewById(R.id.campaign_date_created);
                sponsor.setText(mySponsorName);
                TextView status = (TextView) findViewById(R.id.campaign_status);
                status.setText(myStatus);

            }
            cursor.close();
            db.close();
        } catch(SQLiteException e){
            Toast.makeText(this, "Database is unavailable!", Toast.LENGTH_LONG).show();
        }

    }
}
