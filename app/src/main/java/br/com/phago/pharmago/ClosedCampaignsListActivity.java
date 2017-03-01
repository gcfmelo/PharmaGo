package br.com.phago.pharmago;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ClosedCampaignsListActivity extends ListActivity{
    private SQLiteDatabase db;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listCampaigns = getListView();

        try {
            SQLiteOpenHelper pharmagoDatabaseHelper =new PharmagoDatabaseHelper(this);
            db = pharmagoDatabaseHelper.getReadableDatabase();
            cursor=db.query("CAMPAIGN", new String[]{"_id", "idCampaign","sponsorName", "createdAt", "status"},
                    null,null,null,null,null);
            CursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1, cursor, new String[]{"idCampaign"}, new int[]{android.R.id.text1},0);
        } catch
            (SQLiteException e){
            Toast.makeText(this, "Database Unavailable", Toast.LENGTH_LONG).show();
        }
    }

    private void createCampaingListOnDb(){
        // put some data into CAMPAIGN table
        try {
            SQLiteOpenHelper pharmagoDatabaseHelper =new PharmagoDatabaseHelper(this);
            db = pharmagoDatabaseHelper.getWritableDatabase();
            String mySql = "INSERT OR REPLACE INTO CAMPAIGN [(idSponsor, idCampaign, sequential, sponsorName, "+
                    "eventDate, numberOfQuestions, pointsRightAnswer, pointsParticipation, "+
                    "status, createdAt )] VALUES (1, 1, 1, 'Laboratorio A', '2017-02-20', 2, 50, 100, 'ACTIVE', '2017-02-15 15:54:15.22313' )";

            db.execSQL(mySql);

        } catch
                (SQLiteException e){
            Toast.makeText(this, "Database Unavailable", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onListItemClick(ListView listview,
                                View itemView,
                                int position,
                                long id){
        Intent intent = new Intent(ClosedCampaignsListActivity.this, CampaignsActivityList.class);
        intent.putExtra(CampaignsActivityList.EXTRA_CAMPAIGN_ID, (int) id);
        startActivity(intent);
    }
}