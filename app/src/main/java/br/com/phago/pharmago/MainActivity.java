package br.com.phago.pharmago;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_COUNTER = "COUNTER";
    // global variables to hold user data
    public static String user_id, user_name, user_email;
    public static int mCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView,
                                    View itemView,
                                    int position,
                                    long id) {
                if (position == 0) {

                    // TODO do the "UserMenuActivity" need to handle this Intent in some way?
                    Toast.makeText(MainActivity.this, "You have selected \"User\"", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, UserMenuActivity.class);
                    startActivity(intent);

                }
                if (position == 1) {
                    // TODO do the "AccountMenuActivity" need to handle this Intent in some way?
                    // Intent intent = new Intent(MainActivity.this, AccountMenuActivity.class);
                    //startActivity(intent);
                    Toast.makeText(MainActivity.this, "You have selected \"Account\"", Toast.LENGTH_SHORT).show();
                }
                if (position == 2) {
                    // TODO do the "CampaignsMenuActivity" need to handle this Intent in some way?


                    Toast.makeText(MainActivity.this, "You have selected \"Campaigns\"", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, CampaignsMenuActivity.class);
                    startActivity(intent);
                }
                if (position == 3) {
                    // TODO do the "AboutMenuActivity" need to handle this Intent in some way?
                    //Intent intent = new Intent(MainActivity.this, AboutMenuActivity.class);
                    //startActivity(intent);
                    Toast.makeText(MainActivity.this, "You have selected \"About\"", Toast.LENGTH_SHORT).show();
                }
            }
        };
        ListView listView = (ListView) findViewById(R.id.list_options);
        listView.setOnItemClickListener(itemClickListener);
    }

    public void onClickStartServices(View view){

        Intent intent = new Intent(this, getWsData.class);
        intent.setAction(getWsData.ACTION_GET_LOGIN_DATA);
        intent.putExtra(getWsData.EXTRA_EMAIL, "gcfmelo@gmail.com");
        intent.putExtra(getWsData.EXTRA_PASSWORD, "abc123");
        startService(intent);

/*
        Intent intent2 = new Intent(this, getWsData.class);
        intent2.setAction(getWsData.ACTION_GET_TRANSACTIONS_DATA);
        intent2.putExtra(getWsData.EXTRA_EMAIL, "gcfmelo@gmail.com");
        intent2.putExtra(getWsData.EXTRA_PASSWORD, "abc123");
        startService(intent2);
*/
        /*
        Intent intent3 = new Intent(this, getWsData.class);
        intent3.setAction(getWsData.ACTION_GET_QUIZ_DATA);
        intent3.putExtra(getWsData.EXTRA_EMAIL, "gcfmelo@gmail.com");
        intent3.putExtra(getWsData.EXTRA_PASSWORD, "abc123");
        startService(intent3);



        // TEST AND DEBUG PURPOSES
        Intent intent4 = new Intent(this, getWsData.class);
        intent4.setAction(getWsData.ACTION_TEST);
        intent4.putExtra(getWsData.EXTRA_EMAIL, "gcfmelo@gmail.com");
        intent4.putExtra(getWsData.EXTRA_PASSWORD, "abc123");
        startService(intent4);
    */
    }


}
