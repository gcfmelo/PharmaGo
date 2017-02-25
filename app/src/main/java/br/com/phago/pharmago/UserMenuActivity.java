package br.com.phago.pharmago;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class UserMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView,
                                    View itemView,
                                    int position,
                                    long id) {
                if (position == 0) {
                    //TODO
                    Toast.makeText(UserMenuActivity.this, "You have selected \"Login\"", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserMenuActivity.this, LoginActivity.class);
                    startActivity(intent);

                }
                if (position == 1) {
                    //TODO
                    // Intent intent = new Intent(CampaignsMenuActivity.this, ClosedCampaignsActivity.class);
                    //startActivity(intent);
                    Toast.makeText(UserMenuActivity.this, "You have selected \"Settings\"", Toast.LENGTH_SHORT).show();
                }
                if (position == 2) {
                    //TODO
                    // Intent intent = new Intent(CampaignsMenuActivity.this, StatisticsCampaignsActivity.class);
                    //startActivity(intent);
                    Toast.makeText(UserMenuActivity.this, "You have selected \"Profile\"", Toast.LENGTH_SHORT).show();
                }

            }
        };
        ListView listView = (ListView) findViewById(R.id.list_options);
        listView.setOnItemClickListener(itemClickListener);
    }
}