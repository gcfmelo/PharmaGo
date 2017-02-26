package br.com.phago.pharmago;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class CampaignsMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaigns_menu);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView,
                                    View itemView,
                                    int position,
                                    long id) {
                if (position == 0) {
                    //TODO
                    // Intent intent = new Intent(CampaignsMenuActivity.this, OpenCampaignsActivity.class);
                    //startActivity(intent);
                    Toast.makeText(CampaignsMenuActivity.this, "You have selected \"Open Opportunities\"", Toast.LENGTH_SHORT).show();
                }
                if (position == 1) {
                    //TODO
                    // Intent intent = new Intent(CampaignsMenuActivity.this, ClosedCampaignsActivity.class);
                    // TODO Append Campaign ID to the intent
                    //startActivity(intent);
                    Toast.makeText(CampaignsMenuActivity.this, "You have selected \"Closed/Expired\"", Toast.LENGTH_SHORT).show();
                }
                if (position == 2) {
                    //TODO
                    // Intent intent = new Intent(CampaignsMenuActivity.this, StatisticsCampaignsActivity.class);
                    //startActivity(intent);
                    Toast.makeText(CampaignsMenuActivity.this, "You have selected \"Statistics\"", Toast.LENGTH_SHORT).show();
                }

            }
        };
        ListView listView = (ListView) findViewById(R.id.list_options);
        listView.setOnItemClickListener(itemClickListener);
    }
}

